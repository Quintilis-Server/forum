package org.quintilis.forum.repositories

import org.quintilis.common.repositories.BaseRepository
import org.quintilis.forum.entities.Topic
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TopicRepository: BaseRepository<Topic, UUID> {
    fun findBySlug(slug: String): Topic?
}