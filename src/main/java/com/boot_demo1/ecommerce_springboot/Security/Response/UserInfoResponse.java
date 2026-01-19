package com.boot_demo1.ecommerce_springboot.Security.Response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Setter
@Getter
public class UserInfoResponse {

    @NotNull
    @Positive
    private Long userId;

    private String jwtToken;

    private String username;
    private List<String> roles;

    public UserInfoResponse(Long userId,String username, List<String> roles, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }


    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.userId = id;
        this.username = username;
        this.roles = roles;
    }
}
