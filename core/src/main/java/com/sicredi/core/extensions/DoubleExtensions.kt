package com.sicredi.core.extensions

import java.text.NumberFormat

val Double.asFormattedPrice: String
    get() = NumberFormat.getCurrencyInstance().format(this)