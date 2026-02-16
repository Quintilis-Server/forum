package org.quintilis.forum.dto

import org.quintilis.common.dto.BaseDTO
import org.quintilis.forum.entities.Category
import java.time.Instant
import java.util.UUID

data class CategoryDTO(
    val id: UUID,
    val title: String,
    val slug: String,
    val description: String?,
    val displayOrder: Int,
    val createdAt: Instant
): BaseDTO<Category> {
    override fun toEntity(): Category {
        return Category().apply {
            this.id = this@CategoryDTO.id
            this.title = this@CategoryDTO.title
            this.slug = this@CategoryDTO.slug
            this.description = this@CategoryDTO.description
            this.displayOrder = this@CategoryDTO.displayOrder
            this.createdAt = this@CategoryDTO.createdAt
        }
    }
}
