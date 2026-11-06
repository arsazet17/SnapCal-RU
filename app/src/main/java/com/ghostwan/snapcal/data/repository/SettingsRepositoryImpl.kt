package com.ghostwan.snapcal.data.repository

import android.content.Context
import com.ghostwan.snapcal.data.local.ApiKeyStore
import com.ghostwan.snapcal.domain.repository.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val prefs = context.getSharedPreferences("food_calories_settings", Context.MODE_PRIVATE)

    private val keyStore = ApiKeyStore(context)

    init {
        // Migrate a key saved by earlier versions; no API key ever enters BuildConfig.
        val legacy = prefs.getString(KEY_API, null)
        if (!legacy.isNullOrBlank()) {
            try {
                if (keyStore.read().isBlank()) keyStore.write(legacy.trim())
            } catch (_: Exception) {
                // If Keystore is unavailable the user can enter the key again.
            } finally {
                prefs.edit().remove(KEY_API).commit()
            }
        }
    }

    override fun getApiKey(): String = keyStore.read()

    override fun setApiKey(key: String) {
        require(ApiKeyStore.isValid(key)) { "Некорректный API-ключ Gemini" }
        keyStore.write(key.trim())
        prefs.edit().remove(KEY_API).commit()
    }

    override fun isShoppingListEnabled(): Boolean {
        return prefs.getBoolean(KEY_SHOPPING_LIST, false)
    }

    override fun setShoppingListEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOPPING_LIST, enabled).apply()
    }

    override fun getBackupFrequencyDays(): Int {
        return prefs.getInt(KEY_BACKUP_FREQUENCY, 1)
    }

    override fun setBackupFrequencyDays(days: Int) {
        prefs.edit().putInt(KEY_BACKUP_FREQUENCY, days).apply()
    }

    override fun isDynamicCalorieGoalEnabled(): Boolean {
        return prefs.getBoolean(KEY_DYNAMIC_CALORIE_GOAL, false)
    }

    override fun setDynamicCalorieGoalEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DYNAMIC_CALORIE_GOAL, enabled).apply()
    }

    override fun getDailyCalorieDeficit(): Int {
        return prefs.getInt(KEY_DAILY_CALORIE_DEFICIT, 500)
    }

    override fun setDailyCalorieDeficit(deficit: Int) {
        prefs.edit().putInt(KEY_DAILY_CALORIE_DEFICIT, deficit).apply()
    }

    override fun isMeasurementsEnabled(): Boolean {
        return prefs.getBoolean(KEY_MEASUREMENTS_ENABLED, false)
    }

    override fun setMeasurementsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MEASUREMENTS_ENABLED, enabled).apply()
    }

    override fun getGeminiModel(): String {
        return prefs.getString(KEY_GEMINI_MODEL, null) ?: DEFAULT_MODEL
    }

    override fun setGeminiModel(model: String) {
        prefs.edit().putString(KEY_GEMINI_MODEL, model).apply()
    }

    private companion object {
        const val DEFAULT_MODEL = "gemini-2.5-flash"
        const val KEY_API = "gemini_api_key_v2"
        const val KEY_SHOPPING_LIST = "shopping_list_enabled"
        const val KEY_BACKUP_FREQUENCY = "backup_frequency_days"
        const val KEY_DYNAMIC_CALORIE_GOAL = "dynamic_calorie_goal"
        const val KEY_DAILY_CALORIE_DEFICIT = "daily_calorie_deficit"
        const val KEY_MEASUREMENTS_ENABLED = "measurements_enabled"
        const val KEY_GEMINI_MODEL = "gemini_model"
    }
}
