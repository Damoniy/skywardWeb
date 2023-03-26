package com.graceful.skyward.web.repository;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class SkywardRepository(@Autowired private val gracefulJdbcTemplate: NamedParameterJdbcTemplate) {

    fun queryForProperties(): Map<String, String>? {
        return gracefulJdbcTemplate.queryForObject(
            "select cd_version_server, cd_version_client from \"public\".\"mod_versions\" where ie_latest_version = 'Y'",
            emptyMap<String, String>()) { resultSet, _ ->
            mapOf(
                Pair("serverVersion", resultSet.getString("cd_version_server")),
                Pair("clientVersion", resultSet.getString("cd_version_client"))
            )
        }
    }

    fun queryForAttributes(uuid: String): Map<String, String>? {
        val parameterSource = MapSqlParameterSource()
        parameterSource.addValue("id", uuid)

        return gracefulJdbcTemplate.queryForObject(
            "select qt_strength, qt_agility, qt_vitality, qt_intelligence, qt_wisdom, qt_charisma, qt_luck from player_attributes where id = :id",
            parameterSource) { resultSet, _ ->
            mapOf(
                Pair("qt_strength", resultSet.getString("qt_strength")),
                Pair("qt_agility", resultSet.getString("qt_agility")),
                Pair("qt_vitality", resultSet.getString("qt_vitality")),
                Pair("qt_intelligence", resultSet.getString("qt_intelligence")),
                Pair("qt_wisdom", resultSet.getString("qt_wisdom")),
                Pair("qt_charisma", resultSet.getString("qt_charisma")),
                Pair("qt_luck", resultSet.getString("qt_luck")),
            )
        }
    }

        fun playerExists(uuid: String): Boolean {
        val parameterSource = with(MapSqlParameterSource()) {
            addValue("uuid", uuid)
        }
        return try {
            gracefulJdbcTemplate.queryForObject(
                "select true ie_exists from skyward_player where id = :uuid", parameterSource
            ) { resultSet, _ -> resultSet.getBoolean("ie_exists") }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun insertObject(sql: String, parameterSource: MapSqlParameterSource) {
        gracefulJdbcTemplate.update(sql, parameterSource)
    }
    fun updateObject(sql: String, parameterSource: MapSqlParameterSource) {
        gracefulJdbcTemplate.update(sql, parameterSource)
    }
}
