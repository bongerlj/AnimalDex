package com.animaldex.animaldex;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Questioning extends AppCompatActivity {
    private ArrayList<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        populateQuestions();
        populateListView();
    }

        private void populateQuestions(){
            questions = QuestionParser.MakeQuestions(getResources().openRawResource(R.raw.questions));
        }

    private void populateListView() {
        ArrayAdapter<Question> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);

        //noinspection ConstantConditions
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<Question> {
        public MyListAdapter() {
            super(Questioning.this, R.layout.question_layout, questions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.question_layout, parent, false);
            }

            final Question current = questions.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.item_question);
            makeText.setText(current.getQUESTION());

            Spinner spinner = (Spinner) itemView.findViewById(R.id.spinner);
            ArrayAdapter<String> choice = new ArrayAdapter<>(Questioning.this,android.R.layout.simple_spinner_item,current.getANSWERS());
            spinner.setAdapter(choice);
            choice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    current.setAnswer(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return itemView;
        }
    }

    public void Identify(View view){
        final Toast toast = Toast.makeText(this,"Identifying...",Toast.LENGTH_SHORT);
        toast.show();

        //Creates a delay to notify the user the animal is being identified
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
                Intent intent = new Intent(Questioning.this, Results.class);
                intent.putExtra("Questions", questions);
                startActivity(intent);
            }
        }, 250);
    }

}
