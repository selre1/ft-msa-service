package com.ftcompany.user_service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAccount {
    private String email;
    private String nickname;

    private String accountId;

    private List<ResponseOrder> responseOrders;
}
