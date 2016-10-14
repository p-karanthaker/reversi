package uk.ac.aston.dc2060.group5.reversi.model;

/**
 * Created by Sam on 09/10/2016.
 */
public abstract class AbstractTile {

    /**
     * @param tileCoordinate the coordinate of the tile on the board (8x8)
     */

    public int tileCoordinate;
    public abstract Piece getPiece();
    public abstract boolean isVacant();

    public int getTileCoordinate(int tileCoordinate) {
    return tileCoordinate;
  }


    //need to find a better way to return the tile variable
    public static AbstractTile abstractTile(int tileCoordinate, Piece piece){

        if (piece == null){
            VacantTile tile = new VacantTile(tileCoordinate);
            return tile;

        }
        else {
            FullTile tile = new FullTile(tileCoordinate, piece);
            return tile;
        }

    }


  }
    class VacantTile extends AbstractTile {
        public int tileCoordinate;



        @Override
        public Piece getPiece() {
          return null;
        }

        @Override
        public boolean isVacant() {
          return false;
        }

        public VacantTile(int tileCoordinate) {
            this.tileCoordinate = tileCoordinate;
        }
  }

    class FullTile extends AbstractTile {
        public int tileCoordinate;

        public Piece pieceOnTile;



        @Override
         public Piece getPiece() {
           return null;
         }

         @Override
         public boolean isVacant() {
           return false;
         }

        public FullTile(int tileCoordinate, Piece pieceOnTile) {
            this.tileCoordinate = tileCoordinate;
            this.pieceOnTile = pieceOnTile;
        }
   }






