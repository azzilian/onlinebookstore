package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "books")
@Getter
@Setter
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
public class Book {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Column(unique = true)
    private String isbn;
    @Column(nullable = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book book)) {
            return false;
        }
        return isDeleted() == book.isDeleted()
                && Objects.equals(getId(), book.getId())
                && Objects.equals(getTitle(), book.getTitle())
                && Objects.equals(getAuthor(), book.getAuthor())
                && Objects.equals(getIsbn(), book.getIsbn())
                && Objects.equals(getPrice(), book.getPrice())
                && Objects.equals(getDescription(), book.getDescription())
                && Objects.equals(getCoverImage(), book.getCoverImage())
                && Objects.equals(getCategories(), book.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getTitle(),
                getAuthor(),
                getIsbn(),
                getPrice(),
                getDescription(),
                getCoverImage(),
                isDeleted(),
                getCategories());
    }
}
