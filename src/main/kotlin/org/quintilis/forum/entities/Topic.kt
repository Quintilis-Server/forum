package org.quintilis.forum.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
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
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.quintilis.common.entities.BaseEntity
import org.quintilis.common.entities.auth.User
import org.quintilis.forum.dto.TopicDTO

@Entity
@Table(name = "topics", schema = "forum")
@NoArgsConstructor
@AllArgsConstructor
open class Topic : BaseEntity<TopicDTO> {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    open var title: String? = null

    @Size(max = 255) @NotNull @Column(name = "slug", nullable = false) open var slug: String? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author_id", nullable = false)
    open var author: User? = null // Agora usa org.quintilis.forum.entities.User

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    open var category: Category? = null

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    open var content: String? = null

    @ColumnDefault("0") @Column(name = "views") open var views: Long? = null

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    open var createdAt: Instant? = null

    @Column(name = "deleted_at") open var deletedAt: Instant? = null

    @OneToMany(mappedBy = "topic") open var posts: MutableSet<Post> = mutableSetOf()

    override fun toDTO(): TopicDTO {
        return TopicDTO(
                id = this.id,
                title = this.title!!,
                slug = this.slug!!,
                content = this.content!!,
                views = this.views ?: 0,
                createdAt = this.createdAt ?: Instant.now(),
                author = this.author?.toSummaryDTO()!!,
                posts = this.posts.map { it.toDTO() }.toList(),
        )
    }
}
