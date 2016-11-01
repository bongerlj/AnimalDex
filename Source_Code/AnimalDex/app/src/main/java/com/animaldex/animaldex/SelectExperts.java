package com.animaldex.animaldex;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectExperts extends AppCompatActivity {
    private List<String> availibleExperts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_experts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        availibleExperts.add("Expert 1");
        availibleExperts.add("Expert 2");
        availibleExperts.add("Expert 3");
        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView2);

        //noinspection ConstantConditions
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(SelectExperts.this, R.layout.question_layout, availibleExperts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.expert_layout, parent, false);
            }

            String current = availibleExperts.get(position);

            Switch makeText = (Switch) itemView.findViewById(R.id.switch1);
            makeText.setText(current.toString());
            makeText.setChecked(true);

            return itemView;
        }
    }

    public void toggle(View view){
        Switch s = (Switch) view;
        s.setChecked(true);
        Toast.makeText(this,"Can't Disable Must have 3 Active Experts",Toast.LENGTH_SHORT).show();

    }

}
