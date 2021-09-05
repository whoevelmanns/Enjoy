package de.whwk.enjoy.vote;

import org.json.JSONException;
import org.json.JSONObject;

public class VoteModel {
  private final JSONObject vote;

  public VoteModel(JSONObject vote) {
    this.vote=vote;
  }

  public String getEvent() {
    try {
      return vote.getString("titel");
    } catch (JSONException e) {
      e.printStackTrace();
      return "?Event";
    }
  }

  public String getDate() {
    try {
      return vote.getString("startDate");
    } catch (JSONException e) {
      e.printStackTrace();
      return "?Date";
    }
  }
}
