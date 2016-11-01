package com.animaldex.animaldex;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Results extends AppCompatActivity {
    private List<String> possibleAnimal = new ArrayList<>();
    private List<Question> answers;
    private String result;
    final private int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        answers = getIntent().getParcelableArrayListExtra("Questions");

        /*TODO replace new ArrayList<>() with method call to get list<String> from each expert
          im thinking something like ArrayList<String> expert1 = ShapeExpert.Identify(Questions)
         */

        ArrayList<String> expert1 = new ArrayList<>();
        ArrayList<String> expert2 = new ArrayList<>();
        ArrayList<String> expert3 = new ArrayList<>();
        Set<String> results = new HashSet<>();
        results.addAll(expert1);
        results.retainAll(expert2);
        results.retainAll(expert3);
        possibleAnimal.addAll(results);

        //TODO Remove these extra choices
        possibleAnimal.add("Lion");
        possibleAnimal.add("Tiger");
        possibleAnimal.add("Fox");
        possibleAnimal.add("Hippo");

        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView3);

        //noinspection ConstantConditions
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(Results.this, R.layout.results_layout, possibleAnimal);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.results_layout, parent, false);
            }

            String current = possibleAnimal.get(position);

            Button makeText = (Button) itemView.findViewById(R.id.resultButton);
            makeText.setText(current);

            return itemView;
        }
    }

    public void results(View view) {
        Button b = (Button) view;
        result = b.getText().toString();

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == Dialog.BUTTON_NEGATIVE){
                    dialog.cancel();
                    Toast.makeText(Results.this, result +" Added to History", Toast.LENGTH_SHORT).show();
                    //Use NOFILE to indicate there will be no image
                    writeFile("NOFILE");
                    home();
                }else{
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }

            }
        };

        AlertDialog cameraQuestion = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setNegativeButton("No",listener)
                .setPositiveButton("Yes",listener)
                .setTitle("Camera")
                .setMessage("Do you want to take a picture?")
                .create();
        cameraQuestion.show();

    }

    private void writeFile(String imagepath){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        Calendar rightNow = Calendar.getInstance();
        int month = rightNow.get(Calendar.MONTH) + 1;
        String s;
        if(lastKnownLocation!=null){
            s = result + "," + lastKnownLocation.getLatitude() +","+ lastKnownLocation.getLongitude() + "," + month + "," + rightNow.get(Calendar.DAY_OF_MONTH) + "," + rightNow.get(Calendar.YEAR)+","+imagepath+"\n";
        }else{
            s = result + "," + 0 +","+ 0 + "," + month + "," + rightNow.get(Calendar.DAY_OF_MONTH) + "," + rightNow.get(Calendar.YEAR)+","+imagepath+"\n";
        }
        final String FILENAME = "Sighting_History";
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            fos.write(s.getBytes());
            fos.close();
            Log.v("Sighting History", "Written Successfully " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        home();
    }

    private void home(){
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            try {
                FileOutputStream fos = openFileOutput(result+"_"+timeStamp, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Log.v("File", "Image Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeFile(result+"_"+timeStamp);
            Toast.makeText(Results.this, result +" Added to History", Toast.LENGTH_SHORT).show();
            home();
        }
    }
}
