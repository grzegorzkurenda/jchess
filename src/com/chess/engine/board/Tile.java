package com.chess.engine.board;

import com.chess.engine.board.Tile.EmptyTile;
import com.chess.engine.pieces.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
	
protected final int TileCordinate;
private static final Map<Integer,EmptyTile> Empty_Tiles=createAllPossibleEmptyTiles();
	 
	 private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles(){
	 
			final Map <Integer,EmptyTile> empty_Tile_Map= new HashMap<>();
			for(int i=0;i<64;i++)
			{
				empty_Tile_Map.put(i, new EmptyTile(i));
			}
	
		return empty_Tile_Map;
	 }
	 
	 public static Tile createTile(int Cord,Piece piece) {
		 
		 return piece != null ? new OccupiedTile(Cord,piece): Empty_Tiles.get(Cord); 
	 }
	 public int getTileCord()
	 {
		return this.TileCordinate;
	 }
	private Tile(int Cord){
		
		TileCordinate=Cord;
	}

	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
	public static final class EmptyTile extends Tile{

		private EmptyTile(int Cord) {
			super(Cord);
		}

		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		@Override
		public String toString() {
			return "-";
		}

		@Override
		public Piece getPiece() {
			return null;
		}
	}
	
	public static final class OccupiedTile extends Tile{
		Piece pieceOnTile;
		
		private OccupiedTile(int Cord,Piece p) {
			super(Cord);
			pieceOnTile=p;
		}
		
		@Override
		public String toString() {
			return getPiece().getAlliance().isBlack() ? getPiece().toString().toLowerCase():
			 getPiece().toString();
		}

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return pieceOnTile;
		}
	}
}
