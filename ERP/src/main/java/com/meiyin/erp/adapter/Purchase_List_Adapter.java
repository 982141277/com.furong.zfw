package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.Purchase_List_Entity;
import com.meiyin.erp.util.LogUtil;


/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class Purchase_List_Adapter extends Adapter<Purchase_List_Entity>{

private Context mContext;
private LayoutInflater inflater;
public Purchase_List_Adapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        // TODO Auto-generated constructor stub
        }
static class ViewHolder {
    TextView puchase_prod_name;
    TextView puchase_need_num;
    TextView puchase_prod_Model;
    TextView puchase_supplier1;
    TextView puchase_supplier2;
    TextView puchase_supplier3;
    TextView puchase_price1;
    TextView puchase_price2;
    TextView puchase_price3;
    TextView puchase_bill1;
    TextView puchase_bill2;
    TextView puchase_bill3;
    TextView typeOfPayment1;
    TextView typeOfPayment2;
    TextView typeOfPayment3;
    RadioGroup  puchase_dialog_radio_group;

}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.puchase_list_model, null);
            holder = new ViewHolder();
            holder.puchase_prod_name = (TextView) convertView
                    .findViewById(R.id.puchase_prod_name);
            holder.puchase_need_num = (TextView) convertView
                    .findViewById(R.id.puchase_need_num);
            holder.puchase_prod_Model = (TextView) convertView
                    .findViewById(R.id.puchase_prod_Model);
            holder.puchase_supplier1 = (TextView) convertView
                    .findViewById(R.id.puchase_supplier1);
            holder.puchase_supplier2 = (TextView) convertView
                    .findViewById(R.id.puchase_supplier2);
            holder.puchase_supplier3 = (TextView) convertView
                    .findViewById(R.id.puchase_supplier3);
            holder.puchase_price1 = (TextView) convertView
                    .findViewById(R.id.puchase_price1);
            holder.puchase_price2 = (TextView) convertView
                    .findViewById(R.id.puchase_price2);
            holder.puchase_price3 = (TextView) convertView
                    .findViewById(R.id.puchase_price3);
            holder.puchase_bill1 = (TextView) convertView
                    .findViewById(R.id.puchase_bill1);
            holder.puchase_bill2 = (TextView) convertView
                    .findViewById(R.id.puchase_bill2);
            holder.puchase_bill3 = (TextView) convertView
                    .findViewById(R.id.puchase_bill3);
            holder.typeOfPayment1 = (TextView) convertView
                    .findViewById(R.id.typeOfPayment1);
            holder.typeOfPayment2 = (TextView) convertView
                    .findViewById(R.id.typeOfPayment2);
            holder.typeOfPayment3 = (TextView) convertView
                    .findViewById(R.id.typeOfPayment3);
            holder.puchase_dialog_radio_group = (RadioGroup) convertView
                    .findViewById(R.id.puchase_dialog_radio_group);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Purchase_List_Entity memulists = mList.get(position);
        String Prod_name_1 = memulists.getProd_name_1();
        String need_num=memulists.getNeed_num();
        String Prod_Model = memulists.getProd_Model();
        String Prod_unit = memulists.getProd_unit();
        String supplier1=memulists.getSupplier1();
        String supplier2 =memulists.getSupplier2();
        String supplier3 = memulists.getSupplier3();
        String S_price1=memulists.getS_price1();
        String S_price2 =memulists.getS_price2();
        String S_price3 = memulists.getS_price3();
        String S_bill1=memulists.getS_bill1();
        String S_bill2 =memulists.getS_bill2();
        String S_bill3 = memulists.getS_bill3();
        String TypeOfPayment1=memulists.getTypeOfPayment1();
        String TypeOfPayment2 =memulists.getTypeOfPayment2();
        String TypeOfPayment3 = memulists.getTypeOfPayment3();

        holder.puchase_prod_name.setText(Prod_name_1);
        holder.puchase_need_num.setText("数量："+need_num+Prod_unit);
        holder.puchase_prod_Model.setText("型号："+Prod_Model);
        holder.puchase_supplier1.setText(supplier1);
        holder.puchase_supplier2.setText(supplier2);
        holder.puchase_supplier3.setText(supplier3);
        holder.puchase_price1.setText("￥"+S_price1);
        holder.puchase_price2.setText("￥"+S_price2);
        holder.puchase_price3.setText("￥"+S_price3);

        if(S_bill1.equals("1")){
            holder.puchase_bill1.setText("增票");
        }else if(S_bill1.equals("2")){
            holder.puchase_bill1.setText("普票");
        }else if(S_bill1.equals("3")){
            holder.puchase_bill1.setText("无发票");
        }
        if(S_bill2.equals("1")){
            holder.puchase_bill2.setText("增票");
        }else if(S_bill2.equals("2")){
            holder.puchase_bill2.setText("普票");
        }else if(S_bill2.equals("3")){
            holder.puchase_bill2.setText("无发票");
        }
        if(S_bill3.equals("1")){
            holder.puchase_bill3.setText("增票");
        }else if(S_bill3.equals("2")){
            holder.puchase_bill3.setText("普票");
        }else if(S_bill3.equals("3")){
            holder.puchase_bill3.setText("无发票");
        }
        if(TypeOfPayment1.equals("1")){
            holder.typeOfPayment1.setText("在线付款");
        }else if(TypeOfPayment1.equals("2")){
            holder.typeOfPayment1.setText("货到付款");
        }else if(TypeOfPayment1.equals("3")){
            holder.typeOfPayment1.setText("其他");
        }
        if(TypeOfPayment2.equals("1")){
            holder.typeOfPayment2.setText("在线付款");
        }else if(TypeOfPayment2.equals("2")){
            holder.typeOfPayment2.setText("货到付款");
        }else if(TypeOfPayment2.equals("3")){
            holder.typeOfPayment2.setText("其他");
        }
        if(TypeOfPayment3.equals("1")){
            holder.typeOfPayment3.setText("在线付款");
        }else if(TypeOfPayment3.equals("2")){
            holder.typeOfPayment3.setText("货到付款");
        }else if(TypeOfPayment3.equals("3")){
            holder.typeOfPayment3.setText("其他");
        }
        //只要对RadioGroup进行监听
        holder.puchase_dialog_radio_group.setOnCheckedChangeListener(new CheckedChangeListener(position));

        return convertView;
    }
    public class  CheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        private int position;
        private CheckedChangeListener(int position){
            this.position=position;
        }
        public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(R.id.rgos_1 == checkedId){
                    LogUtil.e("lyt","rgos_1");
                    mList.get(position).setRadios("1");
                }
                else if(R.id.rgos_2 == checkedId){
                    LogUtil.e("lyt","rgos_2");
                    mList.get(position).setRadios("2");
                }
                else if(R.id.rgos_3 == checkedId){
                    LogUtil.e("lyt","rgos_3");
                    mList.get(position).setRadios("3");
                }
            }
    };
}
