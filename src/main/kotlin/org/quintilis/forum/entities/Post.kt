package org.quintilis.forum.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "posts", schema = "forum")
open class Post {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    open var topic: Topic? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    open var author: User? = null // Agora usa org.quintilis.forum.entities.User

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    open var content: String? = null

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    open var createdAt: Instant? = null

}
