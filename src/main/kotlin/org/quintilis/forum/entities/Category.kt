package org.quintilis.forum.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import org.quintilis.common.dto.BaseDTO
import org.quintilis.common.entities.BaseEntity
import org.quintilis.forum.dto.CategoryDTO
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "categories", schema = "forum")
open class Category: BaseEntity<CategoryDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @Size(max = 100)
    @NotNull
    @Column(name = "title", nullable = false, length = 100)
    open var title: String? = null

    @Size(max = 100)
    @NotNull
    @Column(name = "slug", nullable = false, length = 100)
    open var slug: String? = null

    @Column(name = "description", length = Integer.MAX_VALUE)
    open var description: String? = null

    @ColumnDefault("0")
    @Column(name = "display_order")
    open var displayOrder: Int? = null

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    open var createdAt: Instant? = null

    @OneToMany(mappedBy = "category")
    open var topics: MutableSet<Topic> = mutableSetOf()
    override fun toDTO(): CategoryDTO {
        return CategoryDTO(
            id = this.id ?: UUID.randomUUID(),
            title = this.title ?: "",
            slug = this.slug!!,
            description = this.description,
            displayOrder = this.displayOrder!!,
            createdAt = this.createdAt ?: Instant.now(),
            topics = this.topics.map { it.toDTO() }.toList()
        )
    }

}