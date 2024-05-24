package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "cartitems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoppingcart_id",
            referencedColumnName = "id",
            nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "book_id",
            referencedColumnName = "id",
            nullable = false)
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItem cartItem)) {
            return false;
        }
        return getQuantity() == cartItem.getQuantity()
                && Objects.equals(getId(), cartItem.getId())
                && Objects.equals(getShoppingCart(), cartItem.getShoppingCart())
                && Objects.equals(getBook(), cartItem.getBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getShoppingCart(), getBook(), getQuantity());
    }
}
