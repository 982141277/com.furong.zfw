package com.jcd.psms.Application;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * 提供程序退出的方法
 * @author Administrator
 *1
 */

public class BaseApplication extends Application{
	private static Stack<Activity> activityStack;
	private static BaseApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

	}
	
	public static BaseApplication getInstance() {
		return mInstance;
	}
	

	
	/****************** 控制应用中activity的声明周期 *****************/

	/**
	 * add Activity 添加Activity到栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * get current Activity 获取当前Activity（栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		removeActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				removeActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit() {
		try {
			finishAllActivity();
		} catch (Exception e) {
		}
	}
}
