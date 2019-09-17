package com.android.shuizu.myutillibrary.utils

import android.app.Activity
import android.content.Intent
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
fun Activity.PictureSelectorStart(
    maxNum: Int,
    requestCode: Int,
    enableCrop: Boolean = false,
    compress: Boolean = true
) {
    PictureSelector.create(this)
        .openGallery(PictureMimeType.ofImage())
        .enableCrop(enableCrop)
        .compress(compress)
        .withAspectRatio(1, 1)
        .minimumCompressSize(100)
        .freeStyleCropEnabled(true)
        .maxSelectNum(maxNum)
        .forResult(requestCode)
}

// 图片、视频、音频选择结果回调
// 例如 LocalMedia 里面返回三种path
// 1.media.getPath(); 为原图path
// 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
// 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
// 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
fun PictureSelectorObtainMultipleResult(
    data: Intent?,
    enableCrop: Boolean = false,
    compress: Boolean = true
): List<String> {
    val result = ArrayList<String>()
    val selectList = PictureSelector.obtainMultipleResult(data)
    for (i in 0 until selectList.size) {
        val file = if (enableCrop and compress) {
            selectList[i].compressPath
        } else if (enableCrop) {
            selectList[i].cutPath
        } else if (compress) {
            selectList[i].compressPath
        } else {
            selectList[i].path
        }
        result.add(file)
    }
    return result
}