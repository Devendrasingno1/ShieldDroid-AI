package com.system.shielddroidai

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.widget.EditText
import android.widget.Button
import android.widget.HorizontalScrollView
import android.text.Editable
import android.text.TextWatcher
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider
import android.net.Uri
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.content.Intent
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private val appRiskList = mutableListOf<AppRiskInfo>()
    private var currentFilter = "ALL"
    private lateinit var recyclerView: RecyclerView
    private var criticalCount = 0
    private var highCount = 0
    private var mediumCount = 0
    private var lowCount = 0
    private val trustedApps = listOf(
        "com.android.chrome",
        "com.google.android.gm",
        "com.google.android.apps.maps",
        "com.google.android.gms",
        "com.android.settings",
        "com.google.android.youtube"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.setPadding(35, 45, 35, 25)
        mainLayout.setBackgroundColor(Color.parseColor("#F7F9FC"))

        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.setPadding(0, 10, 0, 20)

        val menu = TextView(this)
        menu.text = "☰"
        menu.textSize = 26f
        menu.setTextColor(Color.BLACK)

        val title = TextView(this)
        title.text = "ShieldDroid AI"
        title.textSize = 26f
        title.setTypeface(null, Typeface.BOLD)
        title.setTextColor(Color.BLACK)
        title.setPadding(40, 0, 0, 0)

        val shield = TextView(this)
        shield.text = "🛡"
        shield.textSize = 24f
        shield.setPadding(40, 0, 0, 0)

        header.addView(menu)
        header.addView(title)
        header.addView(shield)

        val subtitle = TextView(this)
        subtitle.text = "AI-Powered Android Malware Detection Engine"
        subtitle.textSize = 13f
        subtitle.setTextColor(Color.DKGRAY)

        val dashboardCard = LinearLayout(this)
        dashboardCard.orientation = LinearLayout.VERTICAL
        dashboardCard.setPadding(30, 25, 30, 25)

        val cardBg = GradientDrawable()
        cardBg.setColor(Color.WHITE)
        cardBg.cornerRadius = 22f
        dashboardCard.background = cardBg

        val dashboard = TextView(this)
        dashboard.text = "Scanning installed apps..."
        dashboard.textSize = 16f
        dashboard.setTypeface(null, Typeface.BOLD)
        dashboard.setTextColor(Color.BLACK)

        val pieChart = RiskPieChartView(this, criticalCount, highCount, mediumCount, lowCount)

        dashboardCard.addView(dashboard)
        dashboardCard.addView(pieChart)

        val searchBar = EditText(this)
        searchBar.hint = "🔍 Search apps..."
        searchBar.textSize = 15f
        searchBar.setPadding(25, 18, 25, 18)
        searchBar.setBackgroundColor(Color.WHITE)

        val filterLayout = LinearLayout(this)
        filterLayout.orientation = LinearLayout.HORIZONTAL

        val filters = listOf("ALL", "CRITICAL", "HIGH", "MEDIUM", "LOW")

        for (filter in filters) {
            val button = Button(this)
            button.text = filter
            button.textSize = 12f
            button.setOnClickListener {
                currentFilter = filter
                applySearchAndFilter(searchBar.text.toString())
            }
            filterLayout.addView(button)
        }

        val filterScroll = HorizontalScrollView(this)
        filterScroll.addView(filterLayout)

        val actionLayout = LinearLayout(this)
        actionLayout.orientation = LinearLayout.HORIZONTAL

        val reportButton = Button(this)
        reportButton.text = "📄 Export PDF\nSave report"
        reportButton.setOnClickListener {
            exportPdfReport()
        }

        val shareButton = Button(this)
        shareButton.text = "📤 Share PDF\nSend report"
        shareButton.setOnClickListener {
            sharePdfReport()
        }

        actionLayout.addView(reportButton)
        actionLayout.addView(shareButton)

        val sectionTitle = TextView(this)
        sectionTitle.text = "Installed Applications"
        sectionTitle.textSize = 16f
        sectionTitle.setTypeface(null, Typeface.BOLD)
        sectionTitle.setTextColor(Color.parseColor("#0B8F2A"))
        sectionTitle.setPadding(0, 25, 0, 10)

        recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        )

        mainLayout.addView(header)
        mainLayout.addView(subtitle)
        mainLayout.addView(dashboardCard)
        mainLayout.addView(searchBar)
        mainLayout.addView(filterScroll)
        mainLayout.addView(actionLayout)
        mainLayout.addView(sectionTitle)
        mainLayout.addView(recyclerView)

        setContentView(mainLayout)

        recyclerView.adapter = AppRiskAdapter(appRiskList)

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applySearchAndFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        Thread {
            scanInstalledApps()

            runOnUiThread {
                dashboard.text = """
📱 Total Apps: ${appRiskList.size}

🔴 Critical: $criticalCount
🟠 High: $highCount
🟡 Medium: $mediumCount
🟢 Low: $lowCount
""".trimIndent()

                dashboardCard.removeView(pieChart)

                val newPieChart = RiskPieChartView(
                    this,
                    criticalCount,
                    highCount,
                    mediumCount,
                    lowCount
                )

                dashboardCard.addView(newPieChart)

                recyclerView.adapter = AppRiskAdapter(appRiskList)
            }
        }.start()
    }

    private fun scanInstalledApps() {
        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        criticalCount = 0
        highCount = 0
        mediumCount = 0
        lowCount = 0
        appRiskList.clear()
        for (app in apps) {
            val appName = pm.getApplicationLabel(app).toString()
            val packageName = app.packageName
            val installerSource = getInstallerSource(packageName)
            val isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                    (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            val permissions = getPermissions(packageName)

            val findings = MalwareRuleEngine.detectPatterns(packageName, permissions)

            var riskScore = calculateRiskScore(permissions)
            if (isSystemApp) {
                riskScore = (riskScore * 0.15).toInt()
            }

            if (installerSource == "Google Play Store" && findings.isEmpty()) {
                riskScore = (riskScore * 0.4).toInt()
            }

            if (installerSource == "Unknown / Sideloaded" && findings.size >= 2 && !isSystemApp) {
                riskScore += 30
            }

            if (findings.size >= 2 && !isSystemApp) {
                riskScore += 20
            }
            if (installerSource == "Google Play Store") {
                riskScore = riskScore.coerceAtMost(69)
            }

            riskScore = riskScore.coerceAtMost(100)

            val riskLevel = getRiskLevel(riskScore)
            when (riskLevel) {
                "CRITICAL" -> criticalCount++
                "HIGH" -> highCount++
                "MEDIUM" -> mediumCount++
                "LOW" -> lowCount++
            }
            val timeline = generateTimeline(appName, riskScore, riskLevel)
            val explanation = generateExplanation(appName, packageName, permissions, riskLevel)
            appRiskList.add(
                AppRiskInfo(
                    appName = appName,
                    packageName = packageName,
                    permissions = permissions,
                    riskScore = riskScore,
                    riskLevel = riskLevel,
                    timeline = timeline,
                    installerSource = installerSource,
                    explanation = explanation,
                    findings = findings
                )
            )
        }

        appRiskList.sortByDescending { it.riskScore }
    }
    private fun getPermissions(packageName: String): List<String> {
        val permissions = mutableListOf<String>()

        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_PERMISSIONS
            )

            packageInfo.requestedPermissions?.forEach {
                permissions.add(it)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return permissions
    }

    private fun calculateRiskScore(permissions: List<String>): Int {
        var score = 0

        for (permission in permissions) {
            score += when {
                permission.contains("SMS") -> 10
                permission.contains("CONTACT") -> 10
                permission.contains("LOCATION") -> 10
                permission.contains("CAMERA") -> 8
                permission.contains("RECORD_AUDIO") -> 15
                permission.contains("CALL_LOG") -> 10
                permission.contains("PHONE_STATE") -> 5
                permission.contains("SYSTEM_ALERT_WINDOW") -> 20
                permission.contains("ACCESSIBILITY") -> 20
                else -> 0
            }
        }

        return score.coerceAtMost(100)
    }

    private fun getRiskLevel(score: Int): String {
        return when {
            score >= 90 -> "CRITICAL"
            score >= 70 -> "HIGH"
            score >= 40 -> "MEDIUM"
            else -> "LOW"
        }
    }
    private fun getInstallerSource(packageName: String): String {
        return try {
            val installer = packageManager.getInstallerPackageName(packageName)

            when (installer) {
                "com.android.vending" -> "Google Play Store"
                null -> "Unknown / Sideloaded"
                else -> installer
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }
    private fun generateTimeline(
        appName: String,
        score: Int,
        level: String
    ): List<String> {
        return listOf(
            "App detected: $appName",
            "Permissions scanned",
            "Risk score calculated: $score/100",
            "Risk level assigned: $level",
            "Security explanation generated"
        )
    }

    private fun generateExplanation(
        appName: String,
        packageName: String,
        permissions: List<String>,
        level: String
    ): String {
        if (permissions.isEmpty()) {
            return "$appName does not request dangerous permissions. Current risk level is $level."
        }

       val findings = MalwareRuleEngine.detectPatterns(packageName, permissions)

        if (findings.isNotEmpty()) {
            return """
Potential suspicious pattern detected:

${findings.joinToString("\n")}

Risk Level: $level
""".trimIndent()
        }

        return "$appName is marked as $level risk because it requests sensitive permissions. Review this app carefully if it is unknown or installed from outside Play Store."
    }
    private fun applySearchAndFilter(query: String) {

        val filteredList = appRiskList.filter { app ->

            val matchesSearch =
                app.appName.contains(query, ignoreCase = true) ||
                        app.packageName.contains(query, ignoreCase = true)

            val matchesFilter =
                currentFilter == "ALL" ||
                        app.riskLevel == currentFilter

            matchesSearch && matchesFilter
        }

        recyclerView.adapter = AppRiskAdapter(filteredList)
    }

    private fun exportPdfReport() {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = android.graphics.Paint()

            var y = 50

            paint.textSize = 22f
            paint.color = Color.BLACK
            canvas.drawText("ShieldDroid AI Security Report", 40f, y.toFloat(), paint)

            y += 40
            paint.textSize = 14f
            canvas.drawText("Total Apps: ${appRiskList.size}", 40f, y.toFloat(), paint)

            y += 25
            canvas.drawText("Critical: $criticalCount", 40f, y.toFloat(), paint)

            y += 25
            canvas.drawText("High: $highCount", 40f, y.toFloat(), paint)

            y += 25
            canvas.drawText("Medium: $mediumCount", 40f, y.toFloat(), paint)

            y += 25
            canvas.drawText("Low: $lowCount", 40f, y.toFloat(), paint)

            y += 40
            paint.textSize = 16f
            canvas.drawText("Top Risk Apps:", 40f, y.toFloat(), paint)

            y += 30
            paint.textSize = 12f

            appRiskList.take(10).forEachIndexed { index, app ->
                canvas.drawText(
                    "${index + 1}. ${app.appName} | ${app.riskLevel} | Score: ${app.riskScore}",
                    40f,
                    y.toFloat(),
                    paint
                )
                y += 22
            }

            pdfDocument.finishPage(page)

            val reportsDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

            val file = File(reportsDir, "ShieldDroid_Report.pdf")

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            Toast.makeText(this, "PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "PDF export failed", Toast.LENGTH_LONG).show()
        }
    }
    private fun sharePdfReport() {
        try {

            val pdfFile = File(
                getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "ShieldDroid_Report.pdf"
            )

            if (!pdfFile.exists()) {
                Toast.makeText(
                    this,
                    "Pehle PDF export karo",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            startActivity(
                Intent.createChooser(
                    intent,
                    "Share ShieldDroid Report"
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(
                this,
                "Share failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}