package chess_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Piece {
int idx;
int x;
int y;
int xp;
int yp;
String name;
JPanel jPanel;
Piece_Type type;
public boolean hasMoved;
public static ArrayList<Point> availableMoves= new ArrayList<Point>();
boolean isWhite;
static LinkedList<Piece> ps;
private String promotedType;
private Point previousPoint;
public static Stack<Piece>lastCapturedPiece = new Stack<Piece>() ; 
public static boolean isPromoted = false ;
public boolean justMovedTwoSquares()
{
	return Math.abs(previousPoint.y-yp)==2;
}
public void setPromotedType(String promotedType)
{
	this.promotedType=promotedType;
	if (promotionZone()&&name.equals("Pawn"))
	{
		name = promotedType;
	}
}
public String getPromotedType()
{
	return promotedType;
}

public Piece(int xp, int yp,String n, boolean isWhite,LinkedList<Piece> ps) {
	super();
	this.xp = xp;
	this.yp = yp;
	x=xp*64;
	y=yp*64;
	this.isWhite = isWhite;
	this.ps=ps;
	this.name=n;
	this.hasMoved=false;
	ps.add(this);
}
public Piece(Piece_Type type,boolean isWhite) {
	this.type = type;
	this.isWhite=isWhite;
}
public void move(int xp, int yp) {
    if (this.isWhite == Chess_game.isWhiteTurn()) {
        // Set the last moved piece
        Chess_game.setLastMovedPiece(this);
        
        // Store the previous position
        if (previousPoint == null) {
            previousPoint = new Point(this.xp, this.yp);
        }
        
        // Validate the move
        if (isValidMove(xp, yp)) {
            // Check if the move puts the king in check
            
                // Capture the piece at the new position if it exists
                Piece capturedPiece = Chess_game.getPiece(xp * 64, yp * 64);
                if (capturedPiece != null) {
                    if (this.isWhite != capturedPiece.isWhite) {
                        capturedPiece.captured();
                        lastCapturedPiece.add(capturedPiece);
                    } else {
                        // Revert to original position if invalid move
                        x = this.xp * 64;
                        y = this.yp * 64;
                        return;
                    }
                } else {
                    lastCapturedPiece.add(null);
                }
                if (this.name.equals("King") && Math.abs(xp - this.xp) == 2 && yp == this.yp) {
                    performCastling(xp, yp);
                }
                // Update piece position
                int oldX = this.xp;
                int oldY = this.yp;
                this.xp = xp;
                this.yp = yp;
                x = xp * 64;
                y = yp * 64;

                // Handle special moves like castling
                

                // Mark king and rook as moved
                if (this.name.equals("King") || this.name.equals("Rook")) {
                    this.hasMoved = true;
                }

                // Switch turn
                Chess_game.switchTurn();

                // Check for checkmate
                boolean currentPlayerIsWhite = Chess_game.isWhiteTurn();
                handleCheckmate(currentPlayerIsWhite);

        
            
        } else {
            // Handle invalid move
            x = this.xp * 64;
            y = this.yp * 64;
            System.out.println("Invalid move");
        }
    } else {
        // Handle move when it's not the player's turn
        x = this.xp * 64;
        y = this.yp * 64;
        System.out.println("Not your turn");
    }
}


public void captured()
{
	ps.remove(this);
}
public void updateAvailableMove()
{
	availableMoves.clear();
	for (int i=0;i<8;i++)
	{
		for (int j=0;j<8;j++)
		{
			if (isValidMove(i, j))
			{
				availableMoves.add(new Point(i, j));
			}
		}
	}
}
public boolean availableRookMove(int newXp, int newYp) {
    int xDiff = Math.abs(newXp - xp);
    int yDiff = Math.abs(newYp - yp);

    if (xDiff != 0 && yDiff == 0) {
        int step = newXp > xp ? 1 : -1;
        for (int i = 1; i < xDiff; i++) {
            int checkX = xp + (i * step);
            Piece pieceAtPosition = Chess_game.getPiece(checkX * 64, yp * 64);
            if (pieceAtPosition != null) {
                    return false;
            }
        }
        Piece finalPiece = Chess_game.getPiece(newXp * 64, yp * 64);
        if (finalPiece != null && finalPiece.isWhite == isWhite) {
            return false;
        }
        return true;
    } else if (yDiff != 0 && xDiff == 0) {
        int step = newYp > yp ? 1 : -1;
        for (int i = 1; i < yDiff; i++) {
            int checkY = yp + (i * step);
            Piece pieceAtPosition = Chess_game.getPiece(xp * 64, checkY * 64);
            if (pieceAtPosition != null) {
                return false;
            }
        }
        Piece finalPiece = Chess_game.getPiece(xp * 64, newYp * 64);
        if (finalPiece != null && finalPiece.isWhite == isWhite) {
            return false;
        }
        return true;
    }
    return false;
}


public boolean availableBishopMove(int newXp, int newYp) {
    int xDiff = Math.abs(newXp - xp);
    int yDiff = Math.abs(newYp - yp);
    if (xDiff == yDiff) {
        int xStep = newXp > xp ? 1 : -1;
        int yStep = newYp > yp ? 1 : -1;
        for (int i = 1; i < xDiff; i++) {
            int checkX = xp + (i * xStep);
            int checkY = yp + (i * yStep);
            Piece pieceAtPosition = Chess_game.getPiece(checkX * 64, checkY * 64);
            if (pieceAtPosition != null) {
                return false;
            }
        }
        Piece finalPiece = Chess_game.getPiece(newXp * 64, newYp * 64);
        return finalPiece == null || finalPiece.isWhite != isWhite;
    }
    return false;
}


public boolean availableQueenMove(int newXp,int newYp)
{
	return availableRookMove(newXp, newYp) || availableBishopMove(newXp, newYp);
}
public boolean availableKnightMove(int newXp,int newYp)
{
	int xDiff=Math.abs(newXp-xp);
	int yDiff=Math.abs(newYp-yp);
	if (Chess_game.getPiece(newXp*64, newYp*64)!=null)
	{
		if (Chess_game.getPiece(newXp*64, newYp*64).isWhite!=isWhite)
		{
			return (xDiff==2 && yDiff==1) || (xDiff==1 && yDiff==2);
		}
		else
		{
			return false;
		}
	}
	return (xDiff==2 && yDiff==1) || (xDiff==1 && yDiff==2);
}
public boolean availablePawnMove(int newXp, int newYp) {
    int xDiff = Math.abs(newXp - xp);
    int yDiff = newYp-yp;
    Piece lastMovedPiece = Chess_game.getLastMovedPiece();
    if (lastMovedPiece != null &&lastMovedPiece.name.equals("Pawn") && lastMovedPiece.justMovedTwoSquares()) {
    if (isWhite && yp==3 && newXp == lastMovedPiece.xp && newYp-lastMovedPiece.yp==-1 ) {
    	    
        	performEnPassant(newXp, newYp);
        	Game_State.makeMove(this.xp, this.yp, newXp, newYp);
            return true;
         }
        if (!isWhite && yp==4 && newXp == lastMovedPiece.xp && newYp-lastMovedPiece.yp==1) {
        	performEnPassant(newXp, newYp);
            return true;
         }
    } 
    if (Chess_game.getPiece(xp * 64, yp * 64).isWhite) {
        if (yp == 6 && Chess_game.getPiece(newXp*64, newYp*64) == null) {
            if (Chess_game.getPiece(newXp*64, (newYp+1)*64)==null)
            {
            	return (xDiff==0 && yDiff>=-2 && yDiff <0);
            }
        }
    
    } else {
        if (yp == 1 && Chess_game.getPiece(newXp*64, newYp*64) == null) {
        	if (Chess_game.getPiece(newXp*64, (newYp-1)*64)==null)
            {
        		return (xDiff == 0 && yDiff <= 2 && yDiff>0);
            }
        }
    }
    
    if (Chess_game.getPiece(newXp*64, newYp*64) != null) {
        if (Chess_game.getPiece(xp * 64, yp * 64).isWhite != Chess_game.getPiece(newXp * 64, newYp * 64).isWhite) {
        	if (Chess_game.getPiece(xp * 64, yp * 64).isWhite)
        	{
        		return ((xDiff==-1 && yDiff==-1)||(xDiff==1 && yDiff==-1));
        	}
        	else
        	{
        		return ((xDiff==-1 && yDiff==1)||(xDiff==1 && yDiff==1));
        	}
        }
        return false;
    } else {
    	if (Chess_game.getPiece(xp * 64, yp * 64).isWhite)
    	{
    		return (xDiff==0&&yDiff==-1);
    	}
    	else
    	{
    		return (xDiff==0&&yDiff==1);
    	}
    }
    
}
public boolean availableKingMove(int newXp, int newYp) {
    int xDiff = Math.abs(newXp - xp);
    int yDiff = Math.abs(newYp - yp);
    if ((xDiff == 1 && yDiff == 0) || (xDiff == 0 && yDiff == 1) || (xDiff == 1 && yDiff == 1)) {
        if (!isUnderThreat(newXp, newYp, this.isWhite)) {
            if (Chess_game.getPiece(newXp * 64, newYp * 64) == null || Chess_game.getPiece(newXp * 64, newYp * 64).isWhite != isWhite) {
                return true;
            }
        }
    }
    if (yDiff == 0 && xDiff == 2) {
        // King-side castling
        if (!hasMoved)
        {
        	// King-side castling
        	if ( newXp == 6) {
        	    Piece rook = Chess_game.getPiece(7*64, yp*64);
        	    // Check if the squares between the king and the rook are empty and not under threat
        	    if (Chess_game.getPiece(5 * 64, yp * 64) == null && !isUnderThreat(5, yp, this.isWhite) &&
        	        Chess_game.getPiece(6 * 64, yp * 64) == null && !isUnderThreat(6, yp, this.isWhite)&&!rook.hasMoved()
        	        &&!isUnderThreat(4, yp, this.isWhite)) {
        	        return true;
        	    }
        	}

        	// Queen-side castling
        	if ( newXp == 2) {
        		Piece rook = Chess_game.getPiece(0, yp*64);
        	    if (Chess_game.getPiece(1 * 64, yp * 64) == null && !isUnderThreat(2, yp, isWhite) &&
        	        Chess_game.getPiece(2 * 64, yp * 64) == null && !rook.hasMoved() &&
        	        Chess_game.getPiece(3 * 64, yp * 64) == null && !isUnderThreat(3, yp, isWhite)
        	        &&!isUnderThreat(4, yp, isWhite)) {
        	    	
        	        return true;
        	    }
        	}

         }
      }
    return false;
}
    
public boolean basicMoveValidity(int newXp,int newYp) {
	if (newXp<0||newXp>7||newYp<0||newYp>7) {
		return false;
	}
	switch (Chess_game.getPiece(xp*64, yp*64).name)
	{
	case "King":
		return availableKingMove(newXp, newYp);
	case "Queen":
		return availableQueenMove(newXp, newYp);
	case "Rook":
		return availableRookMove(newXp, newYp);
	case "Bishop":
		return availableBishopMove(newXp, newYp);
	case "Knight":
		return availableKnightMove(newXp, newYp);
	case "Pawn":
		return availablePawnMove(newXp, newYp);
		default:
			return false;
	}
}
public boolean isValidMove(int newXp,int newYp)
{
	if (!basicMoveValidity(newXp, newYp)) {
		return false;
	}
	return !isInCheckAfterMove(newXp, newYp);
}
public static ArrayList<Point> getAvailableMoves() {
	return availableMoves;
}
public static void setAvailableMoves(ArrayList<Point> availableMoves) {
	Piece.availableMoves = availableMoves;
}

public boolean isUnderThreat(int x, int y, boolean isWhite) {
    for (Piece piece : ps) {
        if (piece.isWhite != isWhite) {
            if (piece.name.equals("Bishop") || piece.name.equals("Queen")) {
            	//System.out.println(piece.name);
                if (isDiagonalThreat(x, y, piece)) {
                    return true;
                }
            } else if (piece.name.equals("Pawn")) {
                // Check pawn attacks
                if (isPawnThreat(x, y, piece)) {
                    return true;
                }
            }
            // Check other movements (horizontal, vertical, and knight movements)
            if (isOtherThreat(x, y, piece)) {
                return true;
            }
        }
    }
    // If no opponent piece can reach the square, it's not under any threat
    return false;
}
public boolean isDiagonalThreat(int x, int y, Piece piece) {
    int pieceX = piece.xp;
    int pieceY = piece.yp;
    int xDiff = Math.abs(x - pieceX);
    int yDiff = Math.abs(y - pieceY);
    if (xDiff == yDiff) {
        int stepX = (x > pieceX) ? 1 : -1;
        int stepY = (y > pieceY) ? 1 : -1;
        pieceX+=stepX;
        pieceY+=stepY;
        for (int i = 1; i <= xDiff-1; i++) {
            if (Chess_game.getPiece(pieceX*64, pieceY*64)!=null)
            {
            	return false;
            }
            pieceX+=stepX;
            pieceY+=stepY;
        }
        if (pieceX==x&&pieceY==y)
        {
        	return true;
        }
    }
    return false;
}
public boolean isPawnThreat(int x, int y, Piece piece) {
    int pieceX = piece.xp;
    int pieceY = piece.yp;
    if (piece.isWhite) {
        return ((x == pieceX - 1 || x == pieceX + 1) && y == pieceY - 1);
    } else {
        return ((x == pieceX - 1 || x == pieceX + 1) && y == pieceY + 1);
    }
}
private boolean isOtherThreat(int x, int y, Piece piece) {
    int pieceX = piece.xp;
    int pieceY = piece.yp;
    int xDiff = Math.abs(x - pieceX);
    int yDiff = Math.abs(y - pieceY);
    if (xDiff != 0 && yDiff == 0) {
        int step = (x > pieceX) ? 1 : -1;
        pieceX+=step;
        for (int i = 1; i <= xDiff-1; i++) {
            if (Chess_game.getPiece(pieceX*64, pieceY*64)!=null)
            {
            	return false;
            }
            pieceX+=step;
        }
        if (pieceX==x && (piece.name.equals("Rook")|| piece.name.equals("Queen")))
        {
        	return true;
        }
    }
    if (xDiff == 0 && yDiff != 0) {
        int step = (y > pieceY) ? 1 : -1;
        pieceY+=step;
        for (int i = 1; i <= yDiff-1; i++) {
        	if (Chess_game.getPiece(pieceX*64, pieceY*64)!=null)
            {
            	return false;
            }
            pieceY+=step;
        }
        if (pieceY==y && (piece.name.equals("Rook")|| piece.name.equals("Queen")))
        {
        	return true;
        }
    }
    if ((xDiff == 2 && yDiff == 1) || (xDiff == 1 && yDiff == 2)) {
        if (piece.name.equals("Knight")) {
            return true;
        }
    }

    return false;
}
public boolean hasMoved()
{
	return hasMoved;
}
public void setMoved(boolean hasMoved)
{
	this.hasMoved=hasMoved;
}
private void performCastling(int newXp, int newYp) {
	if (newXp > xp) { // King-side castling
        // Move the king
        this.xp = 6;
        this.yp = newYp;
        this.x = this.xp * 64;
        this.y = this.yp * 64;
        
        // Move the rook
        Piece rook = Chess_game.getPiece(7 * 64, newYp * 64);
        if (rook != null && rook.name.equals("Rook")) {
            rook.xp = 5;
            rook.yp = newYp;
            rook.x = rook.xp * 64;
            rook.y = rook.yp * 64;
        }
    } else if (newXp < xp) { // Queen-side castling
        // Move the king
        this.xp = 2;
        this.yp = newYp;
        this.x = this.xp * 64;
        this.y = this.yp * 64;
        
        // Move the rook
        Piece rook = Chess_game.getPiece(0 * 64, newYp * 64);
        if (rook != null && rook.name.equals("Rook")) {
            rook.xp = 3;
            rook.yp = newYp;
            rook.x = rook.xp * 64;
            rook.y = rook.yp * 64;
        }
    }
}
private void performEnPassant(int xp,int yp) {
	if (Math.abs(this.xp-xp)==1) {
		int capturedPawnX = xp;
	    int capturedPawnY = isWhite ? (yp + 1) : (yp - 1);
	    // Capture the pawn
	    lastCapturedPiece.add(Chess_game.getPiece(capturedPawnX * 64, capturedPawnY * 64));
	    Chess_game.getPiece(capturedPawnX * 64, capturedPawnY * 64).captured();


	    // Move the current pawn to the target position
	    
	    Chess_game.setLastMovedPiece(this);
	    this.xp = xp;
	    this.yp = yp;
	    Chess_game.switchTurn();
	    return;
	}
}
public boolean promotionZone()
{
	if (isWhite)
	{
		return yp==0;
	}
	else
	{
		return yp==7;
	}
}
public static boolean isInCheck(boolean isWhite)
{
	for (Piece piece :ps)
	{
		if (piece.name.equals("King") && piece.isWhite==isWhite)
		{
			return piece.isUnderThreat(piece.xp, piece.yp, isWhite);
		}
	}
	return false;
}
private boolean isInCheckAfterMove(int newX, int newY) {
    // Simulate the move and check if the king is in check
    int oldX = this.xp;
    int oldY = this.yp;
    Piece capturedPiece = null;

    Piece pieceAtNewPosition = Chess_game.getPiece(newX * 64, newY * 64);
    if (pieceAtNewPosition != null) {
        capturedPiece = pieceAtNewPosition;
        pieceAtNewPosition = null;
    }

    // Perform the move
    this.xp = newX;
    this.yp = newY;

    // Check if the king is in check
    boolean inCheck = this.isInCheck(this.isWhite);

    // Undo the move
    this.xp = oldX;
    this.yp = oldY;

    if (capturedPiece != null) {
        // Restore the captured piece
        capturedPiece.xp = newX;
        capturedPiece.yp = newY;
    }

    return inCheck;
}
public void revise() {
			Piece piece = new Piece(this.xp, this.yp,this.name,this.isWhite,ps);
			return;
}
public static boolean isCheckmate(boolean isWhite) {
    for (Piece piece : ps) {
        if (piece.isWhite == isWhite) {
            piece.updateAvailableMove();
            if (!piece.availableMoves.isEmpty()) {
                return false;
            }
        }
    }
    return isInCheck(isWhite);
}


public static void handleCheckmate(boolean isWhite) {
	if (isCheckmate(isWhite)) {
		Chess_game.jframe.repaint();
        String winner = isWhite ? "Black" : "White";
        int option = JOptionPane.showOptionDialog(null,
            winner + " wins by checkmate!\nWould you like to play again?",
            "Checkmate",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Restart", "Exit"},
            null);
        if (option == JOptionPane.YES_OPTION) {
        	
            Game_State.restartGame();
        } else {
            System.exit(0);
        }
	}   
}

}
