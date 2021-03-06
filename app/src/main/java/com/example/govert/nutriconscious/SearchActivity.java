package com.example.govert.nutriconscious;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements SearchRequest.Callback {
    private ListView lv;
    private TextView tv;
    private ArrayList<FoodItemSimple> foodsFound;
    private int offset;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get ListView and TextView
        lv = (ListView) findViewById(R.id.listView);
        tv = (TextView) findViewById(R.id.searchView);

        // set listener
        lv.setOnItemClickListener(new ListItemClickListener());

        // get offset and date
        offset = getIntent().getIntExtra("dateOffset", 0);
        date = (Date) getIntent().getSerializableExtra("date");
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FoodItemSimple foodSelected = foodsFound.get(position);

            // make intent
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            intent.putExtra("foodItem", foodSelected);
            intent.putExtra("source", "search");
            intent.putExtra("dateOffset", offset);
            intent.putExtra("date", date);

            // start MenuActivity with intent
            startActivity(intent);
            finish();
        }
    }

    public void searchClicked(View view) {
        // hide keyboard
        hideKeyboard(this);

        // get search term
        String term = tv.getText().toString();

        // create url
        String url = "https://api.nutritionix.com/v1_1/search/" + term + "?results=0%3A25" +
                "&cal_min=0&cal_max=50000&fields=item_name,item_id,nf_calories," +
                "nf_serving_size_qty,nf_serving_size_unit" +
                "&appId=3f320916&appKey=fc58ccfd02cc5e1d32acce42ecee8bf6";

        // request search
        SearchRequest x = new SearchRequest(this);
        x.searchFoods(this, url);
    }

    @Override
    public void gotFoods(ArrayList<FoodItemSimple> foods) {
        // save ArrayList of foods that were found
        foodsFound = foods;

        // make adapter
        SearchAdapter adapter = new SearchAdapter(this, 0, foodsFound);

        // set adapter
        lv.setAdapter(adapter);
    }

    @Override
    public void gotFoodsError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // initialize intent
        Intent intent;

        // get source
        String source = getIntent().getStringExtra("source");

        // if source is main, go back
        if (source.equals("main")) {
            intent = new Intent(SearchActivity.this, MainActivity.class);
        }

        // if source is not main, go to diary
        else {
            intent = new Intent(SearchActivity.this, DiaryActivity.class);
        }

        // go
        startActivity(intent);
        finish();
    }
}
