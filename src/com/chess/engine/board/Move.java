package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCord;
	protected final boolean isFirstMove;
	
	public static final Move NULL_MOVE= new NullMove();
	
	private Move(Board board, Piece movedPiece, int destinationCord) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCord = destinationCord;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	private Move(Board board, int destinationCord) {
		this.board = board;
		this.movedPiece = null;
		this.destinationCord = destinationCord;
		this.isFirstMove = false;	
		}
	
	@Override
	public boolean equals(Object arg0) {
		if(this==arg0)
		{
			return true;
		}
		if(!(arg0 instanceof Move))
		{
			System.out.println("blad equals");
			return false;
		}
		final Move otherMove = (Move)arg0;
		return otherMove.getDestinationCord()==this.getDestinationCord()&&
				otherMove.getCurrentCord()==this.getCurrentCord()&&
				getMovedPiece().equals(otherMove.getMovedPiece());
	}
	@Override
	public int hashCode() {
		int result=1;
		int prime=31;
		result = prime*result+this.getDestinationCord();
		result = prime*result+this.movedPiece.hashCode();
		result = prime*result+this.movedPiece.getPiecePosition();
		return result;
	}
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	public int getCurrentCord() {
		return this.getMovedPiece().getPiecePosition();
	}
	public boolean isAttack() {
		return false;
	}
	public boolean isCastlingMove() {
		return false;
	}
	public Piece getAttackedPiece() {
		return null;
	}
	public int getDestinationCord() {
		return this.destinationCord;
	}
	public Board execute() {
		final Builder builder = new Builder();
			for(Piece piece: this.board.currentPlayer().getActivePieces())
			{
				if(!this.movedPiece.equals(piece))
				{
					builder.setPiece(piece);
				}
			}
			for(Piece piece:this.board.currentPlayer().getOpponent().getActivePieces())
			{
				builder.setPiece(piece);
			}
			
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			
		return builder.build();
	}
	public static class MajorAttackMove extends AttackMove{

		public MajorAttackMove(Board board, Piece piece, int destinationCord, Piece attackingPiece) {
			super(board, piece, destinationCord, attackingPiece);
				}
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof MajorAttackMove && super.equals(other);
	}
	@Override
	public String toString() {
		return movedPiece.getPieceType()+BoardUtils.getPositionCord(this.destinationCord);
	}
	}
	final static public class MajorMove extends Move{

		public MajorMove(Board board, Piece movedPiece, int destinationCord) {
			super(board, movedPiece, destinationCord);
		}
		@Override
		public boolean equals(Object arg0) {
			return this== arg0||arg0 instanceof MajorMove && super.equals(arg0)
;		}
		@Override
		public String toString() {
			return movedPiece.getPieceType().toString()+BoardUtils.getPositionCord(this.destinationCord);
		}
	}
	final static public class PawnJump extends Move{

		public PawnJump(Board board, Piece movedPiece, int destinationCord) {
			super(board, movedPiece, destinationCord);
		}
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for(Piece piece:this.board.currentPlayer().getActivePieces())
			{
				if(!this.movedPiece.equals(piece))
				{
					builder.setPiece(piece);
				}
			}
			for(Piece piece:this.board.currentPlayer().getOpponent().getActivePieces())
			{
				builder.setPiece(piece);
			}
			final Pawn movedPawn= (Pawn)this.movedPiece.movePiece(this);	
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		@Override
		public String toString() {
			return BoardUtils.getPositionCord(this.destinationCord);
		}
	}
	static public class AttackMove extends Move{
		Piece attackingPiece;
		public AttackMove(Board board, Piece piece, int destinationCord,Piece attackingPiece) {
			super(board, piece, destinationCord);
			this.attackingPiece=attackingPiece;
		}
		@Override
		public boolean isAttack() {
			return true;
		}
		
		@Override
		public int hashCode() {
			return this.attackingPiece.hashCode()+super.hashCode();
		}
		@Override
		public Piece getAttackedPiece() {
			return this.attackingPiece;
		}
		@Override
		public boolean equals(Object object) {
			if(this==object)
			{
				return true;
			}
			if(!(object instanceof AttackMove))
			{return false;}
			final AttackMove otherAttackMove= (AttackMove) object;
			return super.equals(otherAttackMove)&&getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}
	}
	final static public class PawnMove extends Move{

		public PawnMove(Board board, Piece movedPiece, int destinationCord) {
			super(board, movedPiece, destinationCord);
		}
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnMove&&super.equals(other);
		}
		@Override
		public String toString() {
			return BoardUtils.getPositionCord(this.destinationCord);
		}
	}
	static public class PawnAttackMove extends AttackMove{

		public PawnAttackMove(Board board, Piece movedPiece, int destinationCord,Piece attackingPiece) {
			super(board, movedPiece, destinationCord,attackingPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this== other || other instanceof PawnAttackMove &&super.equals(other);
		}
		@Override
		public String toString() {
			return BoardUtils.getPositionCord(this.movedPiece.getPiecePosition()).substring(0,1)+"x"+
					BoardUtils.getPositionCord(this.destinationCord);
		}
	}
	final static public class PawnEnPassantAttackMove extends PawnAttackMove{

		public  PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCord,Piece attackingPiece) {
			super(board, movedPiece, destinationCord,attackingPiece);
		}
		@Override
		public boolean equals(Object other) {
			return this==other || other instanceof PawnEnPassantAttackMove && super.equals(other);
		}
		@Override
		public Board execute() {
			final Builder builder = new Builder();
				for(Piece piece:this.board.currentPlayer().getActivePieces()) {
					if(!this.movedPiece.equals(piece)) {
						builder.setPiece(piece);
					}
				}
				for(Piece piece:this.board.currentPlayer().getOpponent().getActivePieces()) {
					if(!piece.equals(this.getAttackedPiece())) {
						builder.setPiece(piece);
					}
				}
				builder.setPiece(this.movedPiece.movePiece(this));
				builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
				return builder.build();
		}
	}
	public static class PawnPromotion extends Move{
		final Move decoratedMove;
		final Pawn promotedPawn;
		
		public PawnPromotion(final Move decoratedMove) {
		super(decoratedMove.getBoard(),decoratedMove.getMovedPiece(),decoratedMove.getDestinationCord());
		this.decoratedMove=decoratedMove;
		this.promotedPawn=(Pawn) decoratedMove.getMovedPiece();
		}
		@Override
		public Board execute() {
			final Board pawnMovedBoard = this.decoratedMove.execute();
			final Board.Builder builder = new Builder();
			for(Piece piece:pawnMovedBoard.currentPlayer().getActivePieces()) {
				builder.setPiece(piece);
			}
			for(Piece piece:pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
			return builder.build();
			}
		@Override
		public boolean isAttack() {
			return this.decoratedMove.isAttack();
		}
		@Override
		public Piece getAttackedPiece() {
			return this.decoratedMove.getAttackedPiece();
		}
		@Override
		public String toString() {
			return "";
		}
		@Override
		public int hashCode() {
			return decoratedMove.hashCode()+(31*promotedPawn.hashCode());
		}
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnPromotion && (super.equals(other));
		}
	}
	static abstract class CastleMove extends Move{
		protected Rook castledRook;
		protected int castleRookStart;
		protected int castleRookDestination;

		public CastleMove(Board board, Piece movedPiece, int destinationCord,Rook castledRook,int castleRookStart, int castleRookDestination) {
			super(board, movedPiece, destinationCord);
			this.castledRook=castledRook;
			this.castleRookDestination=castleRookDestination;
			this.castleRookStart=castleRookStart;
		}
		public Rook getCastledRook() {
			return castledRook;
		}
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for(Piece piece:this.board.currentPlayer().getActivePieces())
			{
				if(!this.movedPiece.equals(piece)&&!this.castledRook.equals(piece))
				{
					builder.setPiece(piece);
				}
			}
			for(Piece piece:this.board.currentPlayer().getOpponent().getActivePieces())
			{
				builder.setPiece(piece);
			}	
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setPiece(new Rook(this.castleRookDestination,this.castledRook.getAlliance()));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result=prime*result+this.castleRookDestination;
			result=prime*result+this.castledRook.hashCode();
			return result;
		}
		@Override
		public boolean equals(Object other) {
			if(this==other) {
				return true;
			}
			if(!(other instanceof CastleMove)) {
				return false;
			}
			final CastleMove otherCastleMove = (CastleMove)other;
			return super.equals(otherCastleMove) && this.castledRook.equals(otherCastleMove.getCastledRook());
		}
	}
	final public static class KingSideCastleMove extends CastleMove{
		
		public KingSideCastleMove(Board board, Piece movedPiece, int destinationCord,Rook castledRook,int castleRookStart, int castleRookDestination) {
			super(board, movedPiece, destinationCord, castledRook, castleRookStart, castleRookDestination);
		}
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof KingSideCastleMove&&super.equals(other);
		}
		@Override
		public String toString() {
			return "0-0";
		}
	}
	final public static class QueenSideCastleMove extends CastleMove{
		
		public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCord,Rook castledRook,int castleRookStart, int castleRookDestination) {
			super(board, movedPiece, destinationCord ,castledRook, castleRookStart, castleRookDestination);
		}
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof QueenSideCastleMove&&super.equals(other);
		}
		@Override
		public String toString() {
			return "0-0-0";
		}
	}
	final public static class NullMove extends Move{	
		public NullMove() {
			super(null,65);
		}
		@Override
		public Board execute() {
			throw new RuntimeException("can't execute a move");
		}
		@Override
		public int getCurrentCord() {
			return -1;
		}
	}
	public static class MoveFactor{
		private MoveFactor() {
			throw new RuntimeException("brak");
		}
		public static Move createMove(Board board,int currentCord,int destinationCord)
		{
			for(Move move:board.getLegalMoves()) {
				if(move.getCurrentCord()==currentCord&&move.getDestinationCord()==destinationCord)
				{
					return move;
				}
			}
			return NULL_MOVE;
		}
	}
	public Board getBoard() {
		
		return this.board;
	}
}
