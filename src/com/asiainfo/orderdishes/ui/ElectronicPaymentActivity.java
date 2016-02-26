package com.asiainfo.orderdishes.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.ui.base.AnimatedDoorActivity;
import com.asiainfo.orderdishes.util.CreateQRImageUtil;

public class ElectronicPaymentActivity extends AnimatedDoorActivity {
    private Button settle_back;
    private Button order_zhifu_finish;
    private TextView totalPrice;
    private TextView diskid;
    private ImageView weixin_qrcode_img;

    @Override
    protected int layoutResId() {
        return R.layout.order_settle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.order_settle);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        settle_back = (Button) findViewById(R.id.settle_back);
        order_zhifu_finish = (Button) findViewById(R.id.order_zhifu_finish);
        totalPrice = (TextView) findViewById(R.id.settle_totalprice);
        diskid = (TextView) findViewById(R.id.settle_diskid);
        weixin_qrcode_img = (ImageView) findViewById(R.id.weixin_qrcode_img);

    }

    private void initData() {
        Intent intent = this.getIntent();
        String total = intent.getStringExtra("totalprice");
        String diskName = intent.getStringExtra("diskName");
        String order_id = intent.getStringExtra("orderid");
        if (total != null && diskid != null) {
            totalPrice.setText(total);
            diskid.setText(diskName + "订单");
            StringBuffer url=new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5e2900dbd0a2dfa0&redirect_uri=");
            int price=Integer.valueOf(total)*100;
            String uri="http://www.kxlive.com/busiunion/goV3payPage?attach=0&orderId="+order_id+"&toId=gh_59058027d4b7&money="+price+"&merchantId="+baseApp.getLoginInfo().getMerchantId();
            url.append(Uri.encode(uri));
            url.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
            Log.i("bitmap_url","url:"+url.toString());
            Bitmap bitmap = CreateQRImageUtil.createQRImage(url.toString(), 500, 500);

//            Bitmap bitmap = CreateQRImageUtil.createQRImage("http://www.kxlive.com/busiunion/goAccountPage?orderId=" + order_id + "&totalPrice=" + total + "&toId=gh_59058027d4b7", 500, 500);
            weixin_qrcode_img.setImageBitmap(bitmap);

        }
    }

    private void initListener() {
        settle_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(Constants.ElectronicPayment_resultcode_back);
                backFinish();
            }
        });

        order_zhifu_finish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(Constants.ElectronicPayment_resultcode_finish);

                finish();

            }
        });

    }

}
