package uk.ac.aston.dc2060.group5.reversi.model;

/**
 * Created by Sam on 09/10/2016.
 * Worked on by Dean 
 */
public class Piece {
	
	enum PieceColor {BLACK, WHITE}
	
	PieceColor BLACK = PieceColor.BLACK;
	PieceColor WHITE = PieceColor.WHITE;
	int pieceCoord;
	PieceColor pieceColor;
	
	public Piece(PieceColor color, int coordinate) {
		// TODO Auto-generated constructor stub
		this.pieceCoord = coordinate;
		if(color == BLACK) {
			this.pieceColor = getBLACK();
		} else {
			this.pieceColor = getWHITE();
		}
	}
	
	public PieceColor getBLACK() {
		return BLACK;
	}
	
	public PieceColor getWHITE() {
		return WHITE;
	}
	
	public PieceColor getPieceColor() {
		return pieceColor;
	}
	
	public int getPieceCoord() {
		return pieceCoord;
	}
	
	protected void flipPiece() {
		if(pieceColor == BLACK) {
			this.pieceColor = getWHITE();
		} else if (pieceColor == WHITE) {
			this.pieceColor = getBLACK();
		}
	}
}
