package com.ghostwan.snapcal.presentation

import com.ghostwan.snapcal.data.remote.GeminiApiException
import com.ghostwan.snapcal.data.remote.ProductNotFoundException
import com.ghostwan.snapcal.data.remote.OpenFoodFactsException
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.net.SocketTimeoutException
import org.json.JSONException

/** Never show raw server responses, URLs, credentials, or platform errors in the UI. */
fun Throwable.russianMessage(): String {
    if (this is CancellationException) throw this
    return when (this) {
        is GeminiApiException -> message ?: "Не удалось выполнить запрос к Gemini."
        is ProductNotFoundException -> "Продукт с таким штрихкодом не найден."
        is OpenFoodFactsException -> "Сервис продуктов недоступен. Повторите попытку позже."
        is SocketTimeoutException -> "Сервис не ответил вовремя. Повторите попытку."
        is IOException -> "Не удалось получить данные. Проверьте подключение к интернету и повторите попытку."
        is JSONException -> "ИИ вернул неполный результат. Повторите анализ или уточните описание."
        is SecurityException -> "Нет необходимого разрешения. Проверьте разрешения приложения в настройках Android."
        is IllegalStateException -> when (message) {
            "Не указан API-ключ Gemini. Откройте настройки на экране добавления еды." -> message!!
            "Не удалось прочитать фотографию", "Не удалось обработать фотографию" -> message!!
            else -> "Не удалось выполнить действие. Проверьте настройки и повторите попытку."
        }
        else -> "Не удалось выполнить действие. Повторите попытку."
    }
}
