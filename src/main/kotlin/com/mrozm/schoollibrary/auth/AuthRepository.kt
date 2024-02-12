package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.entity.StudentEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AuthRepository {

    fun save(student: StudentEntity)
    fun findByEmail(email: String): StudentEntity?
}