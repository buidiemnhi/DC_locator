package com.evilgeniustechnologies.dclocator.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

import com.evilgeniustechnologies.dclocator.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by benjamin on 10/4/14.
 */
public class ContactAdapter extends ArrayAdapter<Utils.Tuple> {
    private List<Utils.Tuple> contents;
    private OnContactRemoveListener listener;

    public ContactAdapter(Context context) {
        super(context, R.layout.item_contact);
        this.contents = new ArrayList<Utils.Tuple>();
    }

    public void setOnContactRemoveListener(OnContactRemoveListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(getContext(), R.layout.item_contact, null);
            ViewHolder holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.item_contact_name);
            holder.listener = new ItemListener();
            view.findViewById(R.id.item_contact_remove).setOnClickListener(holder.listener);
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(contents.get(position).tail);
        holder.listener.setPosition(position);
        return view;
    }

    public void addItem(Utils.Tuple item) {
        boolean contained = false;
        for (Utils.Tuple content : contents) {
            if (content.head.equals(item.head)) {
                contained = true;
                break;
            }
        }
        if (!contained) {
            contents.add(item);
        }
        clear();
        addAll(contents);
        notifyDataSetChanged();
    }

    public List<Utils.Tuple> getContents() {
        return contents;
    }

    public ArrayList<String> getObjectIds() {
        ArrayList<String> list = new ArrayList<String>();
        for (Utils.Tuple content : contents) {
            list.add(content.head);
        }
        return list;
    }

    private static class ViewHolder {
        public TextView name;
        public ItemListener listener;
    }

    private class ItemListener implements View.OnClickListener {
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_contact_remove) {
                contents.remove(position);
                clear();
                addAll(contents);
                notifyDataSetChanged();
                listener.onRemove(contents.size());
            }
        }
    }

    public interface OnContactRemoveListener {
        public void onRemove(int size);
    }
}
