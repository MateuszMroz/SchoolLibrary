package com.mrozm.schoollibrary.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@Transactional
class AuthControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var service: IAuthService

    private val objectMapper: ObjectMapper = ObjectMapper()

    private val registerRequest = RegisterRequest(
        firstname = "Serena",
        lastname = "Whitley",
        email = "faith.vargas@example.com",
        password = "conclusionemque"
    )

    private val loginRequest = LoginRequest(
        email = "faith.vargas@example.com",
        password = "conclusionemque"
    )

    @Nested
    @DisplayName("Register")
    @TestInstance(PER_METHOD)
    inner class Register {

        @Test
        fun `should return OK when user register successfully`() {
            // given/when
            val performPost = mockMvc.post("$BASE_URL/register") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerRequest)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

        @Test
        fun `should return Bearer token when user register successfully`() {
            // given/when
            val performPost = mockMvc.post("$BASE_URL/register") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(registerRequest)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.token") { isString() }
                }
        }

        @Test
        fun `should return BAD_REQUEST when user try register with wrong data`() {
            // given
            val request = RegisterRequest(
                firstname = "",
                lastname = "",
                email = "faith.vargas@example.com",
                password = ""
            )

            // when
            val performPost = mockMvc.post("$BASE_URL/register") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("Login")
    @TestInstance(PER_METHOD)
    inner class Login {

        @Test
        fun `should return OK when user log in successfully`() {
            // given
            service.register(registerRequest)

            // when
            val performPost = mockMvc.post("$BASE_URL/login") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequest)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

        @Test
        fun `should return Bearer token when user log in successfully`() {
            // given
            service.register(registerRequest)

            // when
            val performPost = mockMvc.post("$BASE_URL/login") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequest)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.token") { isString() }
                }
        }

        @Test
        fun `should return BAD_REQUEST when user try log in with wrong data`() {
            // given
            val request = LoginRequest(
                email = "",
                password = "conclusionemque"
            )

            // when
            val performPost = mockMvc.post("$BASE_URL/login") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }

    }

    private companion object {
        const val BASE_URL = "/api/v1/auth"
    }
}