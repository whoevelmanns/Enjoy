package de.whwk.enjoy.vote;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.whwk.enjoy.EnjoyActivity;
import de.whwk.enjoy.R;
import de.whwk.enjoy.databinding.FragmentVotingBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotingFragment extends Fragment {
  private final String TAG = this.getClass().getName();
  private FragmentVotingBinding binding;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentVotingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getVotings(((EnjoyActivity) requireActivity()).getUser());
  }

  private void getVotings(JSONObject user) {
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.enjoy-gospel.de/wp-json/enjoy/v1/votes/user", response -> {
      try {
        JSONArray votes = new JSONArray(response);
        Log.i(TAG, votes.toString());
        ArrayList<VoteModel> list = new ArrayList<>();
        for (int i = 0; i < votes.length(); i++) {
          JSONObject vote = votes.getJSONObject(i);
          list.add(new VoteModel(vote));
        }
        VoteAdapter adapter = new VoteAdapter(this,requireView().getContext(), list);
        ListView listView = requireView().findViewById(R.id.listview_voting);
        listView.setAdapter(adapter);
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
          Log.d(TAG, headers.toString());
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

  public void vote(Integer event_id, int status) {
    Toast.makeText(getView().getContext(),"Vote "+event_id + "=>" + status,Toast.LENGTH_SHORT).show();
  }
}