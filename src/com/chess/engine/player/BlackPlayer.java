package com.chess.engine.player;

import java.util.*;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class BlackPlayer extends Player {

	

	public BlackPlayer(Board board, List<Move> legalMoves, List<Move> opponentMoves) {
		super(board, legalMoves, opponentMoves);
	}

	@Override
	public List<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}
	@Override
	protected Collection<Move> calculateKingCastles(Collection<Move> playerMoves, Collection<Move> opponentMoves) {
		final List <Move> kingCastles = new ArrayList<Move>();
		if(this.playerKing.isFirstMove()&&!this.isInCheck())
		{
			//king site castle
			if(!this.board.getTile(5).isTileOccupied()&&!this.board.getTile(6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(7);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove()){
					if(Player.calculateAttackOnTile(5,opponentMoves).isEmpty()&&Player.calculateAttackOnTile(6,opponentMoves).isEmpty()
					&&rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,6,(Rook)rookTile.getPiece(),7,5));
					}
				}
			}
			// queen site castle
			if(!this.board.getTile(3).isTileOccupied()&&!this.board.getTile(2).isTileOccupied()&&!this.board.getTile(1).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(0);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove())
				{
					if(Player.calculateAttackOnTile(1,opponentMoves).isEmpty()&&Player.calculateAttackOnTile(2,opponentMoves).isEmpty()
					&&Player.calculateAttackOnTile(3,opponentMoves).isEmpty()&&rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,(Rook)rookTile.getPiece(), 0, 3));
					}
				}
			}
		}
		return kingCastles;
	}
}
