package com.chess.engine.pieces;

import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected boolean isFirstMove;
	
	 Piece(int piecePosition, Alliance pieceAlliance,final PieceType pieceType,boolean isFirstMove) {
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.pieceType=pieceType;
		this.isFirstMove=isFirstMove;
	}
	
	 public Alliance getAlliance() {
		 return this.pieceAlliance;
	 }
	 
	public abstract List<Move> calculateLegalMoves(Board board);
	
	public abstract Piece movePiece(Move move);

	@Override
	public boolean equals(Object obj) {
		if(obj==this) {
			return true;
		}
		if(!(obj instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece)obj;
		return pieceType==otherPiece.getPieceType()&&pieceAlliance==otherPiece.getAlliance()
				&&piecePosition==otherPiece.getPiecePosition()&&isFirstMove==otherPiece.isFirstMove();
	}

	public boolean isFirstMove() {	
		return this.isFirstMove;
	}

	@Override
	public int hashCode() {
		int result=pieceType.hashCode();
		result=31*result+ pieceAlliance.hashCode();
		result=31*result+ piecePosition;
		result=31*result+ (isFirstMove?1:0);
		return result;
	}

	public Integer getPiecePosition() {
		return this.piecePosition;
	}
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public enum PieceType{
		PAWN("P",100) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KNIGHT("N",300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		BISHOP("B",300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		ROOK("R",500) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return true;
			}
		},
		QUEEN("Q",900) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KING("K",1000) {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		PieceType(String pieceName,int pieceValue)
		{
			this.pieceName=pieceName;
			this.pieceValue=pieceValue;
		}
		@Override
		public String toString() {
			return this.pieceName;
		}
		public int getPieceValue() {
			return this.pieceValue;
		}
		public abstract boolean isKing();
		public abstract boolean isRook();
	}
	
}
