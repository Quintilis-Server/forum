package org.quintilis.forum.service

import jakarta.transaction.Transactional
import org.quintilis.common.entities.auth.User
import org.quintilis.common.exception.NotFoundException
import org.quintilis.common.repositories.PermissionRepository
import org.quintilis.common.repositories.UserRepository
import org.quintilis.common.service.BaseService
import org.quintilis.forum.controller.CategoryController
import org.quintilis.forum.dto.CategoryDTO
import org.quintilis.forum.entities.Category
import org.quintilis.forum.repositories.CategoryRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KProperty1

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val permissionRepository: PermissionRepository,
    private val userRepository: UserRepository
) : BaseService<Category, UUID, CategoryDTO, CategoryController.CategoryReceiverDTO>(categoryRepository) {

    // ========================================================================
    // 1. CONTRATOS DO BASE SERVICE
    // ========================================================================

    override fun getSearchFields(): List<KProperty1<Category, *>> {
        return listOf(
            Category::title,
            Category::slug,
            Category::description
        )
    }

    override fun newDTOToEntity(newDTO: CategoryController.CategoryReceiverDTO): Category {
        return Category().apply {
            this.title = newDTO.title
            this.slug = newDTO.slug
            this.description = newDTO.description
            this.displayOrder = newDTO.displayOrder
            this.createdAt = Instant.now()

            val perms = permissionRepository.findAllById(newDTO.permissions)
            this.permissions.clear()
            this.permissions.addAll(perms)
        }
    }

    override fun updateEntityFromDTO(dto: CategoryDTO, entity: Category) {
        entity.title = dto.title
        entity.slug = dto.slug
        entity.description = dto.description
        entity.displayOrder = dto.displayOrder

        // Assumindo que o seu CategoryDTO tem uma lista de IDs de permissão
        val permissionIds = dto.permissions.map { it.id }
        val perms = permissionRepository.findAllById(permissionIds)
        entity.permissions.clear()
        entity.permissions.addAll(perms)
    }

    // ========================================================================
    // 2. SOBRESCRITAS DE CACHE
    // ========================================================================

    @Cacheable("category", key = "#id")
    override fun findById(id: UUID, includeInactive: Boolean): Category {
        return super.findById(id, includeInactive)
    }

    @Transactional
    @CacheEvict("category_slug", "category", "categories_page", allEntries = true)
    override fun create(dto: CategoryController.CategoryReceiverDTO): CategoryDTO {
        return super.create(dto)
    }

    @Transactional
    @CacheEvict("category_slug", "category", "categories_page", allEntries = true)
    override fun update(dto: CategoryDTO, id: UUID): CategoryDTO {
        return super.update(dto, id)
    }

    @Transactional
    @CacheEvict("category_slug", "category", "categories_page", allEntries = true)
    override fun delete(id: UUID, hardDelete: Boolean): CategoryDTO {
        return super.delete(id, hardDelete)
    }

    // ========================================================================
    // 3. MÉTODOS CUSTOMIZADOS
    // ========================================================================

    @Cacheable("category_slug", key = "#slug")
    fun findBySlug(slug: String): CategoryDTO? = categoryRepository.findBySlug(slug)?.toDTO()

    fun findAllPermitted(user: User): List<CategoryDTO> {
        return categoryRepository.findAllPermitted(user.getAllPermissionsEntity())
            .map { it.toDTO() }
    }
}