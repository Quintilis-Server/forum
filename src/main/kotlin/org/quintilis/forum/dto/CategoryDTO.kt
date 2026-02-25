package org.quintilis.forum.dto

import java.time.Instant
import java.util.UUID
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.quintilis.common.dto.BaseDTO
import org.quintilis.common.dto.auth.PermissionDTO
import org.quintilis.forum.entities.Category

@NoArgsConstructor
@AllArgsConstructor
data class CategoryDTO(
        var id: UUID?,
        var title: String,
        var slug: String,
        var description: String?,
        var displayOrder: Int,
        var createTopicPermission: PermissionDTO?,
        var topics: List<TopicDTO>,
        var createdAt: Instant
) : BaseDTO<Category> {
    override fun toEntity(): Category {
        return Category().apply {
            this.id = this@CategoryDTO.id
            this.title = this@CategoryDTO.title
            this.slug = this@CategoryDTO.slug
            this.description = this@CategoryDTO.description
            this.displayOrder = this@CategoryDTO.displayOrder
            this.createTopicPermission = this@CategoryDTO.createTopicPermission?.toEntity()
            this.createdAt = this@CategoryDTO.createdAt
        }
    }
}
