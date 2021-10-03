package de.whwk.enjoy;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import de.whwk.enjoy.databinding.ActivityMainBinding;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
@Setter
public class EnjoyActivity extends AppCompatActivity {
  private AppBarConfiguration appBarConfiguration;
  private JSONObject user;
  private int eventId;

  public static String getDate(JSONObject event) {
    try {
      String startDate = "" + event.getString("startDate");
      String endDate = "" + event.getString("endDate");
      String startTime = "" + event.getString("startTime");
      String endTime = "" + event.getString("endTime");
      String lDate = startDate + " " + startTime;
      if (!startDate.equals(endDate) || !startTime.equals(endTime)) {
        lDate += " - ";
        if (!startDate.equals(endDate)) {
          lDate += endDate + " ";
        }
        lDate += endTime;
      }
      return lDate;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return "Fehler in Datumsermittlung";
  }

  @SuppressLint("SourceLockedOrientationActivity")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    de.whwk.enjoy.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so Fng
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
      navController.navigate(R.id.to_SettingsFragment);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
            || super.onSupportNavigateUp();
  }
}