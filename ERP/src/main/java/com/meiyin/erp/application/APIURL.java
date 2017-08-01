package com.meiyin.erp.application;

public class APIURL {
	/**
	 * 亮哥电脑接口
	 */
	
//	public static final String API ="13.25.6.201";
//	public static final String API ="192.168.1.15";
//	public static final String MY_CENTER ="my_center/";
//	public static final String API ="192.168.1.109";
//	public static final String APIS ="192.168.1.117";
	/**
	 * 内网测试接口
	 */
//	public static final String API ="192.168.3.54";
//	public static final String MY_CENTER ="";
	/**
	 * 外网接口
	 */
	public static final String API ="192.168.3.80";
	public static final String MY_CENTER ="";
	/**
	 * 外网接口
	 */
	
	public static final String APIS ="222.240.140.30";
//	public static final String API ="222.240.140.30";
//	public static final String MY_CENTER ="";
	//Code下载
	public static final String ERP_VERSION_XML_URL ="http://"+API+"/itsm_new/upload/erp_updateInfo.xml";
//			"http://"+API+"/itsm_new/androidupload!loadfileinfo.it";//下载版本信息
	//APP下载
	public static final String ERP_APKURL = "http://"+API+"/itsm_new/upload/ERP.apk";
//			"http://"+API+"/itsm_new/androidupload!loadfileapk.it";//下载APP
	//ERP登陆
	public static final String LOGINAPI = "http://"+API+"/"+MY_CENTER+"mobilelogin!userLogin.ct";//登陆
	//人员信息
	public static final String COMPEOPLE="http://"+API+"/check/wdoomobile!gettaskUsers.ac";
	
	//申请单
	public static final class CHECK {
		//公告接口
		public static final String TOPICMEMU ="http://"+API+"/check/topicmobile!companytopic.ac";
		public static final String TOPICSHOW ="http://"+API+"/check/topicmobile!showTopic.ac";
		//新增请购申请单物品接口0
		public static final String ADDARTICLE="http://"+API+"/check/requisitionmobile!applicationForm.ac";
		//新增请购申请单物品接口1
		public static final String ADDARTICLE1="http://"+API+"/check/requisitionmobile!getUserJsonByOrgId.ac";
		//新增请购申请单物品接口2
		public static final String ADDARTICLE2="http://"+API+"/check/requisitionmobile!getUserJsonByOrgId2.ac";
		//新增请购申请单物品接口3
		public static final String ADDARTICLE3="http://"+API+"/check/requisitionmobile!selectaccompanyId2.ac";
		//新增请购申请单
		public static final String ADD_REQUISITION="http://"+API+"/check/requisitionmobile!requisitionaddApply.ac";
		//申请单列表
		public static final String MEMUAPI ="http://"+API+"/check/mobile!showAllapply.ac";
		//新增申请单权限获取申请列表
		public static final String ADDMEMUAPI ="http://"+API+"/check/android!selectApplyFromList.ac";
		//新增申请单初始化
		public static final String ADDMEMUINTI ="http://"+API+"/check/android!intiAdd.ac";
		//新增外出申请单
		public static final String ADDMEMULIST="http://"+API+"/check/android!addApply.ac";
		//新增请假申请单
		public static final String ADDMEMULEAVE="http://"+API+"/check/leavemobile!addApply.ac";
		//请假申请单审批方法
		public static final String ADDLEAVEPROVE="http://"+API+"/check/leavemobile!addApprove.ac";
		//请假申请单提交证明材料
		public static final String ADDLEAVEMATERAIL="http://"+API+"/check/leavemobile!addApproveDoc.ac";
		//新增客户数据
		public static final String ADDMEMUCLIENT="http://"+API+"/check/android!getClient.ac";
		//外出申请单
		public static final String MEMU_SQAPI="http://"+API+"/check/mobile!intiUpdateOrApproveOrSeeHistory.ac";//申请单详情
		
		public static final String MEMU_SPAPI= "http://"+API+"/check/mobile!addApprove.ac";//审批
		
		public static final String MEMU_XQAPI= "http://"+API+"/check/mobile!updategoOutApply.ac";//填写详情
		
		public static final String MEMU_HSAPI= "http://"+API+"/check/mobile!addApproveForGoOutApplyByServer.ac";//核实交通费
	
		public static final String MEMUAPI_COUNT ="http://"+API+"/check/mobile!showAllapplySize.ac";//待审批数量
		//加班任务单所有人员信息
		public static final String OVERTIMETASKPEOPLE="http://"+API+"/check/wdoomobile!getUsers.ac";
		//新增加班任务单/新增盖章申请单/新增设备报修申请单/新增转正申请单/新增出差申请单
		public static final String ADDNEWOVERTIMETASK=ADDMEMULIST;
		//加班任务单审批接口/出差申请单审批接口
		public static final String ADDNEWOVERPROVE=MEMU_SPAPI;
		//加班任务单填写详情接口
		public static final String ADDDETAILS=MEMU_SPAPI;
		//加班任务单审批详情页面
		public static final String SHOWAUTHDETAIL="http://"+API+"/check/mobile!showAuthOtDetail.ac";
		//新增费用申请单
		public static final String ADDNEWEXPENSE="http://"+API+"/check/expensemobile!addApply.ac";
		//费用申请单审批接口
		public static final String EXPENSEPROVE="http://"+API+"/check/expensemobile!addApprove.ac";
		//新增离职申请单
		public static final String ADDNEWDIMISSION ="http://"+API+"/check/android!addDimissionApply.ac";
		//填写出差详情
		public static final String UPDATEBEONAPPLY= "http://"+API+"/check/mobile!updateBeonApply.ac";
	}
	//外勤系统
	public static final String GETOUTWORK="http://"+API+"/check/wdoomobile!getAllWdooApply.ac";//外勤列表信息
	
	public static final String OUTWORK_DETAIL="http://"+API+"/check/wdoomobile!getOneWdooApply.ac";//外勤详细信息

	public static final String UPDATEBRUSH="http://"+API+"/check/wdoomobile!brushWdoo.ac";//外勤刷卡
	//项目考勤
	public static final String GOOUT_BRUSH="http://"+API+"/check/android!addgooutBrush.ac";
	//项目考勤列表
	public static final String GOOUT_BRUSH_LIST="http://"+API+"/check/android!gooutBrushlist.ac";
	
	//任务日志管理系统
	public static final String TASKWRITEAPI ="http://"+API+"/check/mobile!writeD.ac";//填写任务日志
	//任务日志详情
	public static final String TASKDETAILSAPI ="http://"+API+"/check/taskmobile!editdayStringReport.ac";
	//全部任务人员列表
	public static final String USERLISTFORALLTASK ="http://"+API+"/check/taskmobile!userlistforAllTask.ac";
	//任务汇报
	public static final String TASKREPORT ="http://"+API+"/check/taskmobile!saveReport.ac";
	//我的任务
	public static final String MYTASKLIST ="http://"+API+"/check/taskmobile!myfindTaskList.ac";
	//任务详情
	public static final String MYFINDOTASK ="http://"+API+"/check/taskmobile!findOtask.ac";
	//完成任务
	public static final String APPFINISHTASK ="http://"+API+"/check/taskmobile!appfinishTask.ac";
	//新增任务
	public static final String APPADDTASK ="http://"+API+"/check/taskmobile!newaddtask.ac";
	
	//IT运维管理系统
	public static final class ITSM {
		public static final String IT_EVENT_LIST ="http://"+API+"/itsm_new/mobile!getIncedentListMobile.it";//IT运维管理事件处理列表
		
		public static final String IT_EVENT_SQ ="http://"+API+"/itsm_new/mobile!getIncedent.it";//IT运维管理事件详细情况
		
		public static final String IT_DELAY_PROCESS_LIST="http://"+API+"/itsm_new/mobile!getIncedentListpre.it";//IT运维待处理事件
		
		public static final String IT_EVENT_ACCEPT="http://"+API+"/itsm_new/mobile!doAcceptIncedentInfo.it";//IT运维待受理事件

		public static final String IT_EVENT_EXECUTE="http://"+API+"/itsm_new/mobile!showProcessIncendetInfoPage.it";//IT运维待执行事件1

		public static final String IT_EVENT_EXECUTES="http://"+API+"/itsm_new/mobile!doProcessIncedentInfo.it";//IT运维待执行事件2
		
		public static final String IT_EVENT_RISE_TWO="http://"+API+"/itsm_new/mobile!cancelDistribute.it";//IT运维待执行事件升级二线
		
		public static final String IT_EVENT_TYPE="http://"+API+"/itsm_new/mobile!getEequiptypeForKey.it";//事件类型
		
		public static final String IT_EVENT_DEPT="http://"+API+"/itsm_new/mobile!getAllDept.it";//运维组
		
		public static final String IT_EVENT_APPLY_ASSIGN_ONE="http://"+API+"/itsm_new/mobile!showIncedentInfoRedoPage.it";//申请重新分配一！！！
		
		public static final String IT_EVENT_APPLY_ASSIGN_TWO="http://"+API+"/itsm_new/mobile!doProcessIncedentInfo2.it";//申请重新分配二！！！
		
		public static final String IT_EVENT_UPDATE="http://"+API+"/itsm_new/mobile!doProcessIncendetInfo.it";//确认分配！！！
		
		public static final String IT_EXAMLIST="http://"+API+"/itsm_new/mobile!getEquipExamList.it";//巡检表！！！
		
		public static final String IT_EXAMINETABLE="http://"+API+"/itsm_new/mobile!getExamineTable.it";//巡检表详情！！！
		
		public static final String IT_ADDEQUIPEXAMLIST="http://"+API+"/itsm_new/mobile!AddEquipExamToIncedentList.it";//提交巡检表！！！
		//更新巡检表！！！
		public static final String IT_DOREFRESHDATAAPP="http://"+API+"/itsm_new/mobile!doRefreshDataapp.it";//更新巡检表！！！
		
		
		
		
		
	}
	
	
}