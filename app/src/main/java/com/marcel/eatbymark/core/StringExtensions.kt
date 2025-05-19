package com.marcel.eatbymark.core

import java.util.Locale

fun String.toTitleCase(locale: Locale = Locale.getDefault()): String {
    return split(" ").joinToString(" ") {
        it.replaceFirstChar { first ->
            if (it.length > 1) first.titlecase(locale)
            else first.uppercase(locale)
        }
    }
}