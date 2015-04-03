package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.db.models.IncomeType;
import com.oliver.moneyassistant.db.utils.TypeConvertor;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishIncomeTypeListener;
import com.oliver.moneyassistant.ui.utils.IncomeTypeGridViewAdapter;
import com.oliver.moneyassistant.ui.utils.OutcomeTypeGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/21.
 */
public class IncomeTypeFragment extends Fragment
        implements View.OnClickListener,
        AdapterView.OnItemClickListener{

    private static final String TAG ="IncomeTypeFragment";
    private GridView mGVTypeList;
    private RelativeLayout mRLSureButton;
    private View mRootView;
    private IncomeTypeGridViewAdapter mAdapter;
    private List<IncomeType> mIncomeTypeList;
    private IncomeType mSelectedIncomeType;
    private OnFinishIncomeTypeListener mIncomeTypeListener;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.income_type_fragment,null);
        initVariable();
        return mRootView;
    }

    private void initVariable(){
        mGVTypeList = (GridView)mRootView.findViewById(R.id.gv_income_type);
        mGVTypeList.setOnItemClickListener(this);
        mRLSureButton = (RelativeLayout)mRootView.findViewById(R.id.rl_ensure_income_type_button);
        mRLSureButton.setOnClickListener(this);
        initIncomeTypeList();
        mAdapter = new IncomeTypeGridViewAdapter(this.getActivity(),mIncomeTypeList);
        mGVTypeList.setAdapter(mAdapter);
    }

    private void initIncomeTypeList(){
        mIncomeTypeList = new ArrayList<>();
        String arr[] = getResources().getStringArray(R.array.income_type);
        for(int i=0;i<arr.length;i++){
            IncomeType type = new IncomeType();
            type.setTypeId(i);
            type.setTypeText(TypeConvertor.getIncomeTypeString(i));
            type.setResId(R.drawable.ic_launcher);
            mIncomeTypeList.add(type);
        }
    }

    @Override
    public void onClick(View v) {
           this.mIncomeTypeListener.addIncomeTypeInfo(mSelectedIncomeType);
           getFragmentManager().popBackStack();
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {
        mSelectedIncomeType = mIncomeTypeList.get(position);
        Log.i(TAG, mSelectedIncomeType.toString());
    }

    public void setOnFinishIncomeTypeListener(OnFinishIncomeTypeListener listener){
        this.mIncomeTypeListener = listener;
    }
}
