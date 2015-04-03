package com.oliver.moneyassistant.ui;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbStrUtil;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.logic.runnables.CheckBudgetTime;
import com.oliver.moneyassistant.logic.runnables.SaveBudget;
import com.oliver.moneyassistant.ui.timekeeper.ScreenInfo;
import com.oliver.moneyassistant.ui.timekeeper.WheelMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.oliver.moneyassistant.ui.utils.SharePreferenceUtils;

/**
 * Created by Oliver on 2015/3/21.
 */
public class AddBudgetFragment extends Fragment implements View.OnClickListener{

    public static String FRAGMENT_TAG="AddBudgetFragment";
    private View mRootView;
    private TextView mTVBudgetMoney;
    private TextView mTVBudgetStartTime;
    private TextView mTVBudgetEndTime;
    private EditText mETBudgetDescribe;


    private RelativeLayout mRLSaveBudgetButton;

    private WheelMain mWheelMain;
    private LayoutInflater mLayoutInflater;

    private Budget mBudget;
    private static final String TAG="AddBudgetFragment";
    private float mBudgetMoney=-1f;
    private String mStartTime;
    private String mEndTime;
    private String mBudgetDescribe;

    private UseCalculatorFragment mUseCalculatorFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mRootView = inflater.inflate(R.layout.add_budget_view,null);
        initVariable();
        return mRootView;
    }

    public void initVariable(){
        mTVBudgetMoney = (TextView)mRootView.findViewById(R.id.tv_budget_money);

        mTVBudgetMoney.setOnClickListener(this);

        mTVBudgetStartTime = (TextView)mRootView.findViewById(R.id.tv_budget_start_time);
        mTVBudgetStartTime.setOnClickListener(this);

        mTVBudgetEndTime = (TextView)mRootView.findViewById(R.id.tv_budget_end_time);
        mTVBudgetEndTime.setOnClickListener(this);

        mETBudgetDescribe = (EditText)mRootView.findViewById(R.id.et_budget_describe);

        mRLSaveBudgetButton = (RelativeLayout)mRootView.findViewById(R.id.rl_save_budget_button);
        mRLSaveBudgetButton.setOnClickListener(this);
    }
    private Handler mAddBudgetHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForHome.BUDGET_DUPLICATED_HANDLER_FLAG:
                    long duplicatedId = msg.getData().getLong(ConstantsForHome.BUDGET_DUPLICATED_ID);
                    if(duplicatedId!=-1L){
                        showDialogDuplicated(duplicatedId);
                    }else{
                        continueToSave(duplicatedId);
                    }
                    break;
                case ConstantsForHome.BUDGET_SAVE_HANDLER_FLAG:
                    boolean isSaved = msg.getData().getBoolean(ConstantsForHome.BUDGET_SAVE_BOOLEAN);
                    if(isSaved){
                        showSaveSucceeded();
                    }else{
                        showSaveFailed();
                    }

            }
        }
    };

    public void showDialogDuplicated(final long duplicatedId){
        new AlertDialog.Builder(AddBudgetFragment.this.getActivity()).setTitle("信息提示")
                .setMessage("与其他预算存在时间重复，是否覆盖？")
                   .setPositiveButton("是", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           continueToSave(duplicatedId);
                       }
                   }).setNegativeButton("否",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void continueToSave(long duplicatedId){
        new Thread(new SaveBudget(this.getActivity(), mAddBudgetHandler, mBudget,duplicatedId)).start();
        SharePreferenceUtils.setUpdateMark(this.getActivity());
    }

    private void checkDuplicated(){
        Log.i(TAG,"checkDuplicated...");
        new Thread(new CheckBudgetTime(this.getActivity(),mAddBudgetHandler,mBudget)).start();
    }
    private void showSaveSucceeded(){
        Toast.makeText(AddBudgetFragment.this.getActivity(),"保存成功",Toast.LENGTH_LONG).show();
        getFragmentManager().popBackStack();
        this.getActivity().finish();
    }
    private void showSaveFailed(){
        Toast.makeText(AddBudgetFragment.this.getActivity(),"保存失败",Toast.LENGTH_LONG).show();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_save_budget_button:
                    mBudget = getBudget();
                    Log.i(TAG, mBudget.toString());
                    if(mBudget!=null){
                        checkDuplicated();
                    }
                break;
            case R.id.tv_budget_start_time:
                   getSelectTime(true);
                break;
            case R.id.tv_budget_end_time:
                   getSelectTime(false);
                break;
            case R.id.tv_budget_money:
                   getBudgetMoney();
                break;
        }

    }

    private void getBudgetMoney(){

        Log.i(TAG,"get Budget Money");
        if(this.mUseCalculatorFragment ==null){
            mUseCalculatorFragment = new UseCalculatorFragment();
            mUseCalculatorFragment.setOnFinishBudgetMoneyListener((HomeSubActivity)getActivity());
            Bundle data =new Bundle();
            data.putString(ConstantsForHome.COME_FROM_FRAGMENT,AddBudgetFragment.FRAGMENT_TAG);
            mUseCalculatorFragment.setArguments(data);
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.sub_fragment_container,
                mUseCalculatorFragment,UseCalculatorFragment.FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private Budget getBudget(){
        if(checkLegal()){
            Budget budget = new Budget();
            budget.setMoney(Float.valueOf(mTVBudgetMoney.getText().toString()));
            budget.setStartTime(TimeUtils.getLongWithTimeString(mTVBudgetStartTime.getText().toString()));
            budget.setEndTime(TimeUtils.getLongWithTimeString(mTVBudgetEndTime.getText().toString()));
            budget.setBudgetTime(TimeUtils.getNow());
            String str = mETBudgetDescribe.getText().toString();
            if(!AbStrUtil.isEmpty(str)){
                budget.setBudgetDescribe(str);
            }
            return budget;
        }
        return null;
    }



    private boolean checkLegal(){
        String str = mTVBudgetMoney.getText().toString();
        if(AbStrUtil.isEmpty(str)){
            showDialog("未输入预算金额");
            return false;
        }
        str = mTVBudgetStartTime.getText().toString();
        if(AbStrUtil.isEmpty(str)){
            showDialog("未输入开始时间");
            return false;
        }
        str = mTVBudgetEndTime.getText().toString();
        if(AbStrUtil.isEmpty(str)){
            showDialog("未输入结束时间");
            return false;
        }
        return true;
    }

    private void showDialog(String str){
        new AlertDialog.Builder(AddBudgetFragment.this.getActivity()).setTitle("错误提示")
                .setMessage(str).setNegativeButton("我知道了",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        }).show();
    }

    private void getSelectTime(final boolean isStart){
        Calendar calendar = Calendar.getInstance();
        View timePickerView =mLayoutInflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo= new ScreenInfo(AddBudgetFragment.this.getActivity());
        mWheelMain = new WheelMain(timePickerView,true);
        mWheelMain.screenheight = screenInfo.getHeight();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute =calendar.get(Calendar.MINUTE);
        mWheelMain.initDateTimePicker(year, month, day,hour,minute);
        new AlertDialog.Builder(AddBudgetFragment.this.getActivity()).setTitle("选择时间")
                .setView(timePickerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String timeStr = mWheelMain.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            format.parse(timeStr).getTime();
                        } catch (ParseException e) {
							/*warning:未处理的exception*/
                            e.printStackTrace();
                        }
                        if(isStart){
                            mTVBudgetStartTime.setText(timeStr);
                        }else{
                            mTVBudgetEndTime.setText(timeStr);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mBudgetMoney!=-1){
            mTVBudgetMoney.setText(MoneyUtils.displayMoney(mBudgetMoney));
        }

        if(mStartTime!=null){
            mTVBudgetStartTime.setText(mStartTime);
        }

        if(mEndTime!=null){
            mTVBudgetEndTime.setText(mEndTime);
        }

        if(mBudgetDescribe!=null){
            mETBudgetDescribe.setText(mBudgetDescribe);
        }

    }

    @Override
    public void onDestroyView() {
        saveSomeData();
        super.onDestroyView();
    }

    private void saveSomeData(){
        String str="";
        str = mTVBudgetStartTime.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mStartTime = str;
        }
        str = mTVBudgetEndTime.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mEndTime = str;
        }
        str = mETBudgetDescribe.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mBudgetDescribe = str;
        }
    }


    public void setBudgetMoney(float money){
        mBudgetMoney = money;
    }

}
