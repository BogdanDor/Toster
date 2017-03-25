package com.bogdandor.toster;

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
        if (groupPosition == 0) {
            text = question.getText();
        } else {
            text = question.getAnswers()[groupPosition - 1].getText();
        }
        TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);
        textGroup.setMovementMethod(LinkMovementMethod.getInstance());
        textGroup.setText(fromHtml(text));
        Button expandGroup = (Button) convertView.findViewById(R.id.expand_group);
        expandGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandableListView expListView = (ExpandableListView) view.getParent().getParent();
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroup(groupPosition);
                } else {
                    expListView.expandGroup(groupPosition);
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
        textItem.setMovementMethod(LinkMovementMethod.getInstance());
        textItem.setText(fromHtml(comment.getText()));
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