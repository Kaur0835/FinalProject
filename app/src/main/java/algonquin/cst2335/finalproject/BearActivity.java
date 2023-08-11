package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

/**
 *
 * Activity to generate bear
 *
 * @author Iesha
 * @version 1.0
 */
public class BearActivity extends AppCompatActivity {

    /**
     *
     */
    private ActivityBearBinding binding;

    /**
     *
     */
    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bear);

        queue = Volley.newRequestQueue(this);
        binding = ActivityBearBinding.inflate(getLayoutInflater());

        Intent fromPrevious = getIntent();


        SharedPreferences bearPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        binding.bearHeight.setText( bearPrefs.getString("Height", null) );
        binding.bearWidth.setText( bearPrefs.getString("Width", null) );

        setSupportActionBar(binding.myToolbar);

        binding.bearGeneratorButton.setOnClickListener(clk -> {

            String width = binding.bearWidth.getText().toString();
            String height = binding.bearHeight.getText().toString();
            String stringURL = null;

            try {
                    stringURL = "https://placebear.com/"
                            + URLEncoder.encode(width,"UTF-8")
                            +  "/"
                            + URLEncoder.encode(height,"UTF-8")
                            + "&appid=a656d228edf40e91a99c6c1412a8dfbe&units=pixels";
            } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
            }

            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try{

                            JSONObject dim = response.getJSONObject( "dim" );

                            JSONArray bearArray = response.getJSONArray ( "img" );
                            JSONObject position0 = bearArray.getJSONObject(0);


                            String imageUrl = "http://placebear.com/" + width + "/" + height + ".png";

                            ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    binding.bearImage.setImageBitmap(bitmap);
                                    Bitmap image = bitmap;


                                    try {
                                        image.compress(Bitmap.CompressFormat.PNG,100, BearActivity.this.openFileOutput(

                                                image + ".png", Activity.MODE_PRIVATE) );

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();

                                    }
                                    int i = 0;
                                }
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                    (error ) -> {
                                        int i = 0;

                            });

                            queue.add(imgReq);

                            runOnUiThread( (  )  -> {
                                //fragment details
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    (error) -> {        });
            queue.add(request);

        });

    }

    /**
     * This creates menu to be selected
     *
     * @param menu The options menu in which you place your items.
     *
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //fill the rest
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        binding = ActivityBearBinding.inflate(getLayoutInflater());
        //imageselected = model.selectedImage.getValue();

        if( item.getItemId() ==  R.id.item_bear2) {
            // case R.id.item_1:

            AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);
            //get index
            // int position = getAbsoluteAdapterPosition();


            //alert dialog
            builder.setTitle("Question:")
                    //message on alert button
                    .setMessage("Do you want to delete this image: " /*+ item.getItemId(R.id.message).getText()*/)
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    //set words of alert buttons
                    .setPositiveButton("Yes", (dialog, cl) -> {

                    })

                    //make window appear
                    .create().show();

        }
        else if (item.getItemId() == R.id.item_bear1) {
            Toast.makeText(getApplicationContext(), "Enter width and height as hinted in pixels",
                    Toast.LENGTH_SHORT).show();
        }
        return true ;
    }
    /**
     *
     * This method creates and stores shared preferences files
     */
    @Override
    protected void onPause() {
        //Shared preferences
        SharedPreferences bearprefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = bearprefs.edit();

        editor.putString("Height", binding.bearHeight.getText().toString() );
        editor.putString("Width", binding.bearWidth.getText().toString() );
        editor.apply();

        super.onPause();
    }

}