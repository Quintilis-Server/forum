package org.quintilis.forum.controller

import io.swagger.v3.oas.annotations.Operation
import org.quintilis.common.entities.auth.User
import org.quintilis.common.exception.NotFoundException
import org.quintilis.common.response.ApiResponse
import org.quintilis.common.response.PageResponse
import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.common.resolvers.CurrentUser
import org.quintilis.forum.service.CategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/category")
class CategoryController(private val categoryService: CategoryService) {
    public data class CategoryReceiverDTO(
        val title: String,
        val slug: String,
        val description: String,
        val displayOrder: Int,
        val permissions: List<Int>
    )

    data class CategoryUpdateDTO(
        val id: UUID,
        val title: String,
        val slug: String,
        val description: String,
        val displayOrder: Int,
        val permissions: List<Int>
    )
    @GetMapping("/all")
    @Operation(summary = "Get all categories")
    fun getAll(@RequestParam(value = "page") pageParam: Int?): ApiResponse<PageResponse<CategoryDTO>> {
        val page = pageParam ?: 1
        return ApiResponse.success(categoryService.findAll(page))
    }

    @GetMapping("/permitted")
    @Operation(summary = "Get categories the user is allowed to post in")
    fun getPermittedCategories(
            @CurrentUser
            user: User
    ): ApiResponse<List<CategoryDTO>> {
        return ApiResponse.success(categoryService.findAllPermitted(user))
    }

//    @GetMapping("/{slug}")
//    @Operation(summary = "Get category by slug")
//    fun getBySlug(@PathVariable slug: String): ApiResponse<CategoryDTO> {
//        val category =
//                categoryService.findBySlug(slug) ?: throw NotFoundException("Category not found")
//        return ApiResponse.success(category)
//    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ApiResponse<CategoryDTO> {
        val category = categoryService.findById(id) ?: throw NotFoundException("Category with id $id not found")
        return ApiResponse.success(category)
    }

    @PutMapping("/{id}/update")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: CategoryUpdateDTO
    ): ApiResponse<CategoryDTO> {
        return ApiResponse.success(categoryService.update(dto))
    }

    @PostMapping("/new")
    @Operation(summary = "Create a new category")
    fun create(@RequestBody categoryDTO: CategoryReceiverDTO, @CurrentUser(requiredPermission = "category.create") user: User): ApiResponse<CategoryDTO> {
        return ApiResponse.success(categoryService.create(categoryDTO))
    }
}
