package de.whwk.enjoy.vote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import de.whwk.enjoy.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VoteAdapter extends BaseAdapter {
  @SuppressWarnings("unused")
  private final String TAG = this.getClass().getName();
  private final VotingFragment votingFragment;
  Context context;
  HashMap<Integer, VoteModel> map;
  Map<Integer, View> myViews = new HashMap<>();


  public VoteAdapter(VotingFragment votingFragment, Context context, HashMap<Integer, VoteModel> map) {
    this.votingFragment = votingFragment;
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
    return Objects.requireNonNull(map.get(position)).getEventId();
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder v;
    //TODO Hier muss ich nochmal ran
    if (true || !myViews.containsKey(position)) {
      convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
      v = new ViewHolder();
      v.title = convertView.findViewById(R.id.event);
      v.date = convertView.findViewById(R.id.date);
      v.location = convertView.findViewById(R.id.location);
      v.statusGrp = convertView.findViewById(R.id.status);
      v.btnNo = convertView.findViewById(R.id.rbtn_0);
      v.btnYes = convertView.findViewById(R.id.rbtn_1);
      v.statusGrp.setOnCheckedChangeListener((radioGroup, id) -> {
        int newStatus = -1;
        switch (id) {
          case R.id.rbtn_0:
            newStatus = 0;
            break;
          case R.id.rbtn_1:
            newStatus = 1;
            break;
        }
        if (newStatus >= 0) {
          VoteModel voteModel = map.get(position);
          assert voteModel != null;
          voteModel.setStatus(newStatus);
          votingFragment.vote(voteModel.getEventId(), newStatus);
        }
      });
      convertView.setTag(v);
      myViews.put(position, convertView);
    } else {
      convertView = myViews.get(position);
      assert convertView != null;
      v = (ViewHolder) convertView.getTag();
    }
    VoteModel voteModel = map.get(position);
    assert voteModel != null;
    v.title.setText(voteModel.getTitel());
    v.date.setText(voteModel.getDate());
    String t = voteModel.getLocation();
    if (t == null) {
      v.location.setVisibility(View.GONE);
    } else {
      v.location.setText(t);
      v.location.setVisibility(View.VISIBLE);
    }
    v.statusGrp.clearCheck();
    Integer status = voteModel.getStatus();
    if (status != null) {
      switch (status) {
        case 0:
          v.btnNo.setChecked(true);
          break;
        case 1:
          v.btnYes.setChecked(true);
          break;
      }
    }
    return convertView;
  }

  static class ViewHolder {
    RadioButton btnNo;
    RadioButton btnYes;
    TextView title, date, location;
    RadioGroup statusGrp;
  }
}


