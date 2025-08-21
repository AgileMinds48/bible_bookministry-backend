package com.evbooksministry.bibleandbookministry.config;

import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<Users> user = repository.findByEmail(userEmail.toLowerCase());

        if (user.isPresent()){
            return new UserPrincipal(
                    user.get(),
                    getAuthorities(user.get())
            );
        }
        throw new UserNotFoundException();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Users user){
        String role = "ROLE_" + user.getUserRole();
        return List.of(new SimpleGrantedAuthority(role));
    }
}
