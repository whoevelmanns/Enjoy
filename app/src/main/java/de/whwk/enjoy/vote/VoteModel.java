package de.whwk.enjoy.vote;

import de.whwk.enjoy.EnjoyActivity;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class VoteModel {
  private int eventId;
  private String titel;
  private String date;
  private String location;
  @Setter
  private Integer status;

  public VoteModel(JSONObject vote) {
    try {
      titel = vote.getString("titel");
      if (!"null".equals(vote.getString("location"))) {
        location =vote.getString("location")+"\n"+vote.getString("adress")+"\n"+vote.getString("zip")+ " " + vote.getString("city");
      }
      date = EnjoyActivity.getDate(vote);
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
