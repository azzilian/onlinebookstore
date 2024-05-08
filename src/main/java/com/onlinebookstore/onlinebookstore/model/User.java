package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET isdeleted = true WHERE id=?")
@SQLRestriction("isdeleted = false")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "shippingaddress")
    private String shippingaddress;

    @Column(nullable = false)
    private boolean isdeleted = false;
}