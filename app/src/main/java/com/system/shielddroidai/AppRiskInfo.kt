package com.system.shielddroidai

data class AppRiskInfo(
    val appName: String,
    val packageName: String,
    val permissions: List<String>,
    val riskScore: Int,
    val riskLevel: String,
    val timeline: List<String>,
    val installerSource: String,
    val explanation: String,
    val findings: List<String>
)