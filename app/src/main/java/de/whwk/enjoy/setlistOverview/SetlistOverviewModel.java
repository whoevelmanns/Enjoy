package de.whwk.enjoy.setlistOverview;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class SetlistOverviewModel {
  private String titel;
  private int id;
  private JSONArray songs;

  public SetlistOverviewModel(int pos,JSONObject setlist) {
    try {
      titel = setlist.getString("Name");
      songs= setlist.getJSONArray("Songs");
      id = pos;
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
