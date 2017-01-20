package uk.ac.aston.dc2060.group5.reversi;

/**
 * Models the object representation of the config.json file.
 *
 * <p>Created by Karan Thaker.</p>
 */
public class Settings {

  private String name;

  private String bgColour;

  private String bgImage;

  private Opacity[] opacity;

  private PieceColours[] pieceColours;

  private PieceColours[] pieceNames;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBgColour() {
    return this.bgColour;
  }

  public void setBgColour(String bgColour) {
    this.bgColour = bgColour;
  }

  public String getBgImage() {
    return this.bgImage;
  }

  public void setBgImage(String bgImage) {
    this.bgImage = bgImage;
  }

  public Opacity[] getOpacity() {
    return this.opacity;
  }

  public void setOpacity(Opacity[] opacity) {
    this.opacity = opacity;
  }

  public PieceColours[] getPieceColours() {
    return this.pieceColours;
  }

  public void setPieceColours(PieceColours[] pieceColours) {
    this.pieceColours = pieceColours;
  }

  public PieceColours[] getPieceNames() {
    return this.pieceNames;
  }

  public class Opacity {

    private int light;

    private int dark;

    public int getLight() {
      return this.light;
    }

    public void setLight(int light) {
      this.light = light;
    }

    public int getDark() {
      return this.dark;
    }

    public void setDark(int dark) {
      this.dark = dark;
    }
  }

  public class PieceColours {

    private String black;

    private String white;

    public String getBlack() {
      return this.black;
    }

    public void setBlack(String black) {
      this.black = black;
    }

    public String getWhite() {
      return this.white;
    }

    public void setWhite(String white) {
      this.white = white;
    }
  }

}
