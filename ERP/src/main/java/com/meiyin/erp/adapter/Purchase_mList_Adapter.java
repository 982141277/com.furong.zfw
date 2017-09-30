package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.Purchase_mList_Entity;


/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class Purchase_mList_Adapter extends Adapter<Purchase_mList_Entity>{

    private Context mContext;
    private LayoutInflater inflater;
    public Purchase_mList_Adapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        // TODO Auto-generated constructor stub
    }
    static class ViewHolder {
        TextView puchase_mprod_name;
        TextView puchase_mneed_num;
        TextView puchase_mprod_Model;
        TextView puchase_msupplier;
        TextView puchase_munivalent;
        TextView puchase_mbill;
        TextView puchase_mpay;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.puchase_mlist_model, null);
            holder = new ViewHolder();
            holder.puchase_mprod_name = (TextView) convertView
                    .findViewById(R.id.puchase_mprod_name);
            holder.puchase_mneed_num = (TextView) convertView
                    .findViewById(R.id.puchase_mneed_num);
            holder.puchase_mprod_Model = (TextView) convertView
                    .findViewById(R.id.puchase_mprod_Model);
            holder.puchase_msupplier = (TextView) convertView
                    .findViewById(R.id.puchase_msupplier);
            holder.puchase_munivalent = (TextView) convertView
                    .findViewById(R.id.puchase_munivalent);
            holder.puchase_mbill = (TextView) convertView
                    .findViewById(R.id.puchase_mbill);
            holder.puchase_mpay = (TextView) convertView
                    .findViewById(R.id.puchase_mpay);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Purchase_mList_Entity memulists = mList.get(position);
        String Prod_name_0 = memulists.getProd_name_0();
        String Number=memulists.getNumber();
        String prod_Model = memulists.getProd_Model();
        String Supplier=memulists.getSupplier();
        String Bill_type=memulists.getBill_type();
        String TypeOfPayment =memulists.getTypeOfPayment();
        String Prod_unit = memulists.getProd_unit();
        String DanWei = memulists.getDanWei();

        holder.puchase_mprod_name.setText(Prod_name_0);
        holder.puchase_mneed_num.setText("数量："+Number+DanWei);
        holder.puchase_mprod_Model.setText("型号："+prod_Model);
        holder.puchase_msupplier.setText(Supplier);
        holder.puchase_munivalent.setText("单价：￥"+Prod_unit);
        if(Bill_type.equals("1")){
            holder.puchase_mbill.setText("增票");
        }else if(Bill_type.equals("2")){
            holder.puchase_mbill.setText("普票");
        }else if(Bill_type.equals("3")){
            holder.puchase_mbill.setText("无发票");
        }
        if(TypeOfPayment.equals("1")){
            holder.puchase_mpay.setText("在线付款");
        }else if(TypeOfPayment.equals("2")){
            holder.puchase_mpay.setText("货到付款");
        }else if(TypeOfPayment.equals("3")){
            holder.puchase_mpay.setText("其他");
        }
        return convertView;
    }

}
