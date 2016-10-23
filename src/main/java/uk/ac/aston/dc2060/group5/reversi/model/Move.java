package uk.ac.aston.dc2060.group5.reversi.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Sam on 19/10/2016.
 */
public class Move {

    public boolean makeMove(Board board, int row, int col, Piece pieceToPlace) {
        // boolean variable to return at end of method, set to true if move is valid
        boolean moveMade = false;

        // Get the tile from the coordinates.
        AbstractTile moveTile = board.getTile(row, col);

        // Check if the tile is vacant, return false if not.
        if (!moveTile.isVacant()) {
            return false;
        }

        // Create a list of the pieces in the surrounding tiles that are of the opposite colour
        List<Piece> surroundingPieces = getOppositePieces(getSurroundingTiles(row, col, board), pieceToPlace);

        // Return false if there are no surrounding pieces of the opposite colour
        if (surroundingPieces.isEmpty()) {
            return false;
        }

        for (Piece p : surroundingPieces) {
            Point coords = board.translateIndexToPoint(p.getTileId());

            if (coords.y == row-1) {
                // Piece is in row above
                if (coords.x == col-1) {
                    // Piece is diagonal left up
                }
                else if (coords.x == col) {
                    // Piece is vertical up
                }
                else if (coords.x == col+1) {
                    // Piece is diagonal right up
                }
            }
            else if (coords.y == row) {
                // Piece is in same row
                if (coords.x == col-1) {
                    // Piece is horizontal left
                }
                else if (coords.x == col+1) {
                    // Piece is horizontal right
                }
            }
            else if (coords.y == row+1) {
                // Piece is in row below
                if (coords.x == col-1) {
                    // Piece is diagonal left down
                }


                else if (coords.x == col) {
                    // Piece is vertical down
                    boolean validMove = true;
                    boolean foundSameColour = false;
                    int finishRow = 0;

                    for (int i = coords.y+2; i < 8; i++) {
                        if (!foundSameColour) {
                            if (!validMove) {
                                // Move is invalid in this direction so break out of loop
                                break;
                            }
                            else {
                                if (board.getTile(i, col).isVacant()) {
                                    // Tile is vacant, move is invalid
                                    validMove = false;
                                }
                                else if (!board.getTile(i, col).isVacant()) {
                                    // This tile is not empty
                                    if (!board.getTile(i, col).getPiece().getPieceColour().equals(pieceToPlace.getPieceColour())) {
                                        // Piece is opposite colour
                                        // Still valid so far!
                                        validMove = true;
                                    }
                                    else {
                                        // Found piece of same colour, move is definitely valid!
                                        foundSameColour = true;
                                        finishRow = i;
                                    }
                                }
                            }
                        }
                    }
                    if (validMove && finishRow != 0) {
                        // Move is valid and we should flip some pieces!
                        for (int i = coords.y+1; i < finishRow; i++) {
                            board.getTile(i, col).getPiece().flipPiece();
                        }

                        moveMade = true;
                    }
                }



                else if (coords.x == col+1) {
                    // Piece is diagonal right down
                }
            }
        }


        return moveMade;
    }

    /**
     * Creates a list containing all of the surrounding tiles of the coordinates passed in.
     * @param row the row coordinate of the tile
     * @param col the column coordinate of the tile
     * @param board the board
     * @return a Point object with Point.x as the column, and Point.y as the row of the array.
     */
    private List<AbstractTile> getSurroundingTiles(int row, int col, Board board) {
        // Get all surrounding tiles and put them into a list.
        List<AbstractTile> surroundingTiles = new ArrayList<>();

        if (row == 0) {
            if (col == 0) {
                // Piece is top left corner
                surroundingTiles.add(board.getTile(row, col+1));
                surroundingTiles.add(board.getTile(row+1, col));
                surroundingTiles.add(board.getTile(row+1, col+1));
            }
            if (col == 7) {
                // Piece is top right corner
                surroundingTiles.add(board.getTile(row, col-1));
                surroundingTiles.add(board.getTile(row+1, col));
                surroundingTiles.add(board.getTile(row+1, col-1));
            }
        }
        else if (row == 7) {
            if (col == 0) {
                // Piece is bottom left corner
                surroundingTiles.add(board.getTile(row-1, col));
                surroundingTiles.add(board.getTile(row-1, col+1));
                surroundingTiles.add(board.getTile(row, col+1));
            }
            if (col == 7) {
                // Piece is bottom right corner
                surroundingTiles.add(board.getTile(row, col-1));
                surroundingTiles.add(board.getTile(row-1, col));
                surroundingTiles.add(board.getTile(row-1, col-1));
            }
        }
        else if (row > 0 && col > 0) {
            // Piece is not top left corner
            if (row > 1) {
                surroundingTiles.add(board.getTile(row-1, col-1));
                surroundingTiles.add(board.getTile(row-1, col));
            }
            if (col > 1) {
                surroundingTiles.add(board.getTile(row, col-1));
            }

            if (col < 7) {
                surroundingTiles.add(board.getTile(row-1, col+1));
                surroundingTiles.add(board.getTile(row, col+1));
            }
            if (row < 7) {
                surroundingTiles.add(board.getTile(row+1, col-1));
                surroundingTiles.add(board.getTile(row+1, col));
                surroundingTiles.add(board.getTile(row+1, col+1));
            }
        }

        return surroundingTiles;
    }

    /**
     * Creates a list containing all of the pieces of the opposite colour to the piece passed in
     * from the list of tiles passed in
     * @param tiles a list of tiles
     * @param centrePiece the piece to compare the list to
     * @return a list of pieces of the opposite colour from the passed in piece from the list of tiles passed in
     */
    private List<Piece> getOppositePieces(List<AbstractTile> tiles, Piece centrePiece) {
        return tiles
                .stream()
                .filter(tile -> !tile.isVacant())
                .map(AbstractTile::getPiece)
                .filter(piece -> piece.getPieceColour() != centrePiece.getPieceColour())
                .collect(Collectors.toList());
    }

    private void flipPieces(List<Piece> pieces) {
        pieces.forEach(Piece::flipPiece);
    }
}
