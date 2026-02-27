package org.quintilis.forum.service

import jakarta.transaction.Transactional
import org.quintilis.common.entities.auth.User
import org.quintilis.common.exception.BadRequestException
import org.quintilis.common.exception.ForbiddenException
import org.quintilis.common.exception.UnauthorizedException
import org.quintilis.common.repositories.UserRepository
import org.quintilis.forum.controller.TopicController
import org.quintilis.forum.entities.Topic
import org.quintilis.forum.repositories.CategoryRepository
import org.quintilis.forum.repositories.TopicRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TopicService(
        private val topicRepository: TopicRepository,
        private val categoryRepository: CategoryRepository,
        private val userRepository: UserRepository
) {

    @Cacheable("topic_slug", key = "#slug")
    fun findBySlug(slug: String): Topic? {
        return topicRepository.findBySlug(slug)
    }

    @Transactional
    fun create(topicDTO: TopicController.TopicReceiverDTO, user: User): Topic {
//        val user =
//                userRepository.findById(java.util.UUID.fromString(userId)).orElse(null)
//                        ?: throw UnauthorizedException("User not found")

        val category =
                categoryRepository.findById(topicDTO.categoryId).orElseThrow {
                    BadRequestException("Category not found", "Category slug not found")
                }

        if (category.permissions.isNotEmpty()) {
            if (!user.hasPermission(category.permissions)) {
                throw ForbiddenException(
                        "You do not have the required permission (${category.permissions.joinToString(", ") { it.name ?: "" }}) to create a topic in this category."
                )
            }
        }

        val baseSlug =
                topicDTO.title
                        .lowercase()
                        .replace("[^a-z0-9\\s-]".toRegex(), "")
                        .replace("\\s+".toRegex(), "-")
        val uniqueSuffix = java.util.UUID.randomUUID().toString().substring(0, 8)
        val generatedSlug = "$baseSlug-$uniqueSuffix"

        val topic =
                Topic().apply {
                    this.title = topicDTO.title
                    this.slug = generatedSlug
                    this.content = topicDTO.content
                    this.category = category
                    this.author = user
                }

        return topicRepository.save(topic)
    }
}
