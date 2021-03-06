package de.whwk.enjoy.vote;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.whwk.enjoy.EnjoyActivity;
import de.whwk.enjoy.R;
import de.whwk.enjoy.databinding.FragmentVotingBinding;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
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
    getVotings();
  }

  private void getVotings() {
    JSONObject user = ((EnjoyActivity) requireActivity()).getUser();
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.enjoy-gospel.de/wp-json/enjoy/v1/votes/user", response -> {
      try {
        JSONArray votes = new JSONArray(response);
        Log.v(TAG, votes.toString());
          HashMap<Integer, VoteModel> map = new HashMap<>();
          for (int i = 0; i < votes.length(); i++) {
            JSONObject vote = votes.getJSONObject(i);
            map.put(i,new VoteModel(vote));
          }
          VoteAdapter adapter = new VoteAdapter(this, requireView().getContext(), map);
          ListView listView = requireView().findViewById(R.id.listview_voting);
          listView.setAdapter(adapter);
          listView.setLongClickable(true);
          listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            VoteModel vote = map.get(position);
            assert vote != null;
            ((EnjoyActivity) requireActivity()).setEventId(vote.getEventId());
            NavHostFragment.findNavController(VotingFragment.this)
                    .navigate(R.id.action_VotingFragment_to_EventFragment);
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

  public void vote(Integer event_id, int status) {
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.PATCH, "https://www.enjoy-gospel.de/wp-json/enjoy/v1/vote/event/" + event_id, response -> {
      try {
        JSONObject vote = new JSONObject(response);
        Log.v(TAG, vote.toString());
        if (vote.getBoolean("changed")) {
          Toast.makeText(requireView().getContext(), "Voting abgeschickt", Toast.LENGTH_SHORT).show();
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }, error -> Log.e(TAG, error.toString())) {
      @Override
      public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        try {
          headers.put("Content-Type", "application/json; charset=UTF-8");
          headers.put("Authorization", "Bearer " + ((EnjoyActivity) requireActivity()).getUser().getString("token"));
          Log.v(TAG, headers.toString());
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        return headers;
      }

      @SneakyThrows
      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public byte[] getBody() {
        JSONObject body = new JSONObject();
        body.put("status", status);
        return body.toString().getBytes(StandardCharsets.UTF_8);
      }
    };
    mRequestQueue.add(stringRequest);

  }
}