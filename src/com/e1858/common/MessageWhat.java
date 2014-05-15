package com.e1858.common;

import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.socket.SocketDefine;

public class MessageWhat
{
	public final static int	OPEN_PROGRESS_DIALOG			= Integer.MIN_VALUE;
	public final static int	CLOSE_PROGRESS_DIALOG			= Integer.MIN_VALUE + 1;
	public final static int	DOWN_UPDATE						= Integer.MIN_VALUE + 2;
	public final static int	DOWN_OVER						= Integer.MIN_VALUE + 3;
	public final static int	UPLOADRESP						= Integer.MIN_VALUE + 4;
	public final static int	PICPREVIEW						= Integer.MIN_VALUE + 5;
	public final static int	UPDATE_GALLERY					= Integer.MIN_VALUE + 6;
	public final static int	DELETE_GALLERY					= Integer.MIN_VALUE + 7;
	public final static int	NETWORK_CLOSED					= Integer.MIN_VALUE + 8;
	public final static int	NETWORK_OPENED					= Integer.MIN_VALUE + 9;
	public final static int	NETWORK_WIFI_CLOSED				= Integer.MIN_VALUE + 10;
	public final static int	NETWORK_WIFI_OPENED				= Integer.MIN_VALUE + 11;
	public final static int	NETWORK_MOBILE_CLOSED			= Integer.MIN_VALUE + 12;
	public final static int	NETWORK_MOBILE_OPENED			= Integer.MIN_VALUE + 13;
	public final static int	ASYNC_IMAGE_LOADER				= Integer.MIN_VALUE + 14;
	public final static int	NETWORK_SOCKET_EXCEPTION		= Integer.MIN_VALUE + 15;
	public final static int	NETWORK_SOCKET_TIMEOUT			= Integer.MIN_VALUE + 16;
	public final static int	LOCATION_UPDATE					= Integer.MIN_VALUE + 17;
	
	public final static int UPDATE_SHEDULES                 =Integer.MIN_VALUE+18;
	public final static int ISEXISTS						=Integer.MIN_VALUE+19;
	public final static int DOWNFIELD                       =Integer.MIN_VALUE+20;
	public final static int UPDATE_DATE                      =Integer.MIN_VALUE+20;
	// httpDefine
	public static final int LOGIN=HttpDefine.LOGIN;
	public static final int LOGIN_RESP=HttpDefine.LOGIN_RESP;
	
	public static final int RESET_PASS=HttpDefine.RESET_PASS;
	public static final int RESET_PASS_RESP=HttpDefine.RESET_PASS_RESP;
	
	public static final int CHANGE_PASS=HttpDefine.CHANGE_PASS;
	public static final int CHANGE_PASS_RESP=HttpDefine.CHANGE_PASS_RESP;
	
	public static final int UPLOAD=HttpDefine.UPLOAD;
	public static final int UPLOAD_RESP=HttpDefine.UPLOAD_RESP;
	
	
	public static final int DOWNLOAD=HttpDefine.DOWNLOAD;
	public static final int DOWNLOAD_RESP=HttpDefine.DOWNLOAD_RESP;
	
	
	public static final int GET_SCHOOL_LIST=HttpDefine.GET_SCHOOL_LIST;//学校列表
	public static final int GET_SCHOOL_LIST_RESP=HttpDefine.GET_SCHOOL_LIST_RESP;
	
	public static final int GET_DEPARTMENT_LIST=HttpDefine.GET_DEPARTMENT_LIST;//院系列表
	public static final int GET_DEPARTMENT_LIST_RESP=HttpDefine.GET_DEPARTMENT_LIST_RESP;
	
	public static final int GET_CLASS_LIST=HttpDefine.GET_CLASS_LIST;//获取班级列表
	public static final int GET_CLASS_LIST_RESP=HttpDefine.GET_CLASS_LIST_RESP;
	
	public static final int GET_TEACHER_LIST=HttpDefine.GET_TEACHER_LIST;//获取教师列表
	public static final int GET_TEACHER_LIST_RESP=HttpDefine.GET_TEACHER_LIST_RESP;
	
	public static final int GET_TEACHERINFO=HttpDefine.GET_TEACHERINFO;//获取教师信息
	public static final int GET_TEACHERINFO_RESP=HttpDefine.GET_TEACHERINFO_RESP;
	
	public static final int EDIT_TEACHERINFO=HttpDefine.EDIT_TEACHERINFO;//修改教师信息
	public static final int EDIT_TEACHERINFO_RESP=HttpDefine.EDIT_TEACHERINFO_RESP;
	
	public static final int GET_STUDENT_LIST=HttpDefine.GET_STUDENT_LIST;//获取学生列表
	public static final int GET_STUDENT_LIST_RESP=HttpDefine.GET_STUDENT_LIST_RESP;
	
	public static final int GET_STUDENTINFO=HttpDefine.GET_STUDENTINFO;//获取学生信息
	public static final int GET_STUDENTINFO_RESP=HttpDefine.GET_STUDENTINFO_RESP;
	
	public static final int EDIT_STUDENTINFO=HttpDefine.EDIT_STUDENTINFO;//更改学生信息
	public static final int EDIT_STUDENTINFO_RESP=HttpDefine.EDIT_STUDENTINFO_RESP;
	
	public static final int GET_SCHOOLMODULE_LIST=HttpDefine.GET_SCHOOLMODULE_LIST;//获取学校选择模块
	public static final int GET_SCHOOLMODULE_LIST_RESP=HttpDefine.GET_SCHOOLMODULE_LIST_RESP;
	
	public static final int GET_UNREADNOTICE_LIST=HttpDefine.GET_UNREADNOTICE_LIST;//获取最新公告列表
	public static final int GET_UNREADNOTICE_LIST_RESP=HttpDefine.GET_UNREADNOTICE_LIST_RESP;
	
	public static final int GET_NOTICE=HttpDefine.GET_NOTICE;//获取公告详细
	public static final int GET_NOTICE_RESP=HttpDefine.GET_NOTICE_RESP;
		
	public static final int ADD_NOTICE=HttpDefine.ADD_NOTICE;//新增公告
	public static final int ADD_NOTICE_RESP=HttpDefine.ADD_NOTICE_RESP;
	
	public static final int GET_UNREADMSG_LIST=HttpDefine.GET_UNREADMSG_LIST;//获取未读消息列表
	public static final int GET_UNREADMSG_LIST_RESP=HttpDefine.GET_UNREADMSG_LIST_RESP;
	
	public static final int SEND_MSG=HttpDefine.SEND_MSG;
	public static final int SEND_MSG_RESP=HttpDefine.SEND_MSG_RESP;
	
	public static final int SEND_DELIVER=HttpDefine.SEND_DELIVER;
	public static final int SEND_DELIVER_RESP=HttpDefine.SEND_DELIVER_RESP;
	
	public static final int GET_EJOURNAL_LIST=HttpDefine.GET_EJOURNAL_LIST;//获取电子校刊
	public static final int GET_EJOURNAL_LIST_RESP=HttpDefine.GET_EJOURNAL_LIST_RESP;
	
	public static final int GET_EJOURNAL=HttpDefine.GET_EJOURNAL;//电子校刊详细
	public static final int GET_EJOURNAL_RESP=HttpDefine.GET_EJOURNAL_RESP;
	
	
	public static final int GET_SCHOOLTIMETABLE=HttpDefine.GET_SCHOOLTIMETABLE;//获取班级课程表
	public static final int GET_SCHOOLTIMETABLE_RESP=HttpDefine.GET_SCHOOLTIMETABLE_RESP;
	
	public static final int GET_COURSE_LIST=HttpDefine.GET_COURSE_LIST;//获取已存在课程列表
	public static final int GET_COURSE_LIST_RESP=HttpDefine.GET_COURSE_LIST_RESP;
	
	public static final int GET_COURSE=HttpDefine.GET_COURSE;//获取课程详细信息
	public static final int GET_COURSE_RESP=HttpDefine.GET_COURSE_RESP;
	
	public static final int ADD_COURSE=HttpDefine.ADD_COURSE;//新增课程
	public static final int ADD_COURSE_RESP=HttpDefine.ADD_COURSE_RESP;
	
	public static final int ADD_COURSETIMES=HttpDefine.ADD_COURSETIMES;//新增课程安排
	public static final int ADD_COURSETIMES_RESP=HttpDefine.ADD_COURSETIMES_RESP;
	
	public static final int GET_LIBRARYGROUP_LIST=HttpDefine.GET_LIBRARYGROUP_LIST;
	public static final int GET_LIBRARYGROUP_LIST_RESP=HttpDefine.GET_LIBRARYGROUP_LIST_RESP;
	
	public static final int GET_LIBRARY_LIST=HttpDefine.GET_LIBRARY_LIST;
	public static final int GET_LIBRARY_LIST_RESP=HttpDefine.GET_LIBRARY_LIST_RESP;
	
	public static final int GET_LIBRARY=HttpDefine.GET_LIBRARY;
	public static final int GET_LIBRARY_RESP=HttpDefine.GET_LIBRARY_RESP;
	
	
	public static final int CHECK_VERSON=HttpDefine.CHECK_VERSION;
	public static final int CHECK_VERSON_RESP=HttpDefine.CHECK_VERSION_RESP;
	
	public static final int LOGOUT=HttpDefine.LOGOUT;
	public static final int LOGOUT_RESP=HttpDefine.LOGOUT_RESP;

	// socketdefine
	public static final int CONNECT=SocketDefine.CONNECT;
	public static final int CONNECT_RESP=SocketDefine.CONNECT_RESP;	
	
	public static final int PUSH_MSG=SocketDefine.PUSH_MSG;
	public static final int PUSH_MSG_RESP=SocketDefine.PUSH_MSG_RESP;
	
	public static final int DISCONNECT=SocketDefine.DISCONNECT;
	public static final int DISCONNECT_RESP=SocketDefine.DISCONNECT_RESP;
	
	public static final int ACTIVE_TEST=SocketDefine.ACTIVE_TEST;
	public static final int ACTIVE_TEST_RESP=SocketDefine.ACTIVE_TEST_RESP;
}
