package de.whwk.enjoy.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.whwk.enjoy.R;

import java.util.HashMap;

public class EventAdapter extends BaseAdapter {
  @SuppressWarnings("unused")
  private final String TAG = this.getClass().getName();
  Context context;
  HashMap<Integer, EventModel> map;

  public EventAdapter(Context context, HashMap<Integer, EventModel> map) {
    this.context = context;
    this.map = map;
  }

  @Override
  public int getCount() {
    return map.size();
  }

  @Override
  public Object getItem(int position) {
    return map.get(position);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.result_list_item, parent, false);
    }
    EventModel eventModel = map.get(position);
    assert eventModel != null;
    ((TextView) convertView.findViewById(R.id.voiceLabel)).setText(eventModel.getTitel());
    ProgressBar pb = convertView.findViewById(R.id.voiceStatus);
    pb.setMax(eventModel.getTotal());
    pb.setProgress(eventModel.getYes()+eventModel.getNo());
    pb.setSecondaryProgress(eventModel.getYes());
    return convertView;
  }
}


