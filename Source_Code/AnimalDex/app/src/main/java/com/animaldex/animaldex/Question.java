package com.animaldex.animaldex;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Josh Voskamp on 3/30/2016.
 */
public class Question implements Parcelable{
    private final String QUESTION;
    private final List<String> ANSWERS;
    private String answer;

    public Question (String question, List<String> answers){
        QUESTION = question;
        ANSWERS = answers;
    }

    protected Question(Parcel in) {
        QUESTION = in.readString();
        ANSWERS = in.createStringArrayList();
        answer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(QUESTION);
        dest.writeStringList(ANSWERS);
        dest.writeString(answer);
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public List<String> getANSWERS() {
        return ANSWERS;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
