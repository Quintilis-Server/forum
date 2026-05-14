package org.quintilis.forum.controller

import org.quintilis.common.controller.BaseController
import org.quintilis.common.dto.minecraft.MineEventDTO
import org.quintilis.common.entities.minecraft.MineEvent
import org.quintilis.common.service.minecraft.MineEventService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class MineEventController(
    override val service: MineEventService
): BaseController<MineEvent, Int, MineEventDTO, MineEventDTO>(service) {

}