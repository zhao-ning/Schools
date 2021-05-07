package com.example.myshools.Consult.Entity

import com.example.myshools.entity.Student
/*
* 每条动态信息
* */



class Share {
    var id:Int =0
    var clickNum:Int=0
    lateinit var publishTime:String
     lateinit var content:String
     lateinit var imgList:String
     var type:Int=0
     var viewNum=0
     lateinit var Comments:MutableList<StudentReportComment>
     lateinit var student: Student
}