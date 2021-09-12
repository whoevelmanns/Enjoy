package de.whwk.enjoy.vote;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class VoteModel {
  private int eventId;
  private String titel;
  private String date;
  @Setter
  private Integer status;

  public VoteModel(JSONObject vote) {
    try {
      titel = vote.getString("titel");
      String startDate = "" + vote.getString("startDate");
      String endDate = "" + vote.getString("endDate");
      String startTime = "" + vote.getString("startTime");
      String endTime = "" + vote.getString("endTime");
      String lDate = startDate + " " + startTime;
      if (!startDate.equals(endDate) || !startTime.equals(endTime)) {
        lDate += " - ";
        if (!startDate.equals(endDate)) {
          lDate += endDate + " ";
        }
        lDate += endTime;
      }
      date = lDate;
      if (!vote.isNull("status")) {
        status = vote.getInt("status");
      }else {
        status=-1;
      }
      eventId = vote.getInt("event_id");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
