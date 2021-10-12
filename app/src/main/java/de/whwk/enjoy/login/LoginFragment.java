package de.whwk.enjoy.login;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import com.android.volley.AuthFailureError;
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
  private static final String PASSWORD = "user_password";
  private static final String LOGIN = "user_name";
  private static final String SAVE_PASSWORD = "save_passwd";
  private final String TAG = this.getClass().getName();
  private FragmentLoginBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentLoginBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(requireContext());
    View p = requireView().getRootView();
    ((EditText) p.findViewById(R.id.user)).setText(settings.getString(LOGIN, ""));
    if (settings.getBoolean(SAVE_PASSWORD, false)) {
      ((EditText) p.findViewById(R.id.password)).setText(settings.getString(PASSWORD, ""));
    } else {
      ((EditText) p.findViewById(R.id.password)).setText("");
    }
    binding.buttonOk.setOnClickListener(view1 -> {
      String user = ((EditText) p.findViewById(R.id.user)).getText().toString();
      String password = ((EditText) p.findViewById(R.id.password)).getText().toString();
      if (!settings.contains(SAVE_PASSWORD)) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Soll das Passwort gespeichert werden?")
                .setMessage("Das kann in den Einstellungen jederzeit geändert werden")
                .setPositiveButton("Ja", (dialog, which) -> {
                  savePassword(settings, user, password, true);
                  dialog.dismiss();
                })
                .setNegativeButton("Nein", (dialog, which) -> {
                  savePassword(settings, user, password, false);
                  dialog.dismiss();
                })
                .show();

      } else {
        savePassword(settings, user, password, null);
      }
      login(user, password);
    });
    binding.buttonLostPasswort.setOnClickListener(view -> {
      try {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://enjoy-gospel.de/lostpassword"));
        startActivity(myIntent);
      } catch (ActivityNotFoundException e) {
        Toast.makeText(requireContext(), "Keine Anwendung gefunden, die auf https://enjoy-gospel.de/lostpassword zugreifen kann", Toast.LENGTH_LONG).show();
        e.printStackTrace();
      }
    });
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void savePassword(SharedPreferences settings, String login, String password, Boolean savePassword) {
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(LOGIN, login);
    if (savePassword != null) {
      editor.putBoolean(SAVE_PASSWORD, savePassword);
    } else {
      savePassword = settings.getBoolean(SAVE_PASSWORD, false);
    }
    if (savePassword) {
      editor.putString(PASSWORD, password);
    } else {
      editor.remove(PASSWORD);
    }
    editor.apply();
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
    }, error -> {
      Log.e(TAG, error.toString());
      if (error instanceof AuthFailureError) {
        Toast.makeText(requireContext(), "Benutzer oder Passwort falsch", Toast.LENGTH_LONG).show();
      }
    }) {
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