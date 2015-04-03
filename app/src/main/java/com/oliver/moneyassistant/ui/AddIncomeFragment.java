package com.oliver.moneyassistant.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.db.models.IncomeType;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.logic.runnables.SaveIncomeItem;
import com.oliver.moneyassistant.ui.timekeeper.ScreenInfo;
import com.oliver.moneyassistant.ui.timekeeper.WheelMain;
import com.oliver.moneyassistant.ui.utils.SharePreferenceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Oliver on 2015/3/20.
 */
public class AddIncomeFragment extends Fragment implements View.OnClickListener{

    public static final String FRAGMENT_TAG="AddIncomeFragment";
    public static final String TAG = "AddIncomeFragment";
    private View mRootView;
    private TextView mTVIncomeMoney;
    private TextView mTVIncomeType;
    private TextView mTVIncomeTime;
    private EditText mETIncomeDescribe;
    private RelativeLayout mRLSaveIncome;

    private IncomeTypeFragment mTypeFragment;
    private IncomeType mIncometype;
    private String mIncomeTime;
    private String mIncomeDescribe;
    private float mIncomeMoney=-1f;
    private LayoutInflater mLayoutInflater;
    private WheelMain mWheelMain;
    private UseCalculatorFragment mUseCalculatorFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.add_income_item_view,null);
        mLayoutInflater =inflater;
        initVariable();
        return mRootView;
    }

    private void initVariable(){

       mTVIncomeMoney = (TextView)mRootView.findViewById(R.id.tv_input_income_money);
       mTVIncomeMoney.setOnClickListener(this);

       mTVIncomeType =(TextView)mRootView.findViewById(R.id.tv_input_income_type);
       mTVIncomeType.setOnClickListener(this);

       mTVIncomeTime =(TextView)mRootView.findViewById(R.id.tv_income_time);
       mTVIncomeTime.setOnClickListener(this);

       mETIncomeDescribe = (EditText)mRootView.findViewById(R.id.et_income_describe);

       mRLSaveIncome = (RelativeLayout)mRootView.findViewById(R.id.rl_save_income_item);
       mRLSaveIncome.setOnClickListener(this);

    }

    private Handler mSaveIncomeHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForHome.INCOME_SAVE_HANDLER_FLAG:
                     boolean saved = msg.getData().getBoolean(ConstantsForHome.INCOME_SAVE_BOOLEAN);
                    if(saved){
                        Toast.makeText(AddIncomeFragment.this.getActivity(),
                                "保存成功",Toast.LENGTH_LONG).show();
                        SharePreferenceUtils.setUpdateMark(AddIncomeFragment.this.getActivity());
                    }else{
                        Toast.makeText(AddIncomeFragment.this.getActivity(),
                                "保存失败",Toast.LENGTH_LONG).show();
                    }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_save_income_item:
                saveIncomeItem();
                break;
            case R.id.tv_input_income_money:
                selectIncomeMoney();
                break;
            case R.id.tv_input_income_type:
                selectIncomeType();
                break;
            case R.id.tv_income_time:
                selectIncomeTime(v);
                break;
        }
    }

    private void saveIncomeItem(){
        if(checkLegal()){
            IncomeItem item = new IncomeItem();
            item.setIncomeMoney(Float.valueOf(mTVIncomeMoney.getText().toString()));
            item.setTypeId(mIncometype.getTypeId());
            Log.i(TAG,"time string:－－－－－－－＞"+mTVIncomeTime.getText().toString());
            long  time = TimeUtils.getLongWithTimeString(mTVIncomeTime.getText().toString());
            Log.i(TAG,"time:－－－－－－－＞"+time);
            item.setIncomeTime(time);
            String str = mETIncomeDescribe.getText().toString();
            if(!AbStrUtil.isEmpty(str)){
                item.setIncomeDescribe(str);
            }
            new Thread(new SaveIncomeItem(
                    this.getActivity(),
                    mSaveIncomeHandler,item)).start();
        }
    }

    private boolean checkLegal(){
        String str=mTVIncomeMoney.getText().toString();
        if(AbStrUtil.isEmpty(str)){
            showAlertDialog("收入金钱不能为空");
            return false;
        }
        str = mTVIncomeType.getText().toString();
        if(AbStrUtil.isEmpty(str)){
            showAlertDialog("收入类型不能为空");
            return false;
        }
        str = mTVIncomeTime.getText().toString();
        if(AbStrUtil.isEmpty(str)){
            showAlertDialog("收入时间不能为空");
            return false;
        }
        return true;
    }

    private void showAlertDialog(String str){
        new AlertDialog.Builder(this.getActivity())
                .setTitle("错误提示")
                .setMessage(str)
                .setNegativeButton("我知道了",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void selectIncomeMoney(){
        if(this.mUseCalculatorFragment ==null){
            mUseCalculatorFragment = new UseCalculatorFragment();
            mUseCalculatorFragment.setOnFinishIncomeMoneyListener((HomeSubActivity) getActivity());
            Bundle data =new Bundle();
            data.putString(ConstantsForHome.COME_FROM_FRAGMENT,AddIncomeFragment.FRAGMENT_TAG);
            mUseCalculatorFragment.setArguments(data);
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.sub_fragment_container,
                mUseCalculatorFragment,UseCalculatorFragment.FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void selectIncomeType(){
        if(mTypeFragment==null) {
            mTypeFragment = new IncomeTypeFragment();
            mTypeFragment.setOnFinishIncomeTypeListener(
                    (HomeSubActivity)(this.getActivity())
            );
        }
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.sub_fragment_container,mTypeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void selectIncomeTime(View v){
        Calendar calendar = Calendar.getInstance();
        View timePickerView =mLayoutInflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo= new ScreenInfo(AddIncomeFragment.this.getActivity());
        mWheelMain = new WheelMain(timePickerView,true);
        mWheelMain.screenheight = screenInfo.getHeight();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute= calendar.get(Calendar.MINUTE);
        mWheelMain.initDateTimePicker(year, month, day, hour, minute);
        new AlertDialog.Builder(AddIncomeFragment.this.getActivity()).setTitle("选择时间")
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
                        mTVIncomeTime.setText(timeStr);
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

    public void setIncomeType(IncomeType type){
        mIncometype = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mIncometype!=null){
            mTVIncomeType.setText(mIncometype.getTypeText());
        }

        if(mIncomeMoney!=-1){
            mTVIncomeMoney.setText(MoneyUtils.displayMoney(mIncomeMoney));
        }

        if(mIncomeDescribe!=null){
            mETIncomeDescribe.setText(mIncomeDescribe);
        }
        if(mIncomeTime!=null){
            mTVIncomeTime.setText(mIncomeTime);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setIncomeMoney(float money){
        Log.i(TAG, "setIncomeMoney.....");
        this.mIncomeMoney = money;
    }

    @Override
    public void onDestroyView() {
        saveSomeData();
        super.onDestroyView();
    }

    private void saveSomeData(){
       String str;
        str = mTVIncomeTime.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mIncomeTime= str;
        }
        str = mETIncomeDescribe.getText().toString();
        if(!AbStrUtil.isEmpty(str)){
            mIncomeDescribe = str;
        }
    }
}
