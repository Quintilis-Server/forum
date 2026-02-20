package org.quintilis.forum.service

import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.repositories.CategoryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun findAll(page:Int): List<CategoryDTO> {
        val pageable = PageRequest.of(page - 1, 10)
        return categoryRepository.findAll(pageable).map { it.toDTO() }.toList()
    }

    fun create(categoryDTO: CategoryDTO): CategoryDTO {
        val category = categoryDTO.toEntity()
        category.id = UUID.randomUUID()
        category.createdAt = Instant.now()
        return categoryRepository.save(category).toDTO()
    }
}