package org.quintilis.forum.repositories

import org.quintilis.forum.entities.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CategoryRepository: JpaRepository<Category, UUID> {
}