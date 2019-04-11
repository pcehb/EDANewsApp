package uk.ac.kent.pceh3.miniproject.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;

/**
 * Created by pceh3 on 11/04/2019.
 */

public class SavedArticlesDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "saved_database";
    public static final String SAVED_TABLE_NAME = "saved";
    public static final String SAVED_COLUMN_ID = "_id";
    public static final String SAVED_COLUMN_URL = "articleUrl";
    public static final String SAVED_COLUMN_TITLE = "title";
    public static final String SAVED_COLUMN_DESC = "description";
    public static final String SAVED_COLUMN_PHOTO = "photo";
    public static final String SAVED_COLUMN_DATE = "date";
    public static final String SAVED_COLUMN_CAT = "categories";


    public SavedArticlesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + SAVED_TABLE_NAME + " (" +
                SAVED_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SAVED_COLUMN_URL + " TEXT, " +
                SAVED_COLUMN_TITLE + " TEXT, " +
                SAVED_COLUMN_DESC + " TEXT, " +
                SAVED_COLUMN_PHOTO + " BLOB, " +
                SAVED_COLUMN_DATE + " TEXT, " +
                SAVED_COLUMN_CAT + " TEXT " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SAVED_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}