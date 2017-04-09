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

import com.bogdandor.toster.R;
import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Comment;
import com.bogdandor.toster.entity.Question;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> objects;

    public QuestionAdapter(Question question) {
        objects = questionToArray(question);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)                                                                                                                     {
        View v;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question, parent, false);
                viewHolder = new QuestionViewHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer, parent, false);
                viewHolder = new AnswerViewHolder(v);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
                viewHolder = new CommentViewHolder(v);
                break;
            default:
                viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                Question question = (Question) objects.get(position);
                ((QuestionViewHolder)holder).titleField.setText(question.getTitle());
                ((QuestionViewHolder)holder).authorField.setText(question.getAuthor().getName());
                ((QuestionViewHolder)holder).textField.setText(fromHtml(question.getText()));
                ((QuestionViewHolder)holder).textField.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 1:
                Answer answer = (Answer) objects.get(position);
                ((AnswerViewHolder)holder).authorField.setText(answer.getAuthor().getName());
                ((AnswerViewHolder)holder).textField.setText(fromHtml(answer.getText()));
                ((AnswerViewHolder)holder).textField.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 2:
                Comment comment = (Comment) objects.get(position);
                ((CommentViewHolder)holder).authorField.setText(comment.getAuthor().getName());
                ((CommentViewHolder)holder).textField.setText(fromHtml(comment.getText()));
                ((CommentViewHolder)holder).textField.setMovementMethod(LinkMovementMethod.getInstance());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        String className = objects.get(position).getClass().getName();
        final String questionClassName = Question.class.getName();
        final String answerClassName = Answer.class.getName();
        final String commentClassName = Comment.class.getName();
        int result;
        if (className == questionClassName) {
            result = 0;
        } else if (className == answerClassName) {
            result = 1;
        } else if (className == commentClassName) {
            result = 2;
        } else {
            result = -1;
        }
        return result;
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

    private class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleField;
        private TextView authorField;
        private TextView textField;

        public QuestionViewHolder(View v) {
            super(v);
            titleField = (TextView) v.findViewById(R.id.title);
            authorField = (TextView) v.findViewById(R.id.author);
            textField = (TextView) v.findViewById(R.id.text);
        }
    }

    private class AnswerViewHolder extends RecyclerView.ViewHolder {
        private TextView authorField;
        private TextView textField;

        public AnswerViewHolder(View v) {
            super(v);
            authorField = (TextView) v.findViewById(R.id.author);
            textField = (TextView) v.findViewById(R.id.text);
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView authorField;
        private TextView textField;

        public CommentViewHolder(View v) {
            super(v);
            authorField = (TextView) v.findViewById(R.id.author);
            textField = (TextView) v.findViewById(R.id.text);
        }
    }

}