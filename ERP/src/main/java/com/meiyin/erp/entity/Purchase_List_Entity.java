package com.meiyin.erp.entity;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class Purchase_List_Entity {

    private String remark;
    private String prod_id;
    private String userid;
    private String prod_Model;
    private String need_num;//数量
    private String prod_unit;
    private String purApplyId;
    private String unit_price;
    private String accompanyId;
    private String accompanyId2;
    private String orgId;
    private String url;
    private String pname;
    private String type_name;
    private String brand;
    private String projectName;
    private String apply_state;

    private String buy1;
    private String s_price3;//单价
    private String s_price2;
    private String s_price1;

    private String typeOfPayment3;//支付方式
    private String typeOfPayment1;
    private String typeOfPayment2;

    private String need_num_old;
    private String supplier1;//供应商
    private String supplier2;
    private String supplier3;

    private String prod_name_1;
    private String prod_name;

    private String s_limitTime1;//保质期
    private String s_limitTime2;
    private String s_limitTime3;

    private String s_bill3;//发票类型
    private String s_bill2;
    private String s_bill1;

    private String s_arrivalTime1;//预计到货时间
    private String s_arrivalTime2;
    private String s_arrivalTime3;

    private String s_remark1;//备注
    private String s_remark2;
    private String s_remark3;
    private String radios;

    public String getRadios() {
        if(null==radios||radios.equals("")){
            return "1";
        }
        return radios;
    }

    public void setRadios(String radios) {
        this.radios = radios;
    }

    public Purchase_List_Entity(String remark, String prod_id, String userid, String prod_Model, String need_num, String prod_unit, String purApplyId, String unit_price, String accompanyId, String accompanyId2, String orgId, String url, String pname, String type_name, String brand, String projectName, String apply_state, String buy1, String s_price3, String s_price2, String s_price1, String typeOfPayment3, String typeOfPayment1, String typeOfPayment2, String need_num_old, String supplier1, String supplier2, String supplier3, String prod_name_1, String prod_name, String s_limitTime1, String s_limitTime2, String s_limitTime3, String s_bill3, String s_bill2, String s_bill1, String s_arrivalTime1, String s_arrivalTime2, String s_arrivalTime3, String s_remark1, String s_remark2, String s_remark3, String radios) {
        this.remark = remark;
        this.prod_id = prod_id;
        this.userid = userid;
        this.prod_Model = prod_Model;
        this.need_num = need_num;
        this.prod_unit = prod_unit;
        this.purApplyId = purApplyId;
        this.unit_price = unit_price;
        this.accompanyId = accompanyId;
        this.accompanyId2 = accompanyId2;
        this.orgId = orgId;
        this.url = url;
        this.pname = pname;
        this.type_name = type_name;
        this.brand = brand;
        this.projectName = projectName;
        this.apply_state = apply_state;
        this.buy1 = buy1;
        this.s_price3 = s_price3;
        this.s_price2 = s_price2;
        this.s_price1 = s_price1;
        this.typeOfPayment3 = typeOfPayment3;
        this.typeOfPayment1 = typeOfPayment1;
        this.typeOfPayment2 = typeOfPayment2;
        this.need_num_old = need_num_old;
        this.supplier1 = supplier1;
        this.supplier2 = supplier2;
        this.supplier3 = supplier3;
        this.prod_name_1 = prod_name_1;
        this.prod_name = prod_name;
        this.s_limitTime1 = s_limitTime1;
        this.s_limitTime2 = s_limitTime2;
        this.s_limitTime3 = s_limitTime3;
        this.s_bill3 = s_bill3;
        this.s_bill2 = s_bill2;
        this.s_bill1 = s_bill1;
        this.s_arrivalTime1 = s_arrivalTime1;
        this.s_arrivalTime2 = s_arrivalTime2;
        this.s_arrivalTime3 = s_arrivalTime3;
        this.s_remark1 = s_remark1;
        this.s_remark2 = s_remark2;
        this.s_remark3 = s_remark3;
        this.radios = radios;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProd_Model() {
        return prod_Model;
    }

    public void setProd_Model(String prod_Model) {
        this.prod_Model = prod_Model;
    }

    public String getNeed_num() {
        return need_num;
    }

    public void setNeed_num(String need_num) {
        this.need_num = need_num;
    }

    public String getProd_unit() {
        return prod_unit;
    }

    public void setProd_unit(String prod_unit) {
        this.prod_unit = prod_unit;
    }

    public String getPurApplyId() {
        return purApplyId;
    }

    public void setPurApplyId(String purApplyId) {
        this.purApplyId = purApplyId;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getAccompanyId() {
        return accompanyId;
    }

    public void setAccompanyId(String accompanyId) {
        this.accompanyId = accompanyId;
    }

    public String getAccompanyId2() {
        return accompanyId2;
    }

    public void setAccompanyId2(String accompanyId2) {
        this.accompanyId2 = accompanyId2;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getApply_state() {
        return apply_state;
    }

    public void setApply_state(String apply_state) {
        this.apply_state = apply_state;
    }

    public String getBuy1() {
        return buy1;
    }

    public void setBuy1(String buy1) {
        this.buy1 = buy1;
    }

    public String getS_price3() {
        return s_price3;
    }

    public void setS_price3(String s_price3) {
        this.s_price3 = s_price3;
    }

    public String getS_price2() {
        return s_price2;
    }

    public void setS_price2(String s_price2) {
        this.s_price2 = s_price2;
    }

    public String getS_price1() {
        return s_price1;
    }

    public void setS_price1(String s_price1) {
        this.s_price1 = s_price1;
    }

    public String getTypeOfPayment3() {
        return typeOfPayment3;
    }

    public void setTypeOfPayment3(String typeOfPayment3) {
        this.typeOfPayment3 = typeOfPayment3;
    }

    public String getTypeOfPayment1() {
        return typeOfPayment1;
    }

    public void setTypeOfPayment1(String typeOfPayment1) {
        this.typeOfPayment1 = typeOfPayment1;
    }

    public String getTypeOfPayment2() {
        return typeOfPayment2;
    }

    public void setTypeOfPayment2(String typeOfPayment2) {
        this.typeOfPayment2 = typeOfPayment2;
    }

    public String getNeed_num_old() {
        return need_num_old;
    }

    public void setNeed_num_old(String need_num_old) {
        this.need_num_old = need_num_old;
    }

    public String getSupplier1() {
        return supplier1;
    }

    public void setSupplier1(String supplier1) {
        this.supplier1 = supplier1;
    }

    public String getSupplier2() {
        return supplier2;
    }

    public void setSupplier2(String supplier2) {
        this.supplier2 = supplier2;
    }

    public String getSupplier3() {
        return supplier3;
    }

    public void setSupplier3(String supplier3) {
        this.supplier3 = supplier3;
    }

    public String getProd_name_1() {
        return prod_name_1;
    }

    public void setProd_name_1(String prod_name_1) {
        this.prod_name_1 = prod_name_1;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getS_limitTime1() {
        return s_limitTime1;
    }

    public void setS_limitTime1(String s_limitTime1) {
        this.s_limitTime1 = s_limitTime1;
    }

    public String getS_limitTime2() {
        return s_limitTime2;
    }

    public void setS_limitTime2(String s_limitTime2) {
        this.s_limitTime2 = s_limitTime2;
    }

    public String getS_limitTime3() {
        return s_limitTime3;
    }

    public void setS_limitTime3(String s_limitTime3) {
        this.s_limitTime3 = s_limitTime3;
    }

    public String getS_bill3() {
        return s_bill3;
    }

    public void setS_bill3(String s_bill3) {
        this.s_bill3 = s_bill3;
    }

    public String getS_bill2() {
        return s_bill2;
    }

    public void setS_bill2(String s_bill2) {
        this.s_bill2 = s_bill2;
    }

    public String getS_bill1() {
        return s_bill1;
    }

    public void setS_bill1(String s_bill1) {
        this.s_bill1 = s_bill1;
    }

    public String getS_arrivalTime1() {
        return s_arrivalTime1;
    }

    public void setS_arrivalTime1(String s_arrivalTime1) {
        this.s_arrivalTime1 = s_arrivalTime1;
    }

    public String getS_arrivalTime2() {
        return s_arrivalTime2;
    }

    public void setS_arrivalTime2(String s_arrivalTime2) {
        this.s_arrivalTime2 = s_arrivalTime2;
    }

    public String getS_arrivalTime3() {
        return s_arrivalTime3;
    }

    public void setS_arrivalTime3(String s_arrivalTime3) {
        this.s_arrivalTime3 = s_arrivalTime3;
    }

    public String getS_remark1() {
        return s_remark1;
    }

    public void setS_remark1(String s_remark1) {
        this.s_remark1 = s_remark1;
    }

    public String getS_remark2() {
        return s_remark2;
    }

    public void setS_remark2(String s_remark2) {
        this.s_remark2 = s_remark2;
    }

    public String getS_remark3() {
        return s_remark3;
    }

    public void setS_remark3(String s_remark3) {
        this.s_remark3 = s_remark3;
    }
}
