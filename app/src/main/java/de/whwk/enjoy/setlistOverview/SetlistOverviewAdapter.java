package de.whwk.enjoy.setlistOverview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.whwk.enjoy.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetlistOverviewAdapter extends BaseAdapter {
  @SuppressWarnings("unused")
  private final String TAG = this.getClass().getName();
  private final SetlistOverviewFragment setlistsFragment;
  Context context;
  HashMap<Integer, SetlistOverviewModel> map;
  Map<Integer, View> myViews = new HashMap<>();

  public SetlistOverviewAdapter(SetlistOverviewFragment setlistsFragment, Context context, HashMap<Integer, SetlistOverviewModel> map) {
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
    return Objects.requireNonNull(map.get(position)).getId();
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder v;
    if (!myViews.containsKey(position)) {
      convertView = LayoutInflater.from(context).inflate(R.layout.setlist_overview_list_item, parent, false);
      v = new ViewHolder();
      v.title = convertView.findViewById(R.id.setlistTitel);
      convertView.setTag(v);
      myViews.put(position, convertView);
    } else {
      convertView = myViews.get(position);
      assert convertView != null;
      v = (ViewHolder) convertView.getTag();
    }
    SetlistOverviewModel model = map.get(position);
    assert model != null;
    v.title.setText(model.getTitel());
    return convertView;
  }

  static class ViewHolder {
    TextView title;
  }
}


