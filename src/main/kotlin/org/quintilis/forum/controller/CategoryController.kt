package org.quintilis.forum.controller

import io.swagger.v3.oas.annotations.Operation
import org.quintilis.common.controller.BaseController
import org.quintilis.common.entities.auth.User
import org.quintilis.common.exception.NotFoundException
import org.quintilis.common.response.ApiResponse
import org.quintilis.common.resolvers.CurrentUser
import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.entities.Category
import org.quintilis.forum.service.CategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryService: CategoryService
) : BaseController<Category, UUID, CategoryDTO, CategoryController.CategoryReceiverDTO>(categoryService) {

    data class CategoryReceiverDTO(
        val title: String,
        val slug: String,
        val description: String,
        val displayOrder: Int,
        val permissions: List<Int>
    )

    @GetMapping("/permitted")
    @Operation(summary = "Get categories the user is allowed to post in")
    fun getPermittedCategories(
        @CurrentUser user: User
    ): ApiResponse<List<CategoryDTO>> {
        return ApiResponse.success(categoryService.findAllPermitted(user))
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by slug")
    fun getBySlug(@PathVariable slug: String): ApiResponse<CategoryDTO> {
        val category = categoryService.findBySlug(slug)
            ?: throw NotFoundException("Category not found")
        return ApiResponse.success(category)
    }
}