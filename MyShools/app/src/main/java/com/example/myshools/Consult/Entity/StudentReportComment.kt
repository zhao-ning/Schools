package com.example.myshools.Consult.Entity

/*
* 动态下的评论数据类
* */
class StudentReportComment {
    private var id:Int=0;
    private var parentId:Int=0
    private var userId:Int=0
    private lateinit var content:String
    private lateinit var cteateTime:String
    private var reportId :Int=0
    private var status:Int=0
}