package com.oliver.moneyassistant.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.view.cropimage.CropImage;
import com.oliver.moneyassistant.R;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ab.view.cropimage.CropImageView;
import java.io.File;
import android.content.Intent;
import android.os.Handler;
/**
 * Created by Oliver on 2015/3/19.
 */
public class CropImageActivity extends Activity{

    private CropImageView mIVCropImage;
    private RelativeLayout mRLTurnLeft;
    private RelativeLayout mRLTurnRight;
    private RelativeLayout mRLCropSure;
    private RelativeLayout mRLCropCancel;
    private String mFilePath;
    private CropImage mCropImg;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.corp_image_activity_view);
        initVariable();
    }
    public void initVariable(){
        mIVCropImage = (CropImageView)this.findViewById(R.id.iv_corp_image);
        mRLTurnLeft = (RelativeLayout)this.findViewById(R.id.rl_turn_left);
        mRLTurnLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCropImg.startRotate(270.0f);
            }
        });
        mRLTurnRight = (RelativeLayout)this.findViewById(R.id.rl_turn_right);
        mRLTurnRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCropImg.startRotate(90.0f);
            }
        });
        mRLCropSure = (RelativeLayout)this.findViewById(R.id.rl_corp_sure);
        mRLCropSure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String path = mCropImg.saveToLocal(mCropImg.cropAndSave());
                Intent intent = new Intent();
                intent.putExtra("path",path);
                setResult(Activity.RESULT_OK,intent);
                CropImageActivity.this.finish();
            }
        });
        mRLCropCancel = (RelativeLayout)this.findViewById(R.id.rl_corp_cancel);
        mRLCropCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImageActivity.this.finish();
            }
        });

        mFilePath = this.getIntent().getStringExtra("path");

        mHandler = new Handler(){
            @Override
          public void handleMessage(Message msg){
                    switch (msg.what){
                        case 0:break;
                    }
            }
        };

        File file = new File(mFilePath);
        Bitmap bitmap = AbFileUtil.getBitmapFromSD(file, AbImageUtil.SCALEIMG,500,500);
        if(bitmap==null){
            Toast.makeText(this,"没有找到图片",Toast.LENGTH_LONG).show();
            this.finish();
        }else{
            mIVCropImage.clear();
            mIVCropImage.setImageBitmap(bitmap);
            mIVCropImage.setImageBitmapResetBase(bitmap,true);
            mCropImg = new CropImage(this,mIVCropImage,mHandler);
            mCropImg.crop(bitmap);
        }
    }
}
