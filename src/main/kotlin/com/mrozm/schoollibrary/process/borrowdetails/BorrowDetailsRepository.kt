package com.mrozm.schoollibrary.process.borrowdetails

import com.mrozm.schoollibrary.process.borrowdetails.model.entity.BorrowDetailsEntity
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.BorrowDetailsFullEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface BorrowDetailsRepository {

    fun findBorrowDetails(studentId: Long, isbn: String): BorrowDetailsEntity?
    fun findFullBorrowDetails(studentId: Long, isbn: String): BorrowDetailsFullEntity?
    fun borrowBook(borrowBook: BorrowDetailsEntity): Int
    fun returnBook(studentId: Long, isbn: String)
}
