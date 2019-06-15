package kh.mini.project.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import kh.mini.project.main.view.Main;
import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.RoomInfo;
import kh.mini.project.model.vo.UserInfo;

public class PaintEx extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new PaintEx(1);
	}

	StartThread startT;


	// 프레임 안에 있는 요소들
	Canvas canvas = new Canvas();

	// 설정을 위한 변수
	Color mypencolor = Color.black;
	boolean eraser_Sel = false;
	int thick = 8;
	int eraserThick = 30;
	boolean clear_Sel = false;
	
	//펜 색상 전송하기위한 코드설정
	String colorCode;

	// 도형
	ShapeSave newshape;

	Vector<Point> sketSP = new Vector<Point>();
	Stack<ShapeSave> shape = new Stack<ShapeSave>();

	// 버튼
	private JButton thick_Bold;
	private JButton thick_Sharp;
	private JButton eraser;

	private JButton color_yellow;
	private JButton color_blue;
	private JButton color_green;
	private JButton color_red;
	private JButton clear;
	private JButton color_black;
	private JProgressBar expBar;
	
	private JLabel levelUpImg;
	private JLabel readyImg;
	private JLabel startImg;

// 각종 변수
	private String id; // 사용자의 id를 저장
	private int room_No; // 게임방 번호
	private StringTokenizer st; // 프로토콜 구현을 위해 필요함. 소켓으로 입력받은 메시지를 분리하는데 쓰임.
	private RoomInfo roomInfo; // 방정보를 객체로 저장한다.
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
	
	
// Network 자원 변수
	private DataOutputStream dos;

	public PaintEx(int room_No) {
		// 사용자의 id를 이어받아온다.
		id = MainView.getId();
		// 게임방 번호를 어이받아온다.
		this.room_No = room_No;
		// MainView로부터 dos를 이어받아온다.
		dos = MainView.getDos();

		// 창을 열자마자 해당 방과 동일한 방에 입장한 사용자의 정보와 방의 정보를 순서대로 받아오기위한 메시지를 보낸다.
//		send_message("GameRoomCheck/" + id + "/" + room_No);

		// 프레임 설정
		setSize(1024, 768);
		setUndecorated(true);
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		getContentPane().setLayout(null);
		
		setCursor(blackCursor);
		
		getContentPane().add(canvas);
		canvas.setBounds(216, 134, 593, 440);
		canvas.setBackground(Color.white);
		canvas.setVisible(false);

		//색깔 버튼
		color_black = new JButton("black");
		color_black.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_black.png")));
		color_black.setContentAreaFilled(false);
		color_black.setBounds(486, 620, 122, 60);
		//color_black.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_black.png")));
		color_black.setFocusPainted(false);
		color_black.setBorderPainted(false);
		getContentPane().add(color_black);
		color_black.addActionListener(this);
		color_black.setVisible(true);

		color_red = new JButton("red");
		color_red.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_red.png")));
		color_red.setContentAreaFilled(false);
		color_red.setBounds(486, 685, 122, 60);
		//color_red.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_red.png")));
		color_red.setFocusPainted(false);
		color_red.setBorderPainted(false);
		getContentPane().add(color_red);
		color_red.addActionListener(this);
		color_red.setVisible(true);

		color_blue = new JButton("blue");
		color_blue.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_blue.png")));
		color_blue.setContentAreaFilled(false);
		//color_blue.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_blue.png")));
		color_blue.setFocusPainted(false);
		color_blue.setBorderPainted(false);
		color_blue.setBounds(615, 620, 122, 60);
		getContentPane().add(color_blue);
		color_blue.addActionListener(this);
		color_blue.setVisible(true);

		color_green = new JButton("green");
		color_green.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_green.png")));
		color_green.setContentAreaFilled(false);
		color_green.setFocusPainted(false);
		color_green.setBorderPainted(false);
		color_green.setBounds(615, 685, 122, 60);
		getContentPane().add(color_green);
		color_green.addActionListener(this);
		color_green.setVisible(true);

		color_yellow = new JButton("yellow");
		color_yellow.setIcon(new ImageIcon(PaintEx.class.getResource("/images/color_yellow.png")));
		color_yellow.setContentAreaFilled(false);
		//color_yellow.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/color_yellow.png")));
		color_yellow.setFocusPainted(false);
		color_yellow.setBorderPainted(false);
		color_yellow.setVisible(true);
		color_yellow.setBounds(743, 620, 122, 60);
		getContentPane().add(color_yellow);
		color_yellow.addActionListener(this);

		//지우개 버튼
		eraser = new JButton(new ImageIcon(PaintEx.class.getResource("/images/eraser.png")));
		eraser.setContentAreaFilled(false);
		eraser.setFocusPainted(false);
		eraser.setBorderPainted(false);
		eraser.setBounds(752, 693, 50, 42);
		getContentPane().add(eraser);
		eraser.addActionListener(this);
		eraser.setVisible(true);

		//클리어 버튼
		clear = new JButton("clear");
		clear.setIcon(new ImageIcon(PaintEx.class.getResource("/images/clear.png")));
		clear.setContentAreaFilled(false);
		clear.setBackground(Color.lightGray);
		clear.setFocusPainted(false);
		clear.setBorderPainted(false);
		clear.setBounds(810, 693, 50, 42);
		getContentPane().add(clear);
		clear.addActionListener(this);

		//펜 굵기 버튼
		thick_Bold = new JButton("굵은 펜");
		thick_Bold.setBackground(Color.lightGray);
		thick_Bold.setBounds(868, 628, 97, 23);
		thick_Bold.setFocusPainted(false);
		getContentPane().add(thick_Bold);
		thick_Bold.addActionListener(this);
		thick_Bold.setVisible(true);

		thick_Sharp = new JButton("얇은 펜");
		thick_Sharp.setBackground(Color.lightGray);
		thick_Sharp.setBounds(868, 654, 97, 23);
		thick_Sharp.setFocusPainted(false);
		getContentPane().add(thick_Sharp);
		thick_Sharp.addActionListener(this);
		thick_Sharp.setVisible(true);

		JButton exit = new JButton("나가기"); // 버튼 액션 해야됨
		exit.setBounds(868, 21, 97, 37);
		exit.setBackground(Color.lightGray);
		exit.setFocusPainted(false);
		getContentPane().add(exit);
		exit.setVisible(true);
		

		// 경험치 표시
		expBar = new JProgressBar();
		expBar.setBounds(276, 753, 495, 10);
		getContentPane().add(expBar);
		expBar.setValue(50);
		expBar.setBackground(Color.white);
		expBar.setForeground(Color.gray);
		
		//levelUp 이미지
		levelUpImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/levelUpImg.gif")));
		getContentPane().add(levelUpImg);
		levelUpImg.setBounds(356,169,300,350);
		levelUpImg.setVisible(false);
		
		//ready이미지
		readyImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/readyImg.png")));
		getContentPane().add(readyImg);
		readyImg.setBounds(356,169,300,300);
		readyImg.setVisible(false);
		
		//start이미지
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
	
	
	// 서버에게 메시지를 보내는 부분
//	private void send_message(String str) {
//		try {
//			dos.writeUTF(str);
//		} catch (IOException e) // 에러 처리 부분
//		{
//			e.printStackTrace();
//		}
//	}

	// 서버로부터 들어오는 모든 메시지
	private void Inmessage(String str) {
		st = new StringTokenizer(str, "@"); // 어떤 문자열을 사용할 것인지, 어떤 문자열로 자를 것인지 => [ NewUser/사용자ID ] 형태로 들어옴

		String protocol = st.nextToken(); // 프로토콜을 저장한다.
		String mUserId = st.nextToken(); // 메시지를 저장한다.

		System.out.println("프로토콜 : " + protocol);
		System.out.println("내용 : " + mUserId);

		// protocol 수신 처리
		switch (protocol) {
		// #제일 처음에 받아오는 방 정보
		case "RoomInfo":
			String room_Name = st.nextToken(); // 방제목
			String room_Pw = st.nextToken(); // 비밀번호
			int fixed_User = Integer.parseInt(st.nextToken()); // 최대 인원(정원)
			int uCount = Integer.parseInt(st.nextToken()); // 현재 인원 수

			// 받은 정보로 roomInfo 객체를 생성한다.
			roomInfo = new RoomInfo(room_No, room_Name, room_Pw, uCount, fixed_User);
			
			break;

		// #기존 접속자의 정보를 받아온다.(앞에서 자신의 정보를 넣기때문에 방금 입장한 사용자의 정보도 포함되어있음)
		case "OldUser":
			// 기존 사용자의 정보를 가져와 저장한다.
			int level = Integer.parseInt(st.nextToken()); // 레벨
			int exp = Integer.parseInt(st.nextToken()); // 경험치
			int corAnswer = Integer.parseInt(st.nextToken()); // 누적 정답수

			// 가져온 정보로 객체를 생성
			UserInfo oldUser = new UserInfo(mUserId, level, exp, corAnswer);

			// 해당 객체를 Vector에 추가(유저 객체를 RooInfo 객체의 벡터에 저장한다)
			roomInfo.addRoom_user_vc(oldUser);
			
			Vector temp = roomInfo.getRoom_user_vc();
			for (int i = 0; i < temp.size(); i++) {
				UserInfo u = (UserInfo) temp.get(i);
				System.out.println("유저 정보 [" +i +"] : " +u);
			}
			// 기존 접속자의 정보를 받고 이어서 본인의 정보를 이어받으므로, 모두 받은 후에 패널 업데이트를 진행한다.
			break;
			// 기존 접속자에게 새로운 접속자를 알린다.
		case "NewUser":
			// 신규 사용자의 정보를 가져와 저장한다.
			level = Integer.parseInt(st.nextToken()); // 레벨
			exp = Integer.parseInt(st.nextToken()); // 경험치
			corAnswer = Integer.parseInt(st.nextToken()); // 누적 정답수

			// 가져온 정보로 객체를 생성
			UserInfo newUser = new UserInfo(mUserId, level, exp, corAnswer);

			// 해당 객체를 Vector에 추가(유저 객체를 RooInfo 객체의 벡터에 저장한다)
			roomInfo.addRoom_user_vc(newUser);

			// 패널 업데이트를 진행한다.
			/*
			 * 패널 업데이트 코드
			 */

			break;
			
		// # 인원이 모두 찼으므로 게임 시작을 해도 좋다는 메시지
		case "GameStart":
			
			//테스트 코드
			System.out.println("GameStart 메시지 수신");
			/* 서버에서 인원이 다 찼으므로 게임 시작을 하도록 하는 코드 */

			break;	
		}
		
		

	}

	// MainView 클래스에서 Paint 클래스로 메시지를 전달하기 위해 사용하는 메소드
	public void paint_Inmessage(String str) {
		Inmessage(str);
	}

	

	// 그림판
	class Canvas extends JPanel {

		MyMouseListener ml = new MyMouseListener();

		// 그림판 마우스 리스너
		Canvas() {
			addMouseListener(ml);
			addMouseMotionListener(ml);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;

			// 그림 그리기
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

			// 잔상 그리기
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

	
	//레벨별 비율 계산해서 경험치 바에 값 설정
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
	
	//목표 인원수에 도달하면 3초뒤에 게임 자동시작하는 스레드(인원수가 차면 스레드 시작되는거 아직 구현X)
	class StartThread extends Thread{
		@Override
		public void run() {
			try {
				System.out.println("시작");
				sleep(3000);
				ReadyImgThread rit = new ReadyImgThread();
				rit.start();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//게임 시작 후 Ready이미지 1.5초띄우고 사라지는 스레드
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
			clear.setCursor(clearCursor);
			getContentPane().setCursor(myCursor);
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
				}
			}
		}
	}
}
