package uk.ac.aston.dc2060.group5.reversi.rulesets;

/**
 * Created by Sam on 09/10/2016.
 */
public class AntiReversi extends AbstractGame {

  public AntiReversi(GameType gameType) {
    super(gameType);
  }

  @Override
  public boolean playerTurn(int coordinate) {
    return false;
  }
}
