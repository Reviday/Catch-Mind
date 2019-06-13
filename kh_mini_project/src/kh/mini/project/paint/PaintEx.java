package kh.mini.project.paint;

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
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.RoomInfo;
import kh.mini.project.model.vo.UserInfo;

public class PaintEx extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new PaintEx(1);
	}

	// ������ �ȿ� �ִ� ��ҵ�
	Canvas canvas = new Canvas();

	// ������ ���� ����
	Color mypencolor = Color.black;
	boolean eraser_Sel = false;
	int thick = 8;
	int eraserThick = 30;
	boolean clear_Sel = false;

	// ����
	ShapeSave newshape;

	Vector<Point> sketSP = new Vector<Point>();
	Stack<ShapeSave> shape = new Stack<ShapeSave>();

	// ��ư
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
	private JProgressBar expBar;
	private JPanel panel_6;
	private JPanel panel_7;
	private JLabel lblNewLabel_1;
	
	private JLabel readyImg;
	private JLabel startImg;

// ���� ����
	private String id; // ������� id�� ����
	private int room_No; // ���ӹ� ��ȣ
	private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
	private RoomInfo roomInfo; // �������� ��ü�� �����Ѵ�.

// Network �ڿ� ����
	private DataOutputStream dos;

	public PaintEx(int room_No) {
		// ������� id�� �̾�޾ƿ´�.
		id = MainView.getId();
		// ���ӹ� ��ȣ�� ���̹޾ƿ´�.
		this.room_No = room_No;
		// MainView�κ��� dos�� �̾�޾ƿ´�.
		dos = MainView.getDos();

		// â�� ���ڸ��� �ش� ��� ������ �濡 ������ ������� ������ ���� ������ ������� �޾ƿ������� �޽����� ������.
		//send_message("GameRoomCheck/" + id + "/" + room_No);

		// ������ ����
		setSize(1024, 768);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.lightGray);

		getContentPane().add(canvas);
		canvas.setBounds(219, 70, 570, 500);
		canvas.setBackground(Color.WHITE);
		canvas.setVisible(true);

		//���� ��ư
		color_black = new JButton(new ImageIcon(PaintEx.class.getResource("/images/black1.png")));
		color_black.setBackground(Color.lightGray);
		color_black.setBounds(223, 586, 48, 49);
		color_black.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/black2.png")));
		color_black.setFocusPainted(false);
		color_black.setBorderPainted(false);
		getContentPane().add(color_black);
		color_black.addActionListener(this);
		color_black.setVisible(true);

		color_red = new JButton(new ImageIcon(PaintEx.class.getResource("/images/red1.png")));
		color_red.setBackground(Color.lightGray);
		color_red.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/red2.png")));
		color_red.setFocusPainted(false);
		color_red.setBorderPainted(false);
		color_red.setBounds(276, 586, 48, 49);
		getContentPane().add(color_red);
		color_red.addActionListener(this);
		color_red.setVisible(true);

		color_blue = new JButton(new ImageIcon(PaintEx.class.getResource("/images/blue1.png")));
		color_blue.setBackground(Color.lightGray);
		color_blue.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/blue2.png")));
		color_blue.setFocusPainted(false);
		color_blue.setBorderPainted(false);
		color_blue.setBounds(329, 586, 48, 49);
		getContentPane().add(color_blue);
		color_blue.addActionListener(this);
		color_blue.setVisible(true);

		color_green = new JButton(new ImageIcon(PaintEx.class.getResource("/images/green1.png")));
		color_green.setBackground(Color.lightGray);
		color_green.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/green2.png")));
		color_green.setFocusPainted(false);
		color_green.setBorderPainted(false);
		color_green.setBounds(382, 586, 48, 49);
		getContentPane().add(color_green);
		color_green.addActionListener(this);
		color_green.setVisible(true);

		color_yellow = new JButton(new ImageIcon(PaintEx.class.getResource("/images/yellow1.png")));
		color_yellow.setBackground(Color.lightGray);
		color_yellow.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/yellow2.png")));
		color_yellow.setFocusPainted(false);
		color_yellow.setBorderPainted(false);
		color_yellow.setVisible(true);
		color_yellow.setBounds(435, 586, 48, 49);
		getContentPane().add(color_yellow);
		color_yellow.addActionListener(this);

		//���찳 ��ư
		eraser = new JButton("eraser");
		eraser.setBackground(Color.lightGray);
		eraser.setFocusPainted(false);
		eraser.setBounds(487, 586, 89, 49);
		getContentPane().add(eraser);
		eraser.addActionListener(this);
		eraser.setVisible(true);

		//Ŭ���� ��ư
		clear = new JButton("clear");
		clear.setBackground(Color.lightGray);
		clear.setFocusPainted(false);
		clear.setBounds(581, 586, 89, 49);
		getContentPane().add(clear);
		clear.addActionListener(this);

		//�� ���� ��ư
		thick_Bold = new JButton("���� ��");
		thick_Bold.setBackground(Color.lightGray);
		thick_Bold.setBounds(682, 586, 97, 23);
		thick_Bold.setFocusPainted(false);
		getContentPane().add(thick_Bold);
		thick_Bold.addActionListener(this);
		thick_Bold.setVisible(true);

		thick_Sharp = new JButton("���� ��");
		thick_Sharp.setBackground(Color.lightGray);
		thick_Sharp.setBounds(682, 612, 97, 23);
		thick_Sharp.setFocusPainted(false);
		getContentPane().add(thick_Sharp);
		thick_Sharp.addActionListener(this);
		thick_Sharp.setVisible(true);

		JButton exit = new JButton("������"); // ��ư �׼� �ؾߵ�
		exit.setBounds(868, 21, 97, 37);
		exit.setBackground(Color.lightGray);
		exit.setFocusPainted(false);
		getContentPane().add(exit);
		exit.setVisible(true);

		//ĳ����â �ӽ÷� �־�а�
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

		JPanel panel = new JPanel();
		panel.setBounds(14, 70, 198, 147);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(14, 12, 87, 123);
		panel.add(lblNewLabel);

		panel_6 = new JPanel();
		panel_6.setBounds(107, 12, 77, 34);
		panel.add(panel_6);

		panel_7 = new JPanel();
		panel_7.setBounds(107, 101, 77, 34);
		panel.add(panel_7);

		lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(107, 53, 77, 42);
		panel.add(lblNewLabel_1);

		// ����ġ ǥ��
		expBar = new JProgressBar();
		expBar.setBounds(284, 695, 495, 14);
		getContentPane().add(expBar);
		expBar.setValue(50);
		expBar.setBackground(Color.white);
		expBar.setForeground(Color.gray);
		
		//ready�̹���
		readyImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/image/readyImg.png")));
		getContentPane().add(readyImg);
		readyImg.setBounds(356,169,300,300);
		readyImg.setVisible(true);
		
		//start�̹���
		startImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/startImg.png")));
		getContentPane().add(startImg);
		startImg.setBounds(356,169,300,300);
		startImg.setVisible(false);
		
		getContentPane().add(canvas);
		canvas.setVisible(true);
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	// �������� �޽����� ������ �κ�
//	private void send_message(String str) {
//		try {
//			dos.writeUTF(str);
//		} catch (IOException e) // ���� ó�� �κ�
//		{
//			e.printStackTrace();
//		}
//	}

	// �����κ��� ������ ��� �޽���
	private void Inmessage(String str) {
		st = new StringTokenizer(str, "@"); // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ => [ NewUser/�����ID ] ���·� ����

		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String Message = st.nextToken(); // �޽����� �����Ѵ�.

		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + Message);

		// protocol ���� ó��
		switch (protocol) {
		// #���� ó���� �޾ƿ��� �� ����
		case "RoomInfo":
			String room_Name = st.nextToken(); // ������
			String room_Pw = st.nextToken(); // ��й�ȣ
			int fixed_User = Integer.parseInt(st.nextToken()); // �ִ� �ο�(����)
			int uCount = Integer.parseInt(st.nextToken()); // ���� �ο� ��

			// ���� ������ roomInfo ��ü�� �����Ѵ�.
			roomInfo = new RoomInfo(room_No, room_Name, room_Pw, uCount, fixed_User);

			break;

		// #������ ���� �޾ƿ���(����� ������ ����)
		case "UserInfo":
			// ������� ������ ������ �����Ѵ�.
			int level = Integer.parseInt(st.nextToken()); // ����
			int exp = Integer.parseInt(st.nextToken()); // ����ġ
			int corAnswer = Integer.parseInt(st.nextToken()); // ���� �����

			// ������ ������ ��ü�� ����
			UserInfo userInfo = new UserInfo(Message, level, exp, corAnswer);

			// �ش� ��ü�� Vector�� �߰�(���� ��ü�� RooInfo ��ü�� ���Ϳ� �����Ѵ�)
			roomInfo.addRoom_user_vc(userInfo);

			// ���� �г��� �����Ѵ�.
			/*
			 * ���� �г� ���� �ڵ�
			 */

			break;

		// #���� �������� ������ �޾ƿ´�.
		case "OldUser":
			// ���� ������� ������ ������ �����Ѵ�.
			level = Integer.parseInt(st.nextToken()); // ����
			exp = Integer.parseInt(st.nextToken()); // ����ġ
			corAnswer = Integer.parseInt(st.nextToken()); // ���� �����

			// ������ ������ ��ü�� ����
			UserInfo oldUser = new UserInfo(Message, level, exp, corAnswer);

			// �ش� ��ü�� Vector�� �߰�(���� ��ü�� RooInfo ��ü�� ���Ϳ� �����Ѵ�)
			roomInfo.addRoom_user_vc(oldUser);
			
			// ���� �������� ������ �ް� �̾ ������ ������ �̾�����Ƿ�, ��� ���� �Ŀ� �г� ������Ʈ�� �����Ѵ�.
			break;
		}

	}

	// MainView Ŭ�������� Paint Ŭ������ �޽����� �����ϱ� ���� ����ϴ� �޼ҵ�
	public void paint_Inmessage(String str) {
		Inmessage(str);
	}

	// class StartPnThread

	// �׸���
	class Canvas extends JPanel {

		MyMouseListener ml = new MyMouseListener();

		// �׸��� ���콺 ������
		Canvas() {
			addMouseListener(ml);
			addMouseMotionListener(ml);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;

			// �׸� �׸���
			if (clear_Sel) {
				clear_Sel = false;
			}

			else {
				for (int i = 0; i < shape.size(); i++) {
					g2.setStroke(new BasicStroke(shape.get(i).thick, BasicStroke.CAP_ROUND, 0));
					g2.setPaint(shape.get(i).mypencolor);
					for (int j = 1; j < shape.get(i).sketchSP.size(); j++)
						g2.drawLine(shape.get(i).sketchSP.get(j - 1).x, shape.get(i).sketchSP.get(j - 1).y,
								shape.get(i).sketchSP.get(j).x, shape.get(i).sketchSP.get(j).y);
				}
			}

			// �ܻ� �׸���
			if (eraser_Sel) {
				g2.setStroke(new BasicStroke(eraserThick, BasicStroke.CAP_ROUND, 0));
				for (int i = 1; i < sketSP.size(); i++) {
					g2.setPaint(mypencolor);
					g2.drawLine(sketSP.get(i - 1).x, sketSP.get(i - 1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
				g2.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, 0));
			} else {

				g2.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, 0));
				for (int i = 1; i < sketSP.size(); i++) {
					g2.setPaint(mypencolor);
					g2.drawLine(sketSP.get(i - 1).x, sketSP.get(i - 1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
			}

		}

	}

	
	//������ ���� ����ؼ� ����ġ �ٿ� �� ����
	public void printExp(int exp, int level) {
		switch (level) {
		case 1:
			expBar.setValue((exp / 10) * 100);
			break;
		case 2:
			expBar.setValue((exp / 40) * 100);
			break;
		case 3:
			expBar.setValue((exp / 80) * 100);
			break;
		case 4:
			expBar.setValue((exp / 130) * 100);
			break;
		case 5:
			expBar.setValue((exp / 200) * 100);
			break;
		case 6:
			expBar.setValue((exp / 290) * 100);
			break;
		case 7:
			expBar.setValue((exp / 400) * 100);
			break;
		case 8:
			expBar.setValue((exp / 540) * 100);
			break;
		case 9:
			expBar.setValue((exp / 710) * 100);
			break;
		case 10:
			expBar.setValue((exp / 910) * 100);
			break;
		case 11:
			expBar.setValue((exp / 1150) * 100);
			break;
		case 12:
			expBar.setValue((exp / 1430) * 100);
			break;
		case 13:
			expBar.setValue((exp / 1750) * 100);
			break;
		}
	}
	
	//��ǥ �ο����� �����ϸ� 3�ʵڿ� ���� �ڵ������ϴ� ������(�ο����� ���� ������ ���۵Ǵ°� ���� ����X)
	class StartThread extends Thread{
		@Override
		public void run() {
			try {
				sleep(3000);
				ReadyImgThread rit = new ReadyImgThread();
				rit.start();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//���� ���� �� Ready�̹��� 1.5�ʶ��� ������� ������
	class ReadyImgThread extends Thread{
		@Override
		public void run() {
			try {
				readyImg.setVisible(true);
				sleep(1500);
				readyImg.setVisible(false);
				StartImgThread sit = new StartImgThread();
				sit.start();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	class StartImgThread extends Thread{
		@Override
		public void run() {
			try {
				startImg.setVisible(true);
				sleep(1500);
				startImg.setVisible(false);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

	class MyMouseListener extends MouseAdapter implements MouseMotionListener {

		
		public void mousePressed(MouseEvent e) {
			newshape = new ShapeSave();
			newshape.mypencolor = mypencolor;

			if (eraser_Sel)
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

		if (e.getSource() == clear) {
			clear_Sel = true;
			canvas.repaint();
			while (!shape.isEmpty())
				shape.pop();
		} else {
			if (e.getSource() == eraser) {
				eraser_Sel = true;
				eraserThick = 30;
				mypencolor = Color.white;
			} else {
				eraser_Sel = false;
				if (e.getSource() == thick_Bold)
					thick = 8;
				else if (e.getSource() == thick_Sharp)
					thick = 3;
				else if (e.getSource() == color_black)
					mypencolor = Color.black;
				else if (e.getSource() == color_red)
					mypencolor = Color.red;
				else if (e.getSource() == color_blue)
					mypencolor = Color.blue;
				else if (e.getSource() == color_green)
					mypencolor = new Color(0, 192, 0);
				else if (e.getSource() == color_yellow)
					mypencolor = Color.yellow;
			}
		}
	}
}
