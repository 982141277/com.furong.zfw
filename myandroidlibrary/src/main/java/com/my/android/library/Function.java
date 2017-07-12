package com.my.android.library;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class Function {

    /*
     * Html图片转换器 使用反射原理把字符串转成R.drawable资源进行适配
     */
    public static class ImageGetter implements Html.ImageGetter {
        private Context context;

        public ImageGetter(Context context) {
            this.context = context;
        }

        @Override
        public Drawable getDrawable(String arg0) {
            // TODO Auto-generated method stub
            Drawable drawable = null;
            int i = 0;
            try {
                Field field = android.R.drawable.class.getField(arg0);
                i = field.getInt(new android.R.drawable());
                drawable = context.getResources()
                        .getDrawable(i);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() - 40,
                        drawable.getIntrinsicHeight() - 40);
            } catch (Exception e) {
            }

            return drawable;
        }

    }

    ;

    /**
     * 是否是英文
     */
    public static boolean isEnglish(String charaString) {

        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     * 是否是数字
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
