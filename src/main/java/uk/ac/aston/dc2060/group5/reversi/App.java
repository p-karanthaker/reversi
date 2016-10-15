package uk.ac.aston.dc2060.group5.reversi;

import uk.ac.aston.dc2060.group5.reversi.gui.BoardUI;
import uk.ac.aston.dc2060.group5.reversi.model.Board;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    System.out.println("Hello World!");
    System.out.println("Git Test - Sam :)");
    System.out.println("Test Maryam !!");
    new BoardUI();

    Board board = new Board();
    System.out.print(board.toString());
  }
}
