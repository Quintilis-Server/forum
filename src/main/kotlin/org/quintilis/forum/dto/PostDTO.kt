package org.quintilis.forum.dto

import org.quintilis.common.dto.UserSummaryDTO
import java.time.Instant
import java.util.UUID

data class PostDTO(
    val id: UUID?,
    val author: UserSummaryDTO,
    val createdAt: Instant,
    val content: String,
)
