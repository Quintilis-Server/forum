package org.quintilis.forum.repositories

import java.util.UUID
import org.quintilis.forum.entities.Category
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, UUID> {

    override fun findAll(): List<Category>

    override fun findAll(pageable: Pageable): Page<Category>

    @CacheEvict(value = ["categories", "categories_page"], allEntries = true)
    override fun <S : Category> save(entity: S): S

    @CacheEvict(value = ["categories", "categories_page"], allEntries = true)
    override fun deleteById(id: UUID)
    fun findBySlug(slug: String): Category?
}
