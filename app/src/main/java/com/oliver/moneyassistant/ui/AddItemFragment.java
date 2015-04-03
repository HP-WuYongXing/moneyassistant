package com.oliver.moneyassistant.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import android.util.Log;

import android.net.Uri;

import com.ab.fragment.AbSampleDialogFragment;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.constants.SharedVariable;
import com.oliver.moneyassistant.db.models.ItemAddress;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.models.OutcomeType;
import com.oliver.moneyassistant.db.models.Picture;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.RandomOIdGenerater;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeAddressListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeTypeListener;
import com.oliver.moneyassistant.logic.runnables.SaveOutcomeItem;
import com.oliver.moneyassistant.ui.timekeeper.*;
import android.widget.ImageView;

import com.oliver.moneyassistant.ui.utils.DialogUtis;
import com.oliver.moneyassistant.ui.utils.OutcomePicAdapter;
import com.oliver.moneyassistant.ui.utils.SharePreferenceUtils;

import java.io.File;

/**
 * Created by Oliver on 2015/3/17.
 */
public class AddItemFragment extends Fragment implements
        View.OnClickListener,AdapterView.OnItemClickListener{

    public static final String FRAGMENT_TAG = "AddItemFragment";
    public static final String TAG = "AddItemFragment";
    private View mRootView;
    private TextView mTVOutcomeMoney;
    private TextView mTVOutcomeType;
    private TextView mTVOutcomeTime;
    private TextView mTVOutcomeAddr;
    private EditText mETOutcomeDescribe;
    private ImageView mIVAddImage;
    private RelativeLayout mRLSaveOutcome;
    private OutcomeType mOutcomeType;
    private ItemAddress mOutcomeAddress;
    private String mOutcomeTime;
    private String mOutcomeDescribe;
    private LayoutInflater mLayoutInflater;
    private WheelMain mWheelMain;
    private List<String> mImagePaths;
    private GridView mImageListView;
    private File mPhotoDir;
    private TextView mTVAddOutcomePic;
    private OutcomePicAdapter mAdapter;

    private UseCalculatorFragment mUseCalculatorFragment;
    private float mOutcomeMoney = -1;

    private boolean mOutcomeMoneyModified=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate... ...");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG,"onAttach.......");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.add_outcome_item_view,null);
        mLayoutInflater = inflater;
        initVariable();

        return mRootView;
    }

    private void initVariable(){
        String path = AbFileUtil.getFileDownloadDir(this.getActivity());
        mPhotoDir = new File(path);

        mTVOutcomeMoney =(TextView)mRootView.findViewById(R.id.tv_input_outcome_money);
        mTVOutcomeMoney.setOnClickListener(this);
        mTVOutcomeType = (TextView)mRootView.findViewById(R.id.tv_input_outcome_type);
        mTVOutcomeType.setOnClickListener(this);
        mTVOutcomeTime =(TextView)mRootView.findViewById(R.id.tv_outcome_time);
        mTVOutcomeTime.setOnClickListener(this);
        mTVOutcomeAddr =(TextView)mRootView.findViewById(R.id.tv_outcome_addr);
        mTVOutcomeAddr.setOnClickListener(this);


        mETOutcomeDescribe = (EditText)mRootView.findViewById(R.id.et_outcome_describe);


        mImageListView = (GridView)mRootView.findViewById(R.id.gv_outcome_picture_list);
        mTVAddOutcomePic= (TextView)mRootView.findViewById(R.id.tv_add_outcome_pic);
        this.mRLSaveOutcome = (RelativeLayout)mRootView.findViewById(R.id.rl_save_outcome_item);
        this.mRLSaveOutcome.setOnClickListener(this);



        this.mTVAddOutcomePic.setOnClickListener(this);

        mImageListView.setOnItemClickListener(this);

        Log.i(TAG,"onCreateView.......");


    }

    private Handler mSaveOutcomeItemHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForHome.OUTCOME_SAVE_HANDLER_FLAG:
                    Bundle data = msg.getData();
                    boolean success = data.getBoolean(ConstantsForHome.OUTCOME_SAVE_BOOLEAN);
                    if(success){
                        showSuccess();
                    }else{
                        showFaild();
                    }
                    break;
            }
        }
    };

    private void showSuccess(){
        Toast.makeText(this.getActivity(),"保存成功",Toast.LENGTH_LONG).show();
        SharePreferenceUtils.setUpdateMark(this.getActivity());
        getFragmentManager().popBackStack();
        getActivity().finish();
    }

    private void showFaild(){
        Toast.makeText(this.getActivity(),"保存失败",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState......");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated... ...");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart... ...");
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState){
        super.onInflate(activity, attrs, savedInstanceState);
        Log.i(TAG,"onInflate... ...");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewCreated... ...");
    }

    @Override
    public void onDestroyView() {
        saveSomeData();
        super.onDestroyView();
        Log.i(TAG,"onDestroyView...");
    }
    private void saveSomeData(){
        String str;
        str = mTVOutcomeTime.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mOutcomeTime = str;
        }
        str = mETOutcomeDescribe.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mOutcomeDescribe = str;
        }
    }

    /*private void saveArguments(){
        Bundle data = new Bundle();
        String outcomeMoney = mTVOutcomeMoney.getText().toString();
        Log.i(TAG,"outcomeMoney:  "+outcomeMoney);
        if(!AbStrUtil.isEmpty(outcomeMoney)){
            data.putString(BUNDLE_MONEY_FLAG,outcomeMoney);
        }
        if(mOutcomeType!=null){
            data.putParcelable(BUNDLE_TYPE_FLAG,mOutcomeType);
        }
        String outcomeTime = mTVOutcomeTime.getText().toString();
        if(!AbStrUtil.isEmpty(outcomeTime)){
            data.putString(BUNDLE_TIME_FLAG,outcomeTime);
        }
        if(mOutcomeAddress!=null){
            data.putParcelable(BUNDLE_ADDR_FLAG,(Parcelable)mOutcomeAddress);
        }
        String outcomeDescribe = mETOutcomeDescribe.getText().toString();
        if(!AbStrUtil.isEmpty(outcomeDescribe)){
            data.putString(BUNDLE_DESCRIBE_FLAG,outcomeDescribe);
        }
        //this.setArguments(data);
    }*/

    /*供subActivity 调用的方法*/
    public void setPathList(List<String>list){
        this.mImagePaths =list;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume... ...");
       /*设置type的值*/
        if(this.mOutcomeType!=null){
            mTVOutcomeType.setText(mOutcomeType.getTypeText());
            mOutcomeMoneyModified = true;
        }

        /*设置address 的值*/
        if(mOutcomeAddress!=null){
            String address = mOutcomeAddress.getAddressHeader();
            mTVOutcomeAddr.setText(address);
        }

        /*设置pathlist*/
        if(mImagePaths!=null) {//设置adapter值
            mAdapter = new OutcomePicAdapter(this.getActivity(), mImagePaths,
                    ConstantsForHome.OUTCOME_PICTURE_WIDTH, ConstantsForHome.OUTCOME_PICTURE_HEIGHT);
            mImageListView.setAdapter(mAdapter);
        }

        /*设置money的值*/
        if(mOutcomeMoney!=-1) {
            mTVOutcomeMoney.setText(MoneyUtils.displayMoney(mOutcomeMoney));
        }

        /*设置outcometime的值*/
        if(mOutcomeTime!=null){
            mTVOutcomeTime.setText(mOutcomeTime);
        }

        /*设置outcomeDescribe的值*/
        if(mOutcomeDescribe!=null){
            mETOutcomeDescribe.setText(mOutcomeDescribe);
        }





    }

   /* private void restoreState(Bundle data){
        if(data!=null){
            String outcomeMoney = data.getString(BUNDLE_MONEY_FLAG);
            if(!AbStrUtil.isEmpty(outcomeMoney)){
                mTVOutcomeMoney.setText(outcomeMoney);
            }
            mOutcomeType = data.getParcelable(BUNDLE_TYPE_FLAG);
            if(mOutcomeType!=null){
                mTVOutcomeType.setText(mOutcomeType.getTypeText());
            }

            String timeStr = data.getString(BUNDLE_TIME_FLAG);
            if(!AbStrUtil.isEmpty(timeStr)){
                mTVOutcomeTime.setText(timeStr);
            }
            mOutcomeAddress = data.getParcelable(BUNDLE_ADDR_FLAG);
            if(mOutcomeAddress!=null){
                mTVOutcomeAddr.setText(mOutcomeAddress.getAddressHeader());
            }
            String describe = data.getString(BUNDLE_DESCRIBE_FLAG);
            if(!AbStrUtil.isEmpty(describe)){
                mETOutcomeDescribe.setText(describe);
            }
        }
    }*/


    public void setOutcomeType(OutcomeType type){
        this.mOutcomeType = type;
    }

    public void setOutcomeAddress(ItemAddress address){
        this.mOutcomeAddress = address;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add_outcome_pic:selectImageSource(v);break;
            case R.id.rl_save_outcome_item:saveOutcomeItem(v);break;
            case R.id.tv_input_outcome_money:selectOutcomeMoney();break;
            case R.id.tv_input_outcome_type:selectOutcomeType(v);break;
            case R.id.tv_outcome_time:selectOutcomeTime(v);break;
            case R.id.tv_outcome_addr:selectOutcomeAddress(v);break;
        }
    }

    public void selectOutcomeMoney(){
        if(mUseCalculatorFragment==null){
            mUseCalculatorFragment = new UseCalculatorFragment();
            Bundle data =new Bundle();
            data.putString(ConstantsForHome.COME_FROM_FRAGMENT,AddItemFragment.FRAGMENT_TAG);
            mUseCalculatorFragment.setArguments(data);
            mUseCalculatorFragment.setOnFinishOutcomeMoneyListener((HomeSubActivity)getActivity());
        }
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction =  manager.beginTransaction();
        transaction.replace(R.id.sub_fragment_container,mUseCalculatorFragment,
                UseCalculatorFragment.FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /*grid view delegate*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:;break;
        }
    }

    private void saveOutcomeItem(View v){
        String str ="";
        if(checkLegal()){
           String oId = RandomOIdGenerater.getRandomId();
           OutcomeItem item = new OutcomeItem();
           str = mTVOutcomeMoney.getText().toString();
           item.setOutcomeMoney(Float.valueOf(str));
           item.setTypeId(this.mOutcomeType.getTypeId());
           item.setOutcomeTime(TimeUtils.getLongWithTimeString(mTVOutcomeTime.getText().toString()));
           item.setoId(oId);
            if(this.mOutcomeAddress!=null) {
               this.mOutcomeAddress.setoId(oId);
               item.setAddress(this.mOutcomeAddress);
           }
           str = mETOutcomeDescribe.getText().toString();
            if(!AbStrUtil.isEmpty(str)){
                item.setOutcomeDescribe(str);
            }

            if(this.mImagePaths.size()!=0){
                List<Picture>list =new ArrayList<>();
                for(int i=0;i<this.mImagePaths.size();i++){
                    Picture p =new Picture();
                    p.setoId(oId);
                    p.setImgPath(mImagePaths.get(i));
                    list.add(p);
                }
                item.setPictures(list);
            }
           new Thread(new SaveOutcomeItem(
                   this.getActivity(),mSaveOutcomeItemHandler,item))
                   .start();
        }

     }

    private boolean checkLegal(){
        if(!mOutcomeMoneyModified){
            showAlertDialog("还没有输入支出金额");
            return false;
        }
        return true;
    }

    private void showAlertDialog(String message){
            DialogUtis.showDialog(this.getActivity(),message);
    }

    private void selectImageSource(View v){

        View view = mLayoutInflater.inflate(R.layout.dialog_choose_album_view,null);
        RelativeLayout useAlbumBtn = (RelativeLayout)view.findViewById(R.id.rl_use_album);
        RelativeLayout useCameraBtn = (RelativeLayout)view.findViewById(R.id.rl_use_camera);
        final AbSampleDialogFragment dialog =  AbDialogUtil.showDialog(view, Gravity.BOTTOM);

        useAlbumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                AddItemFragment.this
                        .getActivity()
                        .startActivityForResult(intent, ConstantsForHome.REQUEST_USE_ALBUM);
            }
        });

        useCameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
                takePhoto();
            }
        });
    }

    private void takePhoto(){
        String photoName =  System.currentTimeMillis()+".jpg";
        File picFile = new File(mPhotoDir,photoName);
        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(picFile));
      //  intent.putExtra("photoPath",picFile.getPath());
        SharedVariable.photoPath = picFile.getPath();
        this.getActivity().startActivityForResult(intent, ConstantsForHome.REQUEST_USE_CAMERA);
    }


    private void selectOutcomeType(View v){
        OutcomeTypeFragment fragment = OutcomeTypeFragment.newInstance();
        fragment.setOnFinishOutcomeTypeListener((OnFinishOutcomeTypeListener)getActivity());
        FragmentManager manager = AddItemFragment.this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.sub_fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void selectOutcomeTime(View v){
        Calendar calendar = Calendar.getInstance();
        View timePickerView =mLayoutInflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo= new ScreenInfo(AddItemFragment.this.getActivity());
        mWheelMain = new WheelMain(timePickerView,true);
        mWheelMain.screenheight = screenInfo.getHeight();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute= calendar.get(Calendar.MINUTE);
        mWheelMain.initDateTimePicker(year, month, day, hour, minute);
        new AlertDialog.Builder(AddItemFragment.this.getActivity()).setTitle("选择时间")
                .setView(timePickerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String timeStr = mWheelMain.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            format.parse(timeStr).getTime();
                        } catch (ParseException e) {
							/*warning:未处理的exception*/
                            e.printStackTrace();
                        }
                        mOutcomeTime=timeStr;
                        mTVOutcomeTime.setText(mOutcomeTime);
                        //  mTvOutcomeYear.setText(timeStr.substring(0, 4)+"年");
                        //    String monthToMinuteStr =timeStr.substring(5);
                        //     monthToMinuteStr=monthToMinuteStr.replace("-", "月").replace(" ", "日 ");
                        //   mTvOutcomeMinute.setText(monthToMinuteStr);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    private void selectOutcomeAddress(View v){
        PopupMenu popupMenu = new PopupMenu(AddItemFragment.this.getActivity(),v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.address,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager manager = AddItemFragment.this.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.action_use_location:
                        UseLocationFragment fragment = new UseLocationFragment();
                        transaction.replace(R.id.sub_fragment_container,fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    case R.id.action_use_input:
                        UseManualAddressFragment fragment2 = new UseManualAddressFragment();
                        fragment2
                                .setOnFinishOutcomeAddressListener((OnFinishOutcomeAddressListener)getActivity());
                        transaction.replace(R.id.sub_fragment_container, fragment2);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void setOutcomeMoney(float money){
        mOutcomeMoney = money;
    }


}
