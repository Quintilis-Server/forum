package org.quintilis.forum.repositories

import org.quintilis.common.entities.auth.Permission
import java.util.UUID
import org.quintilis.forum.entities.Category
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : JpaRepository<Category, UUID> {

    override fun findAll(): List<Category>

    override fun findAll(pageable: Pageable): Page<Category>

    @Query("""
        SELECT DISTINCT c FROM Category c JOIN c.permissions p WHERE p IN :permissions
    """)
    fun findAllPermitted(permissions: Set<Permission>): List<Category>

    @CacheEvict(value = ["categories", "categories_page"], allEntries = true)
    override fun <S : Category> save(entity: S): S

    @CacheEvict(value = ["categories", "categories_page"], allEntries = true)
    override fun deleteById(id: UUID)
    fun findBySlug(slug: String): Category?
}
