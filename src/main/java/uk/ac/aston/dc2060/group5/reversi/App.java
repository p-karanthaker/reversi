package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.gui.MainMenu;

import java.io.IOException;

import javax.swing.UIManager;

/**
 * The main class for the Reversi application.
 */
public class App {

  /**
   * Application entry point. Sets the look and feel to be cross platform so custom UI elements are
   * supported on different operating systems.
   *
   * @param args command line args.
   */
  public static void main(String[] args) throws IOException {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    new MainMenu();
  }
}
