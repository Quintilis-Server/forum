package org.quintilis.forum.dto

import org.quintilis.common.dto.UserSummaryDTO
import java.time.Instant
import java.util.UUID

data class TopicDTO(
    val id: UUID?,
    val title: String,
    val slug: String,
    val content: String,
    val views: Long,
    val createdAt: Instant,
    val author: UserSummaryDTO,
    val posts: List<PostDTO>
)
