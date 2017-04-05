package com.bogdandor.toster.view;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bogdandor.toster.R;
import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Comment;
import com.bogdandor.toster.entity.Question;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private ArrayList<Object> objects;
    private LayoutInflater inflater;

    public QuestionAdapter(Context context, Question question) {
        inflater = LayoutInflater.from(context);
        objects = questionToArray(question);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item, parent, false);
        TextView nameAuthor = (TextView) convertView.findViewById(R.id.name_author);
        TextView textMessage = (TextView) convertView.findViewById(R.id.text_message);
        String author = "";
        String text = "";
        Object object = getItem(position);
        final String questionClassName = Question.class.getName();
        final String answerClassName = Answer.class.getName();
        final String commentClassName = Comment.class.getName();
        String objectClassName = object.getClass().getName();
        if (objectClassName == questionClassName) {
            author = ((Question)object).getAuthor().getName();
            text = ((Question)object).getText();
        } else if (objectClassName == answerClassName) {
            author = ((Answer)object).getAuthor().getName();
            text = ((Answer)object).getText();
        } else if (objectClassName == commentClassName) {
            author = ((Comment)object).getAuthor().getName();
            text = ((Comment)object).getText();
        }
        textMessage.setMovementMethod(LinkMovementMethod.getInstance());
        nameAuthor.setText(author);
        textMessage.setText(fromHtml(text));
        return convertView;
    }

    private ArrayList<Object> questionToArray(Question question) {
        ArrayList<Object> arrayList = new ArrayList<>();
        if (question == null) {
            return arrayList;
        }
        arrayList.add(question);
        Comment[] comments = question.getComments();
        if (comments != null) {
            for (Comment c : comments) {
                arrayList.add(c);
            }
        }
        Answer[] answers = question.getAnswers();
        if (answers == null) {
            return arrayList;
        }
        for (Answer a : answers) {
            arrayList.add(a);
            comments = a.getComments();
            if (comments != null) {
                for (Comment c : comments) {
                    arrayList.add(c);
                }
            }
        }
        return arrayList;
    }

    @SuppressWarnings("deprecation")
    private Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}