package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.AdminDTO;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.dtos.CustomerOrderDTO;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.exceptions.CustomerNotFound;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.mappers.CustomerOrderMapper;
import com.evbooksministry.bibleandbookministry.mappers.UserMapper;
import com.evbooksministry.bibleandbookministry.models.Customer;
import com.evbooksministry.bibleandbookministry.models.Employee;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final BookService bookService;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerRepository customerRepository;

    public AdminService(UserRepository userRepository,
                        EmailService emailService,
                        BookRepository bookRepository,
                        PasswordEncoder passwordEncoder,
                        UserMapper userMapper,
                        BookMapper bookMapper,
                        BookService bookService,
                        EmployeeRepository employeeRepository,
                        OrderRepository orderRepository,
                        PaymentRepository paymentRepository,
                        CustomerOrderMapper customerOrderMapper, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.bookService = bookService;
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.customerOrderMapper = customerOrderMapper;
        this.customerRepository = customerRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::userEntityToUserDTO)
                .toList();
    }

    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }


    public List<Users> getEnabledUser() {
        return userRepository.findByIsActive();
    }


    public UserDTO getUserById(UUID id) {
        return userMapper.userEntityToUserDTO(userRepository.findByUserId(id));
    }

    public UserDTO registerAdmin(AdminDTO admin) {
        Users user = new Users();
        user.setEmail(admin.email());
        user.setUserName(admin.userName());
        user.setPhoneNumber(admin.phone());
        user.setUserRole(UserRole.ADMIN);
        user.setPassword(passwordEncoder.encode(admin.password()));
        user.setFirstName(admin.firstname());
        user.setLastName(admin.lastname());
        user.setActive(true);
        userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(user);
        employeeRepository.save(employee);

        return userMapper.userEntityToUserDTO(user);
    }

    public Integer getTotalAvailableBooks(){
        return bookRepository.countTotalAvailableBooks();
    }

    public Integer getTotalOrders(){
        return orderRepository.countTotalOrders();
    }

    public BigDecimal getTotalSales(){
        return paymentRepository.getTotalSales();
    }

    public List<BookDTO> getBooksLowInStock(){
        return bookRepository.getBookLowInStock()
                .stream()
                .map(bookMapper::bookEntityToBookDTO)
                .toList();
    }

    public List<CustomerOrderDTO> viewCustomerOrderHistory(UUID customerId){
        Customer customer = customerRepository.getCustomerByCustomerId(customerId)
                .orElseThrow(CustomerNotFound::new);

        return orderRepository.findCustomerOrders(customer.getCustomerId())
                .stream()
                .map(customerOrderMapper::toDTO)
                .toList();
    }

    public List<BookDTO> getHighestSellingBooks(){
        return bookRepository.getBooksByAmountSold()
                .stream()
                .map(bookMapper::bookEntityToBookDTO)
                .toList();
    }

    //list of orders api for user, product, timestamp most recent on top

    public List<CustomerOrderDTO> getMostRecentOrders(){
        return orderRepository.getMostRecentOrders()
                .stream()
                .map(customerOrderMapper::toDTO)
                .toList();
    }

}
