package uk.ac.aston.dc2060.group5.reversi.rulesets;

/**
 * Models the object representation of the rules.json file.
 *
 * <p>Created by Karan Thaker.</p>
 */
public class Rules {

  private String title;

  private String text;

  private String image;

  public Rules() {
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

}
