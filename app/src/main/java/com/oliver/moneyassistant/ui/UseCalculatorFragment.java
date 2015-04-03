package com.oliver.moneyassistant.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ab.util.AbStrUtil;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishBudgetMoneyListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishIncomeMoneyListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeMoneyListener;

/**
 * Created by Oliver on 2015/3/22.
 */
public class UseCalculatorFragment extends Fragment{

    public static String FRAGMENT_TAG = "UseCalculatorFragment";

    public static String TAG = "UseCalculatorFragment";

    private View mRootView;
    private String str = "";
    private EditText et_show;
    private int sign = 0, flag = 0;
    private double num1 = 0.0, num2 = 0.0, result = 0.0;
    private View vi;
    private Button mBtSave;
    private OnFinishBudgetMoneyListener mFinishBudgetMoneyListener;
    private OnFinishIncomeMoneyListener mFinishIncomeMoneyListener;
    private OnFinishOutcomeMoneyListener mFinishOutcomeMoneyListener;

    public double calculater() {
        switch (sign) {
            case 0:
                result = num2;
                break;
            case 1:
                result = num1 + num2;
                break;
            case 2:
                result = num1 - num2;
                break;
            case 3:
                result = num1 * num2;
                break;
            case 4:
                result = num1 / num2;
                break;

        }
        num1 = result;
        sign = 0;
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = (View)inflater.inflate(R.layout.use_calculator_view,null);
        initVariable();
        return mRootView;
    }

    private void initVariable(){
        mBtSave = (Button)mRootView.findViewById(R.id.btnSave);
        final Button number[] = new Button[10];
        final Button mark[] = new Button[11];
        mark[0] = (Button) mRootView.findViewById(R.id.btnAdd);
        mark[1] = (Button) mRootView.findViewById(R.id.btnMinus);
        mark[2] = (Button) mRootView.findViewById(R.id.btnMultiply);
        mark[3] = (Button) mRootView.findViewById(R.id.btnDivide);
        mark[4] = (Button) mRootView.findViewById(R.id.btnEqual);
        mark[5] = (Button) mRootView.findViewById(R.id.btnPoint);
        mark[6] = (Button) mRootView.findViewById(R.id.btnClears);
        mark[7] = (Button) mRootView.findViewById(R.id.btnClearAll);
        mark[8] = (Button) mRootView.findViewById(R.id.btnPlusOrMinus);
        mark[9] = (Button) mRootView.findViewById(R.id.btnRadicals);
        mark[10] = (Button) mRootView.findViewById(R.id.btnSquare);

        number[0] = (Button) mRootView.findViewById(R.id.btnZero);// 0
        number[1] = (Button) mRootView.findViewById(R.id.btnOne);// 1
        number[2] = (Button) mRootView.findViewById(R.id.btnTwo);// 2
        number[3] = (Button) mRootView.findViewById(R.id.btnThree);// 3
        number[4] = (Button) mRootView.findViewById(R.id.btnFour);// 4
        number[5] = (Button) mRootView.findViewById(R.id.btnFive);// 5
        number[6] = (Button) mRootView.findViewById(R.id.btnSix);// 6
        number[7] = (Button) mRootView.findViewById(R.id.btnSeven);// 7
        number[8] = (Button) mRootView.findViewById(R.id.btnEight);// 8
        number[9] = (Button) mRootView.findViewById(R.id.btnNine);// 9

        et_show = (EditText) mRootView.findViewById(R.id.et_show);
        et_show.setKeyListener(null);
        et_show.setText(str);

        mBtSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(AbStrUtil.isEmpty(et_show.getText().toString())){
                    ShowAlertDialog("输入金额为空");
                }else{
                    Log.i(TAG, "text:   " + et_show.getText().toString());
                    chooseListener();
                    UseCalculatorFragment.this.getFragmentManager().popBackStack();
                }
            }
        });

        number[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 0;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    char ch1[];
                    ch1 = str.toCharArray();
                    if (!(ch1.length == 1 && ch1[0] == '0')) {
                        str += 0;
                        et_show.setText(str);
                    }

                }
                vi = v;
            }
        });

        number[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 1;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 1;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 2;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 2;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 3;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 3;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 4;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 4;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 5;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 5;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[6].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 6;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 6;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[7].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 7;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 7;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[8].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 8;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 8;
                    et_show.setText(str);
                }
                vi = v;
            }
        });

        number[9].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 1) {
                    str = "";
                    str += 9;
                    et_show.setText(str);
                    flag = 0;
                } else {
                    str += 9;
                    et_show.setText(str);
                }
                vi = v;
            }
        });
        mark[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=""){
                    if(vi==mark[0]||vi==mark[1]||vi==mark[2]||vi==mark[3]){
                        sign=1;
                    }else{
                        num2=Double.parseDouble(str);
                        calculater();
                        str=""+result;
                        et_show.setText(str);
                        sign=1;
                        flag=1;
                        vi=v;
                    }}
            }
        });


        mark[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=""){
                    if(vi==mark[0]||vi==mark[1]||vi==mark[2]||vi==mark[3]){
                        sign=2;
                    }else{
                        num2=Double.parseDouble(str);
                        calculater();
                        str=""+result;
                        et_show.setText(str);
                        sign=2;
                        flag=1;
                        vi=v;
                    }}
            }
        });


        mark[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=""){
                    if(vi==mark[0]||vi==mark[1]||vi==mark[2]||vi==mark[3]){
                        sign=3;
                    }else{
                        num2=Double.parseDouble(str);
                        calculater();
                        str=""+result;
                        et_show.setText(str);
                        sign=3;
                        flag=1;
                        vi=v;
                    }}
            }
        });


        mark[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=""){
                    if(vi==mark[0]||vi==mark[1]||vi==mark[2]||vi==mark[3]){
                        sign=4;
                    }else{
                        num2=Double.parseDouble(str);
                        calculater();
                        str=""+result;
                        et_show.setText(str);
                        sign=4;
                        flag=1;
                        vi=v;
                    }}
            }
        });


        mark[4].setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(str!=""&&vi!=mark[0]&&vi!=mark[1]&&vi!=mark[2]&&vi!=mark[3]){
                    num2=Double.parseDouble(str);
                    calculater();
                    str=""+result;
                    et_show.setText(str);
                    flag=1;
                    vi=v;

                }
            }
        });

        mark[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (str != "" && vi != mark[0] && vi != mark[1] && vi != mark[2] && vi != mark[3]) {
                    num2 = Double.parseDouble(str);
                    calculater();
                    str = "" + result;
                    et_show.setText(str);
                    flag = 1;
                    vi = v;
                }
            }
        });

        mark[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (str == "") {
                    str += ".";
                    et_show.setText(str);
                } else {
                    char ch1[];
                    int x = 0;
                    ch1 = str.toCharArray();
                    for (int i = 0; i < ch1.length; i++)
                        if (ch1[i] == '.')
                            x++;
                    if (x == 0) {
                        str += ".";
                        et_show.setText(str);
                    }
                }

            }
        });

        mark[6].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=null&&str.length()>1){
                    str = str.substring(0, str.length()-1);
                }else if(str!=null&&str.length()==1){
                    str="";
                }

                //str="";
                et_show.setText(str);
                vi=v;
            }
        });

        mark[7].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                num1=0.0;num2=0;result=0.0;
                str="";
                et_show.setText(str);

            }
        });

        mark[8].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(vi!=mark[5]&&str!=""){
                    char ch=str.charAt(0);
                    if(ch=='-')
                        str=str.replace("-","");
                    else
                        str="-"+str;
                    et_show.setText(str);
                }
            }
        });

        mark[9].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=""){
                    double a=Double.parseDouble(str);
                    str=Math.sqrt(a)+"";
                    et_show.setText(str);
                }}
        });

        mark[10].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(str!=""){
                    double a=Double.parseDouble(str);
                    str=""+a*a;
                    et_show.setText(str);
                }}
        });
    }

    private void ShowAlertDialog(String message){
        new AlertDialog.Builder(this.getActivity())
                .setTitle("错误提示").setMessage(message)
                .setNegativeButton("我知道了",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void setOnFinishBudgetMoneyListener(OnFinishBudgetMoneyListener listener){
        this.mFinishBudgetMoneyListener = listener;
    }

    public void setOnFinishIncomeMoneyListener(OnFinishIncomeMoneyListener listener){
        this.mFinishIncomeMoneyListener = listener;
    }

    public void setOnFinishOutcomeMoneyListener(OnFinishOutcomeMoneyListener listener){
        this.mFinishOutcomeMoneyListener = listener;
    }

    private void chooseListener(){
        Bundle data = this.getArguments();
        String str;

        str = data.getString(ConstantsForHome.COME_FROM_FRAGMENT);
        if(AddBudgetFragment.FRAGMENT_TAG.equals(str)){
            mFinishBudgetMoneyListener
                    .setBudgetMoney(Float.valueOf(et_show.getText().toString()));
            return;
        }
        if(AddIncomeFragment.FRAGMENT_TAG.equals(str)){
            mFinishIncomeMoneyListener.
                    setIncomeMoney(Float.valueOf(et_show.getText().toString()));
            return;
        }
        if(AddItemFragment.FRAGMENT_TAG.equals(str)){
            mFinishOutcomeMoneyListener.
                    setOutcomeMoney(Float.valueOf(et_show.getText().toString()));
            return;
        }

    }

}
