package org.quintilis.forum.dto

import java.time.Instant
import java.util.UUID
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.quintilis.common.dto.BaseDTO
import org.quintilis.common.dto.auth.UserSummaryDTO
import org.quintilis.common.entities.auth.User
import org.quintilis.forum.entities.Post

@NoArgsConstructor
@AllArgsConstructor
data class PostDTO(
    var id: UUID?,
    var author: UserSummaryDTO,
    var createdAt: Instant,
    var content: String,
): BaseDTO<Post> {
    override fun toEntity(): Post {
        return Post().apply {
            id = this@PostDTO.id
            author = User().apply {
                this.id = this@PostDTO.author.id
            }
            createdAt = this@PostDTO.createdAt
            content = this@PostDTO.content
        }
    }

}
