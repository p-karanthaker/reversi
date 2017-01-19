package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.gui.MainMenu;

import java.io.IOException;

import javax.swing.UIManager;

/**
 * Hello world.
 */
public class App {

  /**
   * Application entry point.
   *
   * @param args command line args.
   */
  public static void main(String[] args) throws IOException {
    try {
      UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
    } catch (Exception e) {
      e.printStackTrace();
    }
    new MainMenu();
  }
}
