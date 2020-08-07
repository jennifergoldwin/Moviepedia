package com.jenjenfuture.projectuas_2201750761;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DBMovie  {

    private DBHandler dbHandler;
    private Context context;

    private static DBMovie instance;

    public DBMovie(Context context) {
        this.context = context;
        dbHandler = DBHandler.getInstance(context);
    }

    public static synchronized DBMovie getInstance(Context context){
        if (instance==null){
            instance = new DBMovie(context);
            return instance;
        }
        return instance;
    }

    public void insertMovie (Movie movie){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHandler.FIELD_IMDB_ID,movie.getImdbID());
        cv.put(DBHandler.FIELD_POSTER,movie.getPoster());
        cv.put(DBHandler.FIELD_TITLE,movie.getTitle());
        cv.put(DBHandler.FIELD_TYPE,movie.getType());
        cv.put(DBHandler.FIELD_YEAR,movie.getYear());

        db.insert(DBHandler.TABLE_MOVIE,null,cv);
        db.close();

    }

    public void deleteMovie (String imdbID){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        db.delete(DBHandler.TABLE_MOVIE,DBHandler.FIELD_IMDB_ID + " = ? ",new String[]{imdbID});
        db.close();
    }

    public boolean checkMovie (String imdbID){

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String selection = DBHandler.FIELD_IMDB_ID + " = ? ";

        String[] selecttionArg = {imdbID};

        Cursor cursor = db.query(DBHandler.TABLE_MOVIE,null,selection,selecttionArg,null,null,null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount>0) return true;

        return false;
    }
    public List<Movie> getAllMovies(){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Movie> movieList = new ArrayList<>();

        String SELECT_QUERY = "SELECT * FROM " + DBHandler.TABLE_MOVIE;
        Cursor cursor = db.rawQuery(SELECT_QUERY,null);

        if (cursor.moveToFirst()){
            do{
                Movie movie = new Movie();
                movie.setImdbID(cursor.getString(cursor.getColumnIndex(DBHandler.FIELD_IMDB_ID)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(DBHandler.FIELD_POSTER)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(DBHandler.FIELD_TITLE)));
                movie.setYear(cursor.getString(cursor.getColumnIndex(DBHandler.FIELD_YEAR)));
                movie.setType(cursor.getString(cursor.getColumnIndex(DBHandler.FIELD_TYPE)));

                movieList.add(movie);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movieList;

    }

}
