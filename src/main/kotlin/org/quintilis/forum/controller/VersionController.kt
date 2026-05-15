package org.quintilis.forum.controller

import org.quintilis.common.controller.BaseController
import org.quintilis.forum.dto.VersionDTO
import org.quintilis.forum.entities.Version
import org.quintilis.forum.service.VersionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/version")
class VersionController(
    override val service: VersionService
): BaseController<Version, Int, VersionDTO, VersionDTO>(service) {
    override val allowCreate: Boolean
        get() = false

    override val allowUpdate: Boolean
        get() = false

    override val allowDelete: Boolean
        get() = false
}