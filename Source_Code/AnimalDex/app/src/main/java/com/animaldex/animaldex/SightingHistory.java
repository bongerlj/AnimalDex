package com.animaldex.animaldex;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SightingHistory extends AppCompatActivity {

    static ArrayList<SightingHistoryElement> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        populateHistory();
        populateListView();
    }

    private void populateHistory() {
        try {
            history = SightingHistoryParser.parseHistory(openFileInput("Sighting_History"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateListView() {
        //Reverse the list so we see most recent first
        Collections.reverse(history);
        ArrayAdapter<SightingHistoryElement> adapter = new MyListAdapter();
        ListView mylist = (ListView) findViewById(R.id.listView4);
        mylist.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<SightingHistoryElement> {
        public MyListAdapter() {
            super(SightingHistory.this, R.layout.sighting_history_element_layout, history);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.sighting_history_element_layout, parent, false);
            }

            final SightingHistoryElement current = history.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.textView_name);
            makeText.setText(current.toString());

            if(current.getFILEPATH().equals("NOFILE")){
                Log.v("History", "No Image");
                return itemView;
            }

            try {
                ImageView image = (ImageView) itemView.findViewById(R.id.imageView2);
                FileInputStream fin = openFileInput(current.getFILEPATH());
                image.setImageBitmap(BitmapFactory.decodeStream(fin));
                fin.close();
                Log.v("History", "Adding Image");
            } catch (IOException e) {
                Log.e("History", "Adding Image Failed");
            }

            return itemView;
        }
    }

    public void clearSightingHistory(View view){
        FileOutputStream fos = null;
        String s = "";
        String filepath = "Sighting_History";
        try {
            fos = openFileOutput(filepath, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e("Sighting History","File does not exist:" +filepath);
        }
        Toast.makeText(this,"Sighting History Cleared",Toast.LENGTH_SHORT).show();
        for(SightingHistoryElement i : history){
            deleteFile(i.getFILEPATH());
        }
        super.onBackPressed();
    }

    public void displayMap (View view){
        if(history.isEmpty()){
            Toast.makeText(this,"No History To View",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this,SightingHistoryMap.class);
        intent.putParcelableArrayListExtra("History",history);
        startActivity(intent);
    }

}