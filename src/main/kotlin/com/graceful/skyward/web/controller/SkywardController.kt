package com.graceful.skyward.web.controller

import com.graceful.skyward.web.service.SkywardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SkywardController(@Autowired private val skywardService: SkywardService) {

    @GetMapping("/api/v1/data/properties")
    fun getProperties(): ResponseEntity<*> {
        return skywardService.getProperties()
    }
}