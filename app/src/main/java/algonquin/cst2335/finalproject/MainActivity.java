package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;


import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

/**
 * This class is the first Activity of this app; it contains a text message
 * and buttons to s
 *
 * @author Kaur, Iesha
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

     private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.bearButton.setOnClickListener(click -> {

            Intent bearPage = new Intent(MainActivity.this, BearActivity.class);
            startActivity(bearPage);

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


