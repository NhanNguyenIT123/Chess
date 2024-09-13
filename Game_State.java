package chess_game;

import java.util.Stack;

public class Game_State {
private Piece[][] board;
public static Stack<int[]> moveHistory = new Stack<int[]>();

public Game_State() {
	board = new Piece[8][8];
	
	
	board[0][0] = new Piece(Piece_Type.Rook,false);
	board[1][0] = new Piece(Piece_Type.Knight,false);
	board[2][0] = new Piece(Piece_Type.Bishop,false);
	board[3][0] = new Piece(Piece_Type.Queen,false);
	board[4][0] = new Piece(Piece_Type.King,false);
	board[5][0] = new Piece(Piece_Type.Bishop,false);
	board[6][0] = new Piece(Piece_Type.Knight,false);
	board[7][0] = new Piece(Piece_Type.Rook,false);
	board[0][1] = new Piece(Piece_Type.Pawn,false);
	board[1][1] = new Piece(Piece_Type.Pawn,false);
	board[2][1] = new Piece(Piece_Type.Pawn,false);
	board[3][1] = new Piece(Piece_Type.Pawn,false);
	board[4][1] = new Piece(Piece_Type.Pawn,false);
	board[5][1] = new Piece(Piece_Type.Pawn,false);
	board[6][1] = new Piece(Piece_Type.Pawn,false);
	board[7][1] = new Piece(Piece_Type.Pawn,false);
	
	
	board[0][7] = new Piece(Piece_Type.Rook,true);
	board[1][7] = new Piece(Piece_Type.Knight,true);
	board[2][7] = new Piece(Piece_Type.Bishop,true);
	board[3][7] = new Piece(Piece_Type.Queen,true);
	board[4][7] = new Piece(Piece_Type.King,true);
	board[5][7] = new Piece(Piece_Type.Bishop,true);
	board[6][7] = new Piece(Piece_Type.Knight,true);
	board[7][7] = new Piece(Piece_Type.Rook,true);
	board[0][6] = new Piece(Piece_Type.Pawn,true);
	board[1][6] = new Piece(Piece_Type.Pawn,true);
	board[2][6] = new Piece(Piece_Type.Pawn,true);
	board[3][6] = new Piece(Piece_Type.Pawn,true);
	board[4][6] = new Piece(Piece_Type.Pawn,true);
	board[5][6] = new Piece(Piece_Type.Pawn,true);
	board[6][6] = new Piece(Piece_Type.Pawn,true);
	board[7][6] = new Piece(Piece_Type.Pawn,true);
	
	
}
public static void makeMove(int fromX,int fromY,int toX,int toY) {
	int [] move = {fromX,fromY,toX,toY};
	moveHistory.add(move);
	
}
public static void unmove() {
	try {
		if ( moveHistory !=null && !moveHistory.isEmpty()) {
			int [] move = moveHistory.pop();
			int fromX = move[0];
			int fromY  = move[1];
			int toX = move[2];
			int toY = move[3];
			Piece piece = Chess_game.getPiece(toX*64, toY*64);
			if (piece!=null) {
				piece.xp = fromX;
				piece.yp = fromY;
				piece.x=piece.xp*64;
				piece.y=piece.yp*64;
				if (Piece.lastCapturedPiece!=null && !Piece.lastCapturedPiece.isEmpty() ) {
					Piece p = Piece.lastCapturedPiece.pop();
					if (p!=null)
					{
					p.revise();
					}
				}
                if (Piece.isPromoted)
                {
                	piece.name = "Pawn";
                	Piece.isPromoted = false;
                }
				if (piece.name.equals("King") && Math.abs(toX - fromX) == 2) {
                    if (toX == 6) { // King-side castling
                        Piece rook = Chess_game.getPiece(5 * 64, toY * 64);
                        if (rook != null && rook.name.equals("Rook")) {
                            rook.xp = 7;
                            rook.yp = toY;
                            rook.x = rook.xp * 64;
                            rook.y = rook.yp * 64;
                            piece.hasMoved = false;
                            rook.hasMoved = false;
                        }
                    } else if (toX == 2) { // Queen-side castling
                        Piece rook = Chess_game.getPiece(3 * 64, toY * 64);
                        if (rook != null && rook.name.equals("Rook")) {
                            rook.xp = 0;
                            rook.yp = toY;
                            rook.x = rook.xp * 64;
                            rook.y = rook.yp * 64;
                            piece.hasMoved = false;
                            rook.hasMoved = false;
                        }
                    }
                }

				Chess_game.switchTurn();
			}
			else {
				System.out.println("Error");
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public static void restartGame() {
	while (!moveHistory.isEmpty()) {
        unmove();
    }
	Chess_game.whiteTurn = true;
	Chess_game.jframe.repaint();
}
}
