package com.chess.engine.board;

import java.util.*;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public class Board {
	private List<Tile> gameBoard;
	private List <Piece>whitePieces;
	private List <Piece>blackPieces;
	
	private WhitePlayer whitePlayer;
	private BlackPlayer blackPlayer;
	private Player currentPlayer;
	private Pawn enPassantPawn;
	
	public Player whitePlayer() {
		return this.whitePlayer;
		}	
	public Player blackPlayer() {
		return this.blackPlayer;
	}
	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}
	
	private Board(Builder builder)
	{
		this.gameBoard=createGameBoard(builder);
		whitePieces=calculateActivePieces(this.gameBoard, Alliance.WHITE);
		blackPieces=calculateActivePieces(this.gameBoard, Alliance.BLACK);
		this.enPassantPawn=builder.enPassantPawn;
		final List<Move> whiteStandardLegalMoves=calculateLegalMoves(whitePieces);
		final List<Move> blackStandardLegalMoves=calculateLegalMoves(blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
	}
	
	public List <Piece>getWhitePieces(){
		return this.whitePieces;
	}
	public List <Piece>getBlackPieces(){
		return this.blackPieces;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<64;i++)
		{
			String tileText=this.gameBoard.get(i).toString();
			builder.append(String.format("%3s",tileText));
			if((i+1)%8==0) {builder.append("\n");}
		}
	return builder.toString();
	}

	private List<Move> calculateLegalMoves(List<Piece> pieces) {
		List<Move> legalMoves = new ArrayList<>();
		for(Piece piece:pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return legalMoves;
	}

	private static List<Piece> calculateActivePieces(List<Tile> gameBoard,Alliance alliance) {
		List <Piece> result = new ArrayList<Piece>();
		for(Tile tile:gameBoard)
		{
			if(tile.isTileOccupied())
			{
				Piece piece=tile.getPiece();
				if(piece.getAlliance()==alliance)
				{
					result.add(piece);
				}
			}
		}
		return result;
	}
	
	public Tile getTile(int candidateCord) {
		return gameBoard.get(candidateCord);
	}
	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	private List<Tile> createGameBoard(Builder builder)
	{
		final List<Tile>tiles = new ArrayList<Tile>();
		Tile pom;
		for(int i=0;i<64;i++)
		{
			pom=Tile.createTile(i, builder.boardConfig.get(i));
			tiles.add(pom);
		}
		return tiles;
	}

	public static Board createStandardBoard() {
		Builder builder=new Builder();
		/**
		 * black
		 */
		builder.setPiece(new Rook(0,Alliance.BLACK));
		builder.setPiece(new Knight(1,Alliance.BLACK));
		builder.setPiece(new Bishop(2,Alliance.BLACK));
		builder.setPiece(new Queen(3,Alliance.BLACK));
		builder.setPiece(new King(4,Alliance.BLACK,true,true));
		builder.setPiece(new Bishop(5,Alliance.BLACK));
		builder.setPiece(new Knight(6,Alliance.BLACK));
		builder.setPiece(new Rook(7,Alliance.BLACK));
		for(int i=8;i<16;i++)
		{
			builder.setPiece(new Pawn(i,Alliance.BLACK));
		}
		/**
		 * white
		 */
		builder.setPiece(new Rook(56,Alliance.WHITE));
		builder.setPiece(new Knight(57,Alliance.WHITE));
		builder.setPiece(new Bishop(58,Alliance.WHITE));
		builder.setPiece(new Queen(59,Alliance.WHITE));
		builder.setPiece(new King(60,Alliance.WHITE,true,true));
		builder.setPiece(new Bishop(61,Alliance.WHITE));
		builder.setPiece(new Knight(62,Alliance.WHITE));
		builder.setPiece(new Rook(63,Alliance.WHITE));
		for(int i=48;i<56;i++)
		{
			builder.setPiece(new Pawn(i,Alliance.WHITE));
		}
		builder.setMoveMaker(Alliance.WHITE);
		return builder.build();
	}
	
	public static class Builder{
		
		Map <Integer,Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		public Builder() {
			this.boardConfig= new HashMap<Integer, Piece>();
		}
		public Builder setPiece(Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(),piece);
			return this;
		}
		public Builder setMoveMaker(Alliance nextMoveMaker) {
			this.nextMoveMaker=nextMoveMaker;
			return this;
		}
		public Board build()
		{
			return new Board(this);
		}
		public void setEnPassantPawn(Pawn movedPawn) {
			 this.enPassantPawn=movedPawn;
		}
	}

	public List<Move> getLegalMoves() {
		List<Move> i= new ArrayList<>();
		i.addAll(this.blackPlayer.getLegalMoves());
		i.addAll(this.whitePlayer.getLegalMoves());
		return i;
	}
}
