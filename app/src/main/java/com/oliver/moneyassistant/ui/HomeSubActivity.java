package com.oliver.moneyassistant.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.constants.SharedVariable;
import com.oliver.moneyassistant.db.models.OutcomeType;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishBudgetMoneyListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishIncomeMoneyListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishIncomeTypeListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeAddressListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeMoneyListener;
import com.oliver.moneyassistant.logic.callbacklisteners.OnFinishOutcomeTypeListener;
import android.util.Log;
import com.oliver.moneyassistant.db.models.*;
import java.util.*;
public class HomeSubActivity extends ActionBarActivity
        implements OnFinishOutcomeTypeListener,
        OnFinishOutcomeAddressListener,OnFinishIncomeTypeListener,
        OnFinishBudgetMoneyListener ,OnFinishIncomeMoneyListener,
        OnFinishOutcomeMoneyListener{

    private String TAG = "SubActivity";
    /*属于addItemFragment 的全局变量*/
    private List<String> mPathList;
    private  AddItemFragment mOutcomeItemFragment;
    private OutcomeWithinDayFragment mOutcomeWithinDayFragment;
    private AddIncomeFragment mIncomeFragment;
    private AddBudgetFragment mAddBudgetFragment;
    private IncomeWithinDayFragment mIncomeWithinDayFragment;
    private ShowBudgetDetailsFragment mShowBudgetDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        initActionBar();
        initAddItemFragmentVariable();
        Log.i(TAG,"onCreate");
        chooseFragment();
    }

    private void initAddItemFragmentVariable(){
        mPathList = new ArrayList<>();
    }

    private void initActionBar(){
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(chooseActionBarTitle());
    }
    private String chooseActionBarTitle(){
        int fragmentCode = this.getIntent().getIntExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,-1);
        switch (fragmentCode){
            case ConstantsForHome.OUTCOME_WITHIN_DAY_FRAGMENT:
                return ConstantsForHome.OUTCOME_WITHIN_DAY_FRAGMENT_TITLE;
            case ConstantsForHome.ADD_OUTCOME_ITEM_FRAGMENT:
                return ConstantsForHome.ADD_OUTCOME_ITEM_FRAGMENT_TITLE;
        }
        return"";
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void chooseFragment(){
        int fragmentCode = this.getIntent().getIntExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,-1);
        long selectedDay = this.getIntent().getLongExtra(ConstantsForHome.SELECTED_DAY_LONG,-1);
        FragmentManager manager = this.getSupportFragmentManager();
        Log.i(TAG, "backstack count: " + manager.getBackStackEntryCount());
        FragmentTransaction transaction = manager.beginTransaction();

        switch (fragmentCode){
            case ConstantsForHome.OUTCOME_WITHIN_DAY_FRAGMENT:
                if(mOutcomeWithinDayFragment==null) {
                    mOutcomeWithinDayFragment = new OutcomeWithinDayFragment();
                }
                if(selectedDay!=-1){
                    Bundle data = new Bundle();
                    data.putLong(ConstantsForHome.SELECTED_DAY_LONG,selectedDay);
                    mOutcomeWithinDayFragment.setArguments(data);
                }
                 transaction.replace(R.id.sub_fragment_container,mOutcomeWithinDayFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();
                 break;
            case ConstantsForHome.INCOME_WITHIN_DAY_FRAGMENT:
                 if(mIncomeWithinDayFragment==null){
                     mIncomeWithinDayFragment = new IncomeWithinDayFragment();
                 }
                if(selectedDay!=-1){
                    Bundle data = new Bundle();
                    data.putLong(ConstantsForHome.SELECTED_DAY_LONG,selectedDay);
                    mIncomeWithinDayFragment.setArguments(data);
                 }
                 transaction.replace(R.id.sub_fragment_container,mIncomeWithinDayFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();
                 break;
            case ConstantsForHome.ADD_OUTCOME_ITEM_FRAGMENT:
                if(mOutcomeItemFragment ==null) {
                    mOutcomeItemFragment = new AddItemFragment();
                }
                mOutcomeItemFragment.setPathList(mPathList);
                transaction.replace(R.id.sub_fragment_container, mOutcomeItemFragment,AddItemFragment.FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case ConstantsForHome.ADD_INCOME_ITEM_FRAGMENT:
                if(mIncomeFragment==null){
                    mIncomeFragment = new AddIncomeFragment();
                }
                transaction.replace(R.id.sub_fragment_container,mIncomeFragment,
                        AddIncomeFragment.FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case ConstantsForHome.ADD_BUDGET_ITEM_FRAGMENT:
                if(mAddBudgetFragment==null){
                    mAddBudgetFragment = new AddBudgetFragment();
                }
                transaction.replace(R.id.sub_fragment_container,mAddBudgetFragment,
                        AddBudgetFragment.FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case ConstantsForHome.SHOW_BUDGET_DETAIL_FRAGMENT:
                if(mShowBudgetDetailsFragment==null){
                    mShowBudgetDetailsFragment = new ShowBudgetDetailsFragment();
                }
                transaction.replace(R.id.sub_fragment_container,mShowBudgetDetailsFragment,
                        ShowBudgetDetailsFragment.FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
           goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack(){
        FragmentManager manager = this.getSupportFragmentManager();
        int count =  manager.getBackStackEntryCount();
        if(count==1){
            manager.popBackStack();
            this.finish();
        }else if(count==0){
            this.finish();
        }else{
            manager.popBackStack();
        }
    }

    /*outcomeTypefragment 返回的选择结果通过activity传入*/
    @Override
    public void addOutcomeTypeInfo(OutcomeType type) {
        Log.i(TAG,"[before]addOutcomeTypeInfo: "+type.toString());
           FragmentManager manager = this.getSupportFragmentManager();
           Fragment fragment =  manager.findFragmentByTag(AddItemFragment.FRAGMENT_TAG);
        Log.i(TAG,"[after]addOutcomeTypeInfo: "+type.toString());
           if(fragment!=null){
               Log.i(TAG,"in if ...");
               AddItemFragment addItemFragment =(AddItemFragment)fragment;
               addItemFragment.setOutcomeType(type);
           }
    }

    @Override
    public void addOutomeAddrInfo(ItemAddress addr) {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment =  manager.findFragmentByTag(AddItemFragment.FRAGMENT_TAG);
        Log.i(TAG,"[after]addOutcomeTypeInfo: "+addr.toString());
        if(fragment!=null){
            Log.i(TAG,"in if ...");
            AddItemFragment addItemFragment =(AddItemFragment)fragment;
            addItemFragment.setOutcomeAddress(addr);
        }
    }

    @Override
    public void addIncomeTypeInfo(IncomeType type) {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(AddIncomeFragment.FRAGMENT_TAG);
        Log.i(TAG,"[after]addIncomeTypeInfo: "+type.toString());
        if(fragment!=null){
            Log.i(TAG,"in if ...");
            AddIncomeFragment addItemFragment =(AddIncomeFragment)fragment;
            addItemFragment.setIncomeType(type);
        }
    }

    @Override
    public void setBudgetMoney(float money) {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(AddBudgetFragment.FRAGMENT_TAG);
        Log.i(TAG,"setBudgetMoneyInfo...money:"+money);
        if(fragment!=null){
            Log.i(TAG,"in if... ");
            AddBudgetFragment addBudgetFragment = (AddBudgetFragment)fragment;
            addBudgetFragment.setBudgetMoney(money);
        }
    }

    @Override
    public void setIncomeMoney(float money) {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(AddIncomeFragment.FRAGMENT_TAG);
        Log.i(TAG,"setIncomeMoneyInfo...money:"+money);
        if(fragment!=null){
            Log.i(TAG,"in if... ");
            AddIncomeFragment addIncomeFragment = (AddIncomeFragment)fragment;
            addIncomeFragment.setIncomeMoney(money);
        }
    }

    @Override
    public void setOutcomeMoney(float money) {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(AddItemFragment.FRAGMENT_TAG);
        Log.i(TAG,"setOutcomeMoney....money: "+money);
        if(fragment!=null){
            Log.i(TAG,"in if...");
            AddItemFragment addItemFragment = (AddItemFragment)fragment;
            addItemFragment.setOutcomeMoney(money);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case ConstantsForHome.REQUEST_USE_ALBUM:
                    Uri uri = data.getData();
                    String path = getPath(uri);
                    Intent intent = new Intent();
                    Log.i(TAG, "path: " + path);
                    intent.putExtra("path", path);
                    intent.setClass(this,CropImageActivity.class);
                    this.startActivityForResult(intent, ConstantsForHome.REQUEST_CROP_IMG);
                    break;
                case ConstantsForHome.REQUEST_USE_CAMERA:
//                    String photoPath = data.getStringExtra("photoPath");
//                    Log.i(TAG,"path:　"+photoPath);

                   String photoPath = SharedVariable.photoPath;
                    Log.i(TAG,"photoPath: "+photoPath);
                    Intent intent1 = new Intent();
                    intent1.putExtra("path",photoPath);
                    intent1.setClass(this,CropImageActivity.class);
                    this.startActivityForResult(intent1, ConstantsForHome.REQUEST_CROP_IMG);
                    break;
                case ConstantsForHome.REQUEST_CROP_IMG:
                    String newPath = data.getStringExtra("path");
                    mPathList.add(newPath);
                    break;
                default: super.onActivityResult(requestCode, resultCode, data);break;
            }
        }

    }

    private String getPath(Uri uri){
        String can = uri.getAuthority();
        if(can==null)return null;
        String []projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri,projection,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(projection[0]);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        return path;
    }



}
