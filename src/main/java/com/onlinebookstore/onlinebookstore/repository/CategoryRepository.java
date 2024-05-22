package com.onlinebookstore.onlinebookstore.repository;

import com.onlinebookstore.onlinebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
