package common;

import play.*;
import play.mvc.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.*;

public class CommonConfig  {
	
	//url
	public static final String [][]LogList={
		//url
		{
		"login","logout",
		"carInuseList","mycarInuse","carInuseApproveList","carList","carHistory",
		"libraryList","recordList","dataFileList","recordBorrowList","myrecordBorrowList","recordBorrowApproveList",
		"bookTypeList","bookList","bookBorrowList","bookReturnList",
		"meetingRoomList","meetingList","myMeetingList",
		"officeSuppliesList",
		"attendList","offworkList","outtimeList",
		"employeList",
		"customerList",
		"workflowconfigList",
		"workflowMyList","workflowAuditList","workflowViewList",
		"workdocList",
		"officialdocList",
		"planList","planWorkList","planWorkReportList",
		"departmentList","roleList","userList",//"sysLog",
		"scheduleList","businessCardList","email","message"
		},
		
		//模块
		{"登陆系统","登出系统",
		"用车管理","用车管理","用车管理","用车管理","用车管理",
		"资料管理-档案管理","资料管理-档案管理","资料管理-档案管理","资料管理-档案管理","资料管理-档案管理","资料管理-档案管理",
		"资料管理-图书资料管理","资料管理-图书资料管理","资料管理-图书资料管理","资料管理-图书资料管理",
		"会议管理","会议管理","会议管理",
		"资产管理-办公用品管理",
		"人力资源管理-考勤管理","人力资源管理-考勤管理","人力资源管理-考勤管理",
		"人力资源管理-人事管理",
		"客户管理",
		"工作流管理-流程配置",
		"工作流管理-审批管理","工作流管理-审批管理","工作流管理-审批管理",
		"文档管理",
		"公文管理",
		"计划管理","计划管理","计划管理",
		"系统安全管理","系统安全管理","系统安全管理",//"系统安全管理",
		"个人工具","个人工具","个人工具","个人工具"
		},
		
		//功能
		{"","",
		"用车申请","我的用车","用车审批","车辆管理","用车查询",
		"卷库管理","案卷管理","文件管理","借阅申请","我的借阅","借阅审批",
		"图书类别管理","图书管理","图书借阅","图书归还",
		"会议室","使用申请","我的会议",
		"办公用品",
		"考勤","请假","加班",
		"人事管理",
		"客户资料",
		"流程管理",
		"我的流程","我的办理","查看全部",
		"文档管理",
		"公文审批办理",
		"工作计划","工作任务安排","工作报告",
		"部门管理","角色与权限管理","用户管理",//"系统日志管理",
		"日程安排","个人名片夹","邮件管理","实时短消息"
		}
	};

}
