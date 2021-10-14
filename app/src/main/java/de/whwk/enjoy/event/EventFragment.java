package de.whwk.enjoy.event;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import de.whwk.enjoy.EnjoyActivity;
import de.whwk.enjoy.R;
import de.whwk.enjoy.databinding.FragmentEventBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.*;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener((adapterView, view, position, id) -> EventLongClicked(Objects.requireNonNull(map.get(position))));
        PieChart chart = requireView().findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();
        for (Integer  index : map.keySet()) {
          EventModel voice = map.get(index);
          assert voice != null;
          entries.add(new PieEntry(voice.getYes(),voice.getVoiceName()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Label"); // add entries to dataset
        dataSet.addColor(Color.LTGRAY);
        dataSet.addColor(Color.RED);
        dataSet.addColor(Color.GREEN);
        dataSet.addColor(Color.BLUE);
        dataSet.addColor(Color.YELLOW);
        dataSet.setValueTextSize(16);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(new ValueFormatter() {
          @Override
          public String getFormattedValue(float value) {
            DecimalFormat df= new DecimalFormat("#");
            return df.format(value);
          }
        });
        dataSet.setLabel("");
        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.setDescription(null);
        chart.invalidate();
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

  @SuppressLint("ClickableViewAccessibility")
  private boolean EventLongClicked(EventModel event) {
    @SuppressLint("InflateParams")
    View view = ((LayoutInflater) requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup,null);
    ((TextView) view.findViewById(R.id.members)).setText(event.getMembers());
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(view, width, height, focusable);
    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0);
    // dismiss the popup window when touched
    view.setOnTouchListener((v, event1) -> {
      popupWindow.dismiss();
      return true;
    });
    return true;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}