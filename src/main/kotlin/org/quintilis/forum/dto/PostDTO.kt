package org.quintilis.forum.dto

import java.time.Instant
import java.util.UUID
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.quintilis.common.dto.auth.UserSummaryDTO

@NoArgsConstructor
@AllArgsConstructor
data class PostDTO(
        var id: UUID?,
        var author: UserSummaryDTO,
        var createdAt: Instant,
        var content: String,
)
