package uk.ac.aston.dc2060.group5.reversi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uk.ac.aston.dc2060.group5.reversi.model.BoardTest;
import uk.ac.aston.dc2060.group5.reversi.model.MoveTest;
import uk.ac.aston.dc2060.group5.reversi.model.PieceTest;
import uk.ac.aston.dc2060.group5.reversi.model.TileTest;
import uk.ac.aston.dc2060.group5.reversi.players.PlayerTest;

/**
 * Unit test suite for Reversi.
 *
 * Created by Karan Thaker.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    BoardTest.class,
    PieceTest.class,
    TileTest.class,
    MoveTest.class,
    PlayerTest.class
})
public class AppTest {

  public AppTest() {

  }

}
