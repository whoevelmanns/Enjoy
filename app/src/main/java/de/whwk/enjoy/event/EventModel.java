package de.whwk.enjoy.event;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class EventModel {
  private int yes;
  private int no;
  private int total;
  private final String voiceName;
  private final JSONObject voice;

  public EventModel(String voiceName, JSONObject voice) {
    this.voice = voice;
    this.voiceName=voiceName;
    try {
      yes = voice.getInt("yes");
      no = voice.getInt("no");
      total = voice.getInt("total");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public String getTitel() {
    return voiceName + " Ja:" + yes + " Nein:" + no + " ?" + (total - yes - no);
  }
}
