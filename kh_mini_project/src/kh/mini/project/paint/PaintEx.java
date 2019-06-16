package kh.mini.project.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.RoomInfo;
import kh.mini.project.model.vo.UserInfo;

public class PaintEx extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new PaintEx(1);
	}

	StartThread startT;


	// ������ �ȿ� �ִ� ��ҵ�
	Canvas canvas = new Canvas();
	
	//�޴��� ������
	private JLabel menuBar;
	private int mouseX, mouseY;

	// �׸��� ������ ���� ����
	Color mypencolor = Color.black;
	boolean eraser_Sel = false;
	int thick = 8;
	int eraserThick = 30;
	boolean clear_Sel = false;
	String colorCode="black"; //�� ���� �����ϱ����� �ڵ弳��
	int receiveThick;
	boolean receiveEraserSel=false;

	// ����
	ShapeSave newshape;
	ShapeSave mainshape;

	ArrayList<Point> sketSP = new ArrayList<Point>();
	Stack<ShapeSave> shape = new Stack<ShapeSave>();
	
	ArrayList<Point> subSP = new ArrayList<Point>();
	Stack<ShapeSave> receiveshape = new Stack<ShapeSave>();
	

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
	private JButton giveUpBt;
	
	private JProgressBar expBar;
	
	private JLabel levelUpImg;
	private JLabel readyImg;
	private JLabel startImg;
	
	Point maindrow=new Point();
	Point subdrow=new Point();

// ���� ����
	private String id; // ������� id�� ����
	private int room_No; // ���ӹ� ��ȣ
	private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
	private RoomInfo roomInfo; // �������� ��ü�� �����Ѵ�.
	private Toolkit tk = Toolkit.getDefaultToolkit();
	
	JPanel cursorPanel = new JPanel();
	
	Image pen_black = tk.getImage(PaintEx.class.getResource("/images/pen_black.png"));
	Image pen_red = tk.getImage(PaintEx.class.getResource("/images/pen_red.png"));
	Image pen_blue = tk.getImage(PaintEx.class.getResource("/images/pen_blue.png"));
	Image pen_green = tk.getImage(PaintEx.class.getResource("/images/pen_green.png"));
	Image pen_yellow = tk.getImage(PaintEx.class.getResource("/images/pen_yellow.png"));
	Image eraserImg = tk.getImage(PaintEx.class.getResource("/images/eraser.png"));
	Image clearImg = tk.getImage(PaintEx.class.getResource("/images/clear.png"));
	
	
	Cursor blackCursor = tk.createCustomCursor(pen_black, new Point(10,10), "WaterDrop");
	Cursor redCursor = tk.createCustomCursor(pen_red, new Point(10,10), "WaterDrop");
	Cursor blueCursor = tk.createCustomCursor(pen_blue, new Point(10,10), "WaterDrop");
	Cursor greenCursor = tk.createCustomCursor(pen_green, new Point(10,10), "WaterDrop");
	Cursor yellowCursor = tk.createCustomCursor(pen_yellow, new Point(10,10), "WaterDrop");
	Cursor eraserCursor = tk.createCustomCursor(eraserImg, new Point(10,10), "WaterDrop");
	Cursor clearCursor = tk.createCustomCursor(clearImg, new Point(10,10), "WaterDrop");
	
	Cursor myCursor;
	
	private JLabel[] user_lb = new JLabel[6]; // ���� ������ ��� �� (�ִ� �μ� 6�� ����)
	private JLabel[] chatting_lb = new JLabel[6]; // ������ ä���� ��ǳ������ ���� ��(�ִ� �ο� 6�� ����)
	private JLabel[] chattingCover_lb = new JLabel[6];
	private JTextArea[] chatting_ta = new JTextArea[6]; 
	private JPanel[] user_pn = new JPanel[6];
// Label
	private JLabel mainMenuBar = new JLabel();
	
// Textfield
	private JTextField chatting_tf; // ä�� ������ �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�	
	private ImageIcon chatAreaBackground = new ImageIcon(PaintEx.class.getResource("/images/wordBubble.png"));
	
	
// Network �ڿ� ����
	private DataOutputStream dos;

	public PaintEx(int room_No) {
		// ������� id�� �̾�޾ƿ´�.
		id = MainView.getId();
		// ���ӹ� ��ȣ�� ���̹޾ƿ´�.
		this.room_No = room_No;
		// MainView�κ��� dos�� �̾�޾ƿ´�.
		dos = MainView.getDos();

		
		createUserPanel();

		createChattingLabel();

		Font font = new Font("�޸�����ü", Font.BOLD, 17); // ��Ʈ����

		// ä�� �Է�â
		chatting_tf = new JTextField();
		chatting_tf.setBounds(280, 674, 300, 25);
		chatting_tf.setOpaque(true);
		chatting_tf.setDocument(new JTextFieldLimit(20)); // ä�� 45�� ����
		chatting_tf.setFont(font);
		chatting_tf.addKeyListener(new keyAdapter()); // Ŭ������ ������ Ű �̺�Ʈ�� ����
		add(chatting_tf);
		
		
		
		
		// â�� ���ڸ��� �ش� ��� ������ �濡 ������ ������� ������ ���� ������ ������� �޾ƿ������� �޽����� ������.
		send_message("GameRoomCheck/" + id + "/" + room_No);

		// ������ ����
		setSize(1024, 768);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		getContentPane().setLayout(null);
		
		setCursor(blackCursor);
		
		getContentPane().add(canvas);
		canvas.setBounds(216, 134, 593, 440);
		canvas.setBackground(Color.white);
		
		//�޴���
		menuBar = new JLabel();
		menuBar.setBounds(0,0,1024,30);
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-mouseX, y-mouseY);
			}
		});
		getContentPane().add(menuBar);
		

		//���� ��ư
		color_black = new JButton("black");
		color_black.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_black.png")));
		color_black.setContentAreaFilled(false);
		color_black.setBounds(486, 620, 122, 60);
		color_black.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_blackCLK.png")));
		color_black.setFocusPainted(false);
		color_black.setBorderPainted(false);
		getContentPane().add(color_black);
		color_black.addActionListener(this);
		color_black.setVisible(true);

		color_red = new JButton("red");
		color_red.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_red.png")));
		color_red.setContentAreaFilled(false);
		color_red.setBounds(486, 685, 122, 60);
		color_red.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_redCLK.png")));
		color_red.setFocusPainted(false);
		color_red.setBorderPainted(false);
		getContentPane().add(color_red);
		color_red.addActionListener(this);
		color_red.setVisible(true);

		color_blue = new JButton("blue");
		color_blue.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_blue.png")));
		color_blue.setContentAreaFilled(false);
		color_blue.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_blueCLK.png")));
		color_blue.setFocusPainted(false);
		color_blue.setBorderPainted(false);
		color_blue.setBounds(615, 620, 122, 60);
		getContentPane().add(color_blue);
		color_blue.addActionListener(this);
		color_blue.setVisible(true);

		color_green = new JButton("green");
		color_green.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_green.png")));
		color_green.setContentAreaFilled(false);
		color_green.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_greenCLK.png")));
		color_green.setFocusPainted(false);
		color_green.setBorderPainted(false);
		color_green.setBounds(615, 685, 122, 60);
		getContentPane().add(color_green);
		color_green.addActionListener(this);
		color_green.setVisible(true);

		color_yellow = new JButton("yellow");
		color_yellow.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_yellow.png")));
		color_yellow.setContentAreaFilled(false);
		color_yellow.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_yellowCLK.png")));
		color_yellow.setFocusPainted(false);
		color_yellow.setBorderPainted(false);
		color_yellow.setVisible(true);
		color_yellow.setBounds(743, 620, 122, 60);
		getContentPane().add(color_yellow);
		color_yellow.addActionListener(this);

		//���찳 ��ư
		eraser = new JButton(new ImageIcon(PaintEx.class.getResource("/images/eraser.png")));
		eraser.setContentAreaFilled(false);
		eraser.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/eraserCLK.png")));
		eraser.setFocusPainted(false);
		eraser.setBorderPainted(false);
		eraser.setBounds(752, 693, 50, 42);
		getContentPane().add(eraser);
		eraser.addActionListener(this);
		eraser.setVisible(true);

		//Ŭ���� ��ư
		clear = new JButton("clear");
		clear.setIcon(new ImageIcon(PaintEx.class.getResource("/images/clear.png")));
		clear.setContentAreaFilled(false);
		clear.setBackground(Color.lightGray);
		clear.setFocusPainted(false);
		clear.setBorderPainted(false);
		clear.setBounds(810, 693, 50, 42);
		getContentPane().add(clear);
		clear.addActionListener(this);

		//�� ���� ��ư
		thick_Bold = new JButton(new ImageIcon(PaintEx.class.getResource("/images/thick_Bold.png")));
		thick_Bold.setContentAreaFilled(false);
		thick_Bold.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/thick_BoldCLK.png")));
		thick_Bold.setFocusPainted(false);
		thick_Bold.setBorderPainted(false);
		thick_Bold.setBounds(868, 628, 97, 23);
		getContentPane().add(thick_Bold);
		thick_Bold.addActionListener(this);
		thick_Bold.setVisible(true);

		thick_Sharp = new JButton(new ImageIcon(PaintEx.class.getResource("/images/thick_Sharp.png")));
		thick_Sharp.setContentAreaFilled(false);
		thick_Sharp.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/thick_SharpCLK.png")));
		thick_Sharp.setFocusPainted(false);
		thick_Sharp.setBorderPainted(false);
		thick_Sharp.setBounds(868, 654, 97, 23);
		getContentPane().add(thick_Sharp);
		thick_Sharp.addActionListener(this);
		thick_Sharp.setVisible(true);

		JButton exit = new JButton(new ImageIcon(PaintEx.class.getResource("/images/gameroom_Exit.png"))); // ��ư �׼� �ؾߵ�
		exit.setContentAreaFilled(false);
		exit.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/gameroom_ExitCLK.png")));
		clear.setFocusPainted(false);
		clear.setBorderPainted(false);
		exit.setBounds(991, 2, 19, 25);
		getContentPane().add(exit);
		exit.setVisible(true);
		exit.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exit.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				exit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
//					dispose();
					System.exit(0); // �׽�Ʈ ���̿��� ���� ó��
				}
			}
		});
		
		giveUpBt = new JButton(new ImageIcon(PaintEx.class.getResource("/images/giveup.png")));
		giveUpBt.setContentAreaFilled(false);
		giveUpBt.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/giveupCLK.png")));
		giveUpBt.setFocusPainted(false);
		giveUpBt.setBorderPainted(false);
		giveUpBt.setBounds(885, 700, 63, 24);
		getContentPane().add(giveUpBt);
		giveUpBt.setVisible(true);
		

		// ����ġ ǥ��
		expBar = new JProgressBar();
		expBar.setBounds(276, 753, 495, 10);
		getContentPane().add(expBar);
		expBar.setValue(50);
		expBar.setBackground(Color.white);
		expBar.setForeground(Color.gray);
		
		//levelUp �̹���
		levelUpImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/levelUpImg.gif")));
		getContentPane().add(levelUpImg);
		levelUpImg.setBounds(356,169,300,350);
		levelUpImg.setVisible(false);
		
		//ready�̹���
		readyImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/readyImg.png")));
		getContentPane().add(readyImg);
		readyImg.setBounds(356,169,300,300);
		readyImg.setVisible(false);
		
		//start�̹���
		startImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/startImg.png")));
		getContentPane().add(startImg);
		startImg.setBounds(356,169,300,300);
		startImg.setVisible(false);
		
		JLabel gameRoombackground = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/GameRoom_Background.png")));
		gameRoombackground.setBounds(0, 0, 1024, 768);
		getContentPane().add(gameRoombackground);
		
		canvas.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	// �������� �޽����� ������ �κ�
	private void send_message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ���� ó�� �κ�
		{
			e.printStackTrace();
		}
	}

	// �����κ��� ������ ��� �޽���
	private void Inmessage(String str) {
		st = new StringTokenizer(str, "@"); // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ => [ NewUser/�����ID ] ���·� ����

		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String mUserId = st.nextToken(); // �޽����� �����Ѵ�.

		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + mUserId);

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

		// #���� �������� ������ �޾ƿ´�.(�տ��� �ڽ��� ������ �ֱ⶧���� ��� ������ ������� ������ ���ԵǾ�����)
		case "OldUser":
			// ���� ������� ������ ������ �����Ѵ�.
			int level = Integer.parseInt(st.nextToken()); // ����
			int exp = Integer.parseInt(st.nextToken()); // ����ġ
			int corAnswer = Integer.parseInt(st.nextToken()); // ���� �����

			// ������ ������ ��ü�� ����
			UserInfo oldUser = new UserInfo(mUserId, level, exp, corAnswer);

			// �ش� ��ü�� Vector�� �߰�(���� ��ü�� RooInfo ��ü�� ���Ϳ� �����Ѵ�)
			roomInfo.addRoom_user_vc(oldUser);

			// ���� �г� ������Ʈ
			updateUserPanel();
			
			Vector temp = roomInfo.getRoom_user_vc();
			for (int i = 0; i < temp.size(); i++) {
				UserInfo u = (UserInfo) temp.get(i);
				System.out.println("���� ���� [" + i + "] : " + u);
			}
			// ���� �������� ������ �ް� �̾ ������ ������ �̾�����Ƿ�, ��� ���� �Ŀ� �г� ������Ʈ�� �����Ѵ�.
			break;
			// ���� �����ڿ��� ���ο� �����ڸ� �˸���.
		case "NewUser":
			// �ű� ������� ������ ������ �����Ѵ�.
			level = Integer.parseInt(st.nextToken()); // ����
			exp = Integer.parseInt(st.nextToken()); // ����ġ
			corAnswer = Integer.parseInt(st.nextToken()); // ���� �����

			// ������ ������ ��ü�� ����
			UserInfo newUser = new UserInfo(mUserId, level, exp, corAnswer);

			// �ش� ��ü�� Vector�� �߰�(���� ��ü�� RooInfo ��ü�� ���Ϳ� �����Ѵ�)
			roomInfo.addRoom_user_vc(newUser);

			// ���� �г� ������Ʈ
			updateUserPanel();
			
			// �г� ������Ʈ�� �����Ѵ�.
			/*
			 * �г� ������Ʈ �ڵ�
			 */

			break;
		// #ä��
		case "ChattingPA":
			st = new StringTokenizer(str, "/@", true); // ��ȹ����"/"�� ��ū���� �����Ѵ�.
			for (int i = 0; i < 4; i++) {
				st.nextToken(); // ��ū ���ſ�
			}
			ArrayList<String> chattingMsgList = new ArrayList<String>(); // ä�ø޽��� ������ ����Ʈ
			String totalChattingMsg = ""; // ��ü ä�� �޽��� ���� ����
			String tempMsg = "";
			while (st.hasMoreTokens()) { // ������ ���� ��ū�� ������ true�� ������ false�� �����Ѵ�.
				tempMsg = st.nextToken();
				System.out.println("ä�� ��ū�� ���:" + tempMsg);
				chattingMsgList.add(tempMsg); // �޽��� ��ū�� ArrayList�� �߰�
			}

			for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList�� ��� �޽����� totalChattingMsg�� �����Ѵ�.
				totalChattingMsg += chattingMsgList.get(i);
			}

			System.out.println("Paint ä�� ���� : " + totalChattingMsg);

			setChattingLabel(mUserId, totalChattingMsg);

			break;
			
		// # �ο��� ��� á���Ƿ� ���� ������ �ص� ���ٴ� �޽���
		case "GameStart":
			
			//�׽�Ʈ �ڵ�
			System.out.println("GameStart �޽��� ����");
			/* �������� �ο��� �� á���Ƿ� ���� ������ �ϵ��� �ϴ� �ڵ� */
			startT = new StartThread();
			startT.start();

			break;	
			
		case "GameRoomPaint" :
			
			String mouseState = st.nextToken();
			if(mouseState.equals("mousePress")) {
				String receiveColor = st.nextToken();
				
				System.out.println("mousePress");
				mainshape = new ShapeSave();
				
				switch(receiveColor) {
				case "black": mypencolor=Color.black; break;
				case "red": mypencolor=Color.red; break;
				case "blue": mypencolor=Color.blue; break;
				case "green": mypencolor=Color.green; break;
				case "yellow": mypencolor=Color.yellow; break;
				case "white": mypencolor=Color.white; break;
				}
				mainshape.mypencolor=mypencolor;
				
			}
			else if(mouseState.equals("mouseRelease")) {
				System.out.println("mouseRelease");
				receiveshape.add(mainshape);
				subSP.clear();
				repaint();
			}
			else if(mouseState.equals("mouseDrag")) {
				int pointX1=Integer.parseInt(st.nextToken());
				int pointY1=Integer.parseInt(st.nextToken());
				int pointX2=Integer.parseInt(st.nextToken());
				int pointY2=Integer.parseInt(st.nextToken());
				receiveThick = Integer.parseInt(st.nextToken());
				receiveEraserSel = Boolean.valueOf(st.nextToken()).booleanValue();
				
				if(receiveEraserSel) {
					mainshape.thick=eraserThick;
				}
				else {
					mainshape.thick=receiveThick;
				}	
				
				//��ǥ�׽�Ʈ
				System.out.println("���� ��ǥ : " + pointX1 + ", " + pointY1 + ", " + pointX2 + ", " + pointY2);
				//���۹��� ��ǥ ����
				maindrow.setLocation(pointX1, pointY1);
				subdrow.setLocation(pointX2, pointY2);
				mainshape.sketchSP.add(maindrow.getLocation());
				subSP.add(subdrow.getLocation());
				
				repaint();
			
			}
			else if(mouseState.equals("canvasClear")) {
				System.out.println("canvasClear");
				clear_Sel=true;
				canvas.repaint();
				while(!receiveshape.isEmpty())
					receiveshape.pop();
//				subSP.clear();
//				canvas.repaint();
			}
		
			break;
		}
	}
		
		
	// MainView Ŭ�������� Paint Ŭ������ �޽����� �����ϱ� ���� ����ϴ� �޼ҵ�
	public void paint_Inmessage(String str) {
		Inmessage(str);
	}

	// ���� ���� ������ ���������� ���� ���� ���� ������ش�.
	private void createUserPanel() {
		
		for(int i=0; i<user_pn.length; i++) {
			user_pn[i] = new JPanel();
			
			// switch������ ������� ��
			switch (i) {
			case 0: // 1��ĭ ��ġ setBounds(9, 62, 188, 147);
					user_pn[i].setBounds(9, 62, 188, 147);
					break;
			case 1: // 2��ĭ ��ġ setBounds(825, 62, 188, 147);
					user_pn[i].setBounds(825, 62, 188, 147);
					break;
			case 2: // 3��ĭ ��ġ setBounds(9, 241, 188, 147);
					user_pn[i].setBounds(9, 241, 188, 147);
					break;
			case 3: // 4��ĭ ��ġ setBounds(825, 241, 188, 147);
					user_pn[i].setBounds(825, 241, 188, 147);
					break;
			case 4: // 5��ĭ ��ġ setBounds(9, 419, 188, 147);
					user_pn[i].setBounds(9, 419, 188, 147);
					break;
			case 5: // 6��ĭ ��ġ setBounds(825, 419, 188, 147);
					user_pn[i].setBounds(825, 419, 188, 147);
					break;
			}
			
			user_pn[i].setLayout(null);
			user_pn[i].setOpaque(true);
			user_pn[i].setBackground(new Color(40,40,40,40)); // ����
			add(user_pn[i]);
			
			
		}	
	}
	
	private void updateUserPanel() {
		
		// ��� user_lb�� ������ �ڵ�. �� �迭�� ������ŭ ����(���� ���������� Ŭ�� �̺�Ʈ�� �߰��� ����)
		System.out.println("updateUserLabel����");
		for (int i = 0; i < user_lb.length; i++) {
			
			// ������ �ο� �� ��ŭ �ش� �ݺ����� �����Ų��.
			if (i < roomInfo.getRoom_user_vc().size()) {
				// �� ���� ��ü���� ��������Ʈ�� �����Ͽ� �ش� ���� ��ü�� �����´�.
				// �ε����� ���� ���� ��Ű�Ƿ� ���� ������ �ʿ� ����
				UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);

				System.out.println(u.getUserID() + "���� �� ����");

				// ������ ���� ��ü�� �̿��ؼ� UserLabel ��ü�� ������ user_lb�� �����Ų��.
				user_lb[i] = new UserLabel(u);

				user_pn[i].add(user_lb[i]);

			}
			// �г��� ��������� �����ϱ����� �޼ҵ�
			revalidate(); // ���̾ƿ� ��ȭ�� ��Ȯ�� ��Ų��.
			repaint(); // removeAll()�� ���� ���� �� ������ �ڽ��� �̹����� ����� �� �ʿ��ϴ�.
		}
	}
	
	// #ä�� ���� ����� ��Ȱ�� ���·� �ʱ�ȭ ��Ų��. 
	private void createChattingLabel() {
		Font chatlbfont = new Font("�޸�����ü", Font.BOLD,15 );
		
		for(int i=0; i<chatting_lb.length; i++) {
			chatting_lb[i] = new JLabel(chatAreaBackground);
			chatting_lb[i].setBackground(new Color(0,0,0,0));
			
			chattingCover_lb[i] = new JLabel("", SwingConstants.CENTER);
			chattingCover_lb[i].setVerticalTextPosition(SwingConstants.CENTER); // ����Ʈ ���� ��� ����(�ڵ� ���� �Ǵ� �����̱��ѵ�..)
			chattingCover_lb[i].setHorizontalTextPosition(SwingConstants.CENTER); // �ؽ�Ʈ ���� ��� ����
//			chattingCover_lb[i].setText("<html><body>������������ ģ�����<br></body></html>");
			chattingCover_lb[i].setFont(chatlbfont);
			
			chatting_lb[i].add(chattingCover_lb[i]);
			add(chatting_lb[i]);
			
			// switch������ ������� ��
			switch(i) {
			case 0: chatting_lb[i].setBounds(10, 38, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 1: chatting_lb[i].setBounds(826, 38, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 2:	chatting_lb[i].setBounds(10, 217, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 3: chatting_lb[i].setBounds(826, 217, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 4: chatting_lb[i].setBounds(10, 395, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 5:	chatting_lb[i].setBounds(826, 395, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			}
			
			// ������ �Ϸ������Ƿ� �ش� ���� �������ʰ� �Ѵ�.
			chatting_lb[i].setVisible(false);
			chattingCover_lb[i].setVisible(false);
		}
	}
	
	
	// # ä���� ������ �޽����� ���� �޼ҵ��, �ش� ������ ���̵� �޾� �����Ѵ�.
	private void setChattingLabel(String userID, String msg) {
		// �ش� ������ ������� ó���Ѵ�. (�溹ó���� ���� ������ �����尡 �ƴϸ� ä�� �ϳ��ϳ� ��ٷ�����)
		new Thread() {
			public void run() {
				
				// roomInfo���� ��������� ������ �ش� ������ ã�´�.
				for (int i = 0; i < roomInfo.getRoom_user_vc().size(); i++) {
					// �ش� ������ ��ü�� ������ UserInfo ��ü�� �����ϰ�
					UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);
					// ������ ���̵�� ��ġ�ϴ� ������ ã����
					if (userID.equals(u.getUserID())) {
						// �ش� ������ �ε��� ���� �̿��� chattingCover_lb�� set�Ѵ�.
						// �켱 msg�� 20�� ������ �ɷ� ������, �ٹٲ� ó���� ���� 10�ڸ� �ִ�� ���ڸ� �����ش�.
						if (msg.length() > 10) { // �޽����� ���̰� 10 �ʰ����
							// �ش� ���ڿ��� �ٹٲ� ó���ϱ����� HTML�� ����Ѵ�.
							String brString = "<html><body>";
							brString += msg.substring(0, 9); // �ε��� ��ġ 0~9���� �ڸ��� ���ڿ� ����
							brString += "<br>"; // ���ڿ� �ٹٲ� �ٽ�
							brString += msg.substring(10, msg.length());
							brString += "</body></html>"; // HTML ���� ��
							
							// ó���� ���ڿ��� chattingCover_lb�� set
							chattingCover_lb[i].setText(brString);
							
							// �ش� ���� ���̰� ó���Ѵ�.
							chatting_lb[i].setVisible(true);
							chattingCover_lb[i].setVisible(true);
							
						} else { // 10�� ���϶��
							// �׳� �޽����� ������.
							chattingCover_lb[i].setText(msg);
							
							// �ش� ���� ���̰� ó���Ѵ�.
							chatting_lb[i].setVisible(true);
							chattingCover_lb[i].setVisible(true);
						}

						// 5�ʵ��� �ش� �����带 ���߰�
						try {
							Thread.sleep(5000); // 0.5�ʵ� ����
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}

						// 5�� �� �ش� �󺧵��� �������ʰ� ó���Ѵ�.
						chatting_lb[i].setVisible(false);
						chattingCover_lb[i].setVisible(false);
					}
				}

			}
		}.start();
	}
	
	// ���� ������ ���� �� ���� Ŭ���� 
	class UserLabel extends JLabel{
		private JLabel user_Image;
		private JLabel user_Id;
		private JLabel user_Level;
		private JLabel user_CorAnswer;
		
		// ���� x(190.909) y(151.695)
		// ������ x(184.67) y(146.738)
		// �����簢�� x(91.788) y(136.07)
		// �����簢��3�� x(77) y(42)
		public UserLabel(UserInfo inGameUser) { // ���� ��ü�� �Է¹޴´�.
			
			Font userLabelFont = new Font("�޸�����ü", Font.PLAIN,18 ); //��Ʈ����
			
			setSize(190,151);
			
			// user_Image ��  (������ ���� ĳ���� �̹��� ����)

			/*    ������ ���� ĳ���� �̹����� �ҷ����� �ڵ� �ʿ�      */
			
			user_Image = new JLabel(inGameUser.getCharImg(), SwingConstants.CENTER); // ��� ����
			user_Image.setBounds(8, 6, 91, 135);
			user_Image.setBackground(new Color(0,0,0,0));
			user_Image.setOpaque(true);
			add(user_Image);
			System.out.println(inGameUser.getCharImg());
			
			
			// user_Id ��
			user_Id = new JLabel("",SwingConstants.CENTER); // ��� ����
			user_Id.setText(inGameUser.getUserID());
			user_Id.setHorizontalTextPosition(JLabel.CENTER);
			user_Id.setBounds(104, 7, 76, 41);
			user_Id.setFont(userLabelFont);
			user_Id.setBackground(new Color(0,0,0,0));
			user_Id.setOpaque(true);
			add(user_Id);
			System.out.println(inGameUser.getUserID());
			
			// user_Level �� (������ ���� ��� �̹��� ����)
			
			/*    ������ ���� ��� �̹����� �ҷ����� �ڵ� �ʿ�      */
			
			user_Level = new JLabel(inGameUser.getGradeImg(),SwingConstants.CENTER); // ��� ����
			user_Level.setBounds(104, 53, 76, 41);
			user_Level.setBackground(new Color(0,0,0,0));
			user_Level.setOpaque(true);
			add(user_Level);
			System.out.println(inGameUser.getGradeImg());
			
			// user_CorAnswer
			user_CorAnswer = new JLabel("",SwingConstants.CENTER); // ��� ����
			user_CorAnswer.setText(Integer.toString(inGameUser.getCorAnswer()));
			user_CorAnswer.setBounds(104, 99, 76, 41);
			user_CorAnswer.setBackground(new Color(0,0,0,0));
			user_CorAnswer.setOpaque(true);
			user_CorAnswer.setFont(userLabelFont);
			add(user_CorAnswer);
			System.out.println(inGameUser.getCorAnswer());
		}
	}
	
	// �ؽ�Ʈ �ʵ� ���� �� ������ ���� Ŭ���� �� �޼ҵ�
	public class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	} // JTextFieldLimit class ��
	
	// Ű �̺�Ʈ�� �ֱ����� Ŭ����
	public class keyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// ���͸� ������ ������ �ǰ� �ϱ����� �޼ҵ�
				String message = chatting_tf.getText();
				if (message.equals("")) { // �ƹ��͵� �Է����� �ʾ��� �� �˸�â�� ���
					JOptionPane.showMessageDialog(null, "������ �Է��Ͻñ� �ٶ��ϴ�.", "�˸�", JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingPA/" + id + "/" + roomInfo.getRoom_No() +"/"+ message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class ��

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

			//���۹��� �׸��׸���
			if(clear_Sel) {
				clear_Sel=false;
			}
			else {
				for (int i = 0; i < receiveshape.size(); i++) {
					g2.setStroke(new BasicStroke(receiveshape.get(i).thick, BasicStroke.CAP_ROUND, 0));
					g2.setPaint(receiveshape.get(i).mypencolor);
					for (int j = 1; j < receiveshape.get(i).sketchSP.size(); j++)
						g2.drawLine(receiveshape.get(i).sketchSP.get(j - 1).x, receiveshape.get(i).sketchSP.get(j - 1).y,
								receiveshape.get(i).sketchSP.get(j).x, receiveshape.get(i).sketchSP.get(j).y);
				}
			}
			
			//���۹��� �ܻ�׸���
			if (receiveEraserSel) {
				g2.setStroke(new BasicStroke(30, BasicStroke.CAP_ROUND, 0));
				for (int i = 1; i < sketSP.size(); i++) {
					g2.setPaint(mypencolor);
					g2.drawLine(subSP.get(i - 1).x, subSP.get(i - 1).y, subSP.get(i).x, subSP.get(i).y);
				}
				//g2.setStroke(new BasicStroke(receiveThick, BasicStroke.CAP_ROUND, 0));
			}
			g2.setStroke(new BasicStroke(receiveThick, BasicStroke.CAP_ROUND, 0));
			for (int i = 1; i < subSP.size(); i++) {
				g2.setPaint(mypencolor);
				g2.drawLine(subSP.get(i - 1).x, subSP.get(i - 1).y, subSP.get(i).x, subSP.get(i).y);
			}
			
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
				System.out.println("����");
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
				sleep(1000);
				startImg.setVisible(false);
				canvas.setVisible(true);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

	class MyMouseListener extends MouseAdapter implements MouseMotionListener {

		
		public void mousePressed(MouseEvent e) {
			newshape = new ShapeSave();
			newshape.mypencolor = mypencolor;

			send_message("GameRoomPaint/" + room_No+"/"+"mousePress" +"/"+colorCode);

		}

		public void mouseReleased(MouseEvent e) {
			shape.add(newshape);
			sketSP.clear();

			send_message("GameRoomPaint/"+room_No+"/"+"mouseRelease");

			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (eraser_Sel)
				newshape.thick = eraserThick;
			else
				newshape.thick = thick;
			
			newshape.sketchSP.add(e.getPoint());
			sketSP.add(e.getPoint());
			
			
			//System.out.println("���� �׸��׸��� ��ǥ x��ǥ:"+sketSP.get(sketSP.size()-1).x+", y��ǥ:"+sketSP.get(sketSP.size()-1).y);
			
			send_message("GameRoomPaint/"+room_No+"/"+"mouseDrag/"+e.getPoint().x+"/"+e.getPoint().y
					+"/"+e.getPoint().x+"/"+e.getPoint().y+"/"+thick+"/"+eraser_Sel);
			
			
			
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
			clear.setCursor(clearCursor);
			getContentPane().setCursor(myCursor);
			send_message("GameRoomPaint/"+room_No+"/"+"canvasClear");
		} else {
			if (e.getSource() == eraser) {
				eraser_Sel = true;
				eraserThick = 30;
				mypencolor = Color.white;
				colorCode = "white";
				myCursor=eraserCursor;
				eraser.setCursor(myCursor);
				getContentPane().setCursor(myCursor);
			} else {
				eraser_Sel = false;
				if (e.getSource() == thick_Bold)
					thick = 8;
				else if (e.getSource() == thick_Sharp)
					thick = 3;
				else if (e.getSource() == color_black) {
					mypencolor = Color.black;
					colorCode = "black";
					myCursor=blackCursor;
					color_black.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
				}
				else if (e.getSource() == color_red) {
					mypencolor = Color.red;
					colorCode = "red";
					myCursor=redCursor;
					color_red.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
				}
				else if (e.getSource() == color_blue) {
					mypencolor = Color.blue;
					colorCode = "blue";
					myCursor=blueCursor;
					color_blue.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
				}
				else if (e.getSource() == color_green) {
					mypencolor = new Color(0, 192, 0);
					colorCode = "green";
					myCursor=greenCursor;
					color_green.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
					
				}
				else if (e.getSource() == color_yellow) {
					mypencolor = Color.yellow;
					colorCode = "yellow";
					myCursor=yellowCursor;
					color_yellow.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
					System.out.println("yellow");
				}
			}
		}
	}
}
