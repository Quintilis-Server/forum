package org.quintilis.forum.controller

import io.swagger.v3.oas.annotations.Operation
import java.util.UUID
import org.quintilis.common.dto.auth.UserSummaryDTO
import org.quintilis.common.entities.auth.User
import org.quintilis.common.response.ApiResponse
import org.quintilis.forum.dto.TopicDTO
import org.quintilis.common.resolvers.CurrentUser
import org.quintilis.forum.service.TopicService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topic")
class TopicController(private val topicService: TopicService) {
    public data class TopicReceiverDTO(
            val title: String,
            val content: String,
            val categoryId: UUID,
    )

    @PostMapping("/new")
    @Operation(summary = "Create a new topic")
    fun create(
            @RequestBody topicDTO: TopicReceiverDTO,
            @CurrentUser user: User
    ): ApiResponse<TopicDTO> {
        val topic = topicService.create(topicDTO, user)
        return ApiResponse.success(topic.toDTO())
    }
}
