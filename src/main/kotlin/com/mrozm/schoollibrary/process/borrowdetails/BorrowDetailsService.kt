package com.mrozm.schoollibrary.process.borrowdetails

import com.mrozm.schoollibrary.auth.AuthRepository
import com.mrozm.schoollibrary.process.book.BookRepository
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.BorrowDetailsFull
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.BorrowDetailsEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

interface IBorrowDetailsService {
    fun findBorrowDetails(isbn: String): BorrowDetailsFull?
    fun borrowBook(isbn: String): Int
    fun returnBook(isbn: String)
}

@Service
class BorrowDetailsService(
    private val authService: AuthRepository, // should be Repository or Service ?
    private val bookRepository: BookRepository,
    private val borrowDetailsRepository: BorrowDetailsRepository,
    private val borrowDetailsMapper: BorrowDetailsMapper
) : IBorrowDetailsService {

    override fun findBorrowDetails(isbn: String): BorrowDetailsFull? {
        val username = SecurityContextHolder.getContext().authentication.name ?: throw UserNotFoundException
        val student = authService.findByEmail(username) ?: throw UserNotFoundException
        val book = bookRepository.findByIsbn(isbn) ?: throw BookNotFoundException

        return borrowDetailsRepository.findFullBorrowDetails(student.id, book.isbn)?.let {
            borrowDetailsMapper.mapFrom(it)
        } ?: throw BorrowDetailsNotFoundException
    }

    override fun borrowBook(isbn: String): Int {
        val username = SecurityContextHolder.getContext().authentication.name ?: throw UserNotFoundException
        val student = authService.findByEmail(username) ?: throw UserNotFoundException
        val book = bookRepository.findByIsbnSkipBorrowed(isbn) ?: throw BookNotFoundException

        return borrowDetailsRepository.borrowBook(
            BorrowDetailsEntity(
                studentId = student.id,
                bookIsbn = book.isbn,
            )
        )
    }

    override fun returnBook(isbn: String) {
        val username = SecurityContextHolder.getContext().authentication.name ?: throw UserNotFoundException
        val student = authService.findByEmail(username) ?: throw UserNotFoundException
        val book = bookRepository.findByIsbn(isbn) ?: throw BookNotFoundException

        val borrowDetails = borrowDetailsRepository.findBorrowDetails(student.id, book.isbn)
        if (borrowDetails != null) {
            borrowDetailsRepository.returnBook(student.id, book.isbn)
        } else throw BorrowDetailsNotFoundException
    }

}

data object UserNotFoundException : NoSuchElementException("User not found")
data object BookNotFoundException : NoSuchElementException("Book not found")
data object BorrowDetailsNotFoundException : NoSuchElementException("Borrow details not found")