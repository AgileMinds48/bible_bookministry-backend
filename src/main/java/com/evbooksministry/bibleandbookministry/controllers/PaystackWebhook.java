/*
package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.EmailRequest;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.exceptions.OrderNotFound;
import com.evbooksministry.bibleandbookministry.models.Cart;
import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.OrderRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.hc.client5.http.utils.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Set;

import static com.evbooksministry.bibleandbookministry.enums.OrderStatus.PAID;


@RestController
@RequestMapping("/api/v1")
public class PaystackWebhook {
    static Dotenv dotenv = Dotenv.configure().load();
    private final OrderRepository orderRepository;
    private static final String API_SECRET_KEY = dotenv.get("PAYSTACK_SECRET");
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PaystackWebhook(OrderRepository orderRepository,
                           UserRepository userRepository,
                           EmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("x-paystack-signature") String signature) {
        try {
            //verify the signature
            if (!isValidSignature(payload, signature)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode payloadJson = mapper.readTree(payload);
            String reference = payloadJson.path("data").path("reference").asText();
            String transactionId = payloadJson.path("data").path("id").asText();

            System.out.println("Transaction reference: " + reference);
            System.out.println("Transaction id: " + transactionId);

            List<CustomerOrders> orders = orderRepository.findByOrderReference(reference);
            Set<Users> admins = userRepository.findByUserRole(UserRole.ADMIN);
            for (Users admin : admins) {
                EmailRequest adminAlert = new EmailRequest(
                        admin.getEmail(),
                        "New Sale"
                );
                Context adminContext = new Context();
                adminContext.setVariable("totalPrice", orders.getFirst().getTotalPrice());
                emailService.sendEmail(adminAlert, "NewSale", adminContext);
            }
            List<CustomerOrders> order = orderRepository.findByOrderReference(reference);
            if (order.isEmpty()) {
                throw new OrderNotFound();
            } else {
                for (CustomerOrders customerOrder : order) {
                    customerOrder.setOrderStatus(PAID);
                    orderRepository.save(customerOrder);
                    System.out.println("Customer order ID " + customerOrder.getOrderReference());
                }

            }
            Cart cart = order.getFirst().getUser().getUserCart();
            cart.getCartItems().clear();
            cartRepository.save(cart);



            //log the payload for debugging
            System.out.println("Received payload: " + payload);

            //handle specific events
            if (payload.contains("\"event\":\"charge.success\"")) {
                System.out.println("Transaction was successful");
                //todo extract transaction details and update database
            } else {
                System.out.println("Event not handled.");
            }
            return ResponseEntity.ok("Webhook handled successfully");
        }
        catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

    }

    private boolean isValidSignature(String payload, String signature) {
        try {
            //create HMAC-SHA512 signature
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(API_SECRET_KEY.getBytes(), "HmacSHA512");
            sha512Hmac.init(secretKey);
            byte[] hashedPayload = sha512Hmac.doFinal(payload.getBytes());

            //convert the hashed payload to a hex string
            String expectedSignature = Hex.encodeHexString(hashedPayload);

            //compare the signature (case-sensitive)
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
*/
