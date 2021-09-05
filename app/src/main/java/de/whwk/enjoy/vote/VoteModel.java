package de.whwk.enjoy.vote;

import org.json.JSONException;
import org.json.JSONObject;

public class VoteModel {
  private final JSONObject vote;

  public VoteModel(JSONObject vote) {
    this.vote=vote;
  }

  public String getTitel() {
    try {
      return vote.getString("titel");
    } catch (JSONException e) {
      e.printStackTrace();
      return "?Event";
    }
  }

  public String getDate() {
    try {
      String startDate = "" + vote.getString("startDate");
      String endDate = "" + vote.getString("endDate");
      String startTime = "" + vote.getString("startTime");
      String endTime = ""+ vote.getString("endTime");
      String ret = startDate +" "+ startTime;
      if (!startDate.equals(endDate) || !startTime.equals(endTime)) {
        ret +=" - ";
        if (!startDate.equals(endDate)){
          ret+=endDate + " ";
        }
        ret += endTime;
      }
      return ret;
    } catch (JSONException e) {
      e.printStackTrace();
      return "?Date";
    }
  }

  public Integer getStatus() {
    try {
      return vote.getInt("status");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer getEventId() {
    try {
      return vote.getInt("event_id");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }
}
