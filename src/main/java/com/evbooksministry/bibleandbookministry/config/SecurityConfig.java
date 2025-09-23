package com.evbooksministry.bibleandbookministry.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;
    private final AllRequestsLoggingFilter loggingFilter;


    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JWTFilter jwtFilter,
                          AllRequestsLoggingFilter loggingFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required (most specific first)
                        .requestMatchers(
                                "/api/v1/auth/signup",
                                "/api/v1/auth/login",
                                "/api/v1/admin/register",
                                "/api/v1/webhook",
                                "/api/v1/books/public/**",
                                "/api/v1/books/search",
                                "/api/v1/roles/create",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v1/books/all-books",
                                "/api/v1/books/*",
                                "/api/v1/webhook"
                        ).permitAll()

                        // Admin-only endpoints (more specific patterns first)
                        .requestMatchers(
                                "/api/v1/admin/**",
                                "/api/v1/order/update-status",
                                "/api/v1/books/update",
                                "/api/v1/books/update-media",
                                "/api/v1/books/update-details",
                                "/api/v1/books/remove-product/**",
                                "/api/v1/users/**",
                                "/api/v1/orders/**", // Fixed: was "/api/v1/orders/*"
                                "/api/v1/admin/books/get-available",
                                "/api/v1/admin/orders/total-sales", // Fixed: added leading slash
                                "/api/v1/admin/get-user/*",
                                "/api/v1/admin/get-users",
                                "api/v1/admin/count-users",
                                "/api/v1/admin/add-book",
                                "/api/v1/admin/update-details",
                                "/api/v1/admin/update-media",
                                "/api/v1/admin/remove-book/*",
                                "/api/v1/admin/orders/get-total",
                                "/api/v1/admin/orders/total-sales",
                                "/api/v1/admin/books/low-stock",
                                "/api/v1/admin/orders/customer/*",
                                "/api/v1/admin/books/highest-selling",
                                "/api/v1/admin/category/create-defaults",
                                "/api/v1/admin/category/new",
                                "/api/v1/roles/create",
                                "/api/v1/roles/**",
                                "/api/v1/roles/code/*"
                        ).hasRole("ADMIN")

                        // Customer endpoints
                        .requestMatchers(
                                "/api/v1/cart/**",
                                "/api/v1/order/customer/**",
                                "/api/v1/profile/**",
                                "/api/v1/reviews/**",
                                "/api/v1/cart/add",
                                "/api/v1/cart/remove-item",
                                "/api/v1/cart/clear",
                                "/api/v1/cart/get-items",
                                "/api/v1/order/checkout",
                                "/api/v1/order/customer/get-order",
                                "/api/v1/order/buy-now",
                                "/api/v1/order/get-order/**",
                                "/api/v1/order/update-status",
                                "/api/v1/user/book-catalog",
                                "/api/v1/user/get-book/**",
                                "/api/v1/cart/add"
                        ).hasRole("CUSTOMER")

                        // Shared endpoints requiring authentication (any authenticated user)
                        .requestMatchers(
                                "/api/v1/books/details/**",
                                "/api/v1/books/list",
                                "/api/v1/order/checkout",
                                "api/v1/books/get-book/*",
                                "api/v1/books/get-category/*",
                                "api/v1/books/all-books"
                        ).permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"Greetings\": \"Type Shi Chale\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Ground Up chaleeeeeeeeeee\"}");
                        })
                )
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:3001",
                "https://bible-bookministry.vercel.app",
                "https://www.bibleandbookministry.app"
        ));
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

}
