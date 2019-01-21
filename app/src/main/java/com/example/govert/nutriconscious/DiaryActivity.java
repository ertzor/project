package com.example.govert.nutriconscious;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.govert.nutriconscious.FoodItem.getFoodsFromCursor;
import static com.example.govert.nutriconscious.FoodItem.makeDate;

public class DiaryActivity extends AppCompatActivity {
    private TextView date, caloriesLeft;
    private ListView lv;
    private FoodDatabaseHelper dbFood;
    private UserDataBaseHelper dbUser;
    private DiaryAdapter adapter;
    private ArrayList<FoodItem> foodItems;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        // get views
        date = findViewById(R.id.dayOrMonth);
        caloriesLeft = findViewById(R.id.caloriesLeft);
        lv = findViewById(R.id.diaryList);

        // get dbs
        dbFood = FoodDatabaseHelper.getInstance(this);
        dbUser = UserDataBaseHelper.getInstance(this);

        // get cursors
        Cursor cursorFood = dbFood.selectFoodsByDate(makeDate());
        Cursor cursorUser = dbUser.selectUser();

        // get user
        user = User.getUser(cursorUser);

        // get foodItems
        foodItems = getFoodsFromCursor(cursorFood);

        // get adapter
        adapter = new DiaryAdapter(this, 0, foodItems);

        // set DiaryAdapter
        lv.setAdapter(adapter);

        // set caloriesLeft
        Float caloriesUser = BigDecimal.valueOf(user.getCalories()).floatValue();
        Float caloriesFood = 0f;
        for (FoodItem foodItem : foodItems) {
            caloriesFood += (foodItem.getCalories() * foodItem.getServingQTY());
        }

        caloriesLeft.setText(String.format(Locale.getDefault(), "Calories left: %.0f - %.0f = %.0f", caloriesUser,
                caloriesFood, (caloriesUser - caloriesFood)));
    }


    public void searchClicked(View view) {
        startActivity(new Intent(DiaryActivity.this, SearchActivity.class));
    }

    public void diaryClicked(View view) {
        startActivity(new Intent(DiaryActivity.this, DiaryActivity.class));
    }

    public void homeClicked(View view) {
        startActivity(new Intent(DiaryActivity.this, MainActivity.class));
    }

    public void profileClicked(View view) {
        startActivity(new Intent(DiaryActivity.this, ProfileActivity.class));
    }
}
