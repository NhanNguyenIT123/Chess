package chess_game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import chess_game.Chess_game;
public class ChessPanel extends JPanel {
	private LinkedList<Piece> ps;
	Image[] img;
	public ChessPanel(LinkedList<Piece> ps,Image[] img)
	{
		this.ps=ps;
		this.img=img;	
	}
	public Dimension getPreferredSize()
	{
		return getParent().getSize();
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean isWhite = true;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (isWhite) {
                    g.setColor(new Color(235, 235, 208));
                } else {
                    g.setColor(new Color(119, 148, 148));
                }
                g.fillRect(x * 64, y * 64, 64, 64);
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
        for (Piece p : Chess_game.ps) {
        	int idx = 0;
            if (p.name.equalsIgnoreCase("King")) {
                idx = 0;
            }
            if (p.name.equalsIgnoreCase("Queen")) {
                idx = 1;
            }
            if (p.name.equalsIgnoreCase("Bishop")) {
                idx = 2;
            }
            if (p.name.equalsIgnoreCase("Knight")) {
                idx = 3;
            }
            if (p.name.equalsIgnoreCase("Rook")) {
                idx = 4;
            }
            if (p.name.equalsIgnoreCase("Pawn")) {
                idx = 5;
            }
            if (!p.isWhite) {
                idx += 6;
            }
            g.drawImage(img[idx], p.x, p.y, this);
        }
        ArrayList<Point> selectedPieceAvailableMoves = Chess_game.selectedPiece.getAvailableMoves();
        if (selectedPieceAvailableMoves != null) {
            g.setColor(Color.BLACK);
            for (Point move : selectedPieceAvailableMoves) {
                int moveX = move.x * 64;
                int moveY = move.y * 64;
                g.drawRect(moveX, moveY, 64, 64);
            }
        }
    }
	
}
