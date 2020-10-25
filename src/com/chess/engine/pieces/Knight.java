package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
public class Knight extends Piece {

	public Knight(int piecePosition, Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance, PieceType.KNIGHT,true);
	}
	public Knight(int piecePosition, Alliance pieceAlliance,boolean isFirstMove) {
		super(piecePosition, pieceAlliance, PieceType.KNIGHT,isFirstMove);
	}
	private final int[] CANDIDATE_MOVE_CORD = {-17,-15,-10,-6,6,10,15,17};
	
	@Override
	public String toString() {
		return Piece.PieceType.KNIGHT.toString();
	}
	@Override
	public Piece movePiece(Move move) {
		return new Knight(move.getDestinationCord(),move.getMovedPiece().getAlliance());
	}
	@Override
	public List<Move> calculateLegalMoves(Board board) {
		List<Move>legalMoves=new ArrayList<>();
		
		
		for(int candidate:CANDIDATE_MOVE_CORD)
		{
			int candidateCord=this.piecePosition+candidate;
			if(BoardUtils.isValidTileCord(candidateCord)) {
				
				if(isFirstColumnExclusion(this.piecePosition,candidate)||isSecondColumnExclusion(this.piecePosition,candidate)
					||isSeventhColumnExclusion(this.piecePosition,candidate)||isEightColumnExclusion(this.piecePosition,candidate)) {
					continue;
				}
				
				final Tile candidateTile=board.getTile(candidateCord);
				if(!candidateTile.isTileOccupied())
				{
					legalMoves.add(new MajorMove(board,this,candidateCord));
				}
				else {
					
					final Piece pieceLocation=candidateTile.getPiece();//zbijana figura
					if(pieceLocation.getAlliance()!=this.pieceAlliance)
					{
						legalMoves.add(new MajorAttackMove(board,this,candidateCord,pieceLocation));
					}
				}
			}
		}
		return legalMoves;
	}

	private static boolean isFirstColumnExclusion(int piecePosition,int candidateCord)
	{
		if(piecePosition%8==0&&(candidateCord==-17||candidateCord==-10||candidateCord==6||candidateCord==15))
		{
			return true;
		}
		 return false;
	}
	private static boolean isSecondColumnExclusion(int piecePosition,int candidateCord)
	{
		if(piecePosition%8==1&&(candidateCord==-10||candidateCord==6))
		{
			return true;
		}
		 return false;
	}
	private static boolean isSeventhColumnExclusion(int piecePosition,int candidateCord)
	{
		if(piecePosition%8==6&&(candidateCord==10||candidateCord==-6))
		{
			return true;
		}
		 return false;
	}
	private static boolean isEightColumnExclusion(int piecePosition,int candidateCord)
	{
		if(piecePosition%8==7&&(candidateCord==17||candidateCord==10||candidateCord==-6||candidateCord==-15))
		{
			return true;
		}
		 return false;
	}
	
}
