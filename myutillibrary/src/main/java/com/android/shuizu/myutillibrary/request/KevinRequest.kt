package com.android.shuizu.myutillibrary.request

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.utils.getLoadigDialog
import com.android.shuizu.myutillibrary.utils.getLoginErrDialog
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

open class RequestResult(
    val retInt: Int = 0,
    val retErr: String = "",
    val retUrl: String = "",
    val retCounts: Int = 0
) {
    override fun toString(): String {
        return "RequestResult(retInt=$retInt, retErr='$retErr', retCounts=$retCounts)"
    }
}

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/31/031.
 */
class KevinRequest private constructor(val context: Context) {

    companion object {
        var sessionId: String = ""

        private val MEDIA_TYPE_JSON =
            "application/x-www-form-urlencoded charset=utf-8".toMediaTypeOrNull()//mdiatype 这个需要和服务端保持一致

        const val LOGINERR = "loginerr"//需要重新登录错误信息

        fun build(c: Context): KevinRequest {
            return KevinRequest(c)
        }

        private val mOkHttpClient: OkHttpClient by lazy {
            val httpBuilder = OkHttpClient.Builder()
            httpBuilder
                //设置超时
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(150, TimeUnit.SECONDS)
                .build()
        }
    }

    private var successCallback: SuccessCallback? = null
    private var errorCallback: ErrorCallback? = null
    private var loginErrCallback: LoginErrCallback? = null
    private var progressCallback: ProgressCallback? = null
    private var downloadSuccessCallback: DownloadSuccessCallback? = null
    private var requestUrl: String = ""
    private var dialog: Dialog? = null
    private var map: Map<String, Any>? = null

    fun setRequestUrl(url: String): KevinRequest {
        requestUrl = url
        return this
    }

    fun setDialog(): KevinRequest {
        dialog = getLoadigDialog(context)
        return this
    }

    fun setSuccessCallback(c: SuccessCallback): KevinRequest {
        successCallback = c
        return this
    }

    fun setErrorCallback(c: ErrorCallback): KevinRequest {
        errorCallback = c
        return this
    }

    fun setLoginErrCallback(c: LoginErrCallback): KevinRequest {
        loginErrCallback = c
        return this
    }

    fun openLoginErrCallback(cls: Class<*>) {
        setLoginErrCallback(object : KevinRequest.LoginErrCallback {
            override fun onLoginErr(context: Context) {
                getLoginErrDialog(context, object : DialogUIListener() {
                    override fun onNegative() {

                    }

                    override fun onPositive() {
                        context.startActivity(Intent(context, cls))
                        (context as Activity).finish()
                    }
                })

            }
        })
    }

    fun setProgressCallback(c: ProgressCallback): KevinRequest {
        progressCallback = c
        return this
    }

    fun setDownloadSuccessCallback(c: DownloadSuccessCallback): KevinRequest {
        downloadSuccessCallback = c
        return this
    }

    fun setDataMap(m: Map<String, Any>): KevinRequest {
        map = m
        return this
    }

    fun request() {
        dialog?.show()
        context.doAsync {
            val request = Request.Builder().url(requestUrl).addHeader("cookie", sessionId).build()
            val call = mOkHttpClient.newCall(request)
            //请求加入调度
            val response = call.execute()
            if (response.isSuccessful) {
                val string = response.body!!.string()
                getSession(response)
                D("requestResult = $string")
                uiThread {
                    dialog?.dismiss()
                    successCallback?.onSuccess(context, string)
                }
            } else {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, response.message)
                }
            }
        }
    }

    fun getRequest() {
        dialog?.show()
        context.doAsync {
            val request = Request.Builder().url(requestUrl).addHeader("cookie", sessionId).build()
            val call = mOkHttpClient.newCall(request)
            //请求加入调度
            val response = call.execute()
            if (response.isSuccessful) {
                val string = response.body!!.string()
                D("requestResult = $string")
                getSession(response)
                val res = Gson().fromJson(string, RequestResult::class.java)
                if (res.retInt == 1) {
                    uiThread {
                        dialog?.dismiss()
                        successCallback?.onSuccess(context, string)
                    }
                } else {
                    if (res.retErr == LOGINERR) {
                        uiThread {
                            dialog?.dismiss()
                            loginErrCallback?.onLoginErr(context)
                        }
                    } else {
                        uiThread {
                            dialog?.dismiss()
                            errorCallback?.onError(context, res.retErr)
                        }
                    }
                }
            } else {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, response.message)
                }
            }
        }
    }

    fun postRequest() {
        dialog?.show()
        context.doAsync {
            val requestBody = Gson().toJson(map).toRequestBody(MEDIA_TYPE_JSON)
            val request =
                Request.Builder().url(requestUrl).post(requestBody).addHeader("cookie", sessionId)
                    .build()
            try {
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val string = response.body!!.string()
                    D("requestResult = $string")
                    getSession(response)
                    val res: RequestResult = Gson().fromJson(string, RequestResult::class.java)
                    if (res.retInt == 1) {
                        uiThread {
                            dialog?.dismiss()
                            successCallback?.onSuccess(context, string)
                        }
                    } else {
                        if (res.retErr == LOGINERR) {
                            uiThread {
                                dialog?.dismiss()
                                loginErrCallback?.onLoginErr(context)
                            }
                        } else {
                            uiThread {
                                dialog?.dismiss()
                                errorCallback?.onError(context, res.retErr)
                            }
                        }
                    }
                } else {
                    uiThread {
                        dialog?.dismiss()
                        errorCallback?.onError(context, response.message)
                    }
                }
            } catch (e: Exception) {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, e.toString())
                }
            }
        }
    }

    fun uploadFile(files: ArrayList<String>) {
        dialog?.show()
        doAsync {
            val name = if (files.size > 1) {
                "uploadedfile[]"
            } else {
                "uploadedfile"
            }
            val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            for (i in 0 until files.size) {
                val file = File(files[i])
                val fileBody =
                    file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                requestBodyBuilder.addFormDataPart(
                    name,
                    files[i].substring(files[i].lastIndexOf("/")),
                    fileBody
                )
            }
            val requestBody = requestBodyBuilder.build()
            val request = Request.Builder()
                .url(requestUrl)
                .post(requestBody).addHeader("cookie", sessionId)
                .build()
            try {
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val string = response.body!!.string()
                    getSession(response)
                    val res: RequestResult = Gson().fromJson(string, RequestResult::class.java)
                    if (res.retInt == 1) {
                        uiThread {
                            dialog?.dismiss()
                            successCallback?.onSuccess(context, string)
                        }
                    } else {
                        if (res.retErr == LOGINERR) {
                            uiThread {
                                dialog?.dismiss()
                                loginErrCallback?.onLoginErr(context)
                            }
                        } else {
                            uiThread {
                                dialog?.dismiss()
                                errorCallback?.onError(context, res.retErr)
                            }
                        }
                    }
                } else {
                    uiThread {
                        dialog?.dismiss()
                        errorCallback?.onError(context, "无响应:" + response.message)
                    }
                }
            } catch (e: Exception) {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, "异常:" + e.toString())
                }
            }
        }
    }

    fun downLoadFile(destFileDir: String) {
        doAsync {
            val request = Request.Builder().url(requestUrl).addHeader("cookie", sessionId).build()
            mOkHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    uiThread {
                        errorCallback?.onError(context, e.toString())
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    var ips: InputStream? = null
                    val buf = ByteArray(2048)
                    var len: Int
                    var fos: FileOutputStream? = null
//                FileUtil.isFileExist(destFileDir)
                    // 储存下载文件的目录
//                val savePath = isExistDir(destFileDir)
                    try {
                        ips = response.body!!.byteStream()
                        val total = response.body!!.contentLength()
                        val file = File(destFileDir)
                        fos = FileOutputStream(file)
                        var sum = 0
                        while (true) {
                            len = ips.read(buf)
                            if (len == -1) {
                                break
                            }
                            fos.write(buf, 0, len)
                            sum += len
                            val progress = (sum * 1.0f / total * 100).toLong()
                            // 下载中
                            uiThread {
                                progressCallback?.onProgress(total, progress)
                            }
                        }
                        fos.flush()
                        uiThread {
                            downloadSuccessCallback?.onDownloadSucces(file)
                        }
                    } catch (e: Exception) {
                        uiThread {
                            errorCallback?.onError(context, e.toString())
                        }
                    } finally {
                        try {
                            if (ips != null)
                                ips.close()
                        } catch (e: IOException) {
                        }
                        try {
                            if (fos != null)
                                fos.close()
                        } catch (e: IOException) {
                        }
                    }
                }

            })
        }
    }


    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private fun isExistDir(saveDir: String): String {
        // 下载位置
        val downloadFile = File(Environment.getExternalStorageDirectory(), saveDir)
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile()
        }
        return downloadFile.absolutePath
    }

    private fun getSession(response: Response) {
        val headers = response.headers
        val cookies = headers.values("Set-Cookie")
        if (cookies.isEmpty()) {
            return
        }
        val session = cookies[0]
        sessionId = session.substring(0, session.indexOf(";"))
    }


    interface ProgressCallback {
        fun onProgress(total: Long, progress: Long)
    }

    interface DownloadSuccessCallback {
        fun onDownloadSucces(file: File)
    }

    interface SuccessCallback {
        fun onSuccess(context: Context, result: String)
    }

    interface ErrorCallback {
        fun onError(context: Context, error: String)
    }

    interface LoginErrCallback {
        fun onLoginErr(context: Context)
    }

}