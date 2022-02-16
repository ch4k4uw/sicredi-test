package com.sicredi.core.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

val Long.asLocalDateTime: LocalDateTime
    get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())