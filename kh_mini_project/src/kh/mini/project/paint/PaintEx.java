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


	// 프레임 안에 있는 요소들
	Canvas canvas = new Canvas();
	
	// Panel, Label (유저 정보 및 채팅)
	private JLabel[] user_lb = new JLabel[6]; // 유저 정보를 띄울 라벨 (최대 인수 6인 기준)
	private JLabel[] chatting_lb = new JLabel[6]; // 유저의 채팅을 말풍선으로 띄우는 라벨(최대 인원 6인 기준)
	private JLabel[] chattingCover_lb = new JLabel[6];
	private JPanel[] user_pn = new JPanel[6];
	
	//메뉴바 설정용
	private JLabel menuBar;
	private int mouseX, mouseY;

	// 그림판 설정을 위한 변수
	Color mypencolor = Color.black;
	boolean eraser_Sel = false;
	int thick = 8;
	int eraserThick = 30;
	boolean clear_Sel = false;
	String colorCode="black"; //펜 색상 전송하기위한 코드설정
	int receiveThick;
	boolean receiveEraserSel=false;

	// 도형
	ShapeSave newshape;
	ShapeSave mainshape;

	ArrayList<Point> sketSP = new ArrayList<Point>();
	Stack<ShapeSave> shape = new Stack<ShapeSave>();
	
	ArrayList<Point> subSP = new ArrayList<Point>();
	Stack<ShapeSave> receiveshape = new Stack<ShapeSave>();
	

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
	private JButton giveUpBt;
	
	private JProgressBar expBar;
	
	private JLabel levelUpImg;
	private JLabel readyImg;
	private JLabel startImg;
	
	private boolean canvasUse=false;
	
	Point maindrow=new Point();
	Point subdrow=new Point();

// 각종 변수
	private String id; // 사용자의 id를 저장
	private int room_No; // 게임방 번호
	private StringTokenizer st; // 프로토콜 구현을 위해 필요함. 소켓으로 입력받은 메시지를 분리하는데 쓰임.
	private RoomInfo roomInfo; // 방정보를 객체로 저장한다.
	private boolean roomCaptain = false; // 방장인 사람에게는 true
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
	
// Textfield
	private JTextField chatting_tf; // 채팅 내용을 입력받기 위한 텍스트필드	
	private ImageIcon chatAreaBackground = new ImageIcon(PaintEx.class.getResource("/images/wordBubble.png"));
	
	
// Network 자원 변수
	private DataOutputStream dos;

	public PaintEx(int room_No) {
		// 사용자의 id를 이어받아온다.
		id = MainView.getId();
		// 게임방 번호를 어이받아온다.
		this.room_No = room_No;
		// MainView로부터 dos를 이어받아온다.
		dos = MainView.getDos();

		// 초기 유저 패널을 생성한다. 
		createUserPanel();

		// 초기 채팅용 라벨을 생성한다.
		createChattingLabel();

		Font font = new Font("휴먼편지체", Font.BOLD, 17); // 폰트설정

		// 채팅 입력창
		chatting_tf = new JTextField();
		chatting_tf.setBounds(280, 674, 300, 25);
		chatting_tf.setOpaque(true);
		chatting_tf.setDocument(new JTextFieldLimit(20)); // 채팅 45자 제한
		chatting_tf.setFont(font);
		chatting_tf.addKeyListener(new keyAdapter()); // 클래스로 정의한 키 이벤트를 적용
		add(chatting_tf);
		
		
		
		
		// 창을 열자마자 해당 방과 동일한 방에 입장한 사용자의 정보와 방의 정보를 순서대로 받아오기위한 메시지를 보낸다.
		send_message("GameRoomCheck/" + id + "/" + room_No);

		// 프레임 설정
		setSize(1024, 768);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		getContentPane().setLayout(null);
		
		setCursor(blackCursor);
		
		getContentPane().add(canvas);
		canvas.setBounds(216, 134, 593, 440);
		canvas.setBackground(Color.white);
		
		//메뉴바
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
		

		//색깔 버튼
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

		//지우개 버튼
		eraser = new JButton(new ImageIcon(PaintEx.class.getResource("/images/eraser.png")));
		eraser.setContentAreaFilled(false);
		eraser.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/eraserCLK.png")));
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

		JButton exit = new JButton(new ImageIcon(PaintEx.class.getResource("/images/gameroom_Exit.png"))); // 버튼 액션 해야됨
		exit.setContentAreaFilled(false);
		exit.setRolloverIcon(new ImageIcon(PaintEx.class.getResource("/images/gameroom_ExitCLK.png")));
		clear.setFocusPainted(false);
		clear.setBorderPainted(false);
		exit.setBounds(991, 2, 19, 25);
		getContentPane().add(exit);
		exit.setVisible(true);
		exit.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				exit.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
				exit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
//					dispose();
					System.exit(0); // 테스트 중이여서 종료 처리
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
		
		canvas.setVisible(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	// 서버에게 메시지를 보내는 부분
	private void send_message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) // 에러 처리 부분
		{
			e.printStackTrace();
		}
	}

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
			String roomCaptainID = st.nextToken();
			
			
			//만약 자신이 방장이라면
			if(mUserId.equals(roomCaptainID)) {
				roomCaptain = true;
			}
			
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

			// 유저 패널 업데이트
			updateUserPanel();
			
			Vector temp = roomInfo.getRoom_user_vc();
			for (int i = 0; i < temp.size(); i++) {
				UserInfo u = (UserInfo) temp.get(i);
				System.out.println("유저 정보 [" + i + "] : " + u);
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

			// 유저 패널 업데이트
			updateUserPanel();
			
			// 패널 업데이트를 진행한다.
			/*
			 * 패널 업데이트 코드
			 */

			break;
		// #채팅
		case "ChattingPA":
			st = new StringTokenizer(str, "/@", true); // 구획문자"/"도 토큰으로 간주한다.
			for (int i = 0; i < 4; i++) {
				st.nextToken(); // 토큰 제거용
			}
			ArrayList<String> chattingMsgList = new ArrayList<String>(); // 채팅메시지 저장할 리스트
			String totalChattingMsg = ""; // 전체 채팅 메시지 저장 변수
			String tempMsg = "";
			while (st.hasMoreTokens()) { // 리턴할 다음 토큰이 있으면 true를 없으면 false를 리턴한다.
				tempMsg = st.nextToken();
				System.out.println("채팅 토큰들 출력:" + tempMsg);
				chattingMsgList.add(tempMsg); // 메시지 토큰을 ArrayList에 추가
			}

			for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList의 모든 메시지를 totalChattingMsg에 저장한다.
				totalChattingMsg += chattingMsgList.get(i);
			}

			System.out.println("Paint 채팅 내용 : " + totalChattingMsg);

			setChattingLabel(mUserId, totalChattingMsg);

			break;
			
		// # 인원이 모두 찼으므로 게임 시작을 해도 좋다는 메시지
		case "GameStart":
			
			//테스트 코드
			System.out.println("GameStart 메시지 수신");
			/* 서버에서 인원이 다 찼으므로 게임 시작을 하도록 하는 코드 */
			startT = new StartThread();
			startT.start();

			break;	
		
		// # 자신의 턴을 진행할 때
		case "YourTurn":

			System.out.println("난 출제자야!");
			canvasUse=true;
			// 출제자에게만 버튼 활성화
			setButtonEnabled(true);
			break;

		// # 문제를 푸는 자들
		case "Solve":

			canvasUse=false;
			System.out.println("난 문제를 풀어!");

			break;
			
		case "GameRoomPaint" :
			
			String mouseState = st.nextToken();
			if(mouseState.equals("mousePress")) {
				String receiveColor = st.nextToken();

				System.out.println("mousePress");
				newshape = new ShapeSave();
				
				switch(receiveColor) {
				case "black": mypencolor=Color.black; break;
				case "red": mypencolor=Color.red; break;
				case "blue": mypencolor=Color.blue; break;
				case "green": mypencolor=Color.green; break;
				case "yellow": mypencolor=Color.yellow; break;
				case "white": mypencolor=Color.white; break;
				}
				newshape.mypencolor=mypencolor;
			
				
			}
			else if(mouseState.equals("mouseRelease")) {
				System.out.println("mouseRelease");
				shape.add(newshape);
				sketSP.clear();
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
					newshape.setThick(eraserThick);
				}
				else {
					newshape.setThick(receiveThick);
				}	
				
				thick=receiveThick;
				
				//좌표테스트
				System.out.println("받은 좌표 : " + pointX1 + ", " + pointY1 + ", " + pointX2 + ", " + pointY2);
				//전송받은 좌표 대입
				maindrow.setLocation(pointX1, pointY1);
				subdrow.setLocation(pointX2, pointY2);
				newshape.sketchSP.add(maindrow.getLocation());
				sketSP.add(subdrow.getLocation());
				
				repaint();
			
			}
			else if(mouseState.equals("canvasClear")) {
				System.out.println("canvasClear");
				clear_Sel=true;
				canvas.repaint();
				while(!shape.isEmpty())
					shape.pop();
			}
		
			break;
		}
	}
		
		
	// MainView 클래스에서 Paint 클래스로 메시지를 전달하기 위해 사용하는 메소드
	public void paint_Inmessage(String str) {
		Inmessage(str);
	}

	// 유저 정보 갱신이 있을때마다 유저 라벨을 새로 만들어준다.
	private void createUserPanel() {
		
		for(int i=0; i<user_pn.length; i++) {
			user_pn[i] = new JPanel();
			
			// switch문으로 나누기로 함
			switch (i) {
			case 0: // 1번칸 위치 setBounds(9, 62, 188, 147);
					user_pn[i].setBounds(9, 62, 188, 147);
					break;
			case 1: // 2번칸 위치 setBounds(825, 62, 188, 147);
					user_pn[i].setBounds(825, 62, 188, 147);
					break;
			case 2: // 3번칸 위치 setBounds(9, 241, 188, 147);
					user_pn[i].setBounds(9, 241, 188, 147);
					break;
			case 3: // 4번칸 위치 setBounds(825, 241, 188, 147);
					user_pn[i].setBounds(825, 241, 188, 147);
					break;
			case 4: // 5번칸 위치 setBounds(9, 419, 188, 147);
					user_pn[i].setBounds(9, 419, 188, 147);
					break;
			case 5: // 6번칸 위치 setBounds(825, 419, 188, 147);
					user_pn[i].setBounds(825, 419, 188, 147);
					break;
			}
			
			user_pn[i].setLayout(null);
			user_pn[i].setOpaque(true);
			user_pn[i].setBackground(new Color(0,0,0,0)); // 투명
			add(user_pn[i]);
			
			
		}	
	}
	
	private void updateUserPanel() {
		
		// 모든 user_lb에 적용할 코드. 라벨 배열의 개수만큼 적용(추후 여유있으면 클릭 이벤트도 추가할 생각)
		System.out.println("updateUserLabel실행");
		for (int i = 0; i < user_lb.length; i++) {
			
			// 유저의 인원 수 만큼 해당 반복문을 진행시킨다.
			if (i < roomInfo.getRoom_user_vc().size()) {
				// 방 정보 객체에서 유저리스트로 접근하여 해당 유저 객체를 꺼내온다.
				// 인덱스로 넣은 순서 지키므로 따로 정렬할 필요 없음
				UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);

				System.out.println(u.getUserID() + "님의 라벨 갱신");

				// 꺼내온 유저 객체를 이용해서 UserLabel 객체를 생성해 user_lb에 저장시킨다.
				user_lb[i] = new UserLabel(u);

				user_pn[i].add(user_lb[i]);

			}
			// 패널의 변경사항을 적용하기위한 메소드
			revalidate(); // 레이아웃 변화를 재확인 시킨다.
			repaint(); // removeAll()에 의해 제거 된 오래된 자식의 이미지를 지우는 데 필요하다.
		}
	}
	
	// #채팅 라벨을 만들어 비활성 상태로 초기화 시킨다. 
	private void createChattingLabel() {
		Font chatlbfont = new Font("휴먼편지체", Font.BOLD,15 );
		
		for(int i=0; i<chatting_lb.length; i++) {
			chatting_lb[i] = new JLabel(chatAreaBackground);
			chatting_lb[i].setBackground(new Color(0,0,0,0));
			
			chattingCover_lb[i] = new JLabel("", SwingConstants.CENTER);
			chattingCover_lb[i].setVerticalTextPosition(SwingConstants.CENTER); // 덱스트 세로 가운데 정렬(자동 정렬 되는 느낌이긴한데..)
			chattingCover_lb[i].setHorizontalTextPosition(SwingConstants.CENTER); // 텍스트 가로 가운데 정렬
			chattingCover_lb[i].setFont(chatlbfont);
			
			chatting_lb[i].add(chattingCover_lb[i]);
			add(chatting_lb[i]);
			
			// switch문으로 나누기로 함
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
			
			// 생성을 완료했으므로 해당 라벨을 보이지않게 한다.
			chatting_lb[i].setVisible(false);
			chattingCover_lb[i].setVisible(false);
		}
	}
	
	
	// # 채팅이 들어오면 메시지를 띄우는 메소드로, 해당 유저의 아이디를 받아 적용한다.
	private void setChattingLabel(String userID, String msg) {
		// 해당 과정을 쓰레드로 처리한다. (충복처리가 많기 때문에 쓰레드가 아니면 채팅 하나하나 기다려야함)
		new Thread() {
			public void run() {
				
				// roomInfo에서 유저목록을 가져와 해당 유저를 찾는다.
				for (int i = 0; i < roomInfo.getRoom_user_vc().size(); i++) {
					// 해당 유저의 객체를 가져와 UserInfo 객체로 생성하고
					UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);
					// 유저의 아이디와 일치하는 유저를 찾으면
					if (userID.equals(u.getUserID())) {
						// 해당 유저의 인덱스 값을 이용해 chattingCover_lb을 set한다.
						// 우선 msg는 20자 제한이 걸려 있으나, 줄바꿈 처리를 위해 10자를 최대로 글자를 나눠준다.
						if (msg.length() > 10) { // 메시지의 길이가 10 초과라면
							// 해당 문자열을 줄바꿈 처리하기위해 HTML을 사용한다.
							String brString = "<html><body>";
							brString += msg.substring(0, 9); // 인덱스 위치 0~9까지 자르고 문자열 누적
							brString += "<br>"; // 문자열 줄바꿈 핵심
							brString += msg.substring(10, msg.length());
							brString += "</body></html>"; // HTML 구문 끝
							
							// 처리된 문자열을 chattingCover_lb에 set
							chattingCover_lb[i].setText(brString);
							
							// 해당 라벨을 보이게 처리한다.
							chatting_lb[i].setVisible(true);
							chattingCover_lb[i].setVisible(true);
							
						} else { // 10자 이하라면
							// 그냥 메시지를 보낸다.
							chattingCover_lb[i].setText(msg);
							
							// 해당 라벨을 보이게 처리한다.
							chatting_lb[i].setVisible(true);
							chattingCover_lb[i].setVisible(true);
						}

						// 5초동안 해당 스레드를 멈추고
						try {
							Thread.sleep(5000); // 0.5초뒤 종료
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}

						// 5초 뒤 해당 라벨들을 보이지않게 처리한다.
						chatting_lb[i].setVisible(false);
						chattingCover_lb[i].setVisible(false);
					}
				}

			}
		}.start();
	}
	
	// 유저 정보를 띄우는 라벨 생성 클래스 
	class UserLabel extends JLabel{
		private JLabel user_Image;
		private JLabel user_Id;
		private JLabel user_Level;
		private JLabel user_CorAnswer;
		
		// 갈색 x(190.909) y(151.695)
		// 베이지 x(184.67) y(146.738)
		// 좌측사각형 x(91.788) y(136.07)
		// 우측사각형3개 x(77) y(42)
		public UserLabel(UserInfo inGameUser) { // 유저 객체를 입력받는다.
			
			Font userLabelFont = new Font("휴먼편지체", Font.PLAIN,18 ); //폰트설정
			
			setSize(190,151);
			
			// user_Image 라벨  (레벨에 따른 캐릭터 이미지 삽입)

			/*    레벨에 따른 캐릭터 이미지를 불러오는 코드 필요      */
			
			user_Image = new JLabel(inGameUser.getCharImg(), SwingConstants.CENTER); // 가운데 정렬
			user_Image.setBounds(8, 6, 91, 135);
			user_Image.setBackground(new Color(0,0,0,0));
			user_Image.setOpaque(true);
			add(user_Image);
			System.out.println(inGameUser.getCharImg());
			
			
			// user_Id 라벨
			user_Id = new JLabel("",SwingConstants.CENTER); // 가운데 정렬
			user_Id.setText(inGameUser.getUserID());
			user_Id.setHorizontalTextPosition(JLabel.CENTER);
			user_Id.setBounds(104, 7, 76, 41);
			user_Id.setFont(userLabelFont);
			user_Id.setBackground(new Color(0,0,0,0));
			user_Id.setOpaque(true);
			add(user_Id);
			System.out.println(inGameUser.getUserID());
			
			// user_Level 라벨 (레벨에 따른 등급 이미지 삽입)
			
			/*    레벨에 따른 등급 이미지를 불러오는 코드 필요      */
			
			user_Level = new JLabel(inGameUser.getGradeImg(),SwingConstants.CENTER); // 가운데 정렬
			user_Level.setBounds(104, 53, 76, 41);
			user_Level.setBackground(new Color(0,0,0,0));
			user_Level.setOpaque(true);
			add(user_Level);
			System.out.println(inGameUser.getGradeImg());
			
			// user_CorAnswer
			user_CorAnswer = new JLabel("",SwingConstants.CENTER); // 가운데 정렬
			user_CorAnswer.setText(Integer.toString(inGameUser.getCorAnswer()));
			user_CorAnswer.setBounds(104, 99, 76, 41);
			user_CorAnswer.setBackground(new Color(0,0,0,0));
			user_CorAnswer.setOpaque(true);
			user_CorAnswer.setFont(userLabelFont);
			add(user_CorAnswer);
			System.out.println(inGameUser.getCorAnswer());
		}
	}
	
	// 텍스트 필드 글자 수 제한을 위한 클래스 및 메소드
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
	} // JTextFieldLimit class 끝
	
	// 키 이벤트를 주기위한 클래스
	public class keyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// 엔터를 누르면 전송이 되게 하기위한 메소드
				String message = chatting_tf.getText();
				if (message.equals("")) { // 아무것도 입력하지 않았을 시 알림창을 띄움
					JOptionPane.showMessageDialog(null, "내용을 입력하시기 바랍니다.", "알림", JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingPA/" + id + "/" + roomInfo.getRoom_No() +"/"+ message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class 끝

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
					g2.setStroke(new BasicStroke(shape.get(i).getThick(), BasicStroke.CAP_ROUND, 0));
					g2.setPaint(shape.get(i).mypencolor);
					for (int j = 1; j < shape.get(i).sketchSP.size(); j++)
						g2.drawLine(shape.get(i).sketchSP.get(j - 1).x, shape.get(i).sketchSP.get(j - 1).y,
								shape.get(i).sketchSP.get(j).x, shape.get(i).sketchSP.get(j).y);
				}
			}

			// 잔상 그리기
			if (eraser_Sel || receiveEraserSel) {
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
			expBar.setMaximum(10);
			break;
		case 2:
			expBar.setMaximum(40);
			break;
		case 3:
			expBar.setMaximum(80);
			break;
		case 4:
			expBar.setMaximum(130);
			break;
		case 5:
			expBar.setMaximum(200);
			break;
		case 6:
			expBar.setMaximum(290);
			break;
		case 7:
			expBar.setMaximum(400);
			break;
		case 8:
			expBar.setMaximum(540);
			break;
		case 9:
			expBar.setMaximum(710);
			break;
		case 10:
			expBar.setMaximum(910);
			break;
		case 11:
			expBar.setMaximum(1150);
			break;
		case 12:
			expBar.setMaximum(1430);
			break;
		case 13:
			expBar.setMaximum(1750);
			break;
		}
	}
	   
	// 게임 시작 및 라운드 시작에 사용될 버튼 활성/비활성 기능
	private void setButtonEnabled(boolean flag) {

		thick_Bold.setEnabled(flag);
		thick_Sharp.setEnabled(flag);
		eraser.setEnabled(flag);
		color_yellow.setEnabled(flag);
		color_red.setEnabled(flag);
		color_blue.setEnabled(flag);
		color_green.setEnabled(flag);
		clear.setEnabled(flag);
		color_black.setEnabled(flag);
		giveUpBt.setEnabled(flag);
	}
	
	//목표 인원수에 도달하면 3초뒤에 게임 자동시작하는 스레드(인원수가 차면 스레드 시작되는거 아직 구현X)
	   class StartThread extends Thread{
	      @Override
	      public void run() {
	         try {
	            System.out.println("시작");
	            setButtonEnabled(false); // 모든 그림 버튼 비활성화
	            sleep(3000);
	            
	            readyImg.setVisible(true);
	            sleep(2500);
	            readyImg.setVisible(false);
	            
	            startImg.setVisible(true);
	            sleep(1500);
	            startImg.setVisible(false);
	            
	            canvas.setVisible(true);
	            
	            // 만약 방장이라면 
	            if(roomCaptain) {
	               send_message("RoundStart/"+id+"/"+room_No);
	            }
	         }catch(InterruptedException e) {
	            e.printStackTrace();
	         }
	      }
	   }
	   

	class MyMouseListener extends MouseAdapter implements MouseMotionListener {

		
		public void mousePressed(MouseEvent e) {
			if(canvasUse) {
				newshape = new ShapeSave();
				newshape.mypencolor = mypencolor;

				send_message("GameRoomPaint/"+id+"/"+ room_No+"/"+"mousePress" +"/"+colorCode);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(canvasUse) {
				shape.add(newshape);
				sketSP.clear();

				send_message("GameRoomPaint/" + id + "/" + room_No + "/" + "mouseRelease");

				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(canvasUse) {
				if (eraser_Sel)
					newshape.setThick(eraserThick);
				else
					newshape.setThick(thick);

				newshape.sketchSP.add(e.getPoint());
				sketSP.add(e.getPoint());

				// System.out.println("현재 그림그리는 좌표 x좌표:"+sketSP.get(sketSP.size()-1).x+",
				// y좌표:"+sketSP.get(sketSP.size()-1).y);

				send_message("GameRoomPaint/" + id + "/" + room_No + "/" + "mouseDrag/" + e.getPoint().x + "/"
						+ e.getPoint().y + "/" + e.getPoint().x + "/" + e.getPoint().y + "/" + thick + "/"
						+ eraser_Sel);

				repaint();
			}
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
			send_message("GameRoomPaint/"+id+"/"+room_No+"/"+"canvasClear");
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
					mypencolor = Color.green;
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
