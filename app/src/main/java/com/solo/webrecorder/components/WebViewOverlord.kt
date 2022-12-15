package com.solo.webrecorder.components

import android.graphics.Bitmap
import android.os.Handler
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewOverlord : WebViewClient {

    private var _messageHandler: Handler

    constructor(handler: Handler) {
        _messageHandler = handler
    }

    companion object {
        const val EventJS = """
            var DOMEvents = {
                UIEvent: "abort DOMActivate error load resize scroll select unload",
                ProgressEvent: "abort error load loadend loadstart progress progress timeout",
                Event: "abort afterprint beforeprint cached canplay canplaythrough change chargingchange chargingtimechange checking close dischargingtimechange DOMContentLoaded downloading durationchange emptied ended ended error error error error fullscreenchange fullscreenerror input invalid languagechange levelchange loadeddata loadedmetadata noupdate obsolete offline online open open orientationchange pause pointerlockchange pointerlockerror play playing ratechange readystatechange reset seeked seeking stalled submit success suspend timeupdate updateready visibilitychange volumechange waiting",
                AnimationEvent: "animationend animationiteration animationstart",
                AudioProcessingEvent: "audioprocess",
                BeforeUnloadEvent: "beforeunload",
                TimeEvent: "beginEvent endEvent repeatEvent",
                OtherEvent: "blocked complete upgradeneeded versionchange",
                FocusEvent: "blur DOMFocusIn  Unimplemented DOMFocusOut  Unimplemented focus focusin focusout",
                MouseEvent: "click contextmenu dblclick mousedown mouseenter mouseleave mousemove mouseout mouseover mouseup show",
                SensorEvent: "compassneedscalibration Unimplemented userproximity",
                OfflineAudioCompletionEvent: "complete",
                CompositionEvent: "compositionend compositionstart compositionupdate",
                ClipboardEvent: "copy cut paste",
                DeviceLightEvent: "devicelight",
                DeviceMotionEvent: "devicemotion",
                DeviceOrientationEvent: "deviceorientation",
                DeviceProximityEvent: "deviceproximity",
                MutationNameEvent: "DOMAttributeNameChanged DOMElementNameChanged",
                MutationEvent: "DOMAttrModified DOMCharacterDataModified DOMNodeInserted DOMNodeInsertedIntoDocument DOMNodeRemoved DOMNodeRemovedFromDocument DOMSubtreeModified",
                DragEvent: "drag dragend dragenter dragleave dragover dragstart drop",
                GamepadEvent: "gamepadconnected gamepaddisconnected",
                HashChangeEvent: "hashchange",
                KeyboardEvent: "keydown keypress keyup",
                MessageEvent: "message message message message",
                PageTransitionEvent: "pagehide pageshow",
                PopStateEvent: "popstate",
                StorageEvent: "storage",
                SVGEvent: "SVGAbort SVGError SVGLoad SVGResize SVGScroll SVGUnload",
                SVGZoomEvent: "SVGZoom",
                TouchEvent: "touchcancel touchend touchenter touchleave touchmove touchstart",
                TransitionEvent: "transitionend",
                WheelEvent: "wheel",
            };    
            for (var DOMEvent in DOMEvents) {
                var DOMEventTypes = DOMEvents[DOMEvent].split(' ');
                DOMEventTypes.filter(function (DOMEventType) {
                    var DOMEventCategory = DOMEvent;
                    document.addEventListener(DOMEventType, function (e) {
                        var extra = ""
                        if (DOMEventCategory == "TouchEvent") {
                            e0 = e.touches[0];
                            extra = extra + " x=" + e0.screenX + ",y=" + e0.screenY
                        }
                        globalThis.JSOL.record(""
                            + DOMEventCategory + ":" + DOMEventType
                            + extra);
                    }, true);
                });
            }
        """
    }

    private fun injectJS(webView: WebView) {
        try {
            webView.evaluateJavascript(EventJS, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (view != null) {
            injectJS(view)
        }
        super.onPageStarted(view, url, favicon)
    }

}