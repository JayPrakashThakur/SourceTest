package com.appniche.sourcetest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JayPrakash on 16-04-2016.
 */
public class SourceAdapter extends BaseAdapter {

    private final String LOG_TAG = SourceAdapter.class.getSimpleName();

    Context context;
    ArrayList<SourceObject> objects = new ArrayList<SourceObject>();

    public SourceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG,"getCount called "+objects.size());
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(LOG_TAG,"getItem called");
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG,"get view method is called");
        SourceObject sourceObject = (SourceObject) getItem(position);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_source,parent,false);
        }

        TextView personName = (TextView) convertView.findViewById(R.id.person_name);
        TextView commit = (TextView) convertView.findViewById(R.id.commit_Number);
        TextView commitMessage = (TextView) convertView.findViewById(R.id.commit_message);

        personName.setText(sourceObject.getPersonName());
        commit.setText(sourceObject.getCommit());
        commitMessage.setText(sourceObject.getCommitMessage());

        return convertView;
    }

    public void setItems(SourceObject[] items){
        this.objects = new ArrayList<SourceObject>();
        for (SourceObject item : items){
            this.objects.add(item);
        }
    }

}