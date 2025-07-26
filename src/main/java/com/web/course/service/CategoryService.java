package com.web.course.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.course.entities.Category;
import com.web.course.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository CategoryRepository;

    public List<Category> findAll() {
        return CategoryRepository.findAll();
    }

    public Category findById(Long id) {
        Optional<Category> obj = CategoryRepository.findById(id);
        return obj.get();
    }
}
