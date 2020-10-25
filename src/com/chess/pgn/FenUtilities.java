package com.chess.pgn;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;

public class FenUtilities {

	private FenUtilities() {
		throw new RuntimeException("Not instantiable");
	}
	
	public static Board createGameFormFEN(final String fenString){
		return null;
	}
	
	public static String createFEN(final Board board) {
		return calculateBoardText(board)+ " " +
			   calculateCurrentPlayerText(board)+ " " +
			   calculateCastleText(board) + " "+
			   calculateEnPassantSquare(board);
	}

	private static String calculateEnPassantSquare(Board board) {
		final Pawn enPassantPawn = board.getEnPassantPawn();
		if(enPassantPawn!= null)
		{
			return BoardUtils.getPositionCord(enPassantPawn.getPiecePosition()+
					(8)* enPassantPawn.getAlliance().getOppositeDirection());
		}
		return "-";
	}

	private static String calculateCastleText(Board board) {
		final StringBuilder builder = new StringBuilder();
		
		if(board.whitePlayer().isKingSideCastleCapable()) {
			builder.append("K");
		}
		if(board.whitePlayer().isQueenSideCastleCapable()) {
			builder.append("Q");
		}
		if(board.blackPlayer().isKingSideCastleCapable()) {
			builder.append("k");
		}
		if(board.blackPlayer().isQueenSideCastleCapable()) {
			builder.append("q");
		}
		final String result =builder.toString();
		return result.isEmpty() ? "-": result; 
	}

	private static String calculateBoardText(Board board) {
		final StringBuilder builder = new StringBuilder();
		for(int i=0;i<64;i++) {
			final String tileText = board.getTile(i).toString();
		}
		builder.insert(8,"/");
		builder.insert(17,"/");
		builder.insert(26,"/");
		builder.insert(35,"/");
		builder.insert(44,"/");
		builder.insert(53,"/");
		builder.insert(62,"/");
		return builder.toString().replaceAll("--------","8")
								.replaceAll("-------","7")
								.replaceAll("------","6")
								.replaceAll("-----","5")
								.replaceAll("----","4")
								.replaceAll("---","3")
								.replaceAll("--","2")
								.replaceAll("-","1");
	}

	private static String calculateCurrentPlayerText(final Board board) {
		return board.currentPlayer().toString().substring(0,1).toLowerCase();
	}
}
