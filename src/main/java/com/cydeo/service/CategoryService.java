package com.cydeo.service;


import com.cydeo.exception.CategoryNotFoundException;
import com.cydeo.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto getById(Long id) ;
    List<CategoryDto> listAllCategories();
    CategoryDto update(CategoryDto category, Long id) ;
    void delete(Long id) ;
    CategoryDto save(CategoryDto category);
    boolean isCategoryDescriptionUnique(String description);


}