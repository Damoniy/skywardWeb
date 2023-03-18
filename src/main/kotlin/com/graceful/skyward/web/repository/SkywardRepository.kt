package com.graceful.skyward.web.repository;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class SkywardRepository(@Autowired private val gracefulJdbcTemplate: NamedParameterJdbcTemplate) {

    fun queryForProperties(): Map<String, String>? {
        return gracefulJdbcTemplate.queryForObject(
            "select cd_version_server, cd_version_client from mod_versions where ie_latest_version = 'Y'",
            emptyMap<String, String>()) { resultSet, _ ->
            mapOf(
                Pair("serverVersion", resultSet.getString("cd_version_server")),
                Pair("serverVersion", resultSet.getString("cd_version_client"))
            )
        }
    }
}
