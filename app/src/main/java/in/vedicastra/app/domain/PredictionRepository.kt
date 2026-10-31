package com.vedicastra.app.domain

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class PredictionRecord(
    val id: Long,
    val createdAt: Long,
    val system: String,
    val forecast: String,
    val dueAt: String,
    val outcome: String
)

class PredictionRepository(context: Context) {
    private val prefs = context.getSharedPreferences("prediction_verification", Context.MODE_PRIVATE)
    fun all(): List<PredictionRecord> {
        val array = JSONArray(prefs.getString("records", "[]"))
        return (0 until array.length()).mapNotNull { i ->
            runCatching {
                val o = array.getJSONObject(i)
                PredictionRecord(o.getLong("id"), o.getLong("createdAt"), o.getString("system"), o.getString("forecast"), o.getString("dueAt"), o.optString("outcome", ""))
            }.getOrNull()
        }.sortedByDescending { it.createdAt }
    }
    fun add(system: String, forecast: String, dueAt: String) {
        val records = all().toMutableList()
        records.add(PredictionRecord(System.currentTimeMillis(), System.currentTimeMillis(), system, forecast, dueAt, ""))
        save(records)
    }
    fun updateOutcome(id: Long, outcome: String) {
        save(all().map { if (it.id == id) it.copy(outcome = outcome) else it })
    }
    private fun save(records: List<PredictionRecord>) {
        val array = JSONArray()
        records.forEach { r ->
            array.put(JSONObject().put("id", r.id).put("createdAt", r.createdAt).put("system", r.system).put("forecast", r.forecast).put("dueAt", r.dueAt).put("outcome", r.outcome))
        }
        prefs.edit().putString("records", array.toString()).apply()
    }
}
