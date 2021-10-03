package de.whwk.enjoy.event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.whwk.enjoy.EnjoyActivity;
import de.whwk.enjoy.R;
import de.whwk.enjoy.databinding.FragmentEventBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventFragment extends Fragment {
  private final String TAG = this.getClass().getName();
  private FragmentEventBinding binding;
  private JSONObject user;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentEventBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (savedInstanceState == null) {
      user = ((EnjoyActivity) requireActivity()).getUser();
      int eventId = ((EnjoyActivity) requireActivity()).getEventId();
      getEvent(eventId);
    }
  }

  private void getEvent(int eventId) {
    if (user == null) return;
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.enjoy-gospel.de/wp-json/enjoy/v1/votes/event/" + eventId, response -> {
      try {
        JSONObject data = new JSONObject(response);
        Log.v(TAG, data.toString());
        HashMap<Integer, EventModel> map = new HashMap<>();
        JSONObject event = data.getJSONObject("event");
        ((TextView) requireView().findViewById(R.id.eventTitel)).setText(event.getString("titel"));
        ((TextView) requireView().findViewById(R.id.eventDate)).setText(EnjoyActivity.getDate(event));
        JSONObject voting = data.getJSONObject("voting");
        JSONArray names = voting.names();
        if (names != null) {
          for (int i = 0; i < names.length(); i++) {
            String titel = names.getString(i);
            map.put(i, new EventModel(titel, voting.getJSONObject(titel)));
          }
        }
        EventAdapter adapter = new EventAdapter(requireView().getContext(), map);
        ListView listView = requireView().findViewById(R.id.listview_events);
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
}