package org.quintilis.forum.service

import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.repositories.CategoryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun findAll(page:Int): List<CategoryDTO> {
        val pageable = PageRequest.of(page - 1, 10)
        return categoryRepository.findAll(pageable).map { it.toDTO() }.toList()
    }

    fun findAll(){

    }
}
