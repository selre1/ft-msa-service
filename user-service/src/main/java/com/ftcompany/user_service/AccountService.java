package com.ftcompany.user_service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Account saveNewAccount(@Valid SignUpForm signUpForm){
        signUpForm.setAccountId(UUID.randomUUID().toString());
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm,Account.class);
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);

        if(account == null){
            throw new UsernameNotFoundException(email);
        }
        return new User(account.getEmail(),account.getPassword(),
                true,true,true,true,new ArrayList<>());
    }

    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            throw new UsernameNotFoundException(email);
        }
        return account;
    }
}
