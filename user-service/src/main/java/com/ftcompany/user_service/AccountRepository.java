package com.ftcompany.user_service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByAccountId(String accountId);

    Account findByEmail(String email);
}
