package org.quintilis.forum.dto

import org.quintilis.common.dto.BaseDTO
import org.quintilis.forum.entities.Version

data class VersionDTO(
    val fileName: String,
    val displayName: String,
    val imageUrl: String,
): BaseDTO<Version, Int>() {
    override fun toEntity(): Version {
        return Version(
            fileName = fileName,
            displayName = Version.VersionType.valueOf(displayName),
            imageUrl = imageUrl
        )
    }
}
