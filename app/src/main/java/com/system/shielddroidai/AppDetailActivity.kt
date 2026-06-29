package com.system.shielddroidai

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class AppDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 60, 40, 40)
        layout.setBackgroundColor(Color.parseColor("#F8F9FA"))

        val appName = intent.getStringExtra("appName") ?: "Unknown App"
        val packageName = intent.getStringExtra("packageName") ?: "Unknown"
        val riskScore = intent.getIntExtra("riskScore", 0)
        val riskLevel = intent.getStringExtra("riskLevel") ?: "LOW"
        val installer = intent.getStringExtra("installerSource") ?: "Unknown"
        val explanation = intent.getStringExtra("explanation") ?: "No explanation available"

        val title = TextView(this)
        title.text = "🛡 $appName"
        title.textSize = 26f

        val details = TextView(this)
        details.text = """
Package Name:
$packageName

Risk Score:
$riskScore/100

Risk Level:
$riskLevel

Installed From:
$installer

Security Explanation:
$explanation
        """.trimIndent()
        details.textSize = 16f
        details.setPadding(0, 30, 0, 0)

        layout.addView(title)
        layout.addView(details)

        setContentView(layout)
    }
}