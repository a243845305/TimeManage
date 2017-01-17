package com.timemanage.utils;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/**
 * 帮助回收activity的类
 * Created by Yawen_Li on 2017/1/13.
 */

public class ActivityCollectorUtil {
    public static List<Activity> activities = new ArrayList<Activity>();


    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}

