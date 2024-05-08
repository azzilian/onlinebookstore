package com.onlinebookstore.onlinebookstore.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String shippingaddress;
}
