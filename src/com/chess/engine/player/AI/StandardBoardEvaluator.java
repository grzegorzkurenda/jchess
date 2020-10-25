package com.chess.engine.player.AI;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator{
	
	private static final int CHECK_BONUS = 50;
	private static final int CHECKMATE_BONUS = 10000;
	private static final int CASTLE_BONUS = 60;
	StandardBoardEvaluator(){
		
	}
	
	@Override
	public int evaluate(Board board, int depth) {
		return scorePlayer(board,board.whitePlayer(),depth)-
				scorePlayer(board,board.blackPlayer(),depth);
	}

	private int scorePlayer(final Board board,
							final Player player,
							final int depth) {
		return pieceValue(player) + mobility(player) 
		+ check(player) + checkmate(player,depth) + castled(player);
	}

	private int castled(Player player) {
		return player.isCastled()? CASTLE_BONUS:0;
	}

	private int checkmate(Player player,int depth) {
		return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS+(100*depth):0;
	}

	private int check(Player player) {
		return player.getOpponent().isInCheck()? CHECK_BONUS : 0;
	}

	private int mobility(Player player) {
		return player.getLegalMoves().size();
	}

	private static int pieceValue(Player player) {
		int pieceValueScore=0;
		for(final Piece piece:player.getActivePieces()) {
			pieceValueScore+=piece.getPieceType().getPieceValue();
		}
		return pieceValueScore;
	}

}
