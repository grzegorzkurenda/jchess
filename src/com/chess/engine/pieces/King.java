package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;

public class King extends Piece {

	private boolean kingSideCastleCapable;
	private	boolean queenSideCastleCapable;
	private boolean isCastled;
	private final int[] CANDIDATE_MOVE_CORD = {-9,-8,-7,-1,1,7,8,9};
	
	public King(int piecePosition, Alliance pieceAlliance,final boolean kingSideCastleCapable,final boolean queenSideCastleCapable ) {
		super(piecePosition, pieceAlliance, PieceType.KING,true);
		this.isCastled=false;
		this.queenSideCastleCapable=queenSideCastleCapable;
		this.kingSideCastleCapable=kingSideCastleCapable;
	}
	public King(int piecePosition, Alliance pieceAlliance,final boolean isFirstMove,final boolean kingSideCastleCapable,final boolean isCastled,final boolean queenSideCastleCapable) {
		super(piecePosition, pieceAlliance, PieceType.KING,isFirstMove);
		this.isCastled=isCastled;
		this.kingSideCastleCapable=kingSideCastleCapable;
		this.queenSideCastleCapable=queenSideCastleCapable;
	}
	public boolean isKingSideCastleCapable() {
		return this.kingSideCastleCapable;
		
	}
	public boolean isQueenSideCastleCapable() {
		return this.queenSideCastleCapable;	
	}
	public boolean isCastled() {
		return this.isCastled;
	}
	@Override
	public String toString() {
		return Piece.PieceType.KING.toString();
	}
	@Override
	public Piece movePiece(Move move) {
		return new King(move.getDestinationCord(),move.getMovedPiece().getAlliance()
						,false,move.isCastlingMove(),false,false);
	}
	@Override
	public List<Move> calculateLegalMoves(Board board) {
		List<Move>legalMoves=new ArrayList<>();
		
		for(int candidate:CANDIDATE_MOVE_CORD) {
			
			int candidateCord=this.piecePosition;	
			if(BoardUtils.isValidTileCord(candidateCord)){
			if(isFirstColumnExclusion(this.piecePosition,candidate)||isEightColumnExclusion(this.piecePosition,candidate)) {
					continue;
				}			
			
				candidateCord+=candidate;
				if(BoardUtils.isValidTileCord(candidateCord)) {
					final Tile candidateTile=board.getTile(candidateCord);
					if(!candidateTile.isTileOccupied())
					{
						legalMoves.add(new MajorMove(board,this,candidateCord));
					}
					else {	
						final Piece pieceLocation=candidateTile.getPiece();
						if(pieceLocation.getAlliance()!=this.pieceAlliance)
						{
							legalMoves.add(new MajorAttackMove(board,this,candidateCord,pieceLocation));
						}
					}
				}
			}
		}
		return legalMoves;
	}
	
	private boolean isEightColumnExclusion(int piecePosition, int candidate) {
	
			if(piecePosition%8==7&&(candidate==-7||candidate==9||candidate==1))
			{
				return true;
			}
			 return false;
	}
	private boolean isFirstColumnExclusion(int piecePosition, int candidate){
		
		if(piecePosition%8==0&&(candidate==7||candidate==-9||candidate==-1))
		{
			return true;
		}
		 return false;
	}

}
