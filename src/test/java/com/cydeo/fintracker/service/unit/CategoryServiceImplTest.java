package com.cydeo.fintracker.service.unit;

import com.cydeo.fintracker.dto.CategoryDto;
import com.cydeo.fintracker.entity.Category;
import com.cydeo.fintracker.exception.CategoryNotFoundException;
import com.cydeo.fintracker.repository.CategoryRepository;
import com.cydeo.fintracker.service.impl.CategoryServiceImpl;
import com.cydeo.fintracker.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void should_throw_an_exception_when_category_doesnt_exist() {
        when(categoryRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getById(1L));
    }

    @Test
    void should_return_categoryDTO_when_category_exist() {
        Category category = new Category();
        category.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);

        when(categoryRepository.findByIdAndIsDeleted(category.getId(), false)).thenReturn(Optional.of(category));
        when(mapperUtil.convert(category, new CategoryDto())).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(category.getId());

        assertThat(result.getId()).isEqualTo(categoryDto.getId());
    }


}
