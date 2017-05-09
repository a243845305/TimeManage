
package com.timemanage.view.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.timemanage.R;
import com.timemanage.bean.User;
import com.timemanage.presenter.activity_presenter.PersonalCompilePresenter;
import com.timemanage.presenter.activity_presenter_interface.IPersonalCompilePresenter;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.activity_interface.IPersonalCompileActivity;
import com.timemanage.widgets.RoundImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 修改个人信息Activity
 *
 * @update 李雅文
 */
public class PersonalCompileActivity extends BaseActivity implements View.OnClickListener, IPersonalCompileActivity {

    private ImageButton ib_Back;        //返回按钮
    private RoundImageView rw_Head;     //头像图标按钮
    private TextView tv_UserNick;       //昵称
    private Button btn_logout;          //完成按钮

    private ProgressDialog pdUpdatawait;
    private User user;

    /**
     * 定义三种状态
     */
    private static final int HEAD_PORTRAIT_PIC = 1;//相册
    private static final int HEAD_PORTRAIT_CAM = 2;//相机
    private static final int HEAD_PORTRAIT_CUT = 3;//图片裁剪
    private File photoFile;
    private Bitmap photoBitmap;

    private String compilestr;  //编辑后的文本

    private IPersonalCompilePresenter iPersonalCompilePresenter;
    private Handler handler;

    private String citystr;
    private String codeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persional_compile);
        handler = new MyHandler(PersonalCompileActivity.this);
        //将Activity与Presenter进行绑定
        iPersonalCompilePresenter = new PersonalCompilePresenter(PersonalCompileActivity.this, PersonalCompileActivity.this, handler);

        Log.e("PersonalCA", "userAvatar:" + UserInfoUtil.getUserAvatar());
        initView();
    }

    private void initView() {
        ib_Back = (ImageButton) this.findViewById(R.id.ib_back);
        rw_Head = (RoundImageView) this.findViewById(R.id.rw_head);
        tv_UserNick = (TextView) this.findViewById(R.id.tv_user_nick);
        tv_PhoneNumber = (TextView) this.findViewById(R.id.tv_phone_number);
        tv_UserAddress = (TextView) this.findViewById(R.id.tv_user_address);
        tv_UserType = (TextView) this.findViewById(R.id.tv_user_type);
        tv_UserIndustry = (TextView) this.findViewById(R.id.tv_industry);
        tv_UserRealName = (TextView) this.findViewById(R.id.tv_realName);
        tv_UserScale = (TextView) this.findViewById(R.id.tv_scale);
        btn_finish = (Button) this.findViewById(R.id.btn_finish);
        rl_head = (RelativeLayout) this.findViewById(R.id.rl_head);
        rl_name = (RelativeLayout) this.findViewById(R.id.rl_name);
        rl_address = (RelativeLayout) this.findViewById(R.id.rl_address);
        rl_industry = (RelativeLayout) this.findViewById(R.id.rl_industry);
        rl_scale = (RelativeLayout) this.findViewById(R.id.rl_scale);

        rl_industry.setOnClickListener(this);
        rl_scale.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        ib_Back.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        pdUpdatawait = new ProgressDialog(PersonalCompileActivity.this);
        pdUpdatawait.setMessage("更新中...");
        pdUpdatawait.setCanceledOnTouchOutside(false);
        pdUpdatawait.setCancelable(true);

        user = iPersonalCompilePresenter.getData();
        updataView(user);
        if (NetUtil.isNetAvailable(PersonalCompileActivity.this)){
            iPersonalCompilePresenter.requestUserData();
        }else {
            showNoNet();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_head:
                showPhotoDialog();
                break;
            case R.id.rl_name:
                compilestr = tv_UserRealName.getText().toString()+"en";
                showCompileDialog(R.id.rl_name);
                break;
            case R.id.rl_industry:
                compilestr = tv_UserIndustry.getText().toString();
                showCompileDialog(R.id.rl_industry);
                break;
            case R.id.rl_scale:
                compilestr = tv_UserScale.getText().toString();
                showCompileDialog(R.id.rl_scale);
                break;
            case R.id.rl_address:
                //TODO 将populwindow写到这
                showAddressSelector();
                break;
            case R.id.btn_finish:
                if (NetUtil.isNetAvailable(PersonalCompileActivity.this)){
                    showProgress(true);
                    iPersonalCompilePresenter.doUpdate();
                }else {
                    showNoNet();
                }
                break;
            default:
                break;
        }

    }

    //编辑用户地址dialog
    public void showAddressSelector() {
        View view = getLayoutInflater().inflate(R.layout.activity_three_level_linkage, null);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        TextView btn_finish = (TextView) view.findViewById(R.id.btn_finish);
        final CityPicker cityPicker = (CityPicker) view.findViewById(R.id.citypicker);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_pop_alert));

        //点击窗口外边消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        //显示位置
        popupWindow.showAtLocation(rl_address, Gravity.BOTTOM, 0, 0);
        cityPicker.setOnSelectingListener(new CityPicker.OnSelectingListener() {
            @Override
            public void selected(boolean selected) {
                citystr = cityPicker.getCity_string();
                codeStr = cityPicker.getCity_code_string();
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 获取最终的数据citystr
                tv_UserAddress.setText(citystr);
                popupWindow.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //判断输入是否为空
    public boolean ismEmpty( String input )
    {
        if ( input == null || "".equals( input ) )
            return true;

        for ( int i = 0; i < input.length(); i++ )
        {
            char c = input.charAt( i );
            if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
            {
                return false;
            }
        }
        return true;
    }

    //编辑昵称、用户行业、规模 调用的dialog
    private void showCompileDialog(final int id) {
        final EditText et = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑：");
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ismEmpty(et.getText().toString())) {
                    Toast.makeText(PersonalCompileActivity.this, "请输入信息！", Toast.LENGTH_SHORT).show();
                } else {
                    compilestr = et.getText().toString();
                    switch (id){
                        case R.id.rl_name:
                            tv_UserRealName.setText(compilestr);
                            break;
                        case R.id.rl_industry:
                            tv_UserIndustry.setText(compilestr);
                            break;
                        case R.id.rl_scale:
                            tv_UserScale.setText(compilestr);
                            break;
                        default:
                            break;
                    }
                    LogUtil.d("PersonalCompileA","compilestr:"+compilestr);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    //显示Dialog选择拍照还是从相册选择
    private void showPhotoDialog() {
        final Dialog dialog = new Dialog(this, R.style.PhotoDialog);
        final View view = LayoutInflater.from(PersonalCompileActivity.this).inflate(R.layout.diallog_personal_head_select, null);
        dialog.setContentView(view);
        TextView tv_PhotoGraph = (TextView) view.findViewById(R.id.tv_personal_photo_graph);
        TextView tv_PhotoAlbum = (TextView) view.findViewById(R.id.tv_personal_photo_album);
        TextView tv_Cancel = (TextView) view.findViewById(R.id.tv_cancel);

        tv_PhotoGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoGraph();
            }
        });

        tv_PhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoAlbum();
            }
        });

        tv_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //设置出现Dialog位置
        Window window = dialog.getWindow();
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        dialog.show();
    }

    //打开相册方法
    private void openPhotoAlbum() {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, HEAD_PORTRAIT_PIC);
    }

    //打开相机方法
    private void openPhotoGraph() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            photoFile = new File(file, System.currentTimeMillis()+"");

            Uri photoUri = Uri.fromFile(photoFile);
            LogUtil.d("PersonalCompileA", "文件路径：" + photoUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, HEAD_PORTRAIT_CAM);
        } else {
            LogUtil.d("PersonalCompileA", "没SD卡");
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    //回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case HEAD_PORTRAIT_CAM:
                    startPhotoZoom(Uri.fromFile(photoFile));
                    break;
                case HEAD_PORTRAIT_PIC:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case HEAD_PORTRAIT_CUT:
                    if (data != null) {
                        photoBitmap = data.getParcelableExtra("data");
                        rw_Head.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        rw_Head.setImageBitmap(photoBitmap);
                        try {
                            File SDCardRoot = Environment.getExternalStorageDirectory();
                            if (ImageUtils.saveBitmap2file(photoBitmap)) {
                                LogUtil.e("PersonalCompileA", "保存图片文件成功");
                                LogUtil.e("PersonalCompileA", "保存图片路径：" + SDCardRoot + ConstantUtil.AVATAR_FILE_PATH);
                                String photoPath = SDCardRoot + ConstantUtil.AVATAR_FILE_PATH;
                                iPersonalCompilePresenter.setImagePath(photoPath);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    break;
            }
        }
        if (requestCode == 1000 && resultCode == 1001) {
            String citystr = data.getStringExtra("result");
            Toast.makeText(this, citystr, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 打开系统图片裁剪功能
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, HEAD_PORTRAIT_CUT);
    }


    public class MyHandler extends Handler {

        WeakReference<PersonalCompileActivity> weakReference;

        public MyHandler(PersonalCompileActivity fragment) {
            weakReference = new WeakReference<PersonalCompileActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtil.GET_NET_DATA:
                    user = iPersonalCompilePresenter.getData();
                    updataView(user);
                    break;
                default:
                    break;
            }
        }
    }

    private void updataView(User user) {
        //本地一定存在的
        tv_UserNick.setText(user.getName());
        tv_PhoneNumber.setText(user.getPhone());
        //不一定有值
        if (!ismEmpty(user.getAvatar())){
            Picasso.with(this)
                    .load(user.getAvatar())
                    .placeholder(R.mipmap.img_default_user_portrait_150px)
                    .error(R.mipmap.img_default_user_portrait_150px)
                    .into(rw_Head);
            Log.e("PersonalCA", "avatar:" + user.getAvatar());
        }
        if (!ismEmpty(user.getRealName())){
            tv_UserRealName.setText(user.getRealName());
        }
        if (!ismEmpty(user.getAddress())){
            tv_UserAddress.setText(user.getAddress());
        }
        if (!ismEmpty(user.getIndustry())){
            tv_UserIndustry.setText(user.getIndustry());
        }
        if (!ismEmpty(user.getScale())){
            tv_UserScale.setText(user.getScale());
        }
        if ("PUBLIC".equals(user.getUserType())) {
            tv_UserType.setText("普通用户");
        } else {
            tv_UserType.setText("专家用户");
        }
    }


    //--------------------接口方法--------------------

    @Override
    public String getRealName() {
        return tv_UserRealName.getText().toString();
    }


    @Override
    public String getUserPosition() {
        return tv_UserAddress.getText().toString();
    }

    @Override
    public String getUserIndustry() {
        return tv_UserIndustry.getText().toString();
    }

    @Override
    public String getUserScale() {
        return tv_UserScale.getText().toString();
    }

    @Override
    public void showUpdataSuccess() {
        Toast.makeText(PersonalCompileActivity.this, "修改信息成功", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showUpdataFailed() {
        Toast.makeText(PersonalCompileActivity.this, "修改信息失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoNet() {
        Toast.makeText(PersonalCompileActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress(boolean flag) {
        if (flag) {
            pdUpdatawait.show();
        } else {
            pdUpdatawait.cancel();
        }
    }

//---------------------接口方法结束--------------------
}
