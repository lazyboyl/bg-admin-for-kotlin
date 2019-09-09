package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.HashMap

/**
 * @author linzf
 * @since 2019-09-09
 * 类描述：实现文件上传
 */
@RestController
@RequestMapping(value = "/file")
class FileController {

    @Value("\${file.upload.path}")
    private val uploadPath: String? = null

    /**
     * 功能描述：实现文件上传
     * @param file 文件的file
     * @param idImageType 文件类型
     * @return
     */
    @PostMapping(value = "uploadFile", consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    fun uploadFile(@RequestParam("file") file: MultipartFile,
                   @RequestParam("idImageType") idImageType: String?): ReturnInfo {
        var idImageType = idImageType
        if (file.isEmpty) {
            return ReturnInfo(SystemStaticConst.FAIL, "上传文件不能为空！")
        }
        if (idImageType == null || "" == idImageType) {
            idImageType = "tmp"
        }
        // 获取文件名
        var fileName = file.originalFilename ?: return ReturnInfo(SystemStaticConst.FAIL, "上传文件不能为空！")
        println("上传的文件名为：$fileName")
        // 获取文件的后缀名
        val suffixName = fileName.substring(fileName.lastIndexOf("."))
        println("上传的后缀名为：$suffixName")
        // 文件上传后的路径
        var filePath = uploadPath
        if (filePath == null || "".equals(filePath, ignoreCase = true)) {
            filePath = "E://test//"
        }
        filePath = "$filePath$idImageType//"
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UuidUtils.getUUID() + suffixName
        val dest = File(filePath + fileName!!)
        // 检测是否存在目录
        if (!dest.parentFile.exists()) {
            dest.parentFile.mkdirs()
        }
        try {
            file.transferTo(dest)
            val entity = HashMap<String, Any>(2)
            entity["name"] = "$idImageType/$fileName"
            entity["url"] = "$idImageType/$fileName"
            return ReturnInfo(SystemStaticConst.SUCCESS, "上传文件成功！", entity)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return ReturnInfo(SystemStaticConst.FAIL, "上传文件失败！请联系管理员！")
        } catch (e: IOException) {
            e.printStackTrace()
            return ReturnInfo(SystemStaticConst.FAIL, "上传文件失败！请联系管理员！")
        }

    }

}