package de.whwk.enjoy.setlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.whwk.enjoy.EnjoyActivity;
import de.whwk.enjoy.R;
import de.whwk.enjoy.databinding.FragmentSetlistBinding;
import de.whwk.enjoy.setlistOverview.SetlistOverviewModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SetlistFragment extends Fragment {
  private final String TAG = this.getClass().getName();
  private FragmentSetlistBinding binding;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentSetlistBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getSetlist();
  }

  private void getSetlist() {
    SetlistOverviewModel setlist = ((EnjoyActivity) requireActivity()).getSetlist();
    HashMap<Integer, JSONObject> map = new HashMap<>();
    View p = requireView().getRootView();
    ((TextView)p.findViewById(R.id.setlistPageTitle)).setText(setlist.getTitel());
    for (int i = 0;i<setlist.getSongs().length();    i++){
      try {
        JSONObject song =setlist.getSongs().getJSONObject(i);
        map.put(i, song);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    SetlistAdapter adapter = new SetlistAdapter(this, requireView().getContext(), map);
    ListView listView = requireView().findViewById(R.id.listview_voting);
    listView.setAdapter(adapter);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}