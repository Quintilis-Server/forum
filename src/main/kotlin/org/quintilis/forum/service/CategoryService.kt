package org.quintilis.forum.service

import jakarta.transaction.Transactional
import org.quintilis.common.entities.auth.User
import org.quintilis.common.exception.NotFoundException
import java.time.Instant
import org.quintilis.common.repositories.PermissionRepository
import org.quintilis.common.repositories.UserRepository
import org.quintilis.common.response.PageResponse
import org.quintilis.forum.controller.CategoryController
import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.entities.Category
import org.quintilis.forum.repositories.CategoryRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryService(
        private val categoryRepository: CategoryRepository,
        private val permissionRepository: PermissionRepository,
        private val userRepository: UserRepository
) {
    @Cacheable("categories_page", key = "#page")
    fun findAll(page: Int): PageResponse<CategoryDTO> {
        val pageable = PageRequest.of(page - 1, 10)
        val page =  categoryRepository.findAll(pageable)
        val dtos = page.map { it.toDTO() }.toList()
        return PageResponse(
            items = dtos,
            totalPages = page.totalPages,
            currentPage = page.number + 1
        )
    }

    fun findAllPermitted(user: User): List<CategoryDTO> {
        return categoryRepository.findAllPermitted(user.getAllPermissionsEntity())
                .map { it.toDTO() }
    }

    @Cacheable("category", key = "#id")
    fun findById(id: UUID): CategoryDTO? = categoryRepository.findById(id).orElseThrow{
        NotFoundException("Category not found")
    }.toDTO()

    @Cacheable("category_slug", key = "#slug")
    fun findBySlug(slug: String): CategoryDTO? = categoryRepository.findBySlug(slug)?.toDTO()

    @Transactional
    @CacheEvict("category_slug", "category", allEntries = true)
    fun create(categoryDTO: CategoryController.CategoryReceiverDTO): CategoryDTO {
        val category = Category()
        category.title = categoryDTO.title
        category.slug = categoryDTO.slug
        category.description = categoryDTO.description
        category.displayOrder = categoryDTO.displayOrder
        category.permissions.clear()
        val permissions = permissionRepository.findAllById(categoryDTO.permissions)
        category.permissions.addAll(permissions)

        category.createdAt = Instant.now()
        return categoryRepository.save(category).toDTO()
    }

    @Transactional
    @CacheEvict("category_slug", "category", allEntries = true)
    fun update(dto: CategoryController.CategoryUpdateDTO): CategoryDTO {
        val category = categoryRepository.findById(dto.id).orElseThrow { NotFoundException("Category not found") }

        category.apply {
            title = dto.title
            slug = dto.slug
            description = dto.description
            displayOrder = dto.displayOrder

            val newPermissions = permissionRepository.findAllById(dto.permissions)
            permissions.clear()
            permissions.addAll(newPermissions)

        }
        return categoryRepository.save(category).toDTO()
    }
}
