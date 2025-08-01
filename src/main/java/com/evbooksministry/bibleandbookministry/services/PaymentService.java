package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.PaymentRequest;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.UUID;

@Service
public class PaymentService {
    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartItemsRepository cartItemRepository;
    private final CartRepository cartRepository;


    private final String secretKey = System.getenv("PAYSTACK_SECRET") ;

    public PaymentService(RestTemplate restTemplate,
                          PaymentRepository paymentRepository,
                          UserRepository userRepository,
                          OrderRepository orderRepository,
                          CartItemsRepository cartItemRepository,
                          CartRepository cartRepository) {
        this.restTemplate = restTemplate;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public PaymentResponse processPayment(String userEmail, BigDecimal totalAmount) throws JsonProcessingException {
        BigDecimal total = totalAmount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
        System.out.println("total amount: " + total);

        // Create payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                userEmail,
                total.toString(),
                UUID.randomUUID()
        );


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.set("Content-Type", "application/json");

        // To debug the actual JSON being sent
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(paymentRequest);
        System.out.println("JSON being sent to Paystack: " + jsonRequest);

        HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);

        String url = "https://api.paystack.co/transaction/initialize";

        Users users = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID userId = users.getUserId();


        Set<CustomerOrders> customerOrders = orderRepository.findByUser_UserIdAndStatus(userId, OrderStatus.PENDING);
        if (customerOrders.isEmpty()) {
            throw new RuntimeException("Orders not found");
        }

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, PaymentResponse.class);
        String reference = request.getBody().reference().toString();
        for(CustomerOrders customerOrdersItem : customerOrders) {
            customerOrdersItem.setOrderReference(reference);
        }
        orderRepository.saveAll(customerOrders);
        System.out.println(response.getBody());
        return response.getBody();
    }

    public void refundPayment(UUID paymentId) {
    }
}
