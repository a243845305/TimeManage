package com.timemanage.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.timemanage.R;
import com.timemanage.adapter.TeamListAdapter;
import com.timemanage.api.IDeveloperDatas;
import com.timemanage.bean.Developer;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.utils.NetUtil;
import com.timemanage.utils.RetrofitUtil;
import com.timemanage.view.activity_interface.ITeamListActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 团队成员界面
 * Created by Yawen_Li on 2017/5/17.
 */
public class TeamListActivity extends BaseActivity implements View.OnClickListener,ITeamListActivity {

    private ImageButton ib_Back;        //返回按钮
    private ListView lv_TeamListView;
    private ProgressDialog pdUpdatawait;
    private Context context;
    private MyHandler handler;

    private ArrayList<Developer> developers;
    private TeamListAdapter teamListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        context = TeamListActivity.this;
        handler = new MyHandler(TeamListActivity.this);
        initView();
    }

    private void initView(){
        ib_Back = (ImageButton) this.findViewById(R.id.ib_back);
        lv_TeamListView = (ListView) this.findViewById(R.id.lv_teamlist);

        pdUpdatawait = new ProgressDialog(TeamListActivity.this);
        pdUpdatawait.setMessage("加载中...");
        pdUpdatawait.setCanceledOnTouchOutside(false);
        pdUpdatawait.setCancelable(true);

        showProgress(true);
        if (NetUtil.isNetAvailable(TeamListActivity.this)){
            getDepDatas();
        }else {
            showProgress(false);
            showNoNet();
        }
        ib_Back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void getDepDatas(){
        Call<JsonObject> call = RetrofitUtil.getRetrofit().create(IDeveloperDatas.class).getDepDatas();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Response<JsonObject> response, Retrofit retrofit) {
                if (response.isSuccess()){
                    JsonObject jsonstr = response.body();
                    JsonArray jsonArray = jsonstr.get("developer").getAsJsonArray();
                    Gson gson = new Gson();
                    LogUtil.e("getDepData",jsonstr.toString());
                    developers = gson.fromJson(jsonArray, new TypeToken<ArrayList<Developer>>(){}.getType());
                    handler.sendEmptyMessage(ConstantUtil.GET_NET_DATA);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtil.e("fail",t.toString());
                showProgress(false);
                Toast.makeText(context,"加载数据失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    public class MyHandler extends Handler {
        WeakReference<TeamListActivity> weakReference;

        public MyHandler(TeamListActivity activity) {
            weakReference = new WeakReference<TeamListActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtil.GET_NET_DATA:

                    teamListAdapter = new TeamListAdapter(context,developers);
                    lv_TeamListView.setAdapter(teamListAdapter);
                    showProgress(false);
                    break;
                default:
                    break;
            }
        }
    }

//=============================接口方法========================

    @Override
    public void showNoNet() {
        Looper.prepare();
        Toast.makeText(TeamListActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    @Override
    public void showProgress(boolean flag) {
        if (flag) {
            pdUpdatawait.show();
        } else {
            pdUpdatawait.cancel();
        }
    }
}
