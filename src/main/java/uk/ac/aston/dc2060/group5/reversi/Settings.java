package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.model.Piece;

/**
 * Created by Sam on 09/10/2016.
 */
public class Settings {

  private String name;

  private String bgColour;

  private String bgImage;

  private Opacity[] opacity;

  private PieceColours[] pieceColours;

  private PieceColours[] pieceNames;

  public void setName(String name) {
    this.name = name;
  }

  public void setBgColour(String bgColour) {
    this.bgColour = bgColour;
  }

  public void setBgImage(String bgImage) {
    this.bgImage = bgImage;
  }

  public void setOpacity(Opacity[] opacity) {
    this.opacity = opacity;
  }

  public void setPieceColours(PieceColours[] pieceColours) {
    this.pieceColours = pieceColours;
  }

  public String getName() {
    return this.name;
  }

  public String getBgColour() {
    return this.bgColour;
  }

  public String getBgImage() {
    return this.bgImage;
  }

  public Opacity[] getOpacity() {
    return this.opacity;
  }

  public PieceColours[] getPieceColours() {
    return this.pieceColours;
  }

  public PieceColours[] getPieceNames() {
    return this.pieceNames;
  }

  public class Opacity {

    private int light;

    private int dark;

    public void setLight(int light) {
      this.light = light;
    }

    public void setDark(int dark) {
      this.dark = dark;
    }

    public int getLight() {
      return this.light;
    }

    public int getDark() {
      return this.dark;
    }
  }

  public class PieceColours {

    private String black;

    private String white;

    public void setBlack(String black) {
      this.black = black;
    }

    public void setWhite(String white) {
      this.white = white;
    }

    public String getBlack() {
      return this.black;
    }

    public String getWhite() {
      return this.white;
    }
  }

}
