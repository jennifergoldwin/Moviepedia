package com.jenjenfuture.projectuas_2201750761;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME ="DBMovie";
    private static final int DB_VERSION = 1;
    private static DBHandler instance;

    public static final String TABLE_MOVIE = "Movie";
    public static final String FIELD_IMDB_ID = "imdbID";
    public static final String FIELD_TITLE = "Title";
    public static final String FIELD_YEAR = "Year";
    public static final String FIELD_POSTER = "Poster";
    public static final String FIELD_TYPE = "Type";

    private static final String CREATE_TABLE_MOVIE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE + "(" +
            FIELD_IMDB_ID + " TEXT PRIMARY KEY , " +
            FIELD_POSTER + " TEXT , " +
            FIELD_TITLE + " TEXT , " +
            FIELD_TYPE + " TEXT , "+
            FIELD_YEAR + " TEXT );";

    private static final String DROP_TABLE_MOVIE = "DROP TABLE IF EXISTS " + TABLE_MOVIE + ";";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHandler getInstance(Context context){
        if (instance==null){
            instance = new DBHandler(context);
            return instance;
        }
        return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_MOVIE);
        onCreate(db);
    }
}

