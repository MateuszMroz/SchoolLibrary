package com.mrozm.schoollibrary.process.borrowdetails

import com.mrozm.schoollibrary.core.IMapper
import com.mrozm.schoollibrary.process.book.model.dto.Book
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.BorrowDetailsFull
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.Student
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.BorrowDetailsFullEntity
import org.springframework.stereotype.Component

@Component
class BorrowDetailsMapper : IMapper<BorrowDetailsFull, BorrowDetailsFullEntity> {

    override fun mapTo(a: BorrowDetailsFull): BorrowDetailsFullEntity {
        return BorrowDetailsFullEntity(
            studentId = a.student.id,
            studentFirstname = a.student.firstname,
            studentLastname = a.student.lastname,
            bookIsbn = a.book.isbn,
            bookTitle = a.book.title,
            bookAuthor = a.book.author,
            bookCategory = enumValueOf(a.book.category.name),
            bookFormat = enumValueOf(a.book.format.name),
            bookRelease = a.book.release,
            borrowDate = a.borrowDate,
            returnDate = a.returnDate,
            borrowStatus = enumValueOf(a.status.name)
        )
    }

    override fun mapFrom(b: BorrowDetailsFullEntity): BorrowDetailsFull {
        return BorrowDetailsFull(
            student = Student(
                id = b.studentId,
                firstname = b.studentFirstname,
                lastname = b.studentLastname
            ), book = Book(
                isbn = b.bookIsbn,
                title = b.bookTitle,
                author = b.bookAuthor,
                category = enumValueOf(b.bookCategory.name),
                format = enumValueOf(b.bookFormat.name),
                release = b.bookRelease
            ),
            borrowDate = b.borrowDate,
            returnDate = b.returnDate,
            status = enumValueOf(b.borrowStatus.name)
        )
    }

}