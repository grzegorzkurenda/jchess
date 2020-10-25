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
import com.chess.engine.pieces.Piece;

public class Bishop extends Piece {

	public Bishop(int piecePosition, Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance, PieceType.BISHOP,true);
	}
	public Bishop(int piecePosition, Alliance pieceAlliance,boolean isFirstMove) {
		super(piecePosition, pieceAlliance, PieceType.BISHOP,isFirstMove);
	}
	private final int[] CANDIDATE_MOVE_CORD = {-9,-7,7,9};
	
	@Override
	public String toString() {
		return Piece.PieceType.BISHOP.toString();
	}
	
	@Override
	public List<Move> calculateLegalMoves(Board board) {
		List<Move>legalMoves=new ArrayList<>();
		
		for(int candidate:CANDIDATE_MOVE_CORD) {
			
			int candidateCord=this.piecePosition;	
			while(BoardUtils.isValidTileCord(candidateCord)){
			if(isFirstColumnExclusion(candidateCord,candidate)||isEightColumnExclusion(candidateCord,candidate)) {
					break;
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
						break;
					}
				}
			}
		}
		return legalMoves;
	}
	
	private boolean isEightColumnExclusion(int piecePosition, int candidate) {
	
			if(piecePosition%8==7&&(candidate==-7||candidate==9))
			{
				return true;
			}
			 return false;
	}
	private boolean isFirstColumnExclusion(int piecePosition, int candidate){
		
		if(piecePosition%8==0&&(candidate==7||candidate==-9))
		{
			return true;
		}
		 return false;
	}

	@Override
	public Piece movePiece(Move move) {
		return new Bishop(move.getDestinationCord(),move.getMovedPiece().getAlliance());
	}
}
