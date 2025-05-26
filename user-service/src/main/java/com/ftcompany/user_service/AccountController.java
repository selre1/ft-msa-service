package com.ftcompany.user_service;

import com.ftcompany.user_service.client.OrderServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private final Environment environment;

    private final OrderServiceClient orderServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Value("${server.port}")
    private String port;

    @Value("${token.expiration_time}")
    private Long tokenExpTime;

    @Value("${value.check}")
    private String valueCheck;

    @GetMapping("/health-check")
    public String status(){
        return String.format("it's Working in User Service  \n" +
                        " Port(local.server.port)=%s \n " +
                        " Port(server.port)=%s \n " +
                        " token exp time=%d \n " +
                        " value change @value check=%s \n" +
                        " value change environment check=%s ",
                environment.getProperty("local.server.port"),
                port,
                tokenExpTime,
                valueCheck,
                environment.getProperty("value.check"));
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUpSubmit(@RequestBody SignUpForm signUpForm){
        accountService.saveNewAccount(signUpForm);
        ResponseAccount responseAccount = modelMapper.map(signUpForm,ResponseAccount.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseAccount);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<ResponseAccount>> getAccounts(){
        List<Account> accounts = accountRepository.findAll();

        List<ResponseAccount> result = new ArrayList<>();
        accounts.forEach(account -> {
            result.add(modelMapper.map(account,ResponseAccount.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ResponseAccount> getAccount(@PathVariable String accountId){
        Account account = accountRepository.findByAccountId(accountId);

        if(account == null){
            throw new UsernameNotFoundException("account not found");
        }
        ResponseAccount result = modelMapper.map(account,ResponseAccount.class);
       /* List<ResponseOrder> orders = orderServiceClient.getOrders(accountId);*/
        log.info("before call orders");
        CircuitBreaker circuitBreaker =  circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orders = circuitBreaker.run(() -> orderServiceClient.getOrders(accountId), throwable -> new ArrayList<>());
        log.info("after call orders");
        result.setResponseOrders(orders);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
