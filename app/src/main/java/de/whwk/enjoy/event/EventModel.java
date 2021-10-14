package de.whwk.enjoy.event;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class EventModel {
  private final String voiceName;
  private final JSONObject voice;
  private int yes;
  private int no;
  private int total;

  public EventModel(String voiceName, JSONObject voice) {
    this.voice = voice;
    this.voiceName = voiceName;
    try {
      yes = voice.getInt("yes");
      no = voice.getInt("no");
      total = voice.getInt("total");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public int getUndefined() {
    return total - yes - no;
  }

  public String getMembers() {
    StringBuilder answer = new StringBuilder(voiceName + "\n\n");
    try {
      JSONObject users = voice.getJSONObject("user");
      JSONArray names = users.names();
      if (names != null) {
        for (int i = 0; i < names.length(); i++) {
          String name = names.getString(i);
          JSONObject user = users.getJSONObject(name);
          if ("1".equals(user.getString("status"))) {
            answer.append(name).append("\n");
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return answer.toString();
  }
}
