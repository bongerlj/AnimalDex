package com.animaldex.animaldex;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Josh Voskamp on 3/30/2016.
 */
public class QuestionParser {
    public static ArrayList<Question> MakeQuestions(InputStream in) {
       Scanner input = new Scanner(in);

        ArrayList<Question> questions = new ArrayList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] splits = line.split(",");

            List<String> answers = new ArrayList<>();
            answers.add(" ");

            for(int i = 1; i<splits.length; i++){
                answers.add(splits[i]);
            }
            questions.add(new Question(splits[0],answers));
        }
        input.close();

        return questions;
    }
}
