package com.asiainfo.orderdishes.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.orderdishes.BaseActivity;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.UpdataInfo;
import com.asiainfo.orderdishes.entity.eventbus.BackLogin;
import com.asiainfo.orderdishes.entity.volley.ResultMap;
import com.asiainfo.orderdishes.entity.volley.merchantLoginData;
import com.asiainfo.orderdishes.entity.volley.queryAllDishesByMerchantIdData;
import com.asiainfo.orderdishes.helper.DbEntity;
import com.asiainfo.orderdishes.helper.PwdEncryptor;
import com.asiainfo.orderdishes.helper.SharedPreferencesUtils;
import com.asiainfo.orderdishes.http.volley.ResultMapRequest;
import com.asiainfo.orderdishes.http.volley.VolleyErrorHelper;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * login登陆首页
 *
 * @author gjr 2014-3-3
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getName();
    @InjectView(R.id.center) private LinearLayout center;
    @InjectView(R.id.login_loginbtn) private Button login;
    @InjectView(R.id.login_username) private EditText username;
    @InjectView(R.id.login_password) private EditText password;
    private AnimationDrawable anim_username, anim_passwd;
    private DbEntity dbEntity;
    /**
     * 对话框通知用户升级程序时msg.what标志符
     */
    private final int UPDATA_CLIENT=1;
    /**
     * 服务器超时msg.what标志符
     */
    private final int GET_UNDATAINFO_ERROR=2;
    /**
     *下载apk失败时msg.what标识符
     */
    private final int DOWN_ERROR=3;
    private int versionname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        baseApp.setLoadDishes(false);
        checkVersion();
        initView();
        initData();
        initListener();
    }

    public void initView() {
//        center = (LinearLayout) findViewById(R.id.center);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        DisplayMetrics metrice = new DisplayMetrics();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(metrice);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                gMetrice.widthPixels * 5 / 12, LayoutParams.WRAP_CONTENT);
        rp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        rp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        center.setLayoutParams(rp);
        anim_username = (AnimationDrawable) username.getBackground();
        anim_passwd = (AnimationDrawable) password.getBackground();
    }

    public void onEventBackgroundThread(BackLogin backLogin) {
        switch (backLogin.getType()) {
            case 1:
                backLogin.getDbEntity().clearDishes();
                break;
            case 2:
                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext);
                backLogin.getDbEntity().updateDb(backLogin.getInfo(),
                        backLogin.getDishes());
                sharedPreferencesUtils.refreshDishesLatestDate();
                break;
            default:
                break;
        }

    }

    public void initData() {
//        username.setText("300000028");
//        password.setText("123");
        dbEntity = new DbEntity();
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext);
//        if (sharedPreferencesUtils.isNeedSyncDishes()) {
            baseApp.setLoadDishes(true);
            BackLogin backLoginClearOrder = new BackLogin();
            backLoginClearOrder.setType(1);
            backLoginClearOrder.setDbEntity(dbEntity);
            EventBus.getDefault().post(backLoginClearOrder);
//        }
//		username.setText("18651868360"); 
//		password.setText("868360");
    }

    public void initListener() {
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoginInfoValid()) {
                    showShortTip("登陆信息有误");
                    return;
                }
                VolleyLogin();
                startLoginAnim();
            }
        });
    }

    /**
     * 动画开始启动
     */
    public void startLoginAnim() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setClickable(false);
        login.setText("正在登录...");
        anim_username.start();
        anim_passwd.start();
    }

    /**
     * 登录失败，动画结束，停在原地
     */
    public void stopLoginAnimKeep() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setClickable(true);
        login.setText("登录");
        anim_username.stop();
        anim_passwd.stop();
    }

    /**
     * 登录失败，动画结束，回到起点
     */
    public void stopLoginToBegin() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setClickable(true);
        login.setText("登录");
        anim_username.start();
        anim_passwd.start();
        anim_username.stop();
        anim_passwd.stop();
    }

    /**
     * 登录成功
     */
    public void loginSuccessed() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setClickable(false);
        login.setText("登录成功");
        anim_username.stop();
        anim_passwd.stop();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        // 登陆成功后返回登陆页面，当前activity会保存登陆成功时的状态，需要重新设置
        username.setText("");
		password.setText("");
        username.setEnabled(true);
        password.setEnabled(true);
        login.setClickable(true);
        login.setText("登录");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void VolleyLogin() {
        dbEntity.clearOrder();
        String param = "/merchantLogin?userName="
                + username.getText().toString()
                + "&passwd="
                + PwdEncryptor.encryptByMD5(password.getText().toString(),
                username.getText().toString());
        Type type = new TypeToken<ResultMap<merchantLoginData>>() {
        }.getType();
        String param2 = "/appController/merchantLogin.do?userName=" + username.getText().toString() + "&passwd=" + password.getText().toString();
        Log.d(TAG, "login url: " + Constants.address + param2);
        ResultMapRequest<ResultMap<merchantLoginData>> resultMapRequest = new ResultMapRequest<ResultMap<merchantLoginData>>(
                Constants.address + param2, type,
                new Response.Listener<ResultMap<merchantLoginData>>() {
                    @Override
                    public void onResponse(ResultMap<merchantLoginData> response) {
                        switch (Integer.valueOf(response.getErrcode())) {
                            case 0:
                                Intent intent = new Intent();
                                merchantLoginData data = response.getData();
                                baseApp.setLoginInfo(data.getMerchantRegister());
                                if (baseApp.isLoadDishes()) {
                                    VolleyQueryAllDishes();
                                }
                                intent.setClass(LoginActivity.this,
                                        HallActivity.class);
                                startActivity(intent);
                                loginSuccessed();
                                break;
                            case 4:
                                stopLoginAnimKeep();
                                showShortTip("登陆失败,用户名或密码错误!");
                            case -1:
                                stopLoginAnimKeep();
                                showShortTip("用户名不存在!");
                                break;
                            default:
                                stopLoginAnimKeep();
                                showShortTip("登陆失败，请检查登陆信息");
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyLogTag, "VolleyError:" + error.getMessage(), error);
                showShortTip(VolleyErrorHelper.getMessage(error, mContext));
                stopLoginAnimKeep();
            }
        });
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        requestQueue.add(resultMapRequest);
    }

    /**
     * 检测登陆信息是否合法
     *
     * @return
     */
    private Boolean isLoginInfoValid() {
        String name = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        //包含空字符串会抛出服务器505异常
        if (name.contains(" ")) {
            return false;
        }
        if (pwd.contains(" ")) {
            return false;
        }
        //没有信息不允许登陆
        if (name.equals("")) {
            return false;
        }
        if (pwd.equals("")) {
            return false;
        }

        return true;
    }


    /**
     * 获取当前商家的所有菜品信息，并且本地缓存
     */
    public void VolleyQueryAllDishes() {
        String param = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId=" + baseApp.getLoginInfo().getChildMerchantId();
        Type type = new TypeToken<ResultMap<queryAllDishesByMerchantIdData>>() {
        }.getType();
        Log.i("Volley", "url:" + Constants.address + param);
        ResultMapRequest<ResultMap<queryAllDishesByMerchantIdData>> resultMapRequest = new ResultMapRequest<ResultMap<queryAllDishesByMerchantIdData>>(
                Constants.address + param,
                type,
                new Response.Listener<ResultMap<queryAllDishesByMerchantIdData>>() {
                    @Override
                    public void onResponse(
                            ResultMap<queryAllDishesByMerchantIdData> response) {
                        switch (Integer.valueOf(response.getErrcode())) {
                            case 0:
                                response.getData().getDishes();
                                BackLogin updateDishesData = new BackLogin();
                                updateDishesData.setType(2);
                                updateDishesData.setDbEntity(dbEntity);
                                updateDishesData.setDishes(response.getData().getDishes());
                                updateDishesData.setInfo(response.getData().getInfo());
                                EventBus.getDefault().post(updateDishesData);
                                baseApp.setLoadDishes(false);
                                break;
                            default:
                                showShortTip(response.getMsg() + "!");
                                dismissLoadingDialog();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                dismissLoadingDialog();
                showShortTip("自动更新菜单失败，请退出重试!");
            }
        });
        requestQueue.add(resultMapRequest);
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1,
                1.0f));
    }

    /**
     * 从服务器获取xml解析并进行比对版本号
     */
    public class CheckVersionTask implements Runnable{

        public void run() {
            try {
                //从资源文件获取服务器 地址
                String path = Constants.VersionServerUrl;
                //包装成url的对象
                URL url = new URL(path);
                HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is =conn.getInputStream();
                 UpdataInfo info =  getUpdataInfo(is);

                if(info.getVersion()<=versionname){
                    Log.i(TAG,"版本号相同无需升级");
//                    LoginMain();
                }else{
                    Log.i(TAG,"版本号不同 ,提示用户升级 ");
                    Message msg = new Message();
                    msg.obj=info;
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                // 待处理
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    UpdataInfo info=(UpdataInfo)msg.obj;
                    showUpdataDialog(info);
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
//                    LoginMain();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show();
//                    LoginMain();
                    break;
            }
        }
    };

    /**
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     * 	1.创建alertDialog的builder.
     *	2.要给builder设置属性, 对话框的内容,样式,按钮
     *	3.通过builder 创建一个对话框
     *	4.对话框show()出来
     */
    protected void showUpdataDialog(final UpdataInfo info) {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setCancelable(false);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"下载apk,更新");
                downLoadApk(info);
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /**
     * 从服务器中下载APK
     */
    protected void downLoadApk(final UpdataInfo info) {
        final ProgressDialog pd;	//进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(info.getUrl(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg=new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 进入程序的主界面
     */
    private void LoginMain(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        //结束掉当前的activity
        this.finish();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }

    /**
     * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
     */
    public static UpdataInfo getUpdataInfo(InputStream is) throws Exception{
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");//设置解析的数据源
        int type = parser.getEventType();
        UpdataInfo info = new UpdataInfo();//实体
        while(type != XmlPullParser.END_DOCUMENT ){
            switch (type) {
                case XmlPullParser.START_TAG:
                    if("version".equals(parser.getName())){
                        info.setVersion(Integer.valueOf(parser.nextText()));	//获取版本号
                    }else if ("url".equals(parser.getName())){
                        info.setUrl(parser.nextText());	//获取要升级的APK文件
                    }else if ("description".equals(parser.getName())){
                        info.setDescription(parser.nextText());	//获取该文件的信息
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }

    /**
     * 获取当前程序的版本号
     */
    private int getVersionName() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionCode;
    }

    public void checkVersion(){
        try {
            versionname=getVersionName();
            CheckVersionTask task=new CheckVersionTask();
            Thread check=new Thread(task);
            check.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}