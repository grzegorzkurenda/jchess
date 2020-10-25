package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;

public class Pawn extends Piece{
	int vector=1;

	public Pawn(int piecePosition, Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance, PieceType.PAWN, true);
			vector=pieceAlliance.getDirection();
	}
	public Pawn(int piecePosition, Alliance pieceAlliance,boolean isFirstMove) {
		super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove);
			vector=pieceAlliance.getDirection();
	}
	@Override
	public Piece movePiece(Move move) {
		return new Pawn(move.getDestinationCord(),move.getMovedPiece().getAlliance());
	}
	@Override
	public String toString() {
		return Piece.PieceType.PAWN.toString();
	}
	
	private final int[] CANDIDATE_MOVE_CORD = {7,8,9,16};
	
	@Override
	public List<Move> calculateLegalMoves(Board board) {
		List<Move>legalMoves=new ArrayList<>();
		
		for(int candidate:CANDIDATE_MOVE_CORD) {
			candidate*=vector;
			int candidateCord=this.piecePosition+candidate;	
			if(!BoardUtils.isValidTileCord(candidateCord)) {
				continue;
			}
			if((candidate==8||candidate==-8) && !board.getTile(candidateCord).isTileOccupied()) {
				if(this.pieceAlliance.isPawnPromotionSquare(candidateCord)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board,this,candidateCord)));
				}
				else {
				legalMoves.add(new PawnMove(board, this, candidateCord));
				}
			}else if((candidate==16&&piecePosition>7&&piecePosition<16)||(candidate==-16&&piecePosition>47&&piecePosition<56)) {
				final int behindCord=this.piecePosition+candidate/2;
				if(!board.getTile(behindCord).isTileOccupied() &&
					!board.getTile(candidateCord).isTileOccupied()) {
					legalMoves.add(new PawnJump(board,this,candidateCord));
				}
			}else if(candidate==7||candidate==-7) {
				if(board.getTile(candidateCord).isTileOccupied()) {
					final Piece pieceOnCandidate=board.getTile(candidateCord).getPiece();
					if(this.pieceAlliance!= pieceOnCandidate.getAlliance()) {
						if(this.pieceAlliance.isPawnPromotionSquare(candidateCord)) {
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board,this,candidateCord,pieceOnCandidate)));
						}
						else {
						legalMoves.add(new PawnAttackMove(board,this,candidateCord,pieceOnCandidate));
					}}
				}	
				else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition+(this.pieceAlliance.getOppositeDirection()))) {
						Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance!= pieceOnCandidate.pieceAlliance) {
							legalMoves.add(new PawnEnPassantAttackMove(board,this,candidateCord,pieceOnCandidate));
						}
					}
				}
			} else if((candidate==9&&!isFirstColumnExclusion(candidateCord))||(candidate==-9&&!isEightColumnExclusion(candidateCord))) {
				if(board.getTile(candidateCord).isTileOccupied()) {
					final Piece pieceOnCandidate=board.getTile(candidateCord).getPiece();
					if(this.pieceAlliance!= pieceOnCandidate.getAlliance()) {
						if(this.pieceAlliance.isPawnPromotionSquare(candidateCord)) {
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board,this,candidateCord,pieceOnCandidate)));
						}
						else {
						legalMoves.add(new PawnAttackMove(board,this,candidateCord,pieceOnCandidate));
					}}
				}
				else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition-(this.pieceAlliance.getOppositeDirection()))) {
						Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance!= pieceOnCandidate.pieceAlliance) {
							legalMoves.add(new PawnEnPassantAttackMove(board,this,candidateCord,pieceOnCandidate));
						}
					}
				}
			}
				}
		return legalMoves;
	}
	public Piece getPromotionPiece() {
		return new Queen(this.getPiecePosition(),this.getAlliance(),false);
	}
	
	private boolean isEightColumnExclusion(int piecePosition) {
		
		if(piecePosition%8==7)
		{
			return true;
		}
	return false;
	}
	
	private boolean isFirstColumnExclusion(int piecePosition){
	
		if(piecePosition%8==0)
		{
			return true;
		}
	 return false;
	}
}
