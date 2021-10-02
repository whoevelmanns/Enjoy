package de.whwk.enjoy.event;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import de.whwk.enjoy.databinding.FragmentEventBinding;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class EventFragment extends Fragment{
  private final String TAG = this.getClass().getName();

  private FragmentEventBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentEventBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void sendAndRequestResponse(String url, JSONObject jsonBody) throws JSONException {
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
      Log.i(TAG, response);
      try {
        JSONObject jsonObject = new JSONObject(response);
        ((EnjoyActivity) requireActivity()).setUser(jsonObject);
        Log.i(TAG, "Anmeldung erfolgt");
        NavHostFragment.findNavController(EventFragment.this)
                .navigate(R.id.action_LoginFragment_to_VotingFragment);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }, error -> Log.e(TAG, error.toString())) {
      @Override
      public String getBodyContentType() {
        return "application/json; charset=utf-8";
      }

      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public byte[] getBody() {
        if (jsonBody == null)
          return null;
        return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
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