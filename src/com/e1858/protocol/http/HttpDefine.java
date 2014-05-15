package com.e1858.protocol.http;

public class HttpDefine
{
	public static final int	RESPONSE_SUCCESS =  0;

	public static final int LOGIN=1002;
	public static final int LOGIN_RESP=91002;
	
	public static final int RESET_PASS=1004;
	public static final int RESET_PASS_RESP=91004;
	
	public static final int CHANGE_PASS=1009;
	public static final int CHANGE_PASS_RESP=91009;
	
	
	public static final int UPLOAD=1801;
	public static final int UPLOAD_RESP=91801;
	
	public static final int DOWNLOAD=1802;
	public static final int DOWNLOAD_RESP=91802;
	
	
	public static final int GET_SCHOOL_LIST=2011;
	public static final int GET_SCHOOL_LIST_RESP=92011;
	
	public static final int GET_DEPARTMENT_LIST=2012;
	public static final int GET_DEPARTMENT_LIST_RESP=92012;
	
	public static final int GET_CLASS_LIST=2013;
	public static final int GET_CLASS_LIST_RESP=92013;
	
	public static final int GET_TEACHER_LIST=2014;//获取教师列表
	public static final int GET_TEACHER_LIST_RESP=92014;
	
	public static final int GET_TEACHERINFO=2015;//获取教师信息
	public static final int GET_TEACHERINFO_RESP=92015;
	
	public static final int EDIT_TEACHERINFO=2016;//修改教师信息
	public static final int EDIT_TEACHERINFO_RESP=92016;
	
	
	public static final int GET_STUDENT_LIST=2017;//获取学生列表
	public static final int GET_STUDENT_LIST_RESP=92017;
	
	public static final int GET_STUDENTINFO=2018;//获取学生信息
	public static final int GET_STUDENTINFO_RESP=92018;
	
	public static final int EDIT_STUDENTINFO=2019;//更改学生信息
	public static final int EDIT_STUDENTINFO_RESP=92019;
	
	public static final int GET_SCHOOLMODULE_LIST=2101;//获取学校选择模块
	public static final int GET_SCHOOLMODULE_LIST_RESP=92101;
	
	public static final int GET_UNREADNOTICE_LIST=2111;//获取最新公告列表
	public static final int GET_UNREADNOTICE_LIST_RESP=92111;
	
	public static final int GET_NOTICE=2112;//获取公告详细
	public static final int GET_NOTICE_RESP=92112;
		
	public static final int ADD_NOTICE=2113;//新增公告
	public static final int ADD_NOTICE_RESP=92113;
	
	public static final int GET_UNREADMSG_LIST=2121;//获取未读消息列表
	public static final int GET_UNREADMSG_LIST_RESP=92121;
	
	public static final int SEND_MSG=2122;
	public static final int SEND_MSG_RESP=92122;
	
	public static final int SEND_DELIVER=2123;
	public static final int SEND_DELIVER_RESP=92123;
	
	
	public static final int GET_EJOURNAL_LIST=2131;//获取电子校刊
	public static final int GET_EJOURNAL_LIST_RESP=92131;
	
	public static final int GET_EJOURNAL=2132;//电子校刊详细
	public static final int GET_EJOURNAL_RESP=92132;
	
	
	public static final int GET_SCHOOLTIMETABLE=2141;//获取班级课程表
	public static final int GET_SCHOOLTIMETABLE_RESP=92141;
	
	public static final int GET_COURSE_LIST=2142;//获取已存在课程列表
	public static final int GET_COURSE_LIST_RESP=92142;
	
	public static final int GET_COURSE=2143;//获取课程详细信息
	public static final int GET_COURSE_RESP=92143;
	
	public static final int ADD_COURSE=2144;//新增课程
	public static final int ADD_COURSE_RESP=92144;
	
	public static final int ADD_COURSETIMES=2145;//新增课程安排
	public static final int ADD_COURSETIMES_RESP=92145;
	
	public static final int GET_LIBRARYGROUP_LIST=2151;
	public static final int GET_LIBRARYGROUP_LIST_RESP=92151;
	
	public static final int GET_LIBRARY_LIST=2152;
	public static final int GET_LIBRARY_LIST_RESP=92152;
	
	public static final int GET_LIBRARY=2153;
	public static final int GET_LIBRARY_RESP=92153;
	
	public static final int CHECK_VERSION=1901;
	public static final int CHECK_VERSION_RESP=91901;
	
	public static final int LOGOUT=1999;
	public static final int LOGOUT_RESP=91999;
}
