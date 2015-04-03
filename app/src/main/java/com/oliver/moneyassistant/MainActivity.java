package com.oliver.moneyassistant;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.*;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.BudgetDao;
import com.oliver.moneyassistant.db.dao.IncomeDao;
import com.oliver.moneyassistant.db.dao.OutcomeDao;
import com.oliver.moneyassistant.db.models.ItemAddress;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.db.models.Picture;
import com.oliver.moneyassistant.db.utils.*;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.logic.callbacklisteners.OutcomeListFragmentCallback;
import com.oliver.moneyassistant.ui.HomeFragment;
import com.oliver.moneyassistant.ui.HomeSubActivity;
import com.oliver.moneyassistant.ui.StockFragment;
import com.oliver.moneyassistant.ui.StockNewsFragment;
import com.oliver.moneyassistant.ui.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        OutcomeListFragmentCallback{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private static final String TAG="MainActivity";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private HomeFragment mHomeFragment;
    private StockNewsFragment mStockNewsFragment;

    public static final String SUM_OF_DAY="sumOfThisMonth";
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private OutcomeDao mOutcomeDao;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
               (DrawerLayout) findViewById(R.id.drawer_layout));
}


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // update the main content by replacing fragments
        switch (position){
            case 0: if(mHomeFragment==null)mHomeFragment = new HomeFragment();
                transaction.replace(R.id.container, mHomeFragment)
                    .commit();
                break;
            case 4:if(mStockNewsFragment==null)mStockNewsFragment = new StockNewsFragment();
                transaction.replace(R.id.container,mStockNewsFragment)
                    .commit();
        }
    }


    /* public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }*/

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void modifyActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    //    if(id==android.R.id.home)Toast.makeText(this,"main activity",Toast.LENGTH_LONG).show();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_settings:new InjectDataAsyncTast().execute("");return true;
            case R.id.action_income:new InjectIcomeDataTask().execute("");return true;
            case R.id.action_budget:new InjectBudgetDataTask().execute("");return true;
            case R.id.action_add:initPopupWindow(item.getActionView());return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    private void initPopupWindow(View actionView){
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_window_view,null);
        final PopupWindow popupWindow = new PopupWindow(view,200,180,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        LinearLayout addOutcome = (LinearLayout)view.findViewById(R.id.ll_add_an_outcome);
        LinearLayout addIncome = (LinearLayout)view.findViewById(R.id.ll_add_an_income);
        LinearLayout addBudget = (LinearLayout)view.findViewById(R.id.ll_add_an_budget);
        addOutcome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"add outcome",Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                        ConstantsForHome.ADD_OUTCOME_ITEM_FRAGMENT);

                intent.setClass(MainActivity.this,HomeSubActivity.class);
                startActivity(intent);
            }
        });
        addIncome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            //    Toast.makeText(MainActivity.this,"add incomeItem",Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                        ConstantsForHome.ADD_INCOME_ITEM_FRAGMENT);

                intent.setClass(MainActivity.this,HomeSubActivity.class);
                startActivity(intent);
            }
        });
        addBudget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
             //   Toast.makeText(MainActivity.this,"add budget",Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                        ConstantsForHome.ADD_BUDGET_ITEM_FRAGMENT);

                intent.setClass(MainActivity.this,HomeSubActivity.class);
                startActivity(intent);
            }
        });
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int actionBarHeight = getSupportActionBar().getHeight();

        popupWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT,20,statusBarHeight+actionBarHeight-20);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setUpdateMark(){
        SharePreferenceUtils.setUpdateMark(this);
    }



    class InjectBudgetDataTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            Budget b = null;
            BudgetDao budgetDao = new BudgetDao(MainActivity.this);
            budgetDao.startWritableDatabase(false);
            b = new Budget();
            b.setBudgetTime(TimeUtils.getNow());
            b.setStartTime(TimeUtils.getFirstDayOfThisMonth());
            b.setEndTime(TimeUtils.getLastSecondOfToday());
            b.setMoney(1000f);
            budgetDao.insert(b);
            budgetDao.closeDatabase();
             return "yes";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this,"inject budget data ccompleted!",Toast.LENGTH_LONG)
                    .show();
            setUpdateMark();

        }
    }



    class InjectIcomeDataTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            IncomeDao incomeDao= new IncomeDao(MainActivity.this);
            incomeDao.startWritableDatabase(false);
            for(int i=0;i<20;i++) {
                String incomeDescribe ="test";
                float incomeMoney = RandomOIdGenerater.getRandomMoneyOfDay();
                int typeId = RandomOIdGenerater.getRandomIncomeType();
                long incomeTime = RandomOIdGenerater.getRandomeTimeOfThisMonth();
                IncomeItem income = new IncomeItem();
                income.setTypeId(typeId);
                income.setIncomeDescribe(incomeDescribe);
                income.setIncomeMoney(incomeMoney);
                income.setIncomeTime(incomeTime);
                incomeDao.insert(income);
            }
            incomeDao.closeDatabase();
            return "yes";
        }

        @Override
        protected void onPostExecute(String s) {
           Toast.makeText(MainActivity.this,"income data inject completed!",Toast.LENGTH_LONG).show();
            setUpdateMark();
        }
    }

    class InjectDataAsyncTast extends AsyncTask<String,Integer,String>{
       @Override
       protected String doInBackground(String... params) {
           OutcomeItem o = null;
           for (int i = 0; i < 30; i++) {
               float price = RandomOIdGenerater.getRandomMoneyOfDay();
               String oId = RandomOIdGenerater.getRandomId();
               int outcomeType = RandomOIdGenerater.getRandomOutcomeType();
               long outcomeTime = RandomOIdGenerater.getRandomeTimeOfThisMonth();
             //  long outcomeTime = TimeUtils.getFirstSecondOfToday()-1000;
               String addrHeader = "test";
               String addrDetail = "test detail";
               String outcomeDesc = "outcome describe";
               List<Picture> plist = new ArrayList<>();
               for(int j=0;i<4;i++){
                   Picture pic = new Picture();
                   pic.setoId(oId);
                   pic.setImgPath("/mnt/sdcard/Download/com.oliver.moneyassistant/images/1426769208578.jpg");
                   plist.add(pic);
               }
               ItemAddress address = new ItemAddress();
               address.setAddressHeader(addrHeader);
               address.setAddressDetails(addrDetail);
               address.setoId(oId);
               o = new OutcomeItem(MainActivity.this,
                       oId,
                       price,
                       outcomeType,
                       outcomeTime,
                       address,
                       outcomeDesc,
                       plist);
               mOutcomeDao.insert(o);
           }
           return "yes";
       }
       @Override
       protected void onPreExecute() {
           mOutcomeDao = new OutcomeDao(MainActivity.this);
           mOutcomeDao.startWritableDatabase(false);
       }

       @Override
       protected void onPostExecute(String s) {
           mOutcomeDao.closeDatabase();
           Toast.makeText(MainActivity.this,"Inject completed!",Toast.LENGTH_LONG).show();
        setUpdateMark();
       }

       @Override
       protected void onProgressUpdate(Integer... values) {

       }
   }



    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.home_view, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }*/

}
