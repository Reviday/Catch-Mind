package paint.practice;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.JLabel;



public class PaintEx extends JFrame implements ActionListener{
	
	public static void main(String[] args) {
		new PaintEx();
	}
	
	
	
	//프레임 안에 있는 요소들
	Canvas canvas = new Canvas();
	
	//설정을 위한 변수
	Color mypencolor = Color.black;
	boolean eraser_Sel=false;
	int thick = 8;
	int eraserThick = 30;
	boolean clear_Sel = false;
	
	//도형
	ShapeSave newshape;
	
	Vector<Point> sketSP = new Vector<Point>();
	Stack<ShapeSave> shape = new Stack<ShapeSave>();
	
	//버튼
	private JButton thick_Bold;
	private JButton thick_Sharp;
	private JButton eraser;

	private JButton color_yellow;
	private JButton color_blue;
	private JButton color_green;
	private JButton color_red;
	private JButton clear;
	private JButton color_black;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	
	
	
	public PaintEx() {
		
		//프레임 설정
		setSize(1024,768);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.lightGray);
		
		//Canvas sketchBook = new Canvas();
		getContentPane().add(canvas);
		canvas.setBounds(219, 70, 570, 500);
		canvas.setBackground(Color.WHITE);
		canvas.setVisible(true);
		
		color_black = new JButton(new ImageIcon(PaintEx.class.getResource("/image/black1.png")));
		color_black.setBackground(Color.lightGray);
		color_black.setBounds(223, 586, 48, 49);
		color_black.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/image/black2.png")));
		color_black.setFocusPainted(false);
		color_black.setBorderPainted(false);
		getContentPane().add(color_black);
		color_black.addActionListener(this);
		color_black.setVisible(true);
		
		color_red = new JButton(new ImageIcon(PaintEx.class.getResource("/image/red1.png")));
		color_red.setBackground(Color.lightGray);
		color_red.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/image/red2.png")));
		color_red.setFocusPainted(false);
		color_red.setBorderPainted(false);
		color_red.setBounds(276, 586, 48, 49);
		getContentPane().add(color_red);
		color_red.addActionListener(this);
		color_red.setVisible(true);
		
		color_blue = new JButton(new ImageIcon(PaintEx.class.getResource("/image/blue1.png")));
		color_blue.setBackground(Color.lightGray);
		color_blue.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/image/blue2.png")));
		color_blue.setFocusPainted(false);
		color_blue.setBorderPainted(false);
		color_blue.setBounds(329, 586, 48, 49);
		getContentPane().add(color_blue);
		color_blue.addActionListener(this);
		color_blue.setVisible(true);
		
		color_green = new JButton(new ImageIcon(PaintEx.class.getResource("/image/green1.png")));
		color_green.setBackground(Color.lightGray);
		color_green.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/image/green2.png")));
		color_green.setFocusPainted(false);
		color_green.setBorderPainted(false);
		color_green.setBounds(382, 586, 48, 49);
		getContentPane().add(color_green);
		color_green.addActionListener(this);
		color_green.setVisible(true);
		
		color_yellow = new JButton(new ImageIcon(PaintEx.class.getResource("/image/yellow1.png")));
		color_yellow.setBackground(Color.lightGray);
		color_yellow.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/image/yellow2.png")));
		color_yellow.setFocusPainted(false);
		color_yellow.setBorderPainted(false);
		color_yellow.setVisible(true);
		color_yellow.setBounds(435, 586, 48, 49);
		getContentPane().add(color_yellow);
		color_yellow.addActionListener(this);
		
		eraser = new JButton("eraser");
		eraser.setBackground(Color.lightGray);
		eraser.setFocusPainted(false);
		eraser.setBounds(487,586,89,49);
		getContentPane().add(eraser);
		eraser.addActionListener(this);
		eraser.setVisible(true);
		
		clear = new JButton("clear");
		clear.setBackground(Color.lightGray);
		clear.setFocusPainted(false);	
		clear.setBounds(581, 586, 89, 49);
		getContentPane().add(clear);
		clear.addActionListener(this);
		
		thick_Bold = new JButton("굵은 펜");
		thick_Bold.setBackground(Color.lightGray);
		thick_Bold.setBounds(682, 586, 97, 23);
		thick_Bold.setFocusPainted(false);
		getContentPane().add(thick_Bold);
		thick_Bold.addActionListener(this);
		thick_Bold.setVisible(true);
		
		thick_Sharp = new JButton("얇은 펜");
		thick_Sharp.setBackground(Color.lightGray);
		thick_Sharp.setBounds(682, 612, 97, 23);
		thick_Sharp.setFocusPainted(false);
		getContentPane().add(thick_Sharp);
		thick_Sharp.addActionListener(this);
		thick_Sharp.setVisible(true);
		
		JButton exit = new JButton("나가기");	//버튼 액션 해야됨
		exit.setBounds(868, 21, 97, 37);
		exit.setBackground(Color.lightGray);
		exit.setFocusPainted(false);
		getContentPane().add(exit);
		exit.setVisible(true);
		
	
		
		panel_1 = new JPanel();
		panel_1.setBounds(12, 246, 198, 147);
		getContentPane().add(panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBounds(12, 423, 198, 147);
		getContentPane().add(panel_2);
		
		panel_3 = new JPanel();
		panel_3.setBounds(798, 70, 198, 147);
		getContentPane().add(panel_3);
		
		panel_4 = new JPanel();
		panel_4.setBounds(798, 246, 198, 147);
		getContentPane().add(panel_4);
		
		panel_5 = new JPanel();
		panel_5.setBounds(798, 423, 198, 147);
		getContentPane().add(panel_5);
		
		lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon(PaintEx.class.getResource("/image/character1.png")));
		lblNewLabel_1.setBounds(22, 87, 89, 112);
		getContentPane().add(lblNewLabel_1);
		
		lblNewLabel = new JLabel("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
		lblNewLabel.setIcon(new ImageIcon(PaintEx.class.getResource("/image/userbackground.png")));
		lblNewLabel.setBounds(12, 70, 198, 147);
		getContentPane().add(lblNewLabel);;
		
		
		
		
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//그림판
	class Canvas extends JPanel{
		
		MyMouseListener ml = new MyMouseListener();
		//그림판 마우스 리스너
		Canvas(){
			 addMouseListener(ml);
			 addMouseMotionListener(ml);
		 }
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			
			//그림 그리기
			if(clear_Sel) {
				clear_Sel=false;
			}
			
			else {
				for(int i=0;i<shape.size();i++) {
				g2.setStroke(new BasicStroke(shape.get(i).thick, BasicStroke.CAP_ROUND,0));
				g2.setPaint(shape.get(i).mypencolor);
				for(int j=1;j<shape.get(i).sketchSP.size();j++)
				g2.drawLine(shape.get(i).sketchSP.get(j-1).x,  shape.get(i).sketchSP.get(j-1).y, shape.get(i).sketchSP.get(j).x, shape.get(i).sketchSP.get(j).y);
				}
				
			}
				
			//잔상 그리기
			
			if(eraser_Sel) {
				g2.setStroke(new BasicStroke(eraserThick, BasicStroke.CAP_ROUND,0));
				for(int i=1;i<sketSP.size();i++)
				{
					g2.setPaint(mypencolor);
					g2.drawLine(sketSP.get(i-1).x, sketSP.get(i-1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
				g2.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND,0));
			}
			else {
				
				g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
				for(int i=1;i<sketSP.size();i++)
				{
					g2.setPaint(mypencolor);
					g2.drawLine(sketSP.get(i-1).x, sketSP.get(i-1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
			}
			
			
		}
		 
	}
	
	class MyMouseListener extends MouseAdapter implements MouseMotionListener{

		public void mousePressed(MouseEvent e) {
			newshape = new ShapeSave();
			newshape.mypencolor=mypencolor;
			
			if(eraser_Sel)
				newshape.thick = eraserThick;
			else
				newshape.thick = thick;
			
		}
		
		public void mouseReleased(MouseEvent e) {
			shape.add(newshape);
			sketSP.removeAllElements();
			repaint();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			
			newshape.sketchSP.add(e.getPoint());
			sketSP.add(e.getPoint());
			repaint();
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==clear) {
			clear_Sel=true;
			canvas.repaint();
			while(!shape.isEmpty())
				shape.pop();
		}
		else {
			if(e.getSource()==eraser) {
				eraser_Sel=true;
				eraserThick=30;
				mypencolor=Color.white;
			}
			else {
				eraser_Sel=false;
				if(e.getSource()==thick_Bold) 
					thick=8;
				else if(e.getSource()==thick_Sharp) 
					thick=3;	
				else if(e.getSource()==color_black) 
					mypencolor=Color.black;	
				else if(e.getSource()==color_red)
					mypencolor=Color.red;
				else if(e.getSource()==color_blue)
					mypencolor=Color.blue;
				else if(e.getSource()==color_green)
					mypencolor=new Color(0,192,0);
				else if(e.getSource()==color_yellow)
					mypencolor=Color.yellow;
			}
		}
	}
}























