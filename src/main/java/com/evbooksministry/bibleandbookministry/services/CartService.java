package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.AddOrRemoveFromCartRequest;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.EmptyCart;
import com.evbooksministry.bibleandbookministry.exceptions.InsufficientBooks;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.models.Cart;
import com.evbooksministry.bibleandbookministry.models.CartItems;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.CartItemsRepository;
import com.evbooksministry.bibleandbookministry.repositories.CartRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemRepository;

    public CartService(UserRepository userRepository,
                       BookRepository bookRepository,
                       CartRepository cartRepository,
                       CartItemsRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Set<CartItems> addItemToCart(AddOrRemoveFromCartRequest request, UUID userID) {
        Users user = userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Cart cart = user.getUserCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUsers(user);
            cart.setCartItems(new HashSet<>());
            user.setUserCart(cart);
        }
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new BookNotFound("Product Not Found"));
        System.out.println(book);

        //throwing an error if product is unavailable
        if (!book.isAvailable()){
            throw new BookNotFound("The product is not available for sale");
        }

        if(book.getAmountInStock() < 1){
            throw new InsufficientBooks();
        }

        if (request.quantity() > book.getAmountInStock()){
            throw new InsufficientBooks();
        }


        Optional<CartItems> existingItems = cart.getCartItems()
                .stream()
                .filter(item -> item.getBook()
                        .getBookId()
                        .equals(request.bookId())).findFirst();

        if (existingItems.isPresent()) {
            BigDecimal ogPrice = book.getBookPrice();
            CartItems item = existingItems.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            item.setPrice(ogPrice.multiply(new BigDecimal(item.getQuantity())));
        }else{
            CartItems item = new CartItems();
            item.setQuantity(request.quantity());
            item.setBook(book);
            item.setPrice(book.getBookPrice().multiply(new BigDecimal(request.quantity())));
            item.setCart(cart);
            cart.getCartItems().add(item);
        }
        cartRepository.save(cart);
        userRepository.save(user);
        book.setAmountInStock(book.getAmountInStock() - request.quantity());
        book.setAmountSold(request.quantity());
        bookRepository.saveAndFlush(book);
        //TODO fix amount sold
        System.out.println("Amount sold: " + book.getAmountSold());
        System.out.println("Amount in stock: " + book.getAmountInStock());


        return cart.getCartItems();
    }

    public void removeItemFromCart(AddOrRemoveFromCartRequest request, UUID userID) {
        Users users = userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Cart cart = users.getUserCart();

        System.out.println("user cart: " + cart.getCartItems());
        Optional<CartItems> items = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getBook()
                        .getBookId()
                        .equals(request.bookId())).findFirst();
        items.ifPresent(item -> cart.getCartItems()
                .remove(item));
        cartRepository.save(cart);
    }

    public void clearCart(UUID userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Cart cart = users.getUserCart();
        cart.getCartItems().clear();
        System.out.println("user cart: " + cart.getCartItems());
        cartRepository.save(cart);
    }

    public Set<CartItems> fetchCartItems(UUID userId) {
        Set<CartItems> items = null;
        try {
            Users users = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));

            Cart cart = users.getUserCart();

            if (cart == null) {
                throw new RuntimeException("UserCart Not Found");
            }

            items = cart.getCartItems();
//            System.out.println(items);

            if (items.isEmpty()) {
                throw new EmptyCart("Your cart is empty");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (items.isEmpty()) {
            throw new EmptyCart();
        }
        return new HashSet<>(items);
    }

    public Set<CartItems> fetchUserCartItems(UUID userId) {
        return new HashSet<>(cartItemRepository.findCartItemsByUser(userId));
    }
}

