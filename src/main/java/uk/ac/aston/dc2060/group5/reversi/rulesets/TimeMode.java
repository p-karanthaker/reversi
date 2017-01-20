package uk.ac.aston.dc2060.group5.reversi.rulesets;

/**
 * Models the different time modes available.
 *
 * <p>Created by Karan Thaker.</p>
 */
public enum TimeMode {
  /**
   * Blitz Mode. Players have 30 seconds each.
   */
  BLITZ("Blitz (30 Seconds)", 30),

  /**
   * Normal Mode. Players have 600 seconds each.
   */
  NORMAL("Normal (10 Minutes)", 600),

  /**
   * Competition Mode. Players have 1800 seconds each.
   */
  COMPETITION("Competition (30 Minutes)", 1800);

  /**
   * The description of the time mode.
   */
  private String description;

  /**
   * The length of time per player for the time mode.
   */
  private int timeModeLengthInSeconds;

  /**
   * Constructs a TimeMode.
   *
   * @param description             the description to give the time mode.
   * @param timeModeLengthInSeconds the lenth of time in seconds per player for the time mode.
   */
  TimeMode(String description, int timeModeLengthInSeconds) {
    this.description = description;
    this.timeModeLengthInSeconds = timeModeLengthInSeconds;
  }

  /**
   * Get the description of the time mode.
   *
   * @return the description of the time mode.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Get the amount of time each player has in the time mode.
   *
   * @return the amount of time each player has in the time mode.
   */
  public int getTimeModeLengthInSeconds() {
    return this.timeModeLengthInSeconds;
  }
}
