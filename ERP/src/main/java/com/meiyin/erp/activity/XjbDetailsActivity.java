package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.ExamListEntity;
import com.meiyin.erp.entity.ExammListEntity;
import com.meiyin.erp.entity.GetExamListEntity;
import com.meiyin.erp.entity.GetmExamListEntity;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class XjbDetailsActivity extends Activity {

	protected Context context;
	protected Dialog_Http_Util dialog_util;
	protected AsyncHttpClient client;
	protected SharedPreferences sp;

	protected Button xbjdetailstextview;
	protected ArrayList<ExamListEntity> list;
	ExpandableListView xjb_list_detail;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xjb_details_main);
		activity=this;
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();

	}

	/*
	 * 初始化标题UI
	 */
	private void initHeader() {
		ImageView headtitleimageView = (ImageView) findViewById(R.id.headtitleimageView);
		headtitleimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				int result = getIntent().getIntExtra("result",
						SPConstant.TAGXJBACTIVITY);
				intent.putExtra("refresh", false);
				XjbDetailsActivity.this.setResult(result, intent);
				XjbDetailsActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("巡检表");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		list = new ArrayList<ExamListEntity>();

		xjb_list_detail = (ExpandableListView) findViewById(R.id.xjb_list_detail);
		View foots = LayoutInflater.from(context).inflate(R.layout.view_mains,
				null);
		xjb_list_detail.addFooterView(foots);
		xbjdetailstextview = (Button) findViewById(R.id.xbjdetailstextview);
		String man_name = getIntent().getStringExtra("Exam_man_name");
		if (man_name.equals("") || null == man_name) {
			xbjdetailstextview.setVisibility(ViewGroup.VISIBLE);
		} else {
			xbjdetailstextview.setVisibility(ViewGroup.GONE);
		}
		dialog_util = new Dialog_Http_Util();
		client = new AsyncHttpClient();
		client.setTimeout(150000);
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		String em_id = getIntent().getStringExtra("em_id");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		param.put("em_id", em_id);
		Dialog progressDialog = dialog_util.showWaitingDialog(
				XjbDetailsActivity.this, "稍等一会", false);
		client.post(APIURL.ITSM.IT_EXAMINETABLE, param, new JsonHandler(
				XjbDetailsActivity.this, progressDialog));
		xbjdetailstextview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String key = sp.getString(SPConstant.MY_TOKEN, "");
				String em_id = getIntent().getStringExtra("em_id");
				ArrayList<GetExamListEntity> lists = new ArrayList<GetExamListEntity>();
				for (int i = 0; i < list.size(); i++) {
					ArrayList<ExammListEntity> equip = list.get(i).getEquipExamList();
					String equip_id = list.get(i).getEquip_id();
					String Brand_name = list.get(i).getBrand_name();
					ArrayList<GetmExamListEntity> listss = new ArrayList<GetmExamListEntity>();
					for (int j = 0; j < equip.size(); j++) {
						String attr_id = equip.get(j).getAttr_id();
						String bz = equip.get(j).getBz();
						String display_type = equip.get(j).getDisplay_type();
						String attr_name = equip.get(j).getAttr_name();
						String normal_range = equip.get(j).getNormal_range();
						String attr_value = equip.get(j).getAttr_value();

						if (null == attr_value || attr_value.equals("")) {
							xjb_list_detail.setSelectedChild(i, j, true);
							xjb_list_detail.expandGroup(i);
							ToastManager.getInstance(context).showToast(
									Brand_name + "/" + attr_name + "未填写！！！");
							return;
						}
						listss.add(new GetmExamListEntity(attr_id, bz, display_type,
								attr_name, normal_range, attr_value));
					}
					lists.add(new GetExamListEntity(listss, equip_id));
				}
				String args = new Gson().toJson(lists);
				Log.e("lyt", args);

				RequestParams param = new RequestParams();
				param.setContentEncoding("utf-8");
				param.put("key", key);
				param.put("em_id", em_id);
				param.put("list", args);

				Dialog progressDialog = dialog_util.showWaitingDialog(
						XjbDetailsActivity.this, "稍等一会", false);

				if (client != null) {
					try {
						client.post(APIURL.ITSM.IT_ADDEQUIPEXAMLIST, param,
								new AddJsonHandler(XjbDetailsActivity.this,
										progressDialog));

					} catch (Exception e) {
					}
				}

			}
		});
	}

	// 巡检表详情
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			LogUtil.e("lyt", response.toString());
			progressDialog.dismiss();
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				JSONObject dlist = response.getJSONObject("dlist");
				JSONObject allMap = dlist.getJSONObject("allMap");

				JSONArray array;
				JSONObject equipTypeMap = dlist.getJSONObject("equipTypeMap");

				Iterator<String> it = equipTypeMap.keys();
				while (it.hasNext()) {
					String etype = (String) equipTypeMap.get(it.next());
					LogUtil.e("lyt", etype);
					String[] ss = etype.split(":");
					LogUtil.e("lyt", ss[0]);
					array = allMap.getJSONArray("equip_list_" + ss[0]);
					Log.e("lyt", array.toString());
					ArrayList<ExamListEntity> lists = new Gson().fromJson(
							array.toString(), new TypeToken<List<ExamListEntity>>() {
							}.getType());
					list.addAll(lists);
				}
				Log.e("lyt", list.get(0).getEq_explain());
				Collections.sort(list, new Comparator<ExamListEntity>() {

					@Override
					public int compare(ExamListEntity arg0, ExamListEntity arg1) {
						// TODO Auto-generated method stub
						String a0 = arg0.getEuip_index();
						String a1 = arg1.getEuip_index();
						int arg0s = 0;
						int arg1s = 0;
						try {
							if (null != a0) {
								arg0s = Integer.valueOf(a0);
							}
							if (null != a1) {
								arg1s = Integer.valueOf(a1);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (arg0s > arg1s) {
							return 1;
						} else if (arg0s < arg1s) {
							return -1;
						} else {
							return 0;
						}

					}
				});
				// for (int i = 0; i < lists.size(); i++) {
				// list.addAll(lists);
				// }
				MyExpandableAdapter adapter = new MyExpandableAdapter();
				xjb_list_detail.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// 巡检表提交
	private class AddJsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected AddJsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			LogUtil.e("lyt", response.toString());

			progressDialog.dismiss();

			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				String message = response.getString("message");
				if (message.equals("success")) {
					ToastManager.getInstance(context)
							.showToastcenter("提交成功！");
					Intent intent = new Intent();
					int result = getIntent().getIntExtra("result",
							SPConstant.TAGXJBACTIVITY);
					intent.putExtra("refresh", true);
					XjbDetailsActivity.this.setResult(result, intent);
					XjbDetailsActivity.this.finish();
				} else {
					ToastManager.getInstance(context)
							.showToastcenter("提交失败！！！");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class MyExpandableAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0).getEquipExamList().size();
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return list.get(arg0).getEquipExamList().get(arg1);
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return arg1;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (arg2 == null) {
				arg2 = LayoutInflater.from(context).inflate(
						R.layout.examlist_main_item, null);
				holder = new ViewHolder();
				holder.examlist_name = (TextView) arg2
						.findViewById(R.id.examlist_name);
				holder.examlist_content = (TextView) arg2
						.findViewById(R.id.examlist_content);

				arg2.setTag(holder);
			} else {
				holder = (ViewHolder) arg2.getTag();
			}
			ExamListEntity mlist = list.get(arg0);
			String Model_num = mlist.getModel_num();
			String Euip_index = mlist.getEuip_index();
			String Brand_name = mlist.getBrand_name();
			String Eq_explain = mlist.getEq_explain();
			if(null!=Euip_index&&null!=Eq_explain&&null!=Brand_name&&null!=Model_num){
			holder.examlist_name.setText(Euip_index + "、" + Brand_name
					+ Model_num);// 名称
			holder.examlist_content.setText(Html.fromHtml(Eq_explain));
			}
			return arg2;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (arg3 == null) {
				arg3 = LayoutInflater.from(context).inflate(
						R.layout.mexamlist_main_item, null);
				holder = new ViewHolder();
				holder.mexamlist_name = (TextView) arg3
						.findViewById(R.id.mexamlist_name);
				holder.mexamlist_content = (EditText) arg3
						.findViewById(R.id.mexamlist_content);
				holder.mexamlist_styte = (TextView) arg3
						.findViewById(R.id.mexamlist_styte);
				holder.Rg = (RadioGroup) arg3
						.findViewById(R.id.radio_group_mexam);
				holder.radio_button1 = (RadioButton) arg3
						.findViewById(R.id.radio_button1);
				holder.radio_button2 = (RadioButton) arg3
						.findViewById(R.id.radio_button2);

				arg3.setTag(holder);
			} else {
				holder = (ViewHolder) arg3.getTag();

			}
			holder.fel = arg0;
			holder.fell = arg1;
			ArrayList<ExammListEntity> mlist = list.get(arg0).getEquipExamList();
			ExammListEntity ms = mlist.get(arg1);

			String Attr_name = ms.getAttr_name();
			String Attr_value = ms.getAttr_value();
			String Attr_color = ms.getAttr_color();
			String Display_unit = ms.getDisplay_unit();
			String Normal_range = ms.getNormal_range();
			String Exam_item_index = ms.getExam_item_index();
			String Checked = ms.getChecked();
			String Checkeds = ms.getCheckeds();
			String Examine_type = ms.getExamine_type();
			String Examine_types = ms.getExamine_types();
			String Display_type = ms.getDisplay_type();

			holder.mexamlist_content.setText(Attr_value);

			if ("3".equals(Display_type)) {
				holder.Rg.setVisibility(ViewGroup.VISIBLE);
				holder.mexamlist_content.setVisibility(ViewGroup.GONE);
				holder.mexamlist_styte.setVisibility(ViewGroup.GONE);
			} else if ("4".equals(Display_type)) {
				holder.mexamlist_content.setVisibility(ViewGroup.VISIBLE);
				holder.mexamlist_styte.setVisibility(ViewGroup.VISIBLE);
				holder.Rg.setVisibility(ViewGroup.GONE);
			}

			if (null != Checked && Checked.equals("true") && null != Checkeds
					&& Checkeds.equals("false")) {
				holder.radio_button1.setChecked(true);
				holder.radio_button2.setChecked(false);
			} else if (null != Checked && Checked.equals("false")
					&& null != Checkeds && Checkeds.equals("true")) {
				holder.radio_button1.setChecked(false);
				holder.radio_button2.setChecked(true);
			} else if (null != Checked && Checked.equals("false")
					&& null != Checkeds && Checkeds.equals("false")) {
				holder.radio_button1.setChecked(true);
				holder.radio_button2.setChecked(false);
			}
			holder.Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					if (arg0.getId() == R.id.radio_group_mexam) {
						switch (arg1) {
						case R.id.radio_button1:
							list.get(holder.fel).getEquipExamList()
									.get(holder.fell).setChecked("true");
							list.get(holder.fel).getEquipExamList()
									.get(holder.fell).setCheckeds("false");
							list.get(holder.fel).getEquipExamList()
									.get(holder.fell).setAttr_value("0");
							break;
						case R.id.radio_button2:
							list.get(holder.fel).getEquipExamList()
									.get(holder.fell).setChecked("false");
							list.get(holder.fel).getEquipExamList()
									.get(holder.fell).setCheckeds("true");
							list.get(holder.fel).getEquipExamList()
									.get(holder.fell).setAttr_value("1");
							break;
						default:
							break;
						}
					}
					// if (arg1 == id1) {
					// Log.e("lyt", "0");
					// list.get(holder.fel).getEquipExamList().get(holder.fell).setAttr_value("0");
					// } else if (arg1 == id2) {
					// Log.e("lyt", "1");
					// list.get(holder.fel).getEquipExamList().get(holder.fell).setAttr_value("1");
					//
					// }

				}
			});
			holder.mexamlist_content.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					list.get(holder.fel).getEquipExamList().get(holder.fell)
							.setAttr_value(arg0.toString());
				}
			});
			holder.mexamlist_name.setText(Exam_item_index + "、" + Attr_name);// 名称

			if (null != Examine_type && null != Examine_types) {
				holder.radio_button1.setText(Examine_type);
				holder.radio_button1.setTextColor(context.getResources()
						.getColor(R.color.black));
				holder.radio_button2.setTextColor(context.getResources()
						.getColor(R.color.black));
				holder.radio_button2.setText(Examine_types);
			}

			if (null != Attr_color && Attr_color.equals("true")) {
				holder.mexamlist_content.setTextColor(context.getResources()
						.getColor(R.color._red));
			} else {
				holder.mexamlist_content.setTextColor(context.getResources()
						.getColor(R.color.black));
			}
			if (null != Normal_range && null != Display_unit) {
				holder.mexamlist_styte.setText(Normal_range + Display_unit);
			}

			return arg3;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean back = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			int result = getIntent().getIntExtra("result",
					SPConstant.TAGXJBACTIVITY);
			intent.putExtra("refresh", false);
			XjbDetailsActivity.this.setResult(result, intent);
			XjbDetailsActivity.this.finish();
			back = false;
		}
		return back;

	}

	public static class ViewHolder {
		public TextView examlist_name;// 名称
		public TextView examlist_content;
		public TextView mexamlist_name;//
		public EditText mexamlist_content;
		public TextView mexamlist_styte;
		public RadioGroup Rg;
		public RadioButton radio_button1;
		public RadioButton radio_button2;
		public int fel;
		public int fell;

	}

}
