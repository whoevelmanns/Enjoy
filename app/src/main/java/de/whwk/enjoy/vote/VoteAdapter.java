package de.whwk.enjoy.vote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import de.whwk.enjoy.R;

import java.util.ArrayList;

public class VoteAdapter extends BaseAdapter {
  @SuppressWarnings("unused")
  private final String TAG = this.getClass().getName();
  private final VotingFragment votingFragment;
  Context context;
  ArrayList<VoteModel> arrayList;

  public VoteAdapter(VotingFragment votingFragment, Context context, ArrayList<VoteModel> list) {
    this.votingFragment = votingFragment;
    this.context = context;
    this.arrayList = list;
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
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
    }
    VoteModel voteModel = arrayList.get(position);
    ((TextView) convertView.findViewById(R.id.event)).setText(voteModel.getTitel());
    ((TextView) convertView.findViewById(R.id.date)).setText(voteModel.getDate());
    TextView tv = convertView.findViewById(R.id.location);
    String t = voteModel.getLocation();
    if (t == null) {
      tv.setVisibility(View.GONE);
    } else {
      tv.setText(t);
    }
    Integer status = voteModel.getStatus();
    Integer event_id = voteModel.getEventId();
    RadioButton btn;
    btn = convertView.findViewById(R.id.rbtn_0);
    if (status != null && status.equals(0)) {
      btn.setChecked(true);
    }
    btn.setOnCheckedChangeListener((compoundButton, b) -> {
      if (b) {
        voteModel.setStatus(0);
        votingFragment.vote(event_id, 0);
      }
    });
    btn = convertView.findViewById(R.id.rbtn_1);
    if (status != null && status.equals(1)) {
      btn.setChecked(true);
    }
    btn.setOnCheckedChangeListener((compoundButton, b) -> {
      if (b) {
        voteModel.setStatus(1);
        votingFragment.vote(event_id, 1);
      }
    });
    return convertView;
  }
}


