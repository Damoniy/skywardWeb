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

        val attributesParameterSource = with(MapSqlParameterSource()) {
            addValue("uuid", uuid)
            addValue("qt_strength", 0)
            addValue("qt_agility", 0)
            addValue("qt_vitality", 0)
            addValue("qt_intelligence", 0)
            addValue("qt_wisdom", 0)
            addValue("qt_charisma", 0)
            addValue("qt_luck", 0)
        }

        if(!skywardRepository.playerExists(uuid)) {
            skywardRepository.insertObject(
                "insert into skyward_player (id, username, ie_active, dt_inclusion, dt_release) " +
                        "values (:uuid, :username, :ie_active, current_timestamp, current_timestamp)",
                parameterSource
            )

            skywardRepository.insertObject("insert into player_attributes(id, qt_strength, qt_agility, qt_vitality, qt_intelligence, qt_wisdom, qt_charisma, qt_luck) " +
                    "values (:uuid, :qt_strength, :qt_agility, :qt_vitality, :qt_intelligence, :qt_wisdom, :qt_charisma, :qt_luck)",
                attributesParameterSource)
            return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
        }

        return ResponseEntity.noContent().build<Any>()
    }

    fun getAttributes(uuid: String): ResponseEntity<*> {
        return ResponseEntity.ok(skywardRepository.queryForAttributes(uuid))
    }

    fun saveAttributes(attributes: Map<String, String>): ResponseEntity<*> {
        val parameterSource = MapSqlParameterSource()

        for(key in attributes.keys) {
            parameterSource.addValue(key, attributes[key])
        }

        skywardRepository.updateObject("update player_attributes " +
                "set qt_strength = :qt_strength, " +
                " qt_agility = :qt_agility, " +
                " qt_vitality = :qt_vitality,  " +
                " qt_intelligence = :qt_intelligence, " +
                " qt_wisdom = :qt_wisdom, " +
                " qt_charisma = :qt_charisma, " +
                " qt_luck = :qt_luck" +
                "where id = :uuid",
                parameterSource)

        return ResponseEntity.ok("Player attributes has been updated.")
    }
}