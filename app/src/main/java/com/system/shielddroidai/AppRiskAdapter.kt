package com.system.shielddroidai

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.content.Intent

class AppRiskAdapter(
    private val appList: List<AppRiskInfo>
) : RecyclerView.Adapter<AppRiskAdapter.AppViewHolder>() {

    class AppViewHolder(
        val layout: LinearLayout
    ) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppViewHolder {

        val layout = LinearLayout(parent.context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30, 30, 30, 30)

        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(20, 20, 20, 20)
        layout.layoutParams = params

        return AppViewHolder(layout)
    }

    override fun onBindViewHolder(
        holder: AppViewHolder,
        position: Int
    ) {

        val app = appList[position]
        val pm = holder.layout.context.packageManager

        val icon = android.widget.ImageView(holder.layout.context)

        try {
            icon.setImageDrawable(
                pm.getApplicationIcon(app.packageName)
            )
        } catch (e: Exception) {
        }

        icon.layoutParams = LinearLayout.LayoutParams(
            100,
            100
        )
        val cardColor = when (app.riskLevel) {
            "CRITICAL" -> Color.parseColor("#FFEBEE")
            "HIGH" -> Color.parseColor("#FFF3E0")
            "MEDIUM" -> Color.parseColor("#FFFDE7")
            else -> Color.parseColor("#E8F5E9")
        }

        val borderColor = when (app.riskLevel) {
            "CRITICAL" -> Color.parseColor("#F44336")
            "HIGH" -> Color.parseColor("#FF9800")
            "MEDIUM" -> Color.parseColor("#FFC107")
            else -> Color.parseColor("#4CAF50")
        }

        val background = GradientDrawable()
        background.setColor(cardColor)
        background.cornerRadius = 24f
        background.setStroke(3, borderColor)

        holder.layout.background = background

        holder.layout.removeAllViews()

        val title = TextView(holder.layout.context)
        title.text = "${app.appName} (${app.riskLevel})"
        title.textSize = 18f
        title.setTextColor(
            when (app.riskLevel) {
                "CRITICAL" -> Color.RED
                "HIGH" -> Color.rgb(255, 140, 0)
                "MEDIUM" -> Color.rgb(180, 140, 0)
                else -> Color.rgb(0, 120, 0)
            }
        )
        val badge = TextView(holder.layout.context)

        badge.text = app.riskLevel

        badge.setPadding(20,10,20,10)

        badge.setTextColor(Color.WHITE)

        badge.setBackgroundColor(
            when(app.riskLevel){
                "CRITICAL" -> Color.RED
                "HIGH" -> Color.parseColor("#FF9800")
                "MEDIUM" -> Color.parseColor("#FFC107")
                else -> Color.parseColor("#4CAF50")
            }
        )
        val score = TextView(holder.layout.context)
        score.text = "Risk Score: ${app.riskScore}"
        score.setTextColor(Color.DKGRAY)
        val installer = TextView(holder.layout.context)
        installer.text = "Installed From: ${app.installerSource}"
        installer.setTextColor(Color.DKGRAY)
        val findingsView = TextView(holder.layout.context)

        findingsView.text =
            if (app.findings.isNotEmpty()) {
                "Threat Findings:\n• " +
                        app.findings.joinToString("\n• ")
            } else {
                "Threat Findings:\n• No suspicious behavior detected"
            }

        findingsView.setTextColor(Color.DKGRAY)
        val explanation = TextView(holder.layout.context)
        explanation.text = app.explanation
        explanation.setTextColor(Color.DKGRAY)

        holder.layout.addView(icon)
        holder.layout.addView(title)
        holder.layout.addView(badge)
        holder.layout.addView(score)
        holder.layout.addView(installer)
        holder.layout.addView(findingsView)
        holder.layout.addView(explanation)
        holder.layout.setOnClickListener {
            val intent = Intent(holder.layout.context, AppDetailActivity::class.java)

            intent.putExtra("appName", app.appName)
            intent.putExtra("packageName", app.packageName)
            intent.putExtra("riskScore", app.riskScore)
            intent.putExtra("riskLevel", app.riskLevel)
            intent.putExtra("installerSource", app.installerSource)
            intent.putExtra("explanation", app.explanation)

            holder.layout.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }
}