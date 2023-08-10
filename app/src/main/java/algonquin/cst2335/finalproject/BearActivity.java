package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class BearActivity extends AppCompatActivity {

    private ActivityBearBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bear);

        //Intent fromPrevious = getIntent();
      //  SharedPreferences bearprefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


        binding.bearGeneratorButton.setOnClickListener(clk -> {





        });


    }

    @Override
    protected void onPause() {
        //Shared preferences
        //SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor = prefs.edit();

        super.onPause();
    }

}