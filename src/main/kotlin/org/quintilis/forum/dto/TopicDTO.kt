package org.quintilis.forum.dto

import java.time.Instant
import java.util.UUID
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.quintilis.common.dto.BaseDTO
import org.quintilis.common.dto.auth.UserSummaryDTO
import org.quintilis.common.entities.auth.User
import org.quintilis.forum.entities.Topic

@NoArgsConstructor
@AllArgsConstructor
data class TopicDTO(
    var id: UUID?,
    var title: String,
    var slug: String,
    var content: String,
    var views: Long,
    var createdAt: Instant,
    var author: UserSummaryDTO,
    var posts: List<PostDTO>
): BaseDTO<Topic> {
    override fun toEntity(): Topic {
        return Topic().apply {
            id = this@TopicDTO.id
            title = this@TopicDTO.title
            slug = this@TopicDTO.slug
            content = this@TopicDTO.content
            views = this@TopicDTO.views
            createdAt = this@TopicDTO.createdAt
            author = User().apply {
                this.id = this@TopicDTO.author.id
            }
            posts = this@TopicDTO.posts.map { it.toEntity() }.toMutableSet()
        }
    }
}
