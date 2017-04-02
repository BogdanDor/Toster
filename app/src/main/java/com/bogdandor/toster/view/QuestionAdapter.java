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
            return 1 * 2;
        }
        return (answers.length + 1) * 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Comment[] comments;
        if (groupPosition % 2 == 0) {
            return 0;
        }
        if (groupPosition == 1) {
            comments = question.getComments();
        } else {
            comments = question.getAnswers()[groupPosition/2 - 1].getComments();
        }
        if (comments == null) {
            return 0;
        }
        return comments.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition % 2 != 0) {
            return null;
        }
        if (groupPosition == 0) {
            return question;
        }
        return question.getAnswers()[(groupPosition / 2) - 1];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Comment[] comments;
        if (groupPosition % 2 == 0) {
            return null;
        }
        if (groupPosition == 1) {
            comments = question.getComments();
        } else {
            comments = question.getAnswers()[groupPosition/2 - 1].getComments();
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
        if (groupPosition % 2 != 0) {
            convertView = inflater.inflate(R.layout.group_indicator, null);
            TextView groupIndicator = (TextView) convertView.findViewById(R.id.group_indicator);
            if (getChildrenCount(groupPosition) == 0) {
                groupIndicator.setText(R.string.no_comments_yet);
            } else if (isExpanded) {
                groupIndicator.setText(R.string.hide_comments);
            } else {
                groupIndicator.setText(R.string.show_comments);
            }
        } else {
            convertView = inflater.inflate(R.layout.group_content, null);
            String text;
            String authorAnswer;
            if (groupPosition == 0) {
                text = question.getText();
                authorAnswer = question.getAuthor().getName();
            } else {
                text = ((Answer)getGroup(groupPosition)).getText();
                authorAnswer = ((Answer)getGroup(groupPosition)).getAuthor().getName();
            }
            TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);
            TextView answerAuthor = (TextView) convertView.findViewById(R.id.answer_author);
            textGroup.setMovementMethod(LinkMovementMethod.getInstance());
            textGroup.setText(fromHtml(text));
            answerAuthor.setText(authorAnswer);
         }
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