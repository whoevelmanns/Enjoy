package de.whwk.enjoy.login;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import de.whwk.enjoy.databinding.FragmentLoginBinding;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class LoginFragment extends Fragment {
  private final String TAG = this.getClass().getName();
  private FragmentLoginBinding binding;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentLoginBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.buttonOk.setOnClickListener(view1 -> {
      View p = view1.getRootView();
      String user = ((EditText) p.findViewById(R.id.user)).getText().toString();
      String password = ((EditText) p.findViewById(R.id.password)).getText().toString();
      login(user, password);
    });
  }

  private void login(String user, String password) {
    String url = "https://www.enjoy-gospel.de/wp-json/jwt-auth/v1/token";
    try {
      JSONObject jsonBody = new JSONObject();
      jsonBody.put("username", user);
      jsonBody.put("password", password);
      sendAndRequestResponse(url, jsonBody);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void sendAndRequestResponse(String url, JSONObject jsonBody) throws JSONException {
    RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
      Log.i(TAG, response);
      try {
        JSONObject jsonObject = new JSONObject(response);
        ((EnjoyActivity) requireActivity()).setUser(jsonObject);
        Log.i(TAG, "Anmeldung erfolgt");
        NavHostFragment.findNavController(LoginFragment.this)
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