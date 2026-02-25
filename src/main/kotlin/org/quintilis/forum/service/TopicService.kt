package org.quintilis.forum.service

import org.quintilis.common.exception.ForbiddenException
import org.quintilis.common.exception.NotFoundException
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

    fun create(topicDTO: TopicController.TopicReceiverDTO, username: String): Topic {
        val user =
                userRepository.findByUsername(username) ?: throw NotFoundException("User not found")

        val category =
                categoryRepository.findBySlug(topicDTO.slug)
                        ?: throw NotFoundException("Category not found")

        if (category.createTopicPermission != null) {
            val permissionName = category.createTopicPermission!!.name!!
            if (!user.hasPermission(permissionName)) {
                throw ForbiddenException(
                        "You do not have the required permission ($permissionName) to create a topic in this category."
                )
            }
        }

        val topic =
                Topic().apply {
                    this.title = topicDTO.title
                    this.slug = topicDTO.slug
                    this.content = topicDTO.content
                    this.category = category
                    this.author = user
                }

        return topicRepository.save(topic)
    }
}
