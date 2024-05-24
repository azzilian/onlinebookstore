package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shoppingcarts")
@Getter
@Setter
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "shoppingCart", fetch = FetchType.LAZY)
    private Set<CartItem> cartItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingCart that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getUser(), that.getUser())
                && Objects.equals(getCartItems(), that.getCartItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getCartItems());
    }
}
