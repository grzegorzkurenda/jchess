package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
	private final Board transitionBoard;
	private final Move transitionMove;
	private final MoveStatus transitionMoveStatus;

	
	public MoveTransition(final Board transitionBoard,
			Move transitionMove,
			MoveStatus transitionMoveStatus)
	{
		this.transitionBoard=transitionBoard;
		this.transitionMove=transitionMove;
		this.transitionMoveStatus=transitionMoveStatus;
	}
	public MoveStatus getMoveStatus(){
		return this.transitionMoveStatus;
	
	}
	public Board getBoard() {
		return this.transitionBoard;
	}
}
