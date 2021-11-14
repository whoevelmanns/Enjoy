package de.whwk.enjoy.setlistOverview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.whwk.enjoy.EnjoyActivity;
import de.whwk.enjoy.R;
import de.whwk.enjoy.databinding.FragmentSetlistOverviewBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetlistOverviewFragment extends Fragment {
  private final String TAG = this.getClass().getName();
  private FragmentSetlistOverviewBinding binding;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentSetlistOverviewBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getSetlists();
  }

  private void getSetlists() {
    JSONObject user = ((EnjoyActivity) requireActivity()).getUser();
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.enjoy-gospel.de/wp-json/enjoy/v1/setlists", response -> {
      try {
        JSONArray setlists = new JSONArray(response);
        Log.v(TAG, setlists.toString());
          HashMap<Integer, SetlistOverviewModel> map = new HashMap<>();
          for (int i = 0; i < setlists.length(); i++) {
            JSONObject list = setlists.getJSONObject(i);
            map.put(i,new SetlistOverviewModel(i,list));
          }
          SetlistOverviewAdapter adapter = new SetlistOverviewAdapter(this, requireView().getContext(), map);
          ListView listView = requireView().findViewById(R.id.listview_voting);
          listView.setAdapter(adapter);
          listView.setLongClickable(true);
          listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            SetlistOverviewModel setlist = map.get(position);
            assert setlist != null;
            ((EnjoyActivity) requireActivity()).setSetlist(setlist);
            NavHostFragment.findNavController(SetlistOverviewFragment.this)
                    .navigate(R.id.to_setlistFragment);
            return true;
          });
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }, error -> Log.e(TAG, error.toString())) {
      @Override
      public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        try {
          headers.put("Content-Type", "application/json; charset=UTF-8");
          headers.put("Authorization", "Bearer " + user.getString("token"));
          Log.v(TAG, headers.toString());
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        return headers;
      }
    };
    mRequestQueue.add(stringRequest);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}