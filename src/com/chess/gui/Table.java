package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.*;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.AI.MinMax;
import com.chess.engine.player.AI.MoveStrategy;
import com.google.common.collect.Lists;

public class Table extends Observable {

	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private final TakenPiecesPanel takenPiecesPanel;
	private final GameHistoryPanel gameHistoryPanel;
	private final MoveLog moveLog;
	private final GameSetup gameSetup;
	private  Board chessBoard;

	
	private final Dimension preferredBoardPanelSize = new Dimension(400,350);
	private final Dimension preferredTileSize = new Dimension(10,10);
	private static String pieceIconPath = "art/";
	private boolean highlightMoves;
	private Tile sourceTile,destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	private Move computerMove;
	
	private static final Table INSTANCE = new Table();
	
	private Table() {
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar=createMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(600, 600);
		this.chessBoard = Board.createStandardBoard();
		this.gameHistoryPanel = new GameHistoryPanel();
		this.takenPiecesPanel = new TakenPiecesPanel();
		this.boardPanel = new BoardPanel();
		this.moveLog = new MoveLog();
		this.addObserver(new TableGameAIWatcher());
		this.gameSetup = new GameSetup(this.gameFrame, true);
		this.highlightMoves = true;
		this.boardDirection=BoardDirection.NORMAL;
		this.gameFrame.add(this.takenPiecesPanel,BorderLayout.WEST);
		this.gameFrame.add(this.gameHistoryPanel,BorderLayout.EAST);
		this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}
	public Board getBoard() {
		return chessBoard;
	}
	public static Table get() {
		return INSTANCE;
	}
	public void show() {
		Table.get().getMoveLog().clear();
		Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
		Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
		Table.get().getBoardPanel().drawBoard(Table.get().getBoard());

	}
	private GameSetup getGameSetup() {
		return this.gameSetup;
	}
	private void setupUpdate(final GameSetup gameSetup) {
		setChanged();
		notifyObservers(gameSetup);
	}
	private static class TableGameAIWatcher implements Observer{

		@Override
		public void update(Observable arg0, Object arg1) {
			if(Table.get().getGameSetup().isAIPlayer(Table.get().getBoard().currentPlayer())
				&& !Table.get().getBoard().currentPlayer().isInCheckMate()
				&& !Table.get().getBoard().currentPlayer().isInStaleMate())
				{
					final AIThinkTank thinkTank = new AIThinkTank();
					thinkTank.execute();
				}
			if(Table.get().getBoard().currentPlayer().isInCheckMate())
			{
				System.out.println("game over"+ Table.get().getBoard().currentPlayer() + "is in checkmate");
				
			}
			if(Table.get().getBoard().currentPlayer().isInStaleMate())
			{
				System.out.println("game over"+ Table.get().getBoard().currentPlayer() + "is in stalemate");
				
			}
		}
		
	}
	private static class AIThinkTank extends SwingWorker<Move, String>{
		private AIThinkTank() {
			
		}

		@Override
		protected Move doInBackground() throws Exception {
		
			final MoveStrategy minMax = new MinMax(4);
			final Move bestMove = minMax.execute(Table.get().getBoard());
			return bestMove;
		}
		@Override
		public void done() {
			try {
				final Move bestMove = get();
				Table.get().updateComputerMove(bestMove);

				Table.get().updateGameBoard(Table.get().getBoard().currentPlayer().makeMove(bestMove).getBoard());
				
				Table.get().getMoveLog().addMove(bestMove);
				Table.get().getGameHistoryPanel().redo(Table.get().getBoard(),Table.get().getMoveLog());
				Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
				Table.get().getBoardPanel().drawBoard(Table.get().getBoard());
				Table.get().moveMadeUpdate(PlayerType.COMPUTER);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
		}
	}
	private JMenuBar createMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createdFileMenu());
		tableMenuBar.add(createPreferenceMenu());
		tableMenuBar.add(createOptionsMenu());
		return tableMenuBar;	
	}

	public void moveMadeUpdate(PlayerType playerType) {
		setChanged();
		notifyObservers(playerType);
	}
	public void updateGameBoard(final Board board) {
		this.chessBoard = board;
	}
	public void updateComputerMove(Move bestMove) {
		this.computerMove =  bestMove;		
	}
	private MoveLog getMoveLog() {
		return this.moveLog;
	}
	private GameHistoryPanel getGameHistoryPanel() {
		return this.gameHistoryPanel;
	}
	private TakenPiecesPanel getTakenPiecesPanel() {
		return this.takenPiecesPanel;
	}
	private BoardPanel getBoardPanel() {
		return this.boardPanel;
	}
	
	private JMenu createdFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("pgn open");
			}
		});
		fileMenu.add(openPGN);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private JMenu createPreferenceMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenu = new JMenuItem("Flip Board");
			flipBoardMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boardDirection=boardDirection.opposite();
					boardPanel.drawBoard(chessBoard);
				}
			});
			preferencesMenu.add(flipBoardMenu);
			preferencesMenu.addSeparator();
			
			final JCheckBoxMenuItem highlightCheckBox=new JCheckBoxMenuItem("Highlight legal moves",false);
			highlightCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					highlightMoves = highlightCheckBox.isSelected();
				}
			});
			preferencesMenu.add(highlightCheckBox);
			return preferencesMenu;
	}
	
	private JMenu createOptionsMenu() {
		
		final JMenu optionMenu = new JMenu("Options");
		final JMenuItem setupMenuItem = new JMenuItem("Setup Game");
		setupMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					Table.get().getGameSetup().promptUser();
					Table.get().setupUpdate(Table.get().getGameSetup());
			}
		});
		optionMenu.add(setupMenuItem);
		return optionMenu;
	}
	
	enum PlayerType{
		HUMAN,
		COMPUTER
	}
	
	private class BoardPanel extends JPanel{
		final List<TilePanel> boardTiles = new ArrayList<>();
		BoardPanel(){
			super(new GridLayout(8,8));
			for(int i=0;i<64;i++)
			{
				final TilePanel tilePanel=new TilePanel(i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			
			setPreferredSize(preferredBoardPanelSize);
			validate();
		}
		public void drawBoard(final Board board) {
			removeAll();
			for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(board,tilePanel.tileId);
				add(tilePanel);
			}
			validate();
			repaint();
		}
		
	}
	
	public static class MoveLog{
		private final List<Move> moves;
		
		MoveLog(){
			this.moves=new ArrayList<Move>();
		}
		public List<Move>getMove(){
			 return this.moves;
		}
		public void addMove(Move move) {
			this.moves.add(move);
		}
		public int size() {
			return this.moves.size();
		}
		public void clear() {
			this.moves.clear();
		}
		public boolean removeMove(Move move) {
			return this.moves.remove(move);
		}
		public Move removeMove(int index) {
			return this.moves.remove(index);
		}
	}
	
	private class TilePanel extends JPanel{
		private final int tileId;
		TilePanel(int tileId){
			super(new GridBagLayout());
			this.tileId=tileId;
			
			setPreferredSize(preferredTileSize);
			assignTileColor(tileId);
			assignTilePieceIcon(chessBoard);
			validate();
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isRightMouseButton(e)) {
						sourceTile=null;
						destinationTile=null;
						humanMovedPiece=null;
						}
						else  if(SwingUtilities.isLeftMouseButton(e))
						{
							if(sourceTile==null) {
								sourceTile=chessBoard.getTile(tileId);
								humanMovedPiece=sourceTile.getPiece();
								
								if(humanMovedPiece==null)
								{
									sourceTile=null;
								}
							}
							else 
							{
								destinationTile=chessBoard.getTile(tileId);
								final Move move=Move.MoveFactor.createMove(chessBoard, sourceTile.getTileCord(),destinationTile.getTileCord());
								final MoveTransition moveTransition= chessBoard.currentPlayer().makeMove(move);
								if(moveTransition.getMoveStatus().isDone())
								{
									moveLog.addMove(move);
									chessBoard=moveTransition.getBoard();
								}
								sourceTile=null;
								destinationTile=null;
								humanMovedPiece=null;
							}
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									gameHistoryPanel.redo(chessBoard, moveLog);
									takenPiecesPanel.redo(moveLog);
									if(gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
									Table.get().moveMadeUpdate(PlayerType.HUMAN);	
									}
									boardPanel.drawBoard(chessBoard);									
								}
								
							});
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			validate();
		}
		
		private void highlightLegals(final Board board) {
			if(highlightMoves)
			{
				for(final Move move: pieceLegalMoves(board)) {
					if(move.getDestinationCord()==this.tileId) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		private Collection<Move> pieceLegalMoves(final Board board){
			if(humanMovedPiece!=null&&humanMovedPiece.getAlliance()==board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}
		
		public void drawTile(Board board,int tileId2) {
			assignTileColor(tileId2);
			assignTilePieceIcon(board);
			highlightLegals(chessBoard);
			validate();
			repaint();
		}
		
		private void assignTilePieceIcon(Board board) {
			 this.removeAll();
				if(board.getTile(tileId).isTileOccupied())
				{
					try {
						final BufferedImage piece = 
						ImageIO.read(new File(pieceIconPath+board.getTile(this.tileId).getPiece().getAlliance().toString().substring(0,1)
						+board.getTile(this.tileId).getPiece().toString()+".gif"));
						add(new JLabel(new ImageIcon(piece)));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		
		private void assignTileColor(int tileId2) {
			Color bg = null;
			int row=(tileId2/8);
			int column=tileId2%8;
			if((column+row)%2==0)	{	
				setBackground(bg.WHITE);
			}
			else {
				setBackground(bg.BLACK);
			}
		}
	}
	public enum BoardDirection{
		NORMAL{

			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}

			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return boardTiles;
			}
			
		},
		FLIPPED{

			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);
			}

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
			
		};
		abstract List<TilePanel>traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
	}
	
}
