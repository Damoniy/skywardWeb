package com.graceful.skyward.web.service

import com.graceful.skyward.web.repository.SkywardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class SkywardService(@Autowired private val skywardRepository: SkywardRepository) {

    fun getProperties(): ResponseEntity<*> {
        return ResponseEntity.ok(skywardRepository.queryForProperties())
    }

    fun savePlayer(uuid: String, username: String): ResponseEntity<*> {
        if(uuid == "-1" || username == "-1") return ResponseEntity.noContent().build<Any>()

        val parameterSource = with(MapSqlParameterSource()) {
            addValue("uuid", uuid)
            addValue("username", username)
            addValue("ie_active", "Y")
        }

        if(!skywardRepository.playerExists(uuid)) {
            skywardRepository.insertObject(
                "insert into skyward_player (id, username, ie_active, dt_inclusion, dt_release) " +
                        "values (:uuid, :username, :ie_active, current_timestamp, current_timestamp)",
                parameterSource
            )
            return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
        }

        return ResponseEntity.noContent().build<Any>()
    }
}