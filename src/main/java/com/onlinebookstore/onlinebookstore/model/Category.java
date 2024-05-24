package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "categories")
@Getter
@Setter
@SQLDelete(sql = "UPDATE categories SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category category)) {
            return false;
        }
        return isDeleted() == category.isDeleted()
                && Objects.equals(getId(), category.getId())
                && Objects.equals(getName(), category.getName())
                && Objects.equals(getDescription(), category.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), isDeleted());
    }
}
