package com.bogdandor.toster.view;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bogdandor.toster.R;
import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Comment;
import com.bogdandor.toster.entity.Question;

public class QuestionAdapter extends BaseExpandableListAdapter {
    private Question question;
    private LayoutInflater inflater;

    public QuestionAdapter(Context context, Question question) {
        inflater = LayoutInflater.from(context);
        this.question = question;
    }

    @Override
    public int getGroupCount() {
        Answer[] answers = question.getAnswers();
        if (answers == null) {
            return 1;
        }
        return answers.length + 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Comment[] comments;
        if (groupPosition == 0) {
            comments = question.getComments();
        } else {
            comments = question.getAnswers()[groupPosition - 1].getComments();
        }
        if (comments == null) {
            return 0;
        }
        return comments.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == 0) {
            return question;
        }
        return question.getAnswers()[groupPosition - 1];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Comment[] comments;
        if (groupPosition == 0) {
            comments = question.getComments();
        } else {
            comments = question.getAnswers()[groupPosition - 1].getComments();
        }
        return comments[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group, null);
        }
        String text;
        String authorAnswer;
        if (groupPosition == 0) {
            text = question.getText();
            authorAnswer = question.getAuthor().getName();
        } else {
            text = question.getAnswers()[groupPosition - 1].getText();
            authorAnswer = question.getAnswers()[groupPosition - 1].getAuthor().getName();
        }
        TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);
        TextView answerAuthor = (TextView) convertView.findViewById(R.id.answer_author);
        textGroup.setMovementMethod(LinkMovementMethod.getInstance());
        textGroup.setText(fromHtml(text));
        answerAuthor.setText(authorAnswer);
        Button expandGroup = (Button) convertView.findViewById(R.id.expand_group);
        expandGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandableListView expListView = (ExpandableListView) view.getParent().getParent();
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroup(groupPosition);
                    ((Button) view).setText(R.string.show_comments);
                } else {
                    expListView.expandGroup(groupPosition);
                    ((Button) view).setText(R.string.hide_comments);
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item, parent, false);
        Comment comment = (Comment) getChild(groupPosition, childPosition);
        TextView textItem = (TextView) convertView.findViewById(R.id.text_item);
        TextView authorComment = (TextView) convertView.findViewById(R.id.comment_author);
        textItem.setMovementMethod(LinkMovementMethod.getInstance());
        textItem.setText(fromHtml(comment.getText()));
        authorComment.setText(comment.getAuthor().getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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