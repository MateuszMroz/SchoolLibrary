package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.entity.Role.USER
import com.mrozm.schoollibrary.auth.model.entity.StudentEntity
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.STUDENT_UUID
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
class AuthRepositoryTests {

    @Autowired
    lateinit var authRepository: AuthRepository

    @Test
    fun `should find student when save to database`() {
        // given
        val student = StudentEntity(
            uuid = STUDENT_UUID,
            firstname = "Anne",
            lastname = "Cleveland",
            email = "jewel.martin@example.com",
            pass = "partiendo",
            role = USER
        )

        // when
        authRepository.save(student)
        val result = authRepository.findByEmail("jewel.martin@example.com")

        // then
        assertThat(result).isNotNull
        assertThat(result?.uuid).isEqualTo(STUDENT_UUID)
        assertThat(result?.firstname).isEqualTo("Anne")
        assertThat(result?.lastname).isEqualTo("Cleveland")
        assertThat(result?.email).isEqualTo("jewel.martin@example.com")
        assertThat(result?.role).isEqualTo(USER)
    }
}