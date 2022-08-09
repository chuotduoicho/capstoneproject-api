package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category newCategory;

    @BeforeEach
    void setUp() {
        newCategory = Category.builder()
                .id(UUID.fromString("66c2360c-01f4-483d-a404-e5f0a6818bd9"))
                .name("Web")
                .build();
    }
    @DisplayName("JUnit test for saveCategory method")
    @Test
    void givenCategoryObject_whenSaveCategory_thenReturnCategoryObject() {
        // given - precondition or setup
        given(categoryRepository.save(newCategory)).willReturn(newCategory);
        // when -  action or the behaviour that we are going test
        Category category = categoryService.saveCategory(newCategory);
        // then - verify the output
        Assertions.assertThat(category).isEqualTo(newCategory);
        Assertions.assertThat(category).isNotNull();
    }
    @DisplayName("JUnit test for saveAllCategory method")
    @Test
    void givenListCategory_whenSaveAllCategory_thenReturnSavedListCategories() {
        // given - precondition or setup
        Category newCategory1 = Category.builder()
                .id(UUID.fromString("8fc2519d-4bbf-4a7a-b6da-a7e3fa7b9889"))
                .name("Game design")
                .build();
        List<Category> categories = List.of(newCategory,newCategory1);
        given(categoryRepository.saveAll(categories)).willReturn(List.of(newCategory,newCategory1));
        // when -  action or the behaviour that we are going test
        List<Category> savedCategories = categoryService.saveCategories(categories);
        // then - verify the output
        Assertions.assertThat(savedCategories).isNotNull();
        Assertions.assertThat(savedCategories.size()).isEqualTo(2);
    }
    @DisplayName("JUnit test for getCategories method")
    @Test
    void givenListCategory_whenGetCategories_thenReturnListCategory() {
        // given - precondition or setup
        Category newCategory1 = Category.builder()
                .id(UUID.fromString("8fc2519d-4bbf-4a7a-b6da-a7e3fa7b9889"))
                .name("Game design")
                .build();
        given(categoryRepository.findAll()).willReturn(List.of(newCategory,newCategory1));
        // when -  action or the behaviour that we are going test
        List<Category> categories = categoryService.getCategories();
        // then - verify the output
        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories.size()).isEqualTo(2);
    }
    @DisplayName("JUnit test for getCategoryById method")
    @Test
    void givenCategoryObject_whenGetCategoryById_thenReturnCategoryObject() {
        // given - precondition or setup
        given(categoryRepository.findById(newCategory.getId())).willReturn(Optional.of(newCategory));
        // when -  action or the behaviour that we are going test
        Category category = categoryService.getCategoryById(newCategory.getId());
        // then - verify the output
        Assertions.assertThat(category).isNotNull();
    }
    @DisplayName("JUnit test for getCategoryByName method")
    @Test
    void givenCategoryObject_whenGetCategoryByName_thenReturnCategoryObject() {
        // given - precondition or setup
        given(categoryRepository.findByName(newCategory.getName())).willReturn(newCategory);
        // when -  action or the behaviour that we are going test
        Category category = categoryService.getCategoryByName(newCategory.getName());
        // then - verify the output
        Assertions.assertThat(category).isNotNull();
    }
    @DisplayName("JUnit test for deleteCategoryById method")
    @Test
    void givenCategoryId_whenDeleteCategoryObject_thenReturnMessage() {
        // given - precondition or setup
        willDoNothing().given(categoryRepository).deleteById(newCategory.getId());
        // when -  action or the behaviour that we are going test
        String deleteCategory = categoryService.deleteCategoryById(newCategory.getId());
        // then - verify the output
        Assertions.assertThat(deleteCategory).isEqualTo("Category remove" + newCategory.getId());
    }

    @Test
    void updateCategory() {
        // given - precondition or setup
        given(categoryRepository.findById(newCategory.getId())).willReturn(Optional.of(newCategory));
        given(categoryRepository.save(newCategory)).willReturn(newCategory);
        newCategory.setName("Game design");
        // when -  action or the behaviour that we are going test
        Category updateCategory = categoryService.updateCategory(newCategory);
        // then - verify the output
        Assertions.assertThat(updateCategory.getName()).isEqualTo("Game design");
    }
}