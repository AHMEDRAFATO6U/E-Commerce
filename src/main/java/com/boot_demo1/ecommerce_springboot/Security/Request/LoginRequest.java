package com.boot_demo1.ecommerce_springboot.Security.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
