package de.whwk.enjoy.setlist;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class SetlistModel {
  private String titel;
  private Long id;

  public SetlistModel(JSONObject setlist) {
    try {
      titel = setlist.getString("titel");
      id = setlist.getLong("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
