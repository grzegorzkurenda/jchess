package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

public class TakenPiecesPanel extends JPanel {

	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	private static final Color PANEL_COLOR= Color.decode("0xFD67DF");
	
	private final JPanel northPanel;
	private final JPanel southPanel;
	
	public TakenPiecesPanel() {
		super(new BorderLayout());
		setBackground(Color.decode("0xFD67DF"));
		setBorder(PANEL_BORDER);
		this.northPanel=new JPanel(new GridLayout(8, 2));
		this.southPanel=new JPanel(new GridLayout(8, 2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		add(this.northPanel,BorderLayout.NORTH);
		add(this.southPanel,BorderLayout.SOUTH);
		setPreferredSize(new Dimension(40,100));
	}
	public void redo(MoveLog moveLog) {
		
		this.southPanel.removeAll();
		this.northPanel.removeAll();
		
		final List<Piece>whiteTakenPieces = new ArrayList<>();
		final List<Piece>blackTakenPieces = new ArrayList<>();
		
		for(final Move move:moveLog.getMove()) {
			if(move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				if(takenPiece.getAlliance().isWhite()) {
					whiteTakenPieces.add(takenPiece);
				} else if(takenPiece.getAlliance().isBlack()) {
					blackTakenPieces.add(takenPiece);
				}
				else {
					throw new RuntimeException("shouldn't reach here");
				}
			}
		}
		Collections.sort(whiteTakenPieces, new Comparator<Piece>(){
			@Override
			public int compare(Piece o1, Piece o2) {
				return Ints.compare(o1.getPieceType().getPieceValue(), o2.getPieceType().getPieceValue());
			}
		});
		Collections.sort(blackTakenPieces, new Comparator<Piece>(){
			@Override
			public int compare(Piece o1, Piece o2) {
				return Ints.compare(o1.getPieceType().getPieceValue(), o2.getPieceType().getPieceValue());
			}
		});
		for(final Piece takenPiece:whiteTakenPieces) {
			
			try {
				final BufferedImage image = ImageIO.read(new File("art/"
                        + takenPiece.getAlliance().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".gif"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		for(final Piece takenPiece:blackTakenPieces) {
			
			try {
				final BufferedImage image = ImageIO.read(new File("art/"
                        + takenPiece.getAlliance().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".gif"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
			} catch (IOException e) {
				e.printStackTrace();
			}
			validate();
		}
	}
}
