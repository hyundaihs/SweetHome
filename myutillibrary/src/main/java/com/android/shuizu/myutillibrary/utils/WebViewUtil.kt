package com.android.shuizu.myutillibrary.utils

import android.os.Build
import android.view.View
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION
import android.text.Html
import android.webkit.*
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK




/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/20.
 */
fun WebView.initImageAuto() {
    val settings = getSettings()
    // 设置WebView支持JavaScript
    settings.setJavaScriptEnabled(true)
    //支持自动适配
    settings.setUseWideViewPort(true)
    settings.setLoadWithOverviewMode(true)
//    settings.setSupportZoom(true)  //支持放大缩小
//    settings.setBuiltInZoomControls(true) //显示缩放按钮
//    settings.setBlockNetworkImage(true)// 把图片加载放在最后来加载渲染
//    settings.setAllowFileAccess(true) // 允许访问文件
//    settings.setSaveFormData(true)
//    settings.setGeolocationEnabled(true)
//    settings.setDomStorageEnabled(true)
//    settings.setJavaScriptCanOpenWindowsAutomatically(true)/// 支持通过JS打开新窗口
//    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS)
//    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
////不加这个图片显示不出来
//    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
//    }
//    settings.setBlockNetworkImage(false)


    webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val javascript = "javascript:function ResizeImages() {" +
                    "var myimg,oldwidth" +
                    "var maxwidth = document.body.clientWidth" +
                    "for(i=0i <document.images.lengthi++){" +
                    "myimg = document.images[i]" +
                    "if(myimg.width > maxwidth){" +
                    "oldwidth = myimg.width" +
                    "myimg.width = maxwidth" +
                    "}" +
                    "}" +
                    "}"
            val width = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            val height = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            measure(width, height)
            measuredWidth // 获取宽度
//                view.getMeasuredHeight() // 获取高度
//                val width = String.valueOf(ScreenUtils.widthPixels(mContext))
            loadUrl(javascript)
            loadUrl("javascript:ResizeImages()")
        }
    }
}

fun WebView.loadLocalHtml(html:String) {
    //不现实水平滚动条
    setHorizontalScrollBarEnabled(false)
    //不现实垂直滚动条
    setVerticalScrollBarEnabled(false)
//    //滚动条在WebView内侧显示
//    setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY)
//    //滚动条在WebView外侧显示
//    setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
//    //获取触摸焦点
//    requestFocusFromTouch()

    //声明WebSettings子类
    val webSettings = getSettings()

    //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//    webSettings.setJavaScriptEnabled(true)

    //设置自适应屏幕，两者合用
    webSettings.setUseWideViewPort(true) //将图片调整到适合webview的大小
    webSettings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小

    //缩放操作
//    webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提
//    webSettings.setBuiltInZoomControls(true) //设置内置的缩放控件。若为false，则该WebView不可缩放
//    webSettings.setDisplayZoomControls(false) //隐藏原生的缩放控件

    //其他细节操作
//    webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK) //关闭webview中缓存
//    webSettings.setAllowFileAccess(true) //设置可以访问文件
//    webSettings.setJavaScriptCanOpenWindowsAutomatically(true) //支持通过JS打开新窗口
//    webSettings.setLoadsImagesAutomatically(true) //支持自动加载图片
    webSettings.setDefaultTextEncodingName("utf-8")//设置编码格式

    loadData(html, "text/html", "utf-8")
}

fun WebView.load(content: String) {

    getSettings().setJavaScriptEnabled(true)
    //
    //addJavascriptInterface(JavascriptInterface(), "mainActivity")

    val data = Html.fromHtml(content).toString()
    //替换img属性
    val varjs =
        "<script type='text/javascript'> \nwindow.onload = function()\n{var \$img = document.getElementsByTagName('img');for(var p in  \$img){\$img[p].style.width = '100%'; \$img[p].style.height ='auto'}}</script>"

    //点击查看
    val jsimg =
        "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{mainActivity.startPhotoActivity(this.src);}}}"

    loadDataWithBaseURL(
        "http://www.youwebhost.com",
        varjs + data,
        "text/html",
        "UTF-8",
        null
    )

    setWebViewClient(object : WebViewClient() {
        override fun onPageFinished(webView: WebView, s: String) {
            webView.loadUrl("javascript:($jsimg)()")
        }
    })

}

class JavascriptInterface {
    @android.webkit.JavascriptInterface
    fun startPhotoActivity(imageUrl: String) {
        //根据URL查看大图逻辑

    }
}