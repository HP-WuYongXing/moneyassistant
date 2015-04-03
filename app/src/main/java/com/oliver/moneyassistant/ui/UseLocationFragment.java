package com.oliver.moneyassistant.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;*/
import com.oliver.moneyassistant.R;
import java.util.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UseLocationFragment extends Fragment {

    public static final String MY_TAG="UseLocationFragment";
   /* private LocationClient mLocClient;
    private PoiSearch mPoiSearch = null;
    private PoiNearbySearchOption mNearbyOption;*/
    private Boolean mStartSearchAround=false;
    private LinearLayout mLLLocaionButton;
    private static final int UPDATE_TIME=10000;
    private static final int SEARCH_RADIUS =10000;

    private ArrayList<Map<String,String>> mALAroundList;
    private SimpleAdapter mAddrListAdapter;

    private LayoutInflater mInflater;
    private View mRootView;
    private ListView mLVAroundPlaces;
    private EditText mETPlaceKeywords;
    private EditText mETCurrentLocation;

    public UseLocationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // initVariable();
        this.mInflater = inflater;
        mRootView =inflater.inflate(R.layout.use_location_view,null);
        return mRootView;
    }

   /* private void initVariable(){
        SDKInitializer.initialize(getActivity().getApplication());
        mLocClient = new LocationClient(this.getActivity());
        LocationClientOption locOption = new LocationClientOption();
        locOption.setOpenGps(true);
        locOption.setCoorType("bd0911");
        locOption.setProdName("moneyassistant");
        locOption.setScanSpan(UPDATE_TIME);
        locOption.setIsNeedAddress(true);
        mLocClient.setLocOption(locOption);
        mPoiSearch = PoiSearch.newInstance();

        mETPlaceKeywords= (EditText)mRootView.findViewById(R.id.et_keywords);
        mLVAroundPlaces = (ListView)mRootView.findViewById(R.id.lv_around_location);
        mETCurrentLocation = (EditText)mRootView.findViewById(R.id.et_current_location);
        mALAroundList = new ArrayList<>();
        mAddrListAdapter = new SimpleAdapter(getActivity(),
                mALAroundList,
                R.layout.around_location_cell_view,
                new String[]{"name","detail"},
                new int[]{R.id.tv_addr_header,R.id.tv_addr_detail});
        mLVAroundPlaces.setAdapter(mAddrListAdapter);

        mLLLocaionButton = (LinearLayout)mRootView.findViewById(R.id.ll_location_button);
        mLLLocaionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLocClient.start();
            }
        });

        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo> resultList = poiResult.getAllPoi();
                mALAroundList.clear();
                for(PoiInfo i:resultList){
                    Map<String,String>m = new HashMap<String, String>();
                    m.put("name",i.name);
                    m.put("detail",i.address);
                    mALAroundList.add(m);
                }
                mAddrListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });

        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                mLocClient.stop();
                String keywords = mETPlaceKeywords.getText().toString();
                mETCurrentLocation.setText(bdLocation.getAddrStr());
                if (keywords != null && keywords != "") {
                    mNearbyOption = new PoiNearbySearchOption();
                    mNearbyOption.keyword(keywords);
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    mNearbyOption.location(ll);
                    mNearbyOption.radius(SEARCH_RADIUS);
                    mPoiSearch.searchNearby(mNearbyOption);
                }
            }
        });

        mLVAroundPlaces = (ListView)mRootView.findViewById(R.id.lv_around_location);
        mLVAroundPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }*/
}
