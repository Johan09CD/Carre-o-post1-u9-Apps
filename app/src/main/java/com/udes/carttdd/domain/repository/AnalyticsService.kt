package com.udes.carttdd.domain.repository

interface AnalyticsService {
    fun logEvent(event: String)
}