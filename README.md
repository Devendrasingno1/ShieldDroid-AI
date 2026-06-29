# 🛡 ShieldDroid-AI

> AI-Powered Android Malware Detection Application

ShieldDroid-AI is an Android security application that analyzes installed applications using permission analysis, behavior-based detection rules, and AI-inspired risk scoring to identify potentially malicious apps before they can harm the device.

<img width="1536" height="1024" alt="banner" src="https://github.com/user-attachments/assets/5b1c9328-02e1-4ff8-ab58-b138c30ab9b7" />

---

## 📱 Features

✅ Scan all installed applications

✅ AI-inspired malware detection engine

✅ Permission-based risk analysis

✅ Trusted app whitelist

✅ Malware pattern detection

✅ Risk Score Generation

✅ Detailed App Information

✅ Beautiful Dashboard UI

✅ Pie Chart Risk Visualization

✅ Fast Local Scanning

✅ Lightweight & Privacy Friendly

---

# 📸 Screenshots

| Home Screen | Scan Results |
|-------------|--------------|
|<img width="581" height="1280" alt="WhatsApp Image 2026-06-30 at 12 28 15 AM" src="https://github.com/user-attachments/assets/51a98179-d43b-4556-bb13-8240acd89ed3" />
|<img width="581" height="1280" alt="WhatsApp Image 2026-06-30 at 12 28 16 AM (1)" src="https://github.com/user-attachments/assets/0b46e2c0-e7ba-4042-9ec4-ef4c54dfd301" /><img width="581" height="1280" alt="WhatsApp Image 2026-06-30 at 12 28 15 AM (1)" src="https://github.com/user-attachments/assets/53f32fab-0fca-4243-9ce0-bf9c9b2b94e2" />
 |

| Risk Details | Dashboard |
|--------------|-----------|
|<img width="581" height="1280" alt="WhatsApp Image 2026-06-30 at 12 28 15 AM (2)" src="https://github.com/user-attachments/assets/82d755f6-6a22-4ee2-a463-a5b041b7c17f" />
 | <img width="581" height="1280" alt="WhatsApp Image 2026-06-30 at 12 28 16 AM" src="https://github.com/user-attachments/assets/4f7c57b4-1a51-4d4c-a024-a6c73b858324" />
 |

---

# 🧠 Malware Detection Engine

ShieldDroid-AI uses multiple detection layers.

### Layer 1
Permission Analysis

Checks dangerous permissions like

- READ_SMS
- SEND_SMS
- RECEIVE_SMS
- RECORD_AUDIO
- READ_CONTACTS
- READ_CALL_LOG
- WRITE_SETTINGS
- PACKAGE_USAGE_STATS

---

### Layer 2
Behavior Rules

Detects suspicious combinations of permissions.

Example

```
READ_SMS
+
SEND_SMS
+
RECEIVE_BOOT_COMPLETED
```

↓

Possible SMS Malware

---

### Layer 3
Trusted Application Whitelist

Apps like

- WhatsApp
- Telegram
- Chrome
- Gmail
- YouTube

are ignored to reduce false positives.

---

### Layer 4
Risk Scoring

Every detected app receives a Risk Score based on

- Dangerous Permissions
- Suspicious Patterns
- App Category
- Detection Rules

---

# 📊 Application Workflow

```
Installed Apps

↓

Permission Scanner

↓

Malware Rule Engine

↓

Risk Score Generator

↓

Dashboard

↓

Detailed Report
```

---

# 🛠 Tech Stack

- Kotlin
- Android SDK
- Android Studio
- RecyclerView
- Custom Views
- Material Design
- Canvas API
- XML Layouts
- Gradle

---

# 📂 Project Structure

```
app/

├── java/

│ ├── MainActivity

│ ├── SplashActivity

│ ├── MalwareRuleEngine

│ ├── AppRiskAdapter

│ ├── RiskPieChartView

│ └── AppDetailActivity

│

├── res/

│ ├── layout

│ ├── drawable

│ ├── values

│ └── mipmap

│

└── AndroidManifest.xml
```

---

# 🚀 Future Improvements

- AI Model Integration

- APK Static Analysis

- VirusTotal API

- Network Traffic Monitoring

- Real-time Threat Detection

- Cloud Dashboard

- PDF Report Export

- Explainable AI (XAI)

- Malware Timeline

- Behavioral Learning

---

# 🎯 Target Users

- Android Users

- Security Researchers

- Ethical Hackers

- Students

- SOC Analysts

- Cybersecurity Professionals

---

# 📈 Project Status

✅ Active Development

Version

```
v1.0
```

---

# 👨‍💻 Author

**Devendrasing Rajput**

Computer Engineering Student

Cybersecurity & AI Enthusiast

GitHub

https://github.com/Devendrasingno1

---

# ⭐ Support

If you like this project,

⭐ Star this repository.

---

# 📜 License

MIT License
