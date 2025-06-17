package com.web.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.course.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
