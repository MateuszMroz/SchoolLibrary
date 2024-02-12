package com.mrozm.schoollibrary.process.bookreservation

import com.mrozm.schoollibrary.process.bookreservation.model.entity.BookReservationDetailsEntity
import com.mrozm.schoollibrary.process.bookreservation.model.entity.BookReservationEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface BookReservationRepository {

    fun selectBookReservation(uuid: String, isbn: String): BookReservationEntity?
    fun selectDetailsBookReservation(uuid: String, isbn: String): BookReservationDetailsEntity?
    fun insert(bookReservation: BookReservationEntity)
    fun update(uuid: String, isbn: String)

}
