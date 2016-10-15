package uk.ac.aston.dc2060.group5.reversi.model;

/**
 * Created by Sam on 09/10/2016.
 * Worked on by Dean 
 */
public class Piece {


	private PieceColor pieceColor;
	
	public Piece(PieceColor color) {
    this.pieceColor = color;
	}
	
	public PieceColor getPieceColor() {
		return pieceColor;
	}
	
	protected void flipPiece() {
		if(pieceColor == PieceColor.BLACK) {
			this.pieceColor = PieceColor.WHITE;
		} else {
			this.pieceColor = PieceColor.BLACK;
		}
	}

	@Override
  public String toString() {
    return this.pieceColor.shortName;
  }

  public enum PieceColor {
    BLACK("b"),
    WHITE("w");

    private String shortName;

    PieceColor(String shortName) {
      this.shortName = shortName;
    }

    public String getShortName() {
      return this.shortName;
    }
  }

}
