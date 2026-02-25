package org.quintilis.forum.controller

import io.swagger.v3.oas.annotations.Operation
import java.security.Principal
import org.quintilis.common.response.ApiResponse
import org.quintilis.forum.dto.TopicDTO
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
            val slug: String,
            val content: String,
    )

    @PostMapping("/new")
    @Operation(summary = "Create a new topic")
    fun create(
        @RequestBody topicDTO: TopicReceiverDTO,
        principal: Principal
    ): ApiResponse<TopicDTO> {
        val topic = topicService.create(topicDTO, principal.name)
        return ApiResponse.success(topic.toDTO())
    }
}
