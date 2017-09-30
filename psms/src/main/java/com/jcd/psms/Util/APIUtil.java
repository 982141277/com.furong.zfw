package com.jcd.psms.Util;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class APIUtil {

//    public static String API="http://10.85.239.125:8080/";
    public static String API="http://192.168.3.70:8080/";
//    账号与安全页面
    public static String PSMS_ACCOUNT="file:///"+FileUtil.gethtmlpath()+"/h5/html/psms_account.html";
    //关于机房监控页面
    public static String PSMS_ABOUT="file:///"+FileUtil.gethtmlpath()+"/h5/html/psms_about.html";
    //机房监控首页
    public static String PSMS_MAIN="file:///"+FileUtil.gethtmlpath()+"/h5/html/main.html";
    //机房监控登录页面
    public static String PSMS_LOGIN="file:///"+FileUtil.gethtmlpath()+"/h5/html/login.html";
    //机房监控详情
    public static String PSMS_MAIN_DETAIL="file:///"+FileUtil.gethtmlpath()+"/h5/html/psms_ecahrt.html";
    //机房监控历史数据全屏
    public static String PSMS_MAIN_SCREEN="file:///"+FileUtil.gethtmlpath()+"/h5/html/psms_screen.html";

    //机房监控告警
    public static String PSMS_MESSAGE=API+"check_center/meinn.mn?c=connection&f=getmessage";
    //机房监控告警1
    public static String PSMS_MESSAGE1=API+"check_center";
    //资源文件更新地址
    public static String PSMS_UPDATE=API+"jfjk/html5/psms-android";

    public static String PSMS_VERSION_XML_URL=API+"jfjk/html5/psmsVersionInfo.xml";

}
