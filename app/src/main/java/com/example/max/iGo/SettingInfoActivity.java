package com.example.max.iGo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.UserDefined.MagAddressActivity;
import com.example.max.iGo.UserDefined.Nickname_Setting_Activity;
import com.example.max.iGo.UserDefined.RoundImageView;
import com.example.max.iGo.Utils.CheckNetState;
import com.example.max.iGo.Utils.FileUtil;
import com.example.max.iGo.Utils.GlobalCheckGet;
import com.example.max.iGo.Utils.GlobalCheckPost;
import com.example.max.iGo.Utils.PrefUtils;
import com.example.max.iGo.Utils.SelectPicPopupWindow;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 个人资料设置界面
 * Created by Max on 2016/3/9.
 */
public class SettingInfoActivity extends BaseActivity {

    private TextView titleBarText;
    private ImageView image_back;
    private RelativeLayout setting_addr_layout;
    private RelativeLayout setting_sex_layout;
    private TextView sex2;
    private RelativeLayout rl_nickname_info;
    private static String urlpath = "";            // 图片本地路径
    private SelectPicPopupWindow menuWindow;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private RoundImageView roundImageView;
    private TextView tv_nickname_info;
    Intent intent = null;
    JSONObject jsonObject = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinginfo);
        initView();
        if (intent.getStringExtra("above") != null && intent.getStringExtra("above").equals("true")) {
            System.out.println("来自上一级");
            if (CheckNetState.isNetworkAvailable(SettingInfoActivity.this))
                getUserDataFromServer();
            else
                Toast.makeText(SettingInfoActivity.this,"请检查您的网络设置",Toast.LENGTH_SHORT).show();
            if ("0".equals(PrefUtils.getSex(SettingInfoActivity.this))) {
                sex2.setText("女");
            } else if ("1".equals(PrefUtils.getSex(SettingInfoActivity.this))) {
                sex2.setText("男");
            } else {
                sex2.setText("");
            }
            if (PrefUtils.getUserName(SettingInfoActivity.this).equals("null")) {
                tv_nickname_info.setText("");

            } else {
                tv_nickname_info.setText(PrefUtils.getUserName(SettingInfoActivity.this));

            }
            if (new File(PrefUtils.getUrlPath(SettingInfoActivity.this)).exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(PrefUtils.getUrlPath(SettingInfoActivity.this));
                roundImageView.setImageBitmap(bitmap);
            }else{
                roundImageView.setImageBitmap(getImageFromAssetsFile("defaultheadimg.jpg"));

            }


        }
        System.out.println("我一直在执行");
        if ("yes".equals(intent.getStringExtra("changeNickname"))) {

            System.out.println("来自nickname");
            if (CheckNetState.isNetworkAvailable(SettingInfoActivity.this))
            ModifyUserInfo_OnServer("username", PrefUtils.getUserName(SettingInfoActivity.this));
            else
                Toast.makeText(SettingInfoActivity.this, "请检查您的网络设置", Toast.LENGTH_SHORT).show();

        }


        setting_addr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingInfoActivity.this, MagAddressActivity.class));
                finish();
            }
        });
        setting_sex_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingInfoActivity.this);

                builder.setTitle("选择性别");
                int i = 0;
                if (sex2.getText().toString().equals("男")) {
                    i = 0;
                } else if (sex2.getText().toString().equals("女")) {
                    i = 1;
                }
                builder.setSingleChoiceItems(new String[]{"男", "女"}, i, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sex[] = new String[]{"男", "女"};
                        //  sex2.setText(sex[which]);
                        //   PrefUtils.setSex(SettingInfoActivity.this, sex[which]);
                        dialog.dismiss();
                        int value = (which == 0 ? 1 : 0);
                        if (CheckNetState.isNetworkAvailable(SettingInfoActivity.this))
                            ModifyUserInfo_OnServer("sex", value + "");

                        else
                            Toast.makeText(SettingInfoActivity.this, "请检查您的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        rl_nickname_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingInfoActivity.this, Nickname_Setting_Activity.class));
                finish();
            }
        });
        titleBarText.setText("个人资料");
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


    }

    public void ClickToModifyPortrait(View v) {
        menuWindow = new SelectPicPopupWindow(SettingInfoActivity.this, new MyChoosePicListener());
        menuWindow.showAtLocation(findViewById(R.id.activity_settinginfo),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


    }

    public void initView() {
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        sex2 = (TextView) findViewById(R.id.tv_sex2);
        roundImageView = (RoundImageView) findViewById(R.id.roundImageView);
        rl_nickname_info = (RelativeLayout) findViewById(R.id.rl_nickname_info);
        setting_sex_layout = (RelativeLayout) findViewById(R.id.setting_sex_layout);
        setting_addr_layout = (RelativeLayout) findViewById(R.id.setting_addr_layout);
        tv_nickname_info = (TextView) findViewById(R.id.tv_nickname_info);
        intent = getIntent();

    }

    class MyChoosePicListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;

        }
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(SettingInfoActivity.this, "temphead.jpg", photo);
            // PrefUtils.setUrlPath(SettingInfoActivity.this, urlpath);
            // System.out.println("图片的保存路径" + urlpath);
            // roundImageView.setImageDrawable(drawable);
            if (CheckNetState.isNetworkAvailable(SettingInfoActivity.this))
                ModifyUserInfo_OnServer("icon", urlpath);
            else
                Toast.makeText(SettingInfoActivity.this, "请检查您的网络设置", Toast.LENGTH_SHORT).show();


        }
    }

    public void click_for_nickname(View v) {
        startActivity(new Intent(SettingInfoActivity.this, Nickname_Setting_Activity.class));
    }

    private void ModifyUserInfo_OnServer(final String type, final String value) {

        String user_name = tv_nickname_info.getText().toString();
        //  int user_sex_flag = 1;
        String user_sex = sex2.getText().toString();

        File icon = null;
        //   if (!TextUtils.isEmpty(PrefUtils.getUrlPath(SettingInfoActivity.this)))

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(800);
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        if ("username".equals(type)) {
            params.addBodyParameter("name", value);
        } else if ("sex".equals(type)) {
            System.out.println("sex++++++++++++++++++++++++++++");


            params.addBodyParameter("sex", value);

        } else if ("icon".equals(type)) {
            System.out.println("icon++++++++++++++++++++++++++++");
            icon = new File(value);
            params.addBodyParameter("icon", icon);
        }


        params.addBodyParameter("id", PrefUtils.getId(SettingInfoActivity.this) + "");
        params.addBodyParameter("validation", PrefUtils.getValidation(SettingInfoActivity.this));


        http.send(HttpRequest.HttpMethod.POST, GlobalCheckPost.SERVER_URL + "/mobile_profile/modify",
                params, new RequestCallBack() {
                    @Override
                    public Object getUserTag() {
                        return super.getUserTag();
                    }

                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        //  System.out.println(responseInfo.result.toString() + "LLLsuccess");
                        //  if (PrefUtils.getUrlPath(SettingInfoActivity.this))
                        if (type.equals("icon")) {
                            PrefUtils.setUrlPath(SettingInfoActivity.this, value);
                        }
                        if (type.equals("username")) {
                            PrefUtils.setUserName(SettingInfoActivity.this, value);

                        }
                        if (type.equals("sex")) {
                            PrefUtils.setSex(SettingInfoActivity.this, value);
                        }
                        /**
                         * 注意这里，还没哟不啊ImgURL写入sharedPrefrence里面，所以这里不能够以
                         * PrefUtils.getHeadImgUrl(SettingInfoActivity.this)做为判断条件，除非我们重新获取，重新
                         * 写入。
                         */
//                        if (PrefUtils.getHeadImgUrl(SettingInfoActivity.this) != null &&
//                                !PrefUtils.getHeadImgUrl(SettingInfoActivity.this).equals("null"))
//                        {
                        if (PrefUtils.getUrlPath(SettingInfoActivity.this).contains("temphead")) {
                            Bitmap bitmap = BitmapFactory.decodeFile(PrefUtils.getUrlPath(SettingInfoActivity.this));
                            roundImageView.setImageBitmap(bitmap);

                        } else {
                            roundImageView.setImageBitmap(getImageFromAssetsFile("defaultheadimg.jpg"));
                        }
                        if (PrefUtils.getUserName(SettingInfoActivity.this).equals("null")) {
                            tv_nickname_info.setText("");

                        } else {
                            tv_nickname_info.setText(PrefUtils.getUserName(SettingInfoActivity.this));

                        }

                        //  tv_nickname_info.setText(PrefUtils.getUserName(SettingInfoActivity.this));
                        if ("0".equals(PrefUtils.getSex(SettingInfoActivity.this))) {
                            sex2.setText("女");
                        } else if ("1".equals(PrefUtils.getSex(SettingInfoActivity.this))) {
                            sex2.setText("男");
                        } else {
                            sex2.setText("");
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {


                    }
                });
    }

    public void getUserDataFromServer() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(800);
        http.send(HttpRequest.HttpMethod.GET,
                GlobalCheckGet.SERVER_URL + "/mobile_profile" + "?id=" + PrefUtils.getId(SettingInfoActivity.this) + "" + "&validation=" + URLEncoder.encode(PrefUtils.getValidation(SettingInfoActivity.this)),
                new MyRequestCallBack());


    }

    class MyRequestCallBack extends RequestCallBack {


        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            super.onLoading(total, current, isUploading);
        }

        @Override
        public void onSuccess(ResponseInfo responseInfo) {
            String str = (String) responseInfo.result;
            System.out.println(str + "LLLMMMLLLLLLLLLLLLLLLLLL");
            try {
                System.out.println(str + "?????????????????????????????????");

                jsonObject = new JSONObject(str);
                PrefUtils.setUserName(SettingInfoActivity.this, jsonObject.getString("name"));
                PrefUtils.setHeadImgUrl(SettingInfoActivity.this, jsonObject.getString("icon"));
                PrefUtils.setSex(SettingInfoActivity.this, jsonObject.getString("sex"));
                System.out.println("sex:" + PrefUtils.getSex(SettingInfoActivity.this));
                if ("0".equals(PrefUtils.getSex(SettingInfoActivity.this))) {
                    sex2.setText("女");
                } else if ("1".equals(PrefUtils.getSex(SettingInfoActivity.this))) {
                    sex2.setText("男");
                } else {
                    sex2.setText("");
                }
                System.out.println("name:" + PrefUtils.getUserName(SettingInfoActivity.this));
                if (PrefUtils.getUserName(SettingInfoActivity.this).equals("null")) {
                    tv_nickname_info.setText("");

                } else {
                    tv_nickname_info.setText(PrefUtils.getUserName(SettingInfoActivity.this));

                }
                //  BitmapUtils bitmapUtils = new BitmapUtils(SettingInfoActivity.this);
                if (PrefUtils.getHeadImgUrl(SettingInfoActivity.this) != null &&
                        !PrefUtils.getHeadImgUrl(SettingInfoActivity.this).equals("null")) {
//                    if (!PrefUtils.getUrlPath(SettingInfoActivity.this).contains("temphead")) {
//                        PrefUtils.setUrlPath(SettingInfoActivity.this, Environment.getExternalStorageDirectory() + "/iGo/" + "/temphead.jpg");
//                    }
                    File iconfile=new File(Environment.getExternalStorageDirectory() + "/iGo/" + "/temphead.jpg");
                    if (!iconfile.exists()){
                        HttpUtils httpdownload=new HttpUtils();
                        httpdownload.download(GlobalCheckGet.SERVER_URL + jsonObject.getString("icon"),
                                Environment.getExternalStorageDirectory() + "/iGo/" + "/temphead.jpg", false, false,
                                new RequestCallBack<File>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<File> responseInfo) {
                                        PrefUtils.setUrlPath(SettingInfoActivity.this, Environment.getExternalStorageDirectory() + "/iGo/" + "/temphead.jpg");
                                        Bitmap bitmap = BitmapFactory.decodeFile(PrefUtils.getUrlPath(SettingInfoActivity.this));
                                        roundImageView.setImageBitmap(bitmap);

                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        roundImageView.setImageBitmap(getImageFromAssetsFile("defaultheadimg.jpg"));

                                    }
                                });

                    }


                } else {
                    roundImageView.setImageBitmap(getImageFromAssetsFile("defaultheadimg.jpg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            System.out.println("failure+++" + "LLLMMMLLLLLLLLLLLLLLLLLL");


        }
    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }


}
