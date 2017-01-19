package uk.ac.aston.dc2060.group5.reversi.rulesets;

/**
 * Created by Karan on 19/01/2017.
 */
public enum TimeMode {
  BLITZ("Blitz (30 Seconds)", 30),
  NORMAL("Normal (10 Minutes)", 600),
  COMPETITION("Competition (30 Minutes)", 1800);

  private String description;
  private int timeModeLengthInSeconds;

  TimeMode(String description, int timeModeLengthInSeconds) {
    this.description = description;
    this.timeModeLengthInSeconds = timeModeLengthInSeconds;
  }

  public String getDescription() {
    return this.description;
  }

  public int getTimeModeLengthInSeconds() {
    return this.timeModeLengthInSeconds;
  }
}
