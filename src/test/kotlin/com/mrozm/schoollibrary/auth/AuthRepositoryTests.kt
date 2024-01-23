package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.entity.Role.USER
import com.mrozm.schoollibrary.auth.model.entity.Student
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD

@MybatisTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class AuthRepositoryTests @Autowired constructor(
        private val authRepository: AuthRepository
) {

    @Test
    fun `should return rows affected when save student to database`() {
        // given
        val student = Student(
                firstname = "Anne",
                lastname = "Cleveland",
                email = "jewel.martin@example.com",
                pass = "partiendo",
                role = USER
        )

        // when
        val rowsAffected = authRepository.save(student)

        // then
        assertThat(rowsAffected).isGreaterThan(0)
    }

    @Test
    fun `should find student when save to database`() {
        // given
        val student = Student(
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
        assertThat(result?.id).isNotZero()
        assertThat(result?.firstname).isEqualTo("Anne")
        assertThat(result?.lastname).isEqualTo("Cleveland")
        assertThat(result?.email).isEqualTo("jewel.martin@example.com")
        assertThat(result?.role).isEqualTo(USER)
    }
}