package com.xanime.search

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 11, 16)
    private val panel = Color.rgb(18, 24, 33)
    private val green = Color.rgb(56, 229, 138)
    private val blue = Color.rgb(62, 155, 255)
    private var webView: WebView? = null
    private var homeView: View? = null
    private var showingResults = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        clearLocalWebData()
        showHome()
    }

    private fun clearLocalWebData() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        WebView(this).apply {
            clearCache(true)
            clearHistory()
            clearFormData()
            destroy()
        }
    }

    private fun showHome() {
        showingResults = false
        webView?.let { (it.parent as? android.view.ViewGroup)?.removeView(it); it.destroy() }
        webView = null

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(24), dp(28), dp(24), dp(24))
            setBackgroundColor(bg)
        }

        val spacer = View(this)
        root.addView(spacer, LinearLayout.LayoutParams(1, 0, 1f))

        val logo = TextView(this).apply {
            text = "X-ANIME"
            textSize = 36f
            setTextColor(green)
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
            gravity = Gravity.CENTER
            letterSpacing = 0.08f
        }
        root.addView(logo, LinearLayout.LayoutParams(-1, -2))

        val subtitle = TextView(this).apply {
            text = "EXPLORE EVERYTHING"
            textSize = 11f
            setTextColor(blue)
            gravity = Gravity.CENTER
            letterSpacing = 0.22f
            setPadding(0, dp(7), 0, dp(26))
        }
        root.addView(subtitle)

        val join = Button(this).apply {
            text = "✦  JOIN X-ANIME ON TELEGRAM"
            textSize = 13f
            isAllCaps = false
            setTextColor(Color.BLACK)
            setBackgroundColor(green)
            setOnClickListener {
                val url = "https://t.me/+HCtqGjybzAwzZGU1"
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (_: Exception) {
                    Toast.makeText(this@MainActivity, "Could not open Telegram link", Toast.LENGTH_SHORT).show()
                }
            }
        }
        root.addView(join, LinearLayout.LayoutParams(-1, dp(52)))

        val searchLabel = TextView(this).apply {
            text = "SEARCH THE WEB"
            textSize = 11f
            setTextColor(Color.LTGRAY)
            letterSpacing = 0.14f
            setPadding(2, dp(28), 0, dp(10))
        }
        root.addView(searchLabel, LinearLayout.LayoutParams(-1, -2))

        val searchBox = EditText(this).apply {
            hint = "What are you looking for?"
            textSize = 16f
            singleLine = true
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setTextColor(Color.WHITE)
            setHintTextColor(Color.rgb(145, 155, 170))
            setPadding(dp(15), 0, dp(15), 0)
            setBackgroundColor(panel)
            setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                    performSearch(text.toString())
                    true
                } else false
            }
        }
        root.addView(searchBox, LinearLayout.LayoutParams(-1, dp(54)))

        val searchButton = Button(this).apply {
            text = "SEARCH  →"
            textSize = 14f
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(blue)
            setOnClickListener { performSearch(searchBox.text.toString()) }
        }
        val buttonParams = LinearLayout.LayoutParams(-1, dp(50))
        buttonParams.topMargin = dp(12)
        root.addView(searchButton, buttonParams)

        val note = TextView(this).apply {
            text = "Simple. Fast. No app search history."
            textSize = 12f
            setTextColor(Color.rgb(145, 155, 170))
            gravity = Gravity.CENTER
            setPadding(0, dp(20), 0, 0)
        }
        root.addView(note, LinearLayout.LayoutParams(-1, -2))

        val bottomSpacer = View(this)
        root.addView(bottomSpacer, LinearLayout.LayoutParams(1, 0, 1f))
        homeView = root
        setContentView(root)
        searchBox.requestFocus()
    }

    private fun performSearch(rawQuery: String) {
        val query = rawQuery.trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Type something to search", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } catch (_: Exception) { }

        showingResults = true
        val root = FrameLayout(this).apply { setBackgroundColor(bg) }
        val web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.setSupportZoom(true)
        web.settings.builtInZoomControls = true
        web.settings.displayZoomControls = false
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()
        web.clearHistory()
        web.loadUrl("https://www.google.com/search?q=" + Uri.encode(query))
        webView = web
        root.addView(web, FrameLayout.LayoutParams(-1, -1))

        val topBar = TextView(this).apply {
            text = "‹   X-ANIME SEARCH"
            textSize = 14f
            setTextColor(green)
            setBackgroundColor(bg)
            setPadding(dp(16), dp(12), dp(16), dp(12))
            setOnClickListener { showHome() }
        }
        val params = FrameLayout.LayoutParams(-1, dp(48), Gravity.TOP)
        root.addView(topBar, params)
        web.setPadding(0, dp(48), 0, 0)
        setContentView(root)
    }

    override fun onBackPressed() {
        if (showingResults) {
            val web = webView
            if (web != null && web.canGoBack()) {
                web.goBack()
            } else {
                showHome()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView?.apply {
            clearHistory()
            clearCache(true)
            clearFormData()
            (parent as? android.view.ViewGroup)?.removeView(this)
            destroy()
        }
        clearLocalWebData()
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
