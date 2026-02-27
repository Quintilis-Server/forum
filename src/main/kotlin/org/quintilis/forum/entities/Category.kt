package org.quintilis.forum.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.hibernate.annotations.ColumnDefault
import org.quintilis.common.entities.BaseEntity
import org.quintilis.common.entities.auth.Permission
import org.quintilis.forum.dto.CategoryDTO

@Entity
@Table(name = "categories", schema = "forum")
@NoArgsConstructor
@AllArgsConstructor
open class Category : BaseEntity<CategoryDTO> {
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

    @Column(name = "description", length = Integer.MAX_VALUE) open var description: String? = null

    @ColumnDefault("0") @Column(name = "display_order") open var displayOrder: Int? = null

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    open var createdAt: Instant? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "category_permissions",
        schema = "forum",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    open var permissions: MutableSet<Permission> = mutableSetOf()

    @OneToMany(mappedBy = "category") open var topics: MutableSet<Topic> = mutableSetOf()
    override fun toDTO(): CategoryDTO {
        return CategoryDTO(
                id = this.id ?: java.util.UUID.randomUUID(),
                title = this.title ?: "",
                slug = this.slug!!,
                description = this.description,
                displayOrder = this.displayOrder!!,
                createdAt = this.createdAt ?: Instant.now(),
                permissions = this.permissions.map { it.toDTO() }.toList(),
                topics = this.topics.map { it.toDTO() }
        )
    }
}
