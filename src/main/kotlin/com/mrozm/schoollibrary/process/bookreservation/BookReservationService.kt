package com.mrozm.schoollibrary.process.bookreservation

import com.mrozm.schoollibrary.auth.AuthService
import com.mrozm.schoollibrary.process.book.BookService
import com.mrozm.schoollibrary.process.bookreservation.model.dto.BookReservationResponse
import com.mrozm.schoollibrary.process.bookreservation.model.entity.BookReservationEntity
import org.apache.ibatis.javassist.NotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface IBookReservationService {

    fun findBookReservationDetails(isbn: String): BookReservationResponse
    fun reserveBook(isbn: String)
    fun returnBook(isbn: String)

}

@Service
class BookReservationService(
    private val authService: AuthService,
    private val bookService: BookService,
    private val bookReservationRepository: BookReservationRepository,
    private val bookReservationMapper: BookReservationMapper
) : IBookReservationService {

    @Transactional(readOnly = true)
    override fun findBookReservationDetails(isbn: String): BookReservationResponse {
        val username = SecurityContextHolder.getContext().authentication.name ?: throw UserNotFoundException
        val student = authService.findByEmail(username)
        val book = bookService.findByIsbn(isbn)

        return bookReservationRepository.selectDetailsBookReservation(student.uuid, book.isbn)?.let {
            bookReservationMapper.mapFrom(it)
        } ?: throw BookReservationNotFoundException
    }

    @Transactional
    override fun reserveBook(isbn: String) {
        val username = SecurityContextHolder.getContext().authentication.name ?: throw UserNotFoundException
        val student = authService.findByEmail(username)
        val book = bookService.findByIsbn(isbn)

        val reservation = bookReservationRepository.selectBookReservation(student.uuid, book.isbn)

        if (reservation == null) {
            bookReservationRepository.insert(
                BookReservationEntity(
                    studentUuid = student.uuid,
                    bookIsbn = book.isbn,
                )
            )
        } else throw BoorkReservationAlereadyExistException
    }

    @Transactional
    override fun returnBook(isbn: String) {
        val username = SecurityContextHolder.getContext().authentication.name ?: throw UserNotFoundException
        val student = authService.findByEmail(username)
        val book = bookService.findByIsbn(isbn)

        val borrowDetails = bookReservationRepository.selectBookReservation(student.uuid, book.isbn)
        if (borrowDetails != null) {
            bookReservationRepository.update(student.uuid, book.isbn)
        } else throw BookReservationNotFoundException
    }

}

data object UserNotFoundException : NotFoundException("User not found")
data object BookReservationNotFoundException : NotFoundException("Book reservation details not found")
data object BoorkReservationAlereadyExistException : NotFoundException("Book reservation already exist")