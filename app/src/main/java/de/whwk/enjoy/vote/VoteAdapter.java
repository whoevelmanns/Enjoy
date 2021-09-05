package de.whwk.enjoy.vote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.whwk.enjoy.R;

import java.util.ArrayList;

public class VoteAdapter extends BaseAdapter {
  Context context;
  ArrayList<VoteModel> arrayList;

  public VoteAdapter(Context context, ArrayList<VoteModel> arrayList) {
    this.context = context;
    this.arrayList = arrayList;
  }

  @Override
  public int getCount() {
    return arrayList.size();
  }

  @Override
  public Object getItem(int position) {
    return arrayList.get(position);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public  View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView ==  null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
    }
    ((TextView) convertView.findViewById(R.id.event)).setText(arrayList.get(position).getEvent());
    ((TextView) convertView.findViewById(R.id.date)).setText(arrayList.get(position).getDate());
    return convertView;
  }
}


