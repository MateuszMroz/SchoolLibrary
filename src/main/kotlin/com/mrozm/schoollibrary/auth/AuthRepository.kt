package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.entity.Student
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AuthRepository {
    fun save(student: Student): Int
    fun findByEmail(email: String): Student?
}