package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SetupDataHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 7;
    private static final String DB_NAME = "SetupDB";
    private static final String TABLE_NAME = "setup";

    private static final String CREATE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER NOT NULL PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "merchemail TEXT NOT NULL, " +
                    "programversion TEXT NOT NULL, " +
                    "businessname TEXT NOT NULL, "+
                    "legal TEXT NOT NULL)";


    private static final String INSERT_QUERY =
            "INSERT INTO setup " +
                    "(name, password, merchemail, programversion, businessname, legal) " +
                    "VALUES " +
                    "('Joe Schmoe', 'RADICALDUDE', 'someone@blah.com', '0.01', 'Daves House of Snacks', 'legal')";

    SetupDataHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        db.execSQL(INSERT_QUERY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete the old table data, then re-create the table(s):
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}
