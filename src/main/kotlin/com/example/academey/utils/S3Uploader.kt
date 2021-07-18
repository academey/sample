package com.example.academey.utils

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.academey.config.AwsConfiguration
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

@Component
class S3Uploader(
    private val amazonS3Client: AmazonS3Client,
    private val awsConfiguration: AwsConfiguration
) {
    @Throws(IOException::class)
    fun upload(multipartFile: MultipartFile, dirName: String): String {
        val uploadFile: File =
            convert(multipartFile) ?: throw IllegalArgumentException("MultipartFile -> File 로의 변경이 실패했습니다")

        return upload(uploadFile, dirName)
    }

    private fun upload(uploadFile: File, dirName: String): String {
        val fileName = dirName + "/" + uploadFile.name
        val uploadImageUrl = putS3(uploadFile, fileName)
        removeNewFile(uploadFile)
        return uploadImageUrl
    }

    private fun putS3(uploadFile: File, fileName: String): String {
        amazonS3Client.putObject(
            PutObjectRequest(
                awsConfiguration.s3.bucket,
                fileName,
                uploadFile
            ).withCannedAcl(CannedAccessControlList.Private)
        )
        val s3Url = amazonS3Client.getUrl(awsConfiguration.s3.bucket, fileName).toString()

        return "https://${CLOUD_FRONT_URL}${s3Url.split(".com")[1]}"
    }

    private fun removeNewFile(targetFile: File) {
        if (targetFile.delete()) {
            log.info(java.lang.String.format("파일 (%s) 이 삭제되었습니다.", targetFile))
        } else {
            log.info(java.lang.String.format("파일 (%s) 이 삭제되지 못했습니다.", targetFile))
        }
    }

    @Throws(IOException::class)
    private fun convert(file: MultipartFile): File? {
        val ext = file.originalFilename!!.lastIndexOf(".").let { pos ->
            file.originalFilename!!.substring(pos + 1)
        }
        val convertFile = File("${UUID.randomUUID()}.$ext")
        if (convertFile.createNewFile()) {
            FileOutputStream(convertFile).use { fos ->
                fos.write(file.bytes)
                log.info(java.lang.String.format("파일 (%s)이 생성 되었습니다.", convertFile))
            }
            return convertFile
        }
        return null
    }

    companion object : UpsideLogger() {
        const val CLOUD_FRONT_URL = "d1w0d2nw5v0rx5.cloudfront.net"
    }
}
