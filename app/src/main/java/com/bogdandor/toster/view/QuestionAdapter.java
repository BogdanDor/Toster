package com.bogdandor.toster.view;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Comment;
import com.bogdandor.toster.entity.Question;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private int titleResource;
    private int titleViewResourceId;
    private int itemResource;
    private int nameAuthorViewResourceId;
    private int textMessageViewResourceId;
    private ArrayList<Object> objects;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public QuestionAdapter(int titleResource, int titleViewResourceId, int itemResource, int nameAuthorViewResourceId, int textMessageViewResourceId, Question question) {
        this.titleResource = titleResource;
        this.titleViewResourceId = titleViewResourceId;
        this.itemResource = itemResource;
        this.nameAuthorViewResourceId = nameAuthorViewResourceId;
        this.textMessageViewResourceId = textMessageViewResourceId;
        objects = questionToArray(question);
    }

    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)                                                                                                                     {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(titleResource, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View view = holder.view;
        if (getItemViewType(position) == 0) {
            TextView titleField = (TextView) view.findViewById(titleViewResourceId);
            String title = ((Question)objects.get(0)).getTitle();
            titleField.setText(title);
            return;
        }
        TextView nameAuthorField = (TextView) view.findViewById(nameAuthorViewResourceId);
        TextView textMessageField = (TextView) view.findViewById(textMessageViewResourceId);
        String nameAuthor = "";
        String textMessage = "";
        Object object = objects.get(position - 1);
        final String questionClassName = Question.class.getName();
        final String answerClassName = Answer.class.getName();
        final String commentClassName = Comment.class.getName();
        String objectClassName = object.getClass().getName();
        if (objectClassName == questionClassName) {
            nameAuthor = ((Question)object).getAuthor().getName();
            textMessage = ((Question)object).getText();
        } else if (objectClassName == answerClassName) {
            nameAuthor = ((Answer)object).getAuthor().getName();
            textMessage = ((Answer)object).getText();
        } else if (objectClassName == commentClassName) {
            nameAuthor = ((Comment)object).getAuthor().getName();
            textMessage = ((Comment)object).getText();
        }
        textMessageField.setMovementMethod(LinkMovementMethod.getInstance());
        nameAuthorField.setText(nameAuthor);
        textMessageField.setText(fromHtml(textMessage));
    }

    @Override
    public int getItemCount() {
        return objects.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0 : return 0;
            default: return 1;
        }
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