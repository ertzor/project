package com.example.govert.nutriconscious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    // declare DB_NAME and TABLE_NAMES
    private static final String DB_NAME = "foodDatabase.db";
    private static final String TABLE_NAME_FOOD = "food";

    // declare CREATE_TABLE_SQL query for food
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " + TABLE_NAME_FOOD +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT, date TEXT, calories FLOAT, protein FLOAT, " +
            "carbohydrate FLOAT, fat FLOAT, serving_quantity FLOAT, serving_size TEXT, " +
            "serving_weight FLOAT)";

    // create static instance of EntryDatabase
    private static FoodDatabaseHelper instance;

    private FoodDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOOD);

        // recreate table
        onCreate(db);
    }

    public static FoodDatabaseHelper getInstance(Context context) {
        // if instance exists, return it
        if (instance != null)
            return instance;

        // if instance doesn't exist, created it and return it
        instance = new FoodDatabaseHelper(context);
        return instance;
    }

    public Cursor selectFoods() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM food", null);
    }

    public void updateFood(FoodItem foodItem) {
        // get db
        SQLiteDatabase db = this.getWritableDatabase();

        // declare values
        ContentValues values = new ContentValues();

        // put values
        values.put("item_name", foodItem.getName());
        values.put("date", foodItem.getDate());
        values.put("calories", foodItem.getCalories());
        values.put("protein", foodItem.getProtein());
        values.put("carbohydrate", foodItem.getCarbohydrate());
        values.put("fat", foodItem.getFat());
        values.put("serving_quantity", foodItem.getServingQTY());
        values.put("serving_size", foodItem.getServingSize());

        // get id string
        int id = foodItem.getId();
        String[] whereArgs = new String[] {String.valueOf(id)};

        // update entry
        db.update(TABLE_NAME_FOOD, values, "_id=?", whereArgs);
    }

    public void insertFood(FoodItem foodItem) {
        // get db
        SQLiteDatabase db = this.getWritableDatabase();

        // declare values
        ContentValues values = new ContentValues();

        // put values
        values.put("item_name", foodItem.getName());
        values.put("date", foodItem.getDate());
        values.put("calories", foodItem.getCalories());
        values.put("protein", foodItem.getProtein());
        values.put("carbohydrate", foodItem.getCarbohydrate());
        values.put("fat", foodItem.getFat());
        values.put("serving_quantity", foodItem.getServingQTY());
        values.put("serving_size", foodItem.getServingSize());

        // insert entry into db
        db.insert(TABLE_NAME_FOOD, null, values);
    }

    public void deleteFood(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQLDeleteQuery = "DELETE FROM " + TABLE_NAME_FOOD + " WHERE _id=" + id;

        db.execSQL(SQLDeleteQuery);
    }
}