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
    //机房监控告警
    public static String PSMS_MESSAGE=API+"check_center/meinn.mn?c=connection&f=getmessage";
    //资源文件更新地址
    public static String PSMS_UPDATE=API+"jfjk/html5/psms-android";

    public static String PSMS_VERSION_XML_URL=API+"jfjk/html5/psmsVersionInfo.xml";

}
