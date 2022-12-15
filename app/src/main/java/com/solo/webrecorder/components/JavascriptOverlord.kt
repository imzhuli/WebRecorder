package com.solo.webrecorder.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast


class JavascriptOverlord {

    private lateinit var _messageHandler: Handler
    private lateinit var _webview: WebView

    constructor(handler: Handler, webView: WebView) {
        _messageHandler = handler
        _webview = webView
    }

    // 开放 test 方法给 js，在 web 页面使用 window.test('hello'); 调用。
    @JavascriptInterface
    fun test() {
        record("test from page")
    }

    @JavascriptInterface
    fun record(eventString: String) {
        var msg = Message()
        msg.obj = eventString
        _messageHandler.sendMessage(msg)
    }

}