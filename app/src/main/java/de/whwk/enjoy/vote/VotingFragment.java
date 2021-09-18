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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotingFragment extends Fragment {
  private final String TAG = this.getClass().getName();
  private FragmentVotingBinding binding;
  private JSONObject user;

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
    if (savedInstanceState == null) {
      user = ((EnjoyActivity) requireActivity()).getUser();
      getVotings();
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (user != null) {
      outState.putString("user", user.toString());
    }
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      try {
        String t = savedInstanceState.getString("user");
        if (t != null) {
          user = new JSONObject(t);
          getVotings();
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private void getVotings() {
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.enjoy-gospel.de/wp-json/enjoy/v1/votes/user", response -> {
      try {
        JSONArray votes = new JSONArray(response);
        Log.v(TAG, votes.toString());
        ArrayList<VoteModel> list = new ArrayList<>();
        for (int i = 0; i < votes.length(); i++) {
          JSONObject vote = votes.getJSONObject(i);
          list.add(new VoteModel(vote));
        }
        VoteAdapter adapter = new VoteAdapter(this, requireView().getContext(), list);
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
          JSONObject user = ((EnjoyActivity) requireActivity()).getUser();
          headers.put("Content-Type", "application/json; charset=UTF-8");
          headers.put("Authorization", "Bearer " + user.getString("token"));
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