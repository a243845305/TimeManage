package com.timemanage.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timemanage.R;
import com.timemanage.presenter.activity_presenter.LoginPresenter;
import com.timemanage.presenter.activity_presenter_interface.ILoginPresenter;
import com.timemanage.view.activity_interface.ILoginActivity;

/**
 * Created by Yawen_Li on 2017/5/9.
 */
public class LoginActivity extends BaseActivity implements ILoginActivity, View.OnClickListener {


    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tv_reg;
    private TextView tvForgetPsd;
    private ProgressDialog pdLoginwait;

    private ILoginPresenter iLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //将LoginActiviyt与LoginPresenter进行绑定
        iLoginPresenter = new LoginPresenter(LoginActivity.this);

        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvForgetPsd = (TextView) findViewById(R.id.tv_forgetpsd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tv_reg = (TextView) findViewById(R.id.tv_reg);

        tvForgetPsd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tv_reg.setOnClickListener(this);


        pdLoginwait = new ProgressDialog(LoginActivity.this);
        pdLoginwait.setMessage("登录中...");
        pdLoginwait.setCanceledOnTouchOutside(false);
        pdLoginwait.setCancelable(true);


    }

    /**
     * 在正式提交请求前对用户输入的数据进行验证
     * 通过验证，提交请求
     * 验证失败，提示错误信息
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                iLoginPresenter.initData();
                //输入验证
                if (iLoginPresenter.isPasswordEnable() && iLoginPresenter.isPhoneEnable()) {
                    iLoginPresenter.doLogin();
                } else {
                    //输入验证出错，显示对应错误信息
                    if (!iLoginPresenter.isPasswordEnable()) {
                        Toast.makeText(LoginActivity.this, "密码长度不得小于6位", Toast.LENGTH_LONG).show();
                    } else if (!iLoginPresenter.isPhoneEnable()) {
                        Toast.makeText(LoginActivity.this, "手机号输入不正确，请重新输入", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.tv_reg:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
            case R.id.tv_forgetpsd:
//                Intent intent2 = new Intent(LoginActivity.this,ForgetPwdActivity.class);
//                LoginActivity.this.startActivity(intent2);
                break;
            default:
                break;
        }
    }

    //--------------------接口方法开始-------------
    @Override
    public String getPhone() {
        return etPhone.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void showProgress(boolean flag) {
        if (flag) {
            pdLoginwait.show();
        } else {
            pdLoginwait.cancel();
        }
    }

    @Override
    public void showLoginSucceed() {
        Looper.prepare();
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public void showLoginFailed() {
        Looper.prepare();
        Toast.makeText(LoginActivity.this, "手机号与密码错误", Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public void showRequestTimeout() {
        Looper.prepare();
        Toast.makeText(LoginActivity.this, "登录超时，请检查网络", Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    @Override
    public void finishLoginActivity() {
        LoginActivity.this.finish();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }


//--------------------接口方法结束-------------------

}
