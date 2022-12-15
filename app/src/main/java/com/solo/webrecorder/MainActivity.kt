package com.solo.webrecorder

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.solo.webrecorder.components.JavascriptOverlord
import com.solo.webrecorder.components.WebChromeOverlord
import com.solo.webrecorder.components.WebViewOverlord

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private val homeUrl = "192.168.123.246"
    private lateinit var webView: WebView
    private lateinit var editNewAddress: EditText
    private lateinit var textViewEventLog: TextView
    private lateinit var btnNav: Button
    private lateinit var btnBack: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnHome: Button
    private lateinit var btnForward: Button
    private lateinit var messageHandler: Handler
    private var eventLogs = ""
    private var eventCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init ui bindings
        setContentView(R.layout.activity_main)
        editNewAddress = findViewById(R.id.edit_new_address)
        btnNav = findViewById(R.id.btn_navigate)
        btnBack = findViewById(R.id.btn_nav_back)
        btnRefresh = findViewById(R.id.btn_nav_refresh)
        btnHome = findViewById(R.id.btn_nav_home)
        btnForward = findViewById(R.id.btn_nav_forward)
        editNewAddress.setText(homeUrl)
        textViewEventLog = findViewById(R.id.text_event_log)

        messageHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                this@MainActivity.onTextMessage(msg.obj?.toString())
            }
        }

        initWebView()
        navigate(homeUrl)
    }

    override fun onDestroy() {
        webView.removeAllViews()
        webView.clearHistory()
        webView.clearCache(true)
        webView.destroy()

        messageHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    fun onTextMessage(text:String?)
    {
        if (text == null) {
            Toast.makeText(this,"onTextMessage:null", Toast.LENGTH_SHORT).show()
            return
        }
        eventCount = eventCount + 1
        eventLogs = "event " + eventCount + ": "+ text + "\n" + eventLogs
        textViewEventLog.text = eventLogs
    }

    fun navigate(url: String) {
        eventLogs = ""
        eventCount = 0
        textViewEventLog.text = eventLogs
        webView.loadUrl(url);
    }

    fun reload() {
        eventLogs = ""
        eventCount = 0
        textViewEventLog.text = eventLogs
        webView.reload()
    }

    fun initWebView() {
        webView = findViewById(R.id.ghost_webview)
        webView.webViewClient = WebViewOverlord(messageHandler)
        webView.webChromeClient = WebChromeOverlord()
        webView.addJavascriptInterface(JavascriptOverlord(messageHandler, webView), "JSOL")

        var settings = webView.settings
        settings.allowContentAccess = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false

        // bind events:
        btnNav.setOnClickListener {
            if (editNewAddress.text.toString() == "") {
                return@setOnClickListener
            }
            webView.loadUrl(editNewAddress.text.toString());
        }
        btnBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }
        btnRefresh.setOnClickListener {
            reload()
        }
        btnHome.setOnClickListener {
            navigate(homeUrl)
        }
        btnForward.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }

    }


}