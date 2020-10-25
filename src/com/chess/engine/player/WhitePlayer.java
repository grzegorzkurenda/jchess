package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class WhitePlayer extends Player {


	public WhitePlayer(Board board, List<Move> legalMoves, List<Move> opponentMoves) {
		super(board, legalMoves, opponentMoves);
	}

	@Override
	public List<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(Collection<Move> playerMoves, Collection<Move> opponentMoves) {
		final List <Move> kingCastles = new ArrayList<Move>();
		if(this.playerKing.isFirstMove()&&!this.isInCheck())
		{
			//white king site castle
			if(!this.board.getTile(61).isTileOccupied()&&!this.board.getTile(62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove()){
					if(Player.calculateAttackOnTile(61,opponentMoves).isEmpty()&&Player.calculateAttackOnTile(62,opponentMoves).isEmpty()
					&&rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62, (Rook)rookTile.getPiece(), 63, 61));
					}
				}
			}
			//white queen site castle
			if(!this.board.getTile(59).isTileOccupied()&&!this.board.getTile(58).isTileOccupied()&&!this.board.getTile(57).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(56);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove())
				{
					if(Player.calculateAttackOnTile(58,opponentMoves).isEmpty()&&Player.calculateAttackOnTile(57,opponentMoves).isEmpty()
					&&Player.calculateAttackOnTile(59,opponentMoves).isEmpty()&&rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getPiece(), 56, 59));
					}
				}
			}
		}
		return kingCastles;
	}

}
