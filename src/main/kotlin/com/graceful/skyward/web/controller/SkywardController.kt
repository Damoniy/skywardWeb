package com.graceful.skyward.web.controller

import com.graceful.skyward.web.dto.Residence
import com.graceful.skyward.web.service.SkywardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class SkywardController(@Autowired private val skywardService: SkywardService) {

    @GetMapping("/api/v1/data/properties")
    fun getProperties(): ResponseEntity<*> {
        return skywardService.getProperties()
    }

    @PostMapping("/api/v1/data/player")
    fun savePlayer(@RequestBody body: Map<String, String>): ResponseEntity<*> {
        return skywardService.savePlayer(body.getOrDefault("uuid", "0"), body.getOrDefault("username", "0"))
    }

    @GetMapping("/api/v1/player/{uuid}/attributes")
    fun getAttributes(@PathVariable uuid: String): ResponseEntity<*> {
        return skywardService.getAttributes(uuid)
    }

    @PostMapping("/api/v1/player/persistAttributes")
    fun savePlayerAttributes(@RequestBody attributes: Map<String, String>): ResponseEntity<*> {
        return skywardService.saveAttributes(attributes)
    }

    @GetMapping("/api/v1/cities")
    fun getCities(): ResponseEntity<*> {
        return skywardService.getCityAreas();
    }

    @GetMapping("/api/v1/player/{uuid}/residences")
    fun getPlayerResidences(@PathVariable uuid: String): ResponseEntity<*> {
        return skywardService.getPlayerResidenceAreas(uuid)
    }

    @PostMapping("/api/v1/player/createResidence")
    fun savePlayerResidence(@RequestBody residence: Residence): ResponseEntity<*> {
        skywardService.saveResidence(residence)
        return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    }
}