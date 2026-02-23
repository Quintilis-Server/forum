package org.quintilis.forum.dto

import java.time.Instant
import java.util.UUID
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.quintilis.common.dto.UserSummaryDTO

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
)
