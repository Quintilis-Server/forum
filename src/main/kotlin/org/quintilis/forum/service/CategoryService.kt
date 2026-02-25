package org.quintilis.forum.service

import jakarta.transaction.Transactional
import java.time.Instant
import org.quintilis.common.repositories.PermissionRepository
import org.quintilis.forum.controller.CategoryController
import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.entities.Category
import org.quintilis.forum.repositories.CategoryRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class CategoryService(
        private val categoryRepository: CategoryRepository,
        private val permissionRepository: PermissionRepository
) {
    @Cacheable("categories_page", key = "#page")
    fun findAll(page: Int): List<CategoryDTO> {
        val pageable = PageRequest.of(page - 1, 10)
        return categoryRepository.findAll(pageable).map { it.toDTO() }.toList()
    }

    @Cacheable("category_slug", key = "#slug")
    fun findBySlug(slug: String): CategoryDTO? = categoryRepository.findBySlug(slug)?.toDTO()

    @Transactional
    fun create(categoryDTO: CategoryController.CategoryReceiverDTO): CategoryDTO {
        val category = Category()
        category.title = categoryDTO.title
        category.slug = categoryDTO.slug
        category.description = categoryDTO.description
        category.displayOrder = categoryDTO.display_order
        category.createTopicPermission =
                categoryDTO.createTopicPermissionId?.let { permissionId ->
                    permissionRepository.findById(permissionId).orElse(null)
                }
        //        category.id = UUID.randomUUID()
        category.createdAt = Instant.now()
        return categoryRepository.save(category).toDTO()
    }
}
