package com.ecomhub.product.service.repository;

import com.ecomhub.product.service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
