package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.models.Picture;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.ui.utils.OutcomePicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/25.
 */
public class OutcomeDetailsFragment extends Fragment{
    public static final String FRAGMENT_TAG="OutcomeDetailsFragment";
    private static final String TAG ="OutcomeDetailsFragment";
    private View mRootView;
    private TextView mTVOutcomeMoney;
    private ImageView mIVOutcomeTypeIcon;
    private TextView mTVOutcomeTime;
    private TextView mTVOutcomeAddressH;
    private TextView mTVOutcomeAddressD;
    private TextView mTVOutcomeDescribe;
    private GridView mGVOutcomePhoto;
    private ArrayList<String>mImagePaths;
    private OutcomePicAdapter mPictrueAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.outcome_details_fragment,null);
        initLayoutVariables();
        return mRootView;
    }

    private void initLayoutVariables(){
       this.mTVOutcomeMoney = (TextView)mRootView.findViewById(R.id.tv_outcome_details_money);
       this.mIVOutcomeTypeIcon =(ImageView)mRootView.findViewById(R.id.outcome_details_type_icon);
       this.mTVOutcomeTime =(TextView)mRootView.findViewById(R.id.tv_outcome_details_time);
       this.mTVOutcomeAddressH = (TextView)mRootView.findViewById(R.id.tv_outcome_details_addr_header);
       this.mTVOutcomeAddressD = (TextView)mRootView.findViewById(R.id.tv_outcome_details_addr_details);
       this.mTVOutcomeDescribe = (TextView)mRootView.findViewById(R.id.tv_outcome_details_describe);
       this.mGVOutcomePhoto = (GridView)mRootView.findViewById(R.id.gv_outcome_details_photo_list);
       mImagePaths = new ArrayList<>();
       mPictrueAdapter = new OutcomePicAdapter(this.getActivity(),mImagePaths,
               ConstantsForHome.OUTCOME_PICTURE_WIDTH,
               ConstantsForHome.OUTCOME_PICTURE_HEIGHT);
        mGVOutcomePhoto.setAdapter(mPictrueAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle data = this.getArguments();
        if(data!=null){
            OutcomeItem item = data.getParcelable(ConstantsForHome.OUTCOME_ITEM_DETAILS);
            this.mTVOutcomeMoney.setText(MoneyUtils.displayMoney(item.getOutcomeMoney()));
            this.mIVOutcomeTypeIcon.setImageResource(item.getOutcomeType().getResId());
            Log.i(TAG, "mTVOutcomeTime=" + (mTVOutcomeTime == null));
            this.mTVOutcomeTime.setText(TimeUtils.getTimeStringWithoutSecond(item.getOutcomeTime()));
            this.mTVOutcomeAddressH.setText(item.getAddress().getAddressHeader());
            this.mTVOutcomeAddressD.setText(item.getAddress().getAddressDetails());
            this.mTVOutcomeDescribe.setText(item.getOutcomeDescribe());
            mImagePaths.clear();
            List<Picture> list = item.getPictures();
            if(list!=null) {
                for (Picture p : list) {
                    mImagePaths.add(p.getImgPath());
                }
                mPictrueAdapter.notifyDataSetChanged();
            }
        }
    }
}
