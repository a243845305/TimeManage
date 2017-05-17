package com.timemanage.view.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.timemanage.R;
import com.timemanage.TimeManageAppliaction;
import com.timemanage.adapter.FragmentAdapter;
import com.timemanage.db.DataBaseHelper;
import com.timemanage.db.DataBaseManager;
import com.timemanage.utils.ACache;
import com.timemanage.utils.ActivityCollectorUtil;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.fragment.ListAppTimeFragment;
import com.timemanage.view.fragment.PieChartFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private long currentBackPressedTime = 0;
    private static final int BACK_PRESSED_INTERVAL = 2000;

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ViewPager viewPagerContent;
    private MenuItem menuItem;

    private Button btnStatistics;

    private List<Fragment> fmList;
    private FragmentAdapter fmAdapter;

    private DataBaseManager dbManager;
    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        //数据库初始化操作
        dbManager = new DataBaseManager(TimeManageAppliaction.getContext());
        LogUtil.e("LOOOOOOK", "Here!!!!!!!!!!");
        DebugDB.getAddressLog();
        mCache = ACache.get(TimeManageAppliaction.getContext());

        //启动前台Service
        Intent startServiceIntent = new Intent();
        ComponentName componentName = new ComponentName(ConstantUtil.pkgName, ConstantUtil.serviceName);
        startServiceIntent.setComponent(componentName);
        this.startService(startServiceIntent);

        initView();

    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void initView() {
//        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPagerContent = (ViewPager) findViewById(R.id.vp_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        btnStatistics = (Button) findViewById(R.id.btn_statistics);


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        fmList = new ArrayList<Fragment>();
        if (fmList != null) {
//            fmList.add(new BarChartFragment());
//            fmList.add(new PieChartFragment());
            fmList.add(new ListAppTimeFragment());
        }
        fmAdapter = new FragmentAdapter(getSupportFragmentManager(), fmList);
        viewPagerContent.setAdapter(fmAdapter);
        //只放一个饼状图在上面，扩展的功能先留着，方便以后扩展
//        tabLayout.setupWithViewPager(viewPagerContent);
//
//        tabLayout.getTabAt(0).setText("条形图");
//        tabLayout.getTabAt(0).setText("饼状图");

        navigationView.setNavigationItemSelectedListener(this);
        btnStatistics.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCollectorUtil.finishAll();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.action_settings);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                Intent intent1 = new Intent(MainActivity.this, TeamListActivity.class);
                MainActivity.this.startActivity(intent1);
                break;
            case R.id.nav_gallery:
                Intent intent = new Intent(MainActivity.this, PersonalCompileActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nav_logout:
                ActivityCollectorUtil.finishAll();
                //删除所用缓存
                mCache.clear();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int v = view.getId();
        switch (v) {
            case R.id.btn_statistics:
                StatisticsActivity.actionStart(this);
                break;
            default:
                break;
        }
    }
}
