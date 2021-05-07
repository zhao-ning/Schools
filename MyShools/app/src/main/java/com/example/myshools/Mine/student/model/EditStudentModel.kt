package com.example.myshools.Mine.student.model

import com.example.myshools.Mine.student.edit.GetStudentResult
import com.example.myshools.entity.Constant
import com.example.myshools.entity.RestfulActivity
import com.example.myshools.entity.Student
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class EditStudentModel {
    private var client: OkHttpClient? = OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .build()
    //修改学生信息(无图像)
    fun doEditStudentData(callback:doEditStudentStataus,student:Student){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("student", Gson().toJson(student)) // 传递表单数据
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "student/update")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.doEditStudentFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result= Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.doEditStudentSuccess()
                }else{
                    callback.doEditStudentFailed(2)
                }
            }
        })
    }
    //修改学生信息（带头像）
    fun doEditStudentData(callback:doEditStudentStataus,student:Student,file1:LocalMedia){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("student", Gson().toJson(student)) // 传递表单数据

        // 可使用for循环添加img file
        if (file1!=null){
            val file11 = File(file1.compressPath)

            val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file11) //把文件与类型放入请求体
            requestBodyBuilder.addFormDataPart("file1", file11.name, requestBody) //文件名,请求体里的文件
        }else{
            callback.doEditStudentFailed(0)
        }

        // 3.3 其余一致
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "student/update")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.doEditStudentFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result= Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.doEditStudentSuccess()
                }else{
                    callback.doEditStudentFailed(2)
                }
            }
        })
    }
    //修改学生信息（没有头像，只有学生认证照片2张）
    fun doEditStudentData(callback:doEditStudentStataus,student:Student,file2:LocalMedia,file3: LocalMedia){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("student", Gson().toJson(student)) // 传递表单数据

        // 可使用for循环添加img file
        if (file2!=null&&file3!= null) {
            val file22 = File(file2.compressPath)
            val file33 = File(file3.compressPath)
            val requestBody2: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file22) //把文件与类型放入请求体
            requestBodyBuilder.addFormDataPart("file2", file22.name, requestBody2) //文件名,请求体里的文件
            val requestBody3: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file33) //把文件与类型放入请求体
            requestBodyBuilder.addFormDataPart("file3", file33.name, requestBody3) //文件名,请求体里的文件
        }else{
            callback.doEditStudentFailed(0)
        }

        // 3.3 其余一致
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "student/update")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.doEditStudentFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result= Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.doEditStudentSuccess()
                }else{
                    callback.doEditStudentFailed(2)
                }
            }
        })
    }
    //修改学生信息（包含头像，认证照片）
    fun doEditStudentData(callback:doEditStudentStataus,student:Student,file1:LocalMedia,file2: LocalMedia,file3: LocalMedia){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("student", Gson().toJson(student)) // 传递表单数据

        // 可使用for循环添加img file
        if (file1!=null&&file2!=null&&file3!= null) {
            val file11 = File(file1.compressPath)
            val file22 = File(file2.compressPath)
            val file33 = File(file3.compressPath)
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file11) //把文件与类型放入请求体
            requestBodyBuilder.addFormDataPart("file1", file11.name, requestBody) //文件名,请求体里的文件
            val requestBody2: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file22) //把文件与类型放入请求体
            requestBodyBuilder.addFormDataPart("file2", file22.name, requestBody2) //文件名,请求体里的文件
            val requestBody3: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file33) //把文件与类型放入请求体
            requestBodyBuilder.addFormDataPart("file3", file33.name, requestBody3) //文件名,请求体里的文件
        }else{
            callback.doEditStudentFailed(0)
        }

        // 3.3 其余一致
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "student/update")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.doEditStudentFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result= Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.doEditStudentSuccess()
                }else{
                    callback.doEditStudentFailed(2)
                }
            }
        })

    }
    //获取学生信息
    fun GetStudentDataByPhone(callback: getStuDataStatus, phone: String) {
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("phone", phone).build()
        val request = Request.Builder().url(Constant.url + "student/getByPhone").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.getStuDataFailed(1)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val gson = Gson()
                val result = gson.fromJson<GetStudentResult>(string, GetStudentResult::class.java)
                if (result.code == 2000) {
                    callback.getStuDataSuccess(result.data)
                } else {
                    callback.getStuDataFailed(2)
                }
            }
        })
    }
    interface getStuDataStatus {
        fun getStuDataFailed(int: Int)
        fun getStuDataSuccess(student: Student)

    }
    //修改学生信息状态
    interface doEditStudentStataus{
        fun doEditStudentFailed(int: Int)//0照片加载失败，1网络错误，2后处理端数据错误
        fun doEditStudentSuccess()
    }

}