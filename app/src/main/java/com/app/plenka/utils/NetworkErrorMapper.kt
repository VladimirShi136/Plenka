package com.app.plenka.utils

import java.io.IOException
import java.net.UnknownHostException

fun Throwable.toUserMessage(defaultMessage: String = "Ошибка загрузки"): String {
    return if (this is UnknownHostException || this is IOException) {
        "Проверьте подключение к интернету"
    } else {
        this.localizedMessage ?: defaultMessage
    }
}
