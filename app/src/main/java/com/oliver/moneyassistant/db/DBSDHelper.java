package com.oliver.moneyassistant.db;

import android.content.Context;

import com.ab.db.orm.AbSDDBHelper;
import com.oliver.moneyassistant.db.models.ItemAddress;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.db.models.IncomeType;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.models.OutcomeType;
import com.oliver.moneyassistant.db.models.Picture;

/**
 * Created by Oliver on 2015/3/12.
 */
public class DBSDHelper extends AbSDDBHelper{
    private static final String DBNAME = "moneyassistant.db";

    // 当前数据库的版本
    private static final int DBVERSION = 1;
    // 要初始化的表
    private static final Class<?>[] clazz = {
            OutcomeItem.class,
            Picture.class,
            IncomeItem.class,
            Budget.class,
            IncomeType.class,
            OutcomeType.class,
            ItemAddress.class};
    public DBSDHelper(Context context) {
        //super(context,AbFileUtil.getDbDownloadDir(context), DBNAME, null, DBVERSION, clazz);
        super(context,context.getFilesDir().getPath(), DBNAME, null, DBVERSION, clazz);
    }
}
