package com.meiyin.erp.entity;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class Purchase_mList_Entity {

    private String min;//原来最低价
    private String remark;//备注
    private String prod_id;
    private String prod_Model;//产品型号
    private String number;//数量
    private String bill_type;//发票类型
    private String effective;
    private String apply_estimate;
    private String prod_id_mini;
    private String typeOfPayment;//支付方式
    private String prod_unit;//单价
    private String prod_name_0;//产品名称
    private String danWei;//单位
    private String projectName;//项目名称
    private String supplier;//供应商
    private String purApplyId;

    public Purchase_mList_Entity(String min, String remark, String prod_id, String prod_Model, String number, String bill_type, String effective, String apply_estimate, String prod_id_mini, String typeOfPayment, String prod_unit, String prod_name_0, String danWei, String projectName, String supplier, String purApplyId) {
        this.min = min;
        this.remark = remark;
        this.prod_id = prod_id;
        this.prod_Model = prod_Model;
        this.number = number;
        this.bill_type = bill_type;
        this.effective = effective;
        this.apply_estimate = apply_estimate;
        this.prod_id_mini = prod_id_mini;
        this.typeOfPayment = typeOfPayment;
        this.prod_unit = prod_unit;
        this.prod_name_0 = prod_name_0;
        this.danWei = danWei;
        this.projectName = projectName;
        this.supplier = supplier;
        this.purApplyId = purApplyId;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
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

    public String getProd_Model() {
        return prod_Model;
    }

    public void setProd_Model(String prod_Model) {
        this.prod_Model = prod_Model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getApply_estimate() {
        return apply_estimate;
    }

    public void setApply_estimate(String apply_estimate) {
        this.apply_estimate = apply_estimate;
    }

    public String getProd_id_mini() {
        return prod_id_mini;
    }

    public void setProd_id_mini(String prod_id_mini) {
        this.prod_id_mini = prod_id_mini;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public String getProd_unit() {
        return prod_unit;
    }

    public void setProd_unit(String prod_unit) {
        this.prod_unit = prod_unit;
    }

    public String getProd_name_0() {
        return prod_name_0;
    }

    public void setProd_name_0(String prod_name_0) {
        this.prod_name_0 = prod_name_0;
    }

    public String getDanWei() {
        return danWei;
    }

    public void setDanWei(String danWei) {
        this.danWei = danWei;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPurApplyId() {
        return purApplyId;
    }

    public void setPurApplyId(String purApplyId) {
        this.purApplyId = purApplyId;
    }
}
