package org.quintilis.forum.service

import org.quintilis.common.service.BaseService
import org.quintilis.forum.dto.VersionDTO
import org.quintilis.forum.entities.Version
import org.quintilis.forum.repositories.VersionRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.reflect.KProperty1

@Service
class VersionService(
    private val repository: VersionRepository
): BaseService<Version, Int, VersionDTO, VersionDTO>(repository) {
    override fun newDTOToEntity(newDTO: VersionDTO): Version {
        return newDTO.toEntity()
    }

    override fun getSearchFields(): List<KProperty1<Version, *>> {
        return listOf(Version::displayName)
    }

    override fun updateEntityFromDTO(
        dto: VersionDTO,
        entity: Version
    ){}

    @Cacheable(cacheNames = ["versions"])
    override fun findAll(search: String?, pageable: Pageable, includeInactive: Boolean): Page<VersionDTO> {
        return super.findAll(search, pageable, includeInactive)
    }
}