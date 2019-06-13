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

	// 프레임 안에 있는 요소들
	Canvas canvas = new Canvas();

	// 설정을 위한 변수
	Color mypencolor = Color.black;
	boolean eraser_Sel = false;
	int thick = 8;
	int eraserThick = 30;
	boolean clear_Sel = false;

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

// 각종 변수
	private String id; // 사용자의 id를 저장
	private int room_No; // 게임방 번호
	private StringTokenizer st; // 프로토콜 구현을 위해 필요함. 소켓으로 입력받은 메시지를 분리하는데 쓰임.
	private RoomInfo roomInfo; // 방정보를 객체로 저장한다.

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
		//send_message("GameRoomCheck/" + id + "/" + room_No);

		// 프레임 설정
		setSize(1024, 768);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.lightGray);

		getContentPane().add(canvas);
		canvas.setBounds(219, 70, 570, 500);
		canvas.setBackground(Color.WHITE);
		canvas.setVisible(true);

		//색깔 버튼
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

		//지우개 버튼
		eraser = new JButton("eraser");
		eraser.setBackground(Color.lightGray);
		eraser.setFocusPainted(false);
		eraser.setBounds(487, 586, 89, 49);
		getContentPane().add(eraser);
		eraser.addActionListener(this);
		eraser.setVisible(true);

		//클리어 버튼
		clear = new JButton("clear");
		clear.setBackground(Color.lightGray);
		clear.setFocusPainted(false);
		clear.setBounds(581, 586, 89, 49);
		getContentPane().add(clear);
		clear.addActionListener(this);

		//펜 굵기 버튼
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

		JButton exit = new JButton("나가기"); // 버튼 액션 해야됨
		exit.setBounds(868, 21, 97, 37);
		exit.setBackground(Color.lightGray);
		exit.setFocusPainted(false);
		getContentPane().add(exit);
		exit.setVisible(true);

		//캐릭터창 임시로 넣어둔거
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

		// 경험치 표시
		expBar = new JProgressBar();
		expBar.setBounds(284, 695, 495, 14);
		getContentPane().add(expBar);
		expBar.setValue(50);
		expBar.setBackground(Color.white);
		expBar.setForeground(Color.gray);
		
		//ready이미지
		readyImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/image/readyImg.png")));
		getContentPane().add(readyImg);
		readyImg.setBounds(356,169,300,300);
		readyImg.setVisible(true);
		
		//start이미지
		startImg = new JLabel(new ImageIcon(PaintEx.class.getResource("/images/startImg.png")));
		getContentPane().add(startImg);
		startImg.setBounds(356,169,300,300);
		startImg.setVisible(false);
		
		getContentPane().add(canvas);
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
		String Message = st.nextToken(); // 메시지를 저장한다.

		System.out.println("프로토콜 : " + protocol);
		System.out.println("내용 : " + Message);

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

		// #접속자 정보 받아오기(사용자 본인의 정보)
		case "UserInfo":
			// 사용자의 정보를 가져와 저장한다.
			int level = Integer.parseInt(st.nextToken()); // 레벨
			int exp = Integer.parseInt(st.nextToken()); // 경험치
			int corAnswer = Integer.parseInt(st.nextToken()); // 누적 정답수

			// 가져온 정보로 객체를 생성
			UserInfo userInfo = new UserInfo(Message, level, exp, corAnswer);

			// 해당 객체를 Vector에 추가(유저 객체를 RooInfo 객체의 벡터에 저장한다)
			roomInfo.addRoom_user_vc(userInfo);

			// 유저 패널을 갱신한다.
			/*
			 * 유저 패널 갱신 코드
			 */

			break;

		// #기존 접속자의 정보를 받아온다.
		case "OldUser":
			// 기존 사용자의 정보를 가져와 저장한다.
			level = Integer.parseInt(st.nextToken()); // 레벨
			exp = Integer.parseInt(st.nextToken()); // 경험치
			corAnswer = Integer.parseInt(st.nextToken()); // 누적 정답수

			// 가져온 정보로 객체를 생성
			UserInfo oldUser = new UserInfo(Message, level, exp, corAnswer);

			// 해당 객체를 Vector에 추가(유저 객체를 RooInfo 객체의 벡터에 저장한다)
			roomInfo.addRoom_user_vc(oldUser);
			
			// 기존 접속자의 정보를 받고 이어서 본인의 정보를 이어받으므로, 모두 받은 후에 패널 업데이트를 진행한다.
			break;
		}

	}

	// MainView 클래스에서 Paint 클래스로 메시지를 전달하기 위해 사용하는 메소드
	public void paint_Inmessage(String str) {
		Inmessage(str);
	}

	// class StartPnThread

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
