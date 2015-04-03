package com.oliver.moneyassistant.ui;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.*;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.OutcomeType;
import com.oliver.moneyassistant.db.utils.TypeConvertor;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeTypeListener;
import com.oliver.moneyassistant.ui.utils.OutcomeTypeGridViewAdapter;

import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link OutcomeTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutcomeTypeFragment extends Fragment {
    private static final String TAG = "OutcomeTypeFragment";
    public static final String FRAGMENT_TAG= "OutcomeTypeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View mRootView;
    private GridView mGridView;
    private RelativeLayout mRLOutcomeTypeEnsure;
    private OnFinishOutcomeTypeListener mOutcomeTypeListener;
    private OutcomeType mSelectedType;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OutcomeTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OutcomeTypeFragment newInstance() {
        OutcomeTypeFragment fragment = new OutcomeTypeFragment();
        return fragment;
    }

    public OutcomeTypeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.outcome_type_view, null);
        initVariable();
        initGridView();
        return mRootView;
    }

    private void initVariable(){
        mGridView = (GridView)mRootView.findViewById(R.id.gv_outcome_type);
        mRLOutcomeTypeEnsure = (RelativeLayout)mRootView.findViewById(R.id.rl_ensure_button);
        mRLOutcomeTypeEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedType!=null) {
                    OutcomeTypeFragment.this.getActivity().getSupportFragmentManager()
                            .popBackStack();
                    mOutcomeTypeListener.addOutcomeTypeInfo(mSelectedType);
                }else{
                    Toast.makeText(getActivity(),"未选择类型",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initGridView(){
        List<OutcomeType> list = new ArrayList<>();
        Resources res = getResources();
        String[] array = res.getStringArray(R.array.outcome_type);
        int len = ConstantsForHome.MAX_SOLID_OUTCOME_ITEM_NUM;
        for(int i=0;i<len;i++){
            OutcomeType outcomeType = new OutcomeType();
            outcomeType.setResId(R.drawable.ic_launcher);
            outcomeType.setTypeId(i);
            outcomeType.setTypeText(array[i]);
            list.add(outcomeType);
            Log.i(TAG,outcomeType.toString());
        }
        OutcomeTypeGridViewAdapter adapter = new OutcomeTypeGridViewAdapter(this.getActivity(),list);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       OutcomeType type = new OutcomeType();
                       type.setTypeId(position);
                       type.setTypeText(TypeConvertor.getOutcomeTypeString(position));
                       type.setResId(R.drawable.ic_launcher);
                       mSelectedType = type;
                Log.i(TAG,"selected: "+type.toString());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnFinishOutcomeTypeListener(OnFinishOutcomeTypeListener listener){
        this.mOutcomeTypeListener = listener;
    }

}
