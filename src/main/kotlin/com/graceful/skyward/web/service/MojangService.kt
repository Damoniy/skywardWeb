package com.graceful.skyward.web.service

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.graceful.skyward.BCryptUtils
import com.graceful.skyward.web.repository.SkywardRepository
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MojangService(@Autowired private val skywardRepository: SkywardRepository) {

    private fun getUUID(username: String): String {
        try {
            val httpClient = HttpClients.createDefault()
            val getRequest = HttpGet("https://api.mojang.com/users/profiles/minecraft/$username")
            val response = httpClient.execute(getRequest)
            val entity = response.entity

            return Gson().fromJson<Map<String, String>>(
                EntityUtils.toString(entity),
                JsonObject::class.java
            )["uuid"].orEmpty()


        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun mojangAuthentication(username: String, password: String): Boolean {
        val httpClient = HttpClients.createDefault()
        val postRequest = HttpPost("https://authserver.mojang.com/authenticate")

        val body = mapOf(
            Pair<String, Any>("agent", mapOf(Pair("name", "Minecraft"), Pair("version", "1.19.2"))),
            Pair("username", username),
            Pair("password", password),
            Pair("clientToken", "d58841f7c95959c5f4d1abdd4231c672"),
            Pair("requestUser", true)
        )

        postRequest.entity = StringEntity(Gson().toJson(body))

        postRequest.setHeader("Accept", "application/json")
        postRequest.setHeader("Content-type", "application/json")

        val response = httpClient.execute(postRequest)

        if((200 == response.statusLine.statusCode)) {
            val encoder = BCryptPasswordEncoder()
            val id = getUUID(username)

            if(skywardRepository.userExists(id)) {
                return true
            }

            skywardRepository.insertObject(
                "insert into skyward_user (id, username, user_password) values (${id}, ${username}, ${encoder.encode(password)})",
                MapSqlParameterSource()
            )
        }

        return (200 == response.statusLine.statusCode)
    }

    fun updatePassword(uuid: String, password: String): Boolean {
        if(skywardRepository.hasPasswordFor(uuid)) {
            skywardRepository.updateObject(
                "update skyward_player_password set cd_pin = '${BCryptUtils.encode(password)}' where id = '$uuid'",
                MapSqlParameterSource()
            )
            return true
        }

        return false
    }

    fun createPassword(uuid: String, password: String): Boolean {
        if(!skywardRepository.hasPasswordFor(uuid)) {
            skywardRepository.insertObject("insert into skyward_player_password(id, cd_pin) values ('$uuid', '${BCryptUtils.encode(password)}')",
            MapSqlParameterSource()
            )
            return true
        }
        return false
    }

    fun serverAuthentication(uuid: String, password: String): Boolean {
        return BCryptUtils.macthes(password, skywardRepository.queryForPassword(uuid)["cd_pin"] as String)
    }

    fun skywardAuthentication(uuid: String, password: String): ResponseEntity<*> {
        try {
            if(serverAuthentication(uuid, password)) {
                return ResponseEntity.ok("{ \"isValid\": \"true\" }")
            }
            return ResponseEntity.badRequest().body("{ \"isValid\": \"false\" }")
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body("{ \"isValid\": \"false\" }")
        }
    }

    fun skywardPasswordSynthesis(uuid: String, password: String): ResponseEntity<*> {
        try {
            if(skywardRepository.playerExists(uuid)) {
                if (createPassword(uuid, password)) {
                    return ResponseEntity.status(HttpStatus.CREATED).body("{ \"isCreated\": \"true\" }")
                }
            }
            return ResponseEntity.badRequest().body("{ \"isCreated\": \"false\" }")
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body("{ \"isCreated\": \"false\" }")
        }
    }
}