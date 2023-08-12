package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * Activity to generate a bear image, save and delete.
 *
 * @author Iesha
 * @version 1.0
 */
public class BearActivity extends AppCompatActivity {

    /**
     * This holds android ID's
     *
     */
    private ActivityBearBinding binding2;

    /**
     * This holds queues of volley requests
     *
     */
    protected RequestQueue queue = null;

    /**
     * This functions initailizes layout and runs various events;
     * such as buttons, menu, preferences and image request
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *     <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding2 = ActivityBearBinding.inflate(getLayoutInflater());

        setContentView(binding2.getRoot());

        binding2.bearRecycleView.setAdapter(new RecyclerView.Adapter<ImageRowHolder>() {
            /**
             * This function crates view holder object
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return
             */
            @NonNull
            @Override
            public ImageRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            /**
             * This function  initializes a view holder to go at the row specified by the position parameter.
             *
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull ImageRowHolder holder, int position) {

            }

            /**
             * This function passes the number item draws
             * @return 0
             */
            @Override
            public int getItemCount() {
                return 0;
            }
        });

        setSupportActionBar(binding2.myToolbar);


        queue = Volley.newRequestQueue(this);

        SharedPreferences bearprefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = bearprefs.edit();

        binding2.bearGeneratorButton.setOnClickListener(clk -> {


            edit.putString("Height", binding2.bearHeight.getText().toString() );
            edit.putString("Width", binding2.bearWidth.getText().toString() );


            String width = binding2.bearWidth.getText().toString();
            String height = binding2.bearHeight.getText().toString();
            String stringURL = null;

            try {
                    stringURL = "https://placebear.com/"
                            + URLEncoder.encode(width,"UTF-8")
                            +  "/"
                            + URLEncoder.encode(height,"UTF-8")
                            ;
            } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
            }

            edit.putString("URL", stringURL);



            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try{

                            JSONArray bearArray = response.getJSONArray ( "img" );
                            JSONObject position0 = bearArray.getJSONObject(0);

                            int vis = response.getInt("visibility");
                            String name = response.getString( "baseURI" );

                            String imageUrl = "http://placebear.com/" + width + "/" + height + ".jpg";

                            ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    binding2.bearImage.setImageBitmap(bitmap);
                                    Bitmap image = bitmap;

                                    try {
                                        image.compress(Bitmap.CompressFormat.PNG,100, BearActivity.this.openFileOutput(

                                                imageUrl+ ".png", Activity.MODE_PRIVATE) );

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


            edit.apply();
        });

    }

    /**
     * This functions holds the item in a recycler view
     */
    class ImageRowHolder extends RecyclerView.ViewHolder {
        public ImageRowHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * This function creates menu to be selected
     *
     * @param menu The options menu in which you place your items.
     *
     * @return true for menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //fill the rest
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }

    /**
     * This function identifies the menu option selected by user and runs Toasts and Alertdialog.
     *
     * @param item The menu item that was selected.
     * @return true and runs event of menu item selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        binding2 = ActivityBearBinding.inflate(getLayoutInflater());
        //imageselected = model.selectedImage.getValue();

        if (item.getItemId() == R.id.item_bear1) {
            //About App toast
            Toast.makeText(getApplicationContext(), "This app will generate the photo of a bear." +
                            " Enter the dimensions in pixels to retrieve the image of a bear",
                    Toast.LENGTH_LONG).show();

        } else if ( item.getItemId() ==  R.id.item_bear2) {
            // Delete Menu Item
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

                       /* Snackbar.make( binding.recycleView, "You deleted image #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", click -> {

                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                    thread2.execute(() -> {
                                        cmDAO.insertMessage(m);

                                        messages.add(position,m);
                                        runOnUiThread( () ->  myAdapter.notifyDataSetChanged()); });
                                })
                                .show();*/

                    })
                    .create().show();

        } else if (item.getItemId() == R.id.item_bear3) {
            //Help Menu Item
            Toast.makeText(getApplicationContext(), "Enter width and height as hinted in pixels",
                    Toast.LENGTH_SHORT).show();
        } else if ( item.getItemId() ==  R.id.item_bear4) {
            // Delete Menu Item
            AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);
            //get index
            // int position = getAbsoluteAdapterPosition();
            //alert dialog
            builder.setTitle("Question:")
                    //message on alert button
                    .setMessage("Do you want to save this image: " /*+ item.getItemId(R.id.message).getText()*/)
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    //set words of alert buttons
                    .setPositiveButton("Yes", (dialog, cl) -> {

                           /* Snackbar.make( binding.recycleView, "You saved image #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", click -> {

                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                    thread2.execute(() -> {
                                        cmDAO.insertMessage(m);

                                        messages.add(position,m);
                                        runOnUiThread( () ->  myAdapter.notifyDataSetChanged()); });
                                })
                                .show();*/

                    })
                    .create().show();
        }

        return true ;
    }
    /**
     *
     * This function creates and stores shared preferences files
     */
    @Override
    protected void onPause() {
        //Shared preferences
        //
        SharedPreferences bearprefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       SharedPreferences.Editor edit = bearprefs.edit();

        edit.putString("Height", binding2.bearHeight.getText().toString() );
        edit.putString("Width", binding2.bearWidth.getText().toString() );
        edit.putString("Image", binding2.bearImage.toString());
        edit.apply();

        super.onPause();
    }

}