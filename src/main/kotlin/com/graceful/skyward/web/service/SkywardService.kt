package com.graceful.skyward.web.service

import com.graceful.skyward.web.repository.SkywardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class SkywardService(@Autowired private val skywardRepository: SkywardRepository) {

    fun getProperties(): ResponseEntity<*> {
        return ResponseEntity.ok(skywardRepository.queryForProperties())
    }
}