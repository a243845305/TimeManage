package com.timemanage.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.timemanage.bean.AppInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 扫描本地安装的应用,工具类
 *
 * Created by Yawen_Li on 2017/5/5.
 */
public class ApkUtil {

    static  String TAG = "ApkTool";
    public static ArrayList<AppInfo> mLocalInstallApps = null;

    public static ArrayList<AppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        ArrayList<AppInfo> myAppInfos = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                AppInfo myAppInfo = new AppInfo();
                myAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setAppPackageName(packageInfo.applicationInfo.packageName);
                myAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            LogUtil.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }



    /** first app user */
    public static final int AID_APP = 10000;
    /** offset for uid ranges for each user */
    public static final int AID_USER = 100000;
    public static String getForegroundApp(PackageManager packageManager) {
        File[] files = new File("/proc").listFiles();
        int lowestOomScore = Integer.MAX_VALUE;
        String foregroundProcess = null;
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }
            int pid;
            try {
                pid = Integer.parseInt(file.getName());
            } catch (NumberFormatException e) {
                continue;
            }
            try {
                String cgroup = read(String.format("/proc/%d/cgroup", pid));
                String[] lines = cgroup.split("\n");
                String cpuSubsystem;
                String cpuaccctSubsystem;

                if (lines.length == 2) {//有的手机里cgroup包含2行或者3行，我们取cpu和cpuacct两行数据
                    cpuSubsystem = lines[0];
                    cpuaccctSubsystem = lines[1];
                }else if(lines.length==3){
                    cpuSubsystem = lines[0];
                    cpuaccctSubsystem = lines[2];
                }else {
                    continue;
                }
                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
                    // not an application process
                    continue;
                }
                if (cpuSubsystem.endsWith("bg_non_interactive")) {
                    // background policy
                    continue;
                }
                String cmdline = read(String.format("/proc/%d/cmdline", pid));
                if (cmdline.contains("com.android.") || cmdline.contains("com.meizu.") || cmdline.contains("android.")) {
                    continue;
                }
                int uid = Integer.parseInt(
                        cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
                if (uid >= 1000 && uid <= 1038) {
                    // system process
                    continue;
                }
                int appId = uid - AID_APP;
                int userId = 0;
                // loop until we get the correct user id.
                // 100000 is the offset for each user.
                while (appId > AID_USER) {
                    appId -= AID_USER;
                    userId++;
                }
                if (appId < 0) {
                    continue;
                }
                // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
                // String uidName = String.format("u%d_a%d", userId, appId);
                File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
                if (oomScoreAdj.canRead()) {
                    int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
                    if (oomAdj != 0) {
                        continue;
                    }
                }
                int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore;
                    foregroundProcess = cmdline;
                    LogUtil.e("foregroundProcess:",cmdline);
                    //根据获取到的进程名，拿到了应用的名字并返回
//                    foregroundProcess = packageManager.getApplicationLabel(packageManager.getApplicationInfo(cmdline,
//                            PackageManager.GET_META_DATA)).toString();

                }
            } catch (IOException e) {
                e.printStackTrace();
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
            }
        }
        return foregroundProcess;
    }
    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            output.append('\n').append(line);
        }
        reader.close();
        return output.toString().trim();//不调用trim()，包名后面会带有乱码
    }
}
