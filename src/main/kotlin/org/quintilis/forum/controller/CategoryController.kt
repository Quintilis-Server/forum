package org.quintilis.forum.controller

import io.swagger.v3.oas.annotations.Operation
import org.quintilis.common.response.ApiResponse
import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.service.CategoryService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping("/all")
    @Operation(summary = "Get all categories")
    fun getAll(
        @RequestParam(value = "page") pageParam: Int?
    ): ApiResponse<List<CategoryDTO>> {
        val page = pageParam ?: 1
        return ApiResponse.success(categoryService.findAll(page))
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category")
    fun create(
        @RequestBody categoryDTO: CategoryDTO
    ): ApiResponse<CategoryDTO> {
        return ApiResponse.success(categoryService.create(categoryDTO))
    }
}