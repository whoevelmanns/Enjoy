package de.whwk.enjoy.setlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.whwk.enjoy.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetlistAdapter extends BaseAdapter {
  @SuppressWarnings("unused")
  private final String TAG = this.getClass().getName();
  private final SetlistFragment setlistsFragment;
  Context context;
  HashMap<Integer, JSONObject> map;
  Map<Integer, View> myViews = new HashMap<>();


  public SetlistAdapter(SetlistFragment setlistsFragment, Context context, HashMap<Integer, JSONObject> map) {
    this.setlistsFragment = setlistsFragment;
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
  public long getItemId(int position) {
    return position;
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder v;
    if (!myViews.containsKey(position)) {
      convertView = LayoutInflater.from(context).inflate(R.layout.setlist_list_item, parent, false);
      v = new ViewHolder();
      v.title = convertView.findViewById(R.id.songTitle);
      v.number = convertView.findViewById(R.id.songNumber);
      convertView.setTag(v);
      myViews.put(position, convertView);
    } else {
      convertView = myViews.get(position);
      assert convertView != null;
      v = (ViewHolder) convertView.getTag();
    }
    JSONObject song = map.get(position);
    assert song != null;
    try {
      v.number.setText(song.getString("Nummer"));
      v.title.setText(song.getString("Titel"));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return convertView;
  }

  static class ViewHolder {
    TextView number;
    TextView title;
  }
}


