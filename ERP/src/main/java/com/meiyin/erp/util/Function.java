package com.meiyin.erp.util;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;

import com.meiyin.erp.R;
import com.meiyin.erp.application.MyApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Function {

	/*
	 * Html图片转换器 使用反射原理把字符串转成R.drawable资源进行适配
	 */
	public static ImageGetter imgGetter = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			// LogUtil.i("zkr", source);
			int i = 0;
			try {
				Field field = com.meiyin.erp.R.drawable.class.getField(source);
				i = field.getInt(new R.drawable());
				drawable = MyApplication.getInstance().getResources()
						.getDrawable(i);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth() - 40,
						drawable.getIntrinsicHeight() - 40);
				// LogUtil.d("zkr", i + "");
			} catch (Exception e) {
				// LogUtil.e("zkr", e.toString());
			}

			return drawable;
		}
	};
	public static String getNickNameToHtml(String wxNickname) {
		if (wxNickname == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		try {
			byte[] bytes = wxNickname.getBytes("UTF-8");
			for(int i=0;i<bytes.length;i++){
				//LogUtil.i("Function", "bytes[i]:"+bytes[i]);
			}
			String hexString = getNicknameBytes(bytes);
			int len = 0;
			List<String> listStr = new ArrayList<String>();
			for (int i = 0; i < hexString.length(); len++) {
				String tmp = hexString.substring(i, i + 2);
				if (tmp.equals("ee")) {
					String emujiCode = hexString.substring(i, i + 6);
					String emujiCodes = emujiCode + "20";
					String tempEmuji = "<img src=\"" + emujiCodes + "\"/>";
					// LogUtil.i("Function", "tempEmuji:"+tempEmuji);
					listStr.add(tempEmuji);

					i += 6;
				} else {
					String tempStr = wxNickname.substring(len, len + 1);
					if (isEnglish(tempStr) || tempStr.equals(" ")
							|| isNumeric(tempStr)) {
						System.out.println(tempStr);
						i += 2;
					} else {
						System.out.println(tempStr);
						i += 6;
					}
					

					listStr.add(tempStr);
				}
			}
			for (String s : listStr) {
				sb.append(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 是否是英文
	 * 
	 * @paramc
	 * @return
	 */
	public static boolean isEnglish(String charaString) {

		return charaString.matches("^[a-zA-Z]*");
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	private static String getNicknameBytes(byte[] bytes) {
		// android.util.LogUtil.e(tag, "----------XxLog--Start--------");
		if (bytes == null)
			return "";
		int len = bytes.length;
		if (len <= 0)
			return "";
		int round = (bytes.length - 1) / 100 + 1;
		int p = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < round; i++) {
			int max = (len - p) < 100 ? (len - p) : 100;
			for (int j = 0; j < max; i++) {
				if (p < len)
					sb.append(String.format("%02x", bytes[p++]));
				else
					break;
			}
			LogUtil.e("Fuction", "Hex:" + sb.toString());
		}

		LogUtil.e("Fuction", "----------XxLog--End--------");
		return sb.toString();
	}
}
