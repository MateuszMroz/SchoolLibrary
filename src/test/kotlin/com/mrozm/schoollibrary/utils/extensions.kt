package com.mrozm.schoollibrary.utils

import com.fasterxml.jackson.databind.ObjectMapper

fun createObjectMapper(): ObjectMapper = ObjectMapper().apply { findAndRegisterModules() }