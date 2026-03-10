package org.quintilis.forum.controller

import io.swagger.v3.oas.annotations.Operation
import org.quintilis.common.controller.BaseController
import org.quintilis.common.entities.auth.User
import org.quintilis.common.response.ApiResponse
import org.quintilis.common.resolvers.CurrentUser
import org.quintilis.forum.dto.TopicDTO
import org.quintilis.forum.entities.Topic
import org.quintilis.forum.service.TopicService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/topics")
class TopicController(
    private val topicService: TopicService
) : BaseController<Topic, UUID, TopicDTO, TopicController.TopicReceiverDTO>(topicService) {

    data class TopicReceiverDTO(
        val title: String,
        val content: String,
        val categoryId: UUID,
    )

    override val allowCreate: Boolean = false

    @PostMapping("/post")
    @Operation(summary = "Create a new topic")
    fun createTopic(
        @RequestBody topicDTO: TopicReceiverDTO,
        @CurrentUser user: User
    ): ApiResponse<TopicDTO> {
        // Agora o service já retorna o DTO direto!
        val topic = topicService.createTopicWithUser(topicDTO, user)
        return ApiResponse.success(topic)
    }
}