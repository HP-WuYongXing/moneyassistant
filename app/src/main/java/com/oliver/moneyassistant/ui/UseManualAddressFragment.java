package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.oliver.moneyassistant.*;
import com.oliver.moneyassistant.db.models.ItemAddress;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeAddressListener;

public class UseManualAddressFragment extends Fragment {

	private static String TAG = "UseManualAddressFragment";
	private View mRootView;
	private EditText mETAddressHeader;
	private EditText mETAddressDetails;
	private RelativeLayout mRLSureButton;
	private OnFinishOutcomeAddressListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	     mRootView  = inflater.inflate(R.layout.use_manual_address_view,null);
	     initVariable();
		return mRootView;
	}
	public void initVariable(){
		mETAddressHeader = (EditText)mRootView.findViewById(R.id.et_address_header);
		mETAddressDetails = (EditText)mRootView.findViewById(R.id.et_address_details);
		mRLSureButton = (RelativeLayout)mRootView.findViewById(R.id.rl_address_ensure);
		mRLSureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String header = mETAddressHeader.getText().toString();
				String details = mETAddressDetails.getText().toString();
				if(header==null||header.trim()==""){
					return;
				}
				if(details==null||details.trim()==""){
					return;
				}
				ItemAddress addr = new ItemAddress();
				addr.setAddressHeader(header);
                addr.setAddressDetails(details);
				mListener.addOutomeAddrInfo(addr);
				getFragmentManager().popBackStack();
			}
		});
	}
	
	public void setOnFinishOutcomeAddressListener(OnFinishOutcomeAddressListener listener){
		Log.i(TAG,"setOnFinishOutcomeAddressListener: "+(listener==null));
		this.mListener = listener;
	}

}
