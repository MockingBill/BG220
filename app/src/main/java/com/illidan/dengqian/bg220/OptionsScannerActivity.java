package com.illidan.dengqian.bg220;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.illidan.dengqian.bg220.picture.PickPictureTotalActivity;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.decode.QRDecode;

public class OptionsScannerActivity extends Activity implements OnScannerCompletionListener {




    private ScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scanner_options);

        mScannerView = (ScannerView) findViewById(R.id.scanner_view);

        mScannerView.setOnScannerCompletionListener(this);



        ScannerOptions.Builder builder = new ScannerOptions.Builder();
        builder
                .setFrameStrokeColor(Color.RED)
                .setFrameStrokeWidth(1.5f)

                .setViewfinderCallback(new ScannerOptions.ViewfinderCallback() {
                    @Override
                    public void onDraw(View view, Canvas canvas, Rect frame) {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.search);
                        canvas.drawBitmap(bmp, frame.right / 2, frame.top - bmp.getHeight(), null);
                    }
                })

                .setScanMode(BarcodeFormat.QR_CODE)
                .setTipText("扫码获取拨测要求")
                .setTipTextSize(22)
                .setTipTextColor(getResources().getColor(R.color.arc_blue))
//                .setCameraZoomRatio(2)
        ;
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    PickPictureTotalActivity.gotoActivity(OptionsScannerActivity.this);

            }
        });
        findViewById(R.id.toggleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mScannerView.toggleLight(!isFlaOpen);
            }
        });




        mScannerView.setScannerOptions(builder.build());
    }
    public static boolean isFlaOpen=false;


    @Override
    protected void onResume() {
        mScannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

    @Override
    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        vibrate();
        mScannerView.restartPreviewAfterDelay(0);
        Intent intent=new Intent();
        if(rawResult!=null){
            intent.putExtra("ResultQRCode",rawResult.getText().toString());
        }else{
            intent.putExtra("ResultQRCode","0");
        }
        mScannerView.toggleLight(false);
        setResult(MainActivity.RESULT_CODE_SCAN,intent);
        finish();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PickPictureTotalActivity.REQUEST_CODE_SELECT_PICTURE&&resultCode==PickPictureTotalActivity.REQUEST_CODE_SELECT_PICTURE){
            String picturePath=data.getStringExtra(PickPictureTotalActivity.EXTRA_PICTURE_PATH);
            QRDecode.decodeQR(picturePath, this);
        }


    }
}