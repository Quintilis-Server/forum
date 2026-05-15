package org.quintilis.forum.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumeratedValue
import jakarta.persistence.Table
import org.quintilis.common.entities.IntEntity
import org.quintilis.forum.dto.VersionDTO

@Entity
@Table(name = "versions", schema = "forum")
class Version(
    @Column("file_name")
    val fileName: String,

    @Enumerated(EnumType.STRING)
    @Column("display_name")
    val displayName: VersionType,

    @Column("image_url")
    val imageUrl: String,

): IntEntity<VersionDTO>() {
    enum class VersionType(val displayName: String) {
        PRISM("Prism Launcher"),
        CURSE_FORGE("Curse Forge"),
        MODRINTH("Modrinth"),
    }

    override fun toDTO(): VersionDTO {
        return VersionDTO(
            this.fileName,
            this.displayName.displayName,
            this.imageUrl,
        ).apply {
            id = this@Version.id
            createdAt = this@Version.createdAt!!
        }
    }

}