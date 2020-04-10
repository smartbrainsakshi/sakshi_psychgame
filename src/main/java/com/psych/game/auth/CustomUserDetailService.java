package com.psych.game.auth;

import com.psych.game.exceptions.NoSuchUserException;
import com.psych.game.model.User;
import com.psych.game.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @SneakyThrows
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        Optional<User> user= userRepository.findByEmail(email);

        if(! user.isPresent())
        {
            throw new NoSuchUserException("No user registered with "+ email);
        }
        return new CustomUserDetails(user.get());

    }
}
