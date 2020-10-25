package com.chess.engine.player.AI;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MinMax implements MoveStrategy {
	private final BoardEvaluator boardEvaluator;
	private final int depth;
	
	public MinMax(int depth) {
		this.boardEvaluator = new StandardBoardEvaluator();
		this.depth=depth;
	}
	
	@Override
	public Move execute(Board board) {
		final long startTime = System.currentTimeMillis();
		Move bestMove=null;
		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue;
		System.out.println(board.currentPlayer()+"Thinking with depth"+ depth);
		
		for(final Move move:board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				currentValue = board.currentPlayer().getAlliance().isWhite() ?		
						min(moveTransition.getBoard(),depth-1):
						max(moveTransition.getBoard(),depth-1);
				if(board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
				highestSeenValue=currentValue;
				bestMove = move;
				}
				else if(board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) 
 {
					lowestSeenValue = currentValue;
					bestMove = move;
				}
			}
		}
		final long executionMove = System.currentTimeMillis()-startTime;
		return bestMove;
	}
	@Override
	public String toString() {
		return "MiniMax";
	}
	
	public int min(final Board board, final int depth) {
		if(depth==0)
			return this.boardEvaluator.evaluate(board, depth);
		int lowestSeenValue = Integer.MAX_VALUE;
		for(final Move move:board.currentPlayer().getLegalMoves())
		{
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getBoard(),depth-1);
				if(currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
			}
		}
		return lowestSeenValue;
	}
	
	private static boolean isEndGameScenario(Board board) {
		return board.currentPlayer().isInCheckMate()
				||board.currentPlayer().isInStaleMate();
		}
	
	public int max(final Board board, final int depth) {
		if(depth==0 || isEndGameScenario(board))
			return this.boardEvaluator.evaluate(board, depth);
		int highestSeenValue = Integer.MIN_VALUE;
		for(final Move move:board.currentPlayer().getLegalMoves())
		{
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(moveTransition.getBoard(),depth-1);
				if(currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
			}
		}
		return highestSeenValue;
	}
}
