package com.example.myshools.Mine
import android.util.Log
import com.example.myshools.Mine.organization.edit.GetOrganizationResult
import com.example.myshools.Mine.student.edit.GetStudentResult
import com.example.myshools.entity.*
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class PerisonalDataModel {
    private var client: OkHttpClient? = OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .build()
    //获取组织信息
    fun GetOrganizationById(callback: getOrgDataStatus, phone: String) {
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("phone", phone).build()
        val request = Request.Builder().url(Constant.url + "organization/getByPhone").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.getOrgDataFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val gson = Gson()
                val result = gson.fromJson<GetOrganizationResult>(string, GetOrganizationResult::class.java)
                if (result.code == 2000) {
                    callback.getOrgDataSuccess(result.data)

                } else {
                    callback.getOrgDataFailed(2)
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
    //修改组织信息
    fun updataOrgData(callback: updataOrgDataStatus, orgnaization: Orgnaization, img:LocalMedia){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("organization", Gson().toJson(orgnaization)) // 传递表单数据

        // 可使用for循环添加img file
        if (img!=null) {
                val file = File(img.getCompressPath())
                val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file) //把文件与类型放入请求体
                if (file.exists()) {
                    requestBodyBuilder.addFormDataPart("file", file.name, requestBody) //文件名,请求体里的文件
                }
            }

        // 3.3 其余一致
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "organization/update")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.updataOrgFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result=Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.updateOrgSuccess()
                }else{
                    callback.updataOrgFailed(2)
                }
            }
        })

    }
    fun updataOrgData(callback: updataOrgDataStatus, orgnaization: Orgnaization){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("organization", Gson().toJson(orgnaization)) // 传递表单数据

        // 可使用for循环添加img file

        // 3.3 其余一致
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "organization/update")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.updataOrgFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result=Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.updateOrgSuccess()
                }else{
                    callback.updataOrgFailed(2)
                }
            }
        })

    }
    //修改学生信息
    fun updataStuData(callback: updataStuDataStatus, student: Student, img:LocalMedia){
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("student", Gson().toJson(student)) // 传递表单数据

        // 可使用for循环添加img file
        if (img!=null) {
            val file = File(img.getCompressPath())
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file) //把文件与类型放入请求体
            if (file.exists()) {
                requestBodyBuilder.addFormDataPart("file", file.name, requestBody) //文件名,请求体里的文件
            }
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
                callback.updataStuFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result=Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if(result.code==2000){
                    callback.updataStuSuccess()
                }else{
                    callback.updataStuFailed(2)
                }
            }
        })

    }

    //修改组织信息
    interface updataOrgDataStatus{
        fun updataOrgFailed(int: Int)
        fun updateOrgSuccess()
    }
    //修改学生信息
    interface updataStuDataStatus{
        fun updataStuFailed(int: Int)
        fun updataStuSuccess()
    }

    interface getOrgDataStatus {
        fun getOrgDataFailed(int: Int)
        fun getOrgDataSuccess(orgnaization: Orgnaization)

    }

    interface getStuDataStatus {
        fun getStuDataFailed(int: Int)
        fun getStuDataSuccess(student: Student)

    }

}