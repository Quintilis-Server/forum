package org.quintilis.forum.repositories

import org.quintilis.forum.entities.Topic
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TopicRepository: JpaRepository<Topic, UUID> {
    fun findBySlug(slug: String): Topic?
}