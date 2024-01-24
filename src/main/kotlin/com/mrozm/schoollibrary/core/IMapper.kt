package com.mrozm.schoollibrary.core

interface IMapper<A, B> {
    fun mapTo(a: A): B
    fun mapFrom(b: B): A
}