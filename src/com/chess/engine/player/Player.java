package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

public abstract class Player {
	protected final Board board;
	protected final King playerKing;
	protected final List <Move> legalMoves;
	private final boolean isInCheck;

	public Player(Board board, List<Move> legalMoves, List<Move> opponentMoves) {
	this.board=board;
	this.legalMoves=legalMoves;
	this.playerKing=establishKing();
	this.isInCheck= !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
	}

	protected static Collection <Move>calculateAttackOnTile(int piecePosition,Collection<Move>opponentmoves){
		List<Move> attackMoves = new ArrayList<>();
		for(Move move:opponentmoves)
		{
			if(piecePosition==move.getDestinationCord())
			{
				attackMoves.add(move);
			}
		}
		return attackMoves;
	}

	protected  King establishKing() {
		for(Piece piece:getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				return (King)piece;
			}
		}
		throw new RuntimeException("No King");
	}
	
	public abstract List<Piece>getActivePieces();
	public abstract Alliance getAlliance() ;
	public abstract Player getOpponent();
	
	public boolean isMoveLegal(Move move) {
		return this.legalMoves.contains(move);
	}
	public boolean isInCheck() {
		return isInCheck;
	}
	public boolean isInCheckMate() {
		return this.isInCheck&&!hasEscapeMoves();
	}
	protected boolean hasEscapeMoves() {
		for(Move move:this.legalMoves) {
			MoveTransition transition= makeMove(move);
			if(transition.getMoveStatus().isDone())
			{
				return true;
			}
		}
		return false;
	}
	public boolean isInStaleMate() {
		return !this.isInCheck&&!hasEscapeMoves();
	}
	public boolean isCastled() {
		return false;
	}
	public King getPlayerKing() {
		return this.playerKing;
	}
	public boolean isKingSideCastleCapable() {
		return this.playerKing.isKingSideCastleCapable();
	}
	public boolean isQueenSideCastleCapable() {
		return this.playerKing.isQueenSideCastleCapable();
	}
	public List<Move> getLegalMoves(){
		return this.legalMoves;
	}
	protected  abstract Collection<Move> calculateKingCastles(Collection <Move>playerMoves,Collection <Move>opponentMoves);
	
	public MoveTransition makeMove(Move move) {
		
		if(!this.legalMoves.contains(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		final Board transitionBoard= move.execute();
		final List<Move>kingAttack=(List<Move>) Player.calculateAttackOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());
		if(!kingAttack.isEmpty())
		{
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
	}
}
