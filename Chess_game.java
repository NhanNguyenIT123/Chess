package chess_game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class Chess_game {
public static LinkedList<Piece> ps =new LinkedList<>();
public static Piece selectedPiece ;
public static ArrayList<Point> availableMoves;
public static ChessPanel chessPanel;
private static Piece lastMovedPiece;
public static boolean whiteTurn = true;
public Game_State game_State;
public static JFrame jframe;
public static void setLastMovedPiece(Piece piece)
{
	lastMovedPiece=piece;
}
public static Piece getLastMovedPiece() {
	return lastMovedPiece;
}
public static void main(String[] args) throws IOException {
	BufferedImage all=ImageIO.read(new File("C:\\Users\\HP\\Downloads\\chess.png"));
	Image img[]=new Image[12];
	int idx=0;
	for (int y=0;y<400;y+=200)
	{
		for (int x=0;x<1200;x+=200)
		{
			img[idx]=all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
			idx++;
		}
	}
	Piece wking=new Piece(4, 7, "King",true, ps);
	wking.setMoved(false);
	Piece wqueen=new Piece(3, 7, "Queen",true, ps);
	Piece wrook_1=new Piece(0, 7, "Rook",true, ps);
	wrook_1.setMoved(false);
	Piece wrook_2=new Piece(7, 7, "Rook",true, ps);
	wrook_2.setMoved(false);
	Piece wbishop_dark=new Piece(2, 7, "Bishop",true, ps);
	Piece wbishop_light=new Piece(5, 7, "Bishop",true, ps);
	Piece wknight_1=new Piece(1, 7, "Knight",true, ps);
	Piece wknight_2=new Piece(6, 7, "Knight",true, ps);
	Piece wpawn_1=new Piece(0,6,"Pawn",true,ps);
	Piece wpawn_2=new Piece(1,6,"Pawn",true,ps);
	Piece wpawn_3=new Piece(2,6,"Pawn",true,ps);
	Piece wpawn_4=new Piece(3,6,"Pawn",true,ps);
	Piece wpawn_5=new Piece(4,6,"Pawn",true,ps);
	Piece wpawn_6=new Piece(5,6,"Pawn",true,ps);
	Piece wpawn_7=new Piece(6,6,"Pawn",true,ps);
	Piece wpawn_8=new Piece(7,6,"Pawn",true,ps);
	
	
	Piece bking=new Piece(4, 0, "King",false, ps);
	bking.setMoved(false);
	Piece bqueen=new Piece(3, 0, "Queen",false, ps);
	Piece brook_1=new Piece(0, 0, "Rook",false, ps);
	brook_1.setMoved(false);
	Piece brook_2=new Piece(7, 0, "Rook",false, ps);
	brook_2.setMoved(false);
	Piece bbishop_dark=new Piece(2, 0, "Bishop",false, ps);
	Piece bbishop_light=new Piece(5, 0, "Bishop",false, ps);
	Piece bknight_1=new Piece(1, 0, "Knight",false, ps);
	Piece bknight_2=new Piece(6, 0, "Knight",false, ps);
	Piece bpawn_1=new Piece(0,1,"Pawn",false,ps);
	Piece bpawn_2=new Piece(1,1,"Pawn",false,ps);
	Piece bpawn_3=new Piece(2,1,"Pawn",false,ps);
	Piece bpawn_4=new Piece(3,1,"Pawn",false,ps);
	Piece bpawn_5=new Piece(4,1,"Pawn",false,ps);
	Piece bpawn_6=new Piece(5,1,"Pawn",false,ps);
	Piece bpawn_7=new Piece(6,1,"Pawn",false,ps);
	Piece bpawn_8=new Piece(7,1,"Pawn",false,ps);
	
	
    jframe=new JFrame();
	JLabel whiteClockLabel = new JLabel();
	JLabel blackClockLabel = new JLabel();
	JPanel clockPanel = new JPanel();
	clockPanel.add(whiteClockLabel);
	clockPanel.add(blackClockLabel);
	JButton button = new JButton("bruh");
	clockPanel.add(button);
	clockPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	clockPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 15));
	jframe.add(clockPanel,BorderLayout.EAST);
	jframe.setResizable(false);
	jframe.setLocationRelativeTo(jframe);
	jframe.setLayout(new BorderLayout());
	jframe.setSize(900, 550);
	chessPanel=new ChessPanel(ps,img);
	jframe.add(chessPanel,BorderLayout.CENTER);
	JFrame promotionFrame = new JFrame("Promotion Options");
    promotionFrame.setSize(300, 150);
    promotionFrame.setLayout(new BorderLayout());
    JPanel promotionPanel = new JPanel();
    promotionFrame.add(promotionPanel, BorderLayout.CENTER);
    String[] options = {"Queen", "Rook", "Bishop", "Knight"};
    JComboBox<String> comboBox = new JComboBox<>(options);
    promotionPanel.add(comboBox);
    JButton promoteButton = new JButton("Promote");
    promotionPanel.add(promoteButton);
    promoteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedPromotion = (String) comboBox.getSelectedItem();
            if (selectedPiece != null && selectedPromotion != null) {
                selectedPiece.setPromotedType(selectedPromotion);
               boolean color = selectedPiece.isWhite;
                Piece.handleCheckmate(!color);
                
            }
            promotionFrame.setVisible(false);
            chessPanel.repaint();
        }
    });
    promotionFrame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            if (selectedPiece != null) {
                selectedPiece.setPromotedType("Queen");
                boolean color = selectedPiece.isWhite;
                Piece.handleCheckmate(!color);
                chessPanel.repaint();
            }
        }
    });
    jframe.addMouseMotionListener(new MouseMotionListener() {
 		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if (selectedPiece!=null)
			{
				selectedPiece.x=e.getX();
				selectedPiece.y=e.getY();
				jframe.repaint();
			    
			}
			
		}
	});
	
    jframe.addMouseListener(new MouseListener() {
		
    	@Override
		public void mousePressed(MouseEvent e) {
			selectedPiece=getPiece(e.getX(), e.getY());
			if (selectedPiece!=null)
			{
				selectedPiece.updateAvailableMove();
				availableMoves=selectedPiece.getAvailableMoves();
				jframe.repaint();
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (selectedPiece!=null)
			{
				int newXp=e.getX()/64;
				int newYp=e.getY()/64;
				Game_State.makeMove(selectedPiece.xp, selectedPiece.yp, newXp, newYp);
				selectedPiece.move(newXp, newYp);
				if (selectedPiece.name.equals("Pawn")&& selectedPiece.promotionZone())
				{
					promotionFrame.setVisible(true);
					Piece.isPromoted = true;
				}
				jframe.repaint();
			}
			
		}
		
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			selectedPiece=getPiece(e.getX(), e.getY());
			int x =e.getX();
			int y =e.getY();
			}
	});
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    JButton unmove_Button = new JButton("Unmove");
    unmove_Button.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("Unmove")) {
				Game_State.unmove();
				jframe.repaint();
			}
			
		}
	});
    buttonPanel.add(unmove_Button);
    jframe.add(buttonPanel,BorderLayout.EAST);
    
	jframe.setTitle("Game of chess");
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);

}
public static Piece getPiece(int x,int y)
{
	int xp=x/64;
	int yp=y/64;
	for (Piece p:ps)
	{
		if (p.xp==xp&&p.yp==yp)
		{
			return p;
		}
	}
	return null;
}
public static void switchTurn()
{
	whiteTurn=!whiteTurn;
}
public static boolean isWhiteTurn() {
	return whiteTurn;
}

}
