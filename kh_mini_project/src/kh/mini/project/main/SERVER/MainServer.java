package kh.mini.project.main.SERVER;

// Github 푸쉬할때 자동으로 대문자로 바뀌어서 그냥 대문자로 놓음..
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import kh.mini.project.db.UserController;
import kh.mini.project.main.view.Main;
import kh.mini.project.model.vo.User;
import kh.mini.project.paint.Question;

public class MainServer extends JFrame {
	private static final long serialVersionUID = 1216070372320522836L;
// Frame, Panel
	private JScrollPane statusView = new JScrollPane(); // 포트로 받은 상태 수신창 스크롤팬
	private JTextArea statusArea = new JTextArea(); // statusView에 넣을 텍스트 에어리어
	private JTextArea userListArea = new JTextArea(); // userListView에 넣을 텍스트 에어리어
	private JScrollPane allUserListView; // 모든 유저 리스트를 보이게 할 스크롤팬
	private JScrollPane userListView = new JScrollPane(); // 현재 접속 유저 리스트를 보이게 할 스크롤팬
	private JScrollPane roomListView = new JScrollPane(); // 현재 개설된 방 리스트를 보이게 할 스크롤팬

// Label
	private JLabel mainMenuBar = new JLabel();

// List 
	private JList<UserInfo> userList = new JList<UserInfo>(); // 이걸 그냥 모든 유저 처리하는곳에서 접속/비접속 상태를 추가해서 처리하는것도 방법일듯 하다.
	private JList<RoomInfo> roomList = new JList<RoomInfo>(); // 룸은 제목/방번호/인원/비밀번호 표시?

// Textfield
	private JTextField port_tf; // 포트번호를 입력받기 위한 텍스트필드

// Table
	private DefaultTableModel model;
	private JTable userInfo_Table;
	private static String[] row = { "ID", "Password", "이름", "생년월일", "나이", "E-mail", "Level", "Exp", "누적 정답 개수" };
	private static Object[][] column;

// Network 자원
	private ServerSocket server_Socket; // 서버 소켓
	private Socket socket; // 사용자로부터 받을 소켓
	private int port; // 포트번호

// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수
	private boolean portConection = false; // 포트 정상 연결 후 MainView 연결에 사용될 변수
	private boolean connectCk = false; // 로그인에 사용될 변수
	private int mouseX, mouseY; // 마우스 좌표용 변수
	/* Multi Thread 환경에서는 Vector가 더 나은거같아서 Vector를 써야겠다. */
	private Vector<User> allUser_vc = new Vector<User>(); // 모든 회원의 정보를 담아두는 Vector
	private Vector<UserInfo> user_vc = new Vector<UserInfo>(); // 사용자 Vector
	private Vector<UserInfo> wRoom_vc = new Vector<UserInfo>(); // 대기실 사용자 Vector => 대기실유저/인게임 유저 나눠야하니까 구상해야함.
	private Vector<RoomInfo> room_vc = new Vector<RoomInfo>(); // 게임방 Vector(유저 리스트 포함)
	private StringTokenizer st; // 프로토콜 구현을 위해 필요함. 소켓으로 입력받은 메시지를 분리하는데 쓰임.
	private boolean scrollpanemove = false; // 스크롤 패인에 사용되는 변수(스크롤 허용 관련)

//Image	
	// #MainView 배경
	private Image mainBackgroundImage = new ImageIcon(Main.class.getResource("/images/임시2.jpg")).getImage();
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png"));

//Button
	private JButton exitButton = new JButton(exitBasicImage); // 나가기 버튼

	MainServer() {
		new Intro();
	}

	private void main_View() { // 서버 메인 화면
		// 모든 회원의 정보를 로드해온다.
		allUser_vc = new UserController().dataLoadAll();

		// JFrame mainView
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정
		setTitle("Server"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main에서 고정시킨 화면 해상도를 사용
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0, 0, 0, 0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setVisible(true); // 윈도우를 볼 수 있음.
		setLayout(null);

		// 테이블에 불러온 모든 유저 정보를 불러온다.
		column = new Object[allUser_vc.size()][row.length];
		tableUppate(allUser_vc);
		model = new DefaultTableModel(column, row) {
			public boolean isCellEditable(int rowIndex, int mColIndex)
			{ return false; }
		};
		userInfo_Table = new JTable(model);
		
		// #모든 유저 리스트 스크롤 패인
		allUserListView = new JScrollPane(userInfo_Table);
		allUserListView.setBounds(10, 10, 1004, 250);
		userInfo_Table.setAutoCreateRowSorter(true); // 열을 클릭하면 자동 정렬
		userInfo_Table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		userInfo_Table.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		add(allUserListView);

		// #현재 접속 유저 리스트 스크롤 패인
		userListView.setBounds(10, 270, 300, 250);
		add(userListView);

		// #현재 생성된 방 리스트 스크롤 패인
		roomListView.setBounds(10, 530, 300, 220);
		add(roomListView);

		// Label
		// #메뉴바
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				mainMenuBar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		mainMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
			// #매뉴바 드래그 시, 움직일 수 있게 한다.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(mainMenuBar);

		// #나가기 버튼
		exitButton.setBounds(870, 690, 100, 30);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}

			// 마우스를 버튼에서 떼었을때 이벤트
			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}

			// 마우스로 버튼을 눌렀을 때 이벤트
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0); // 프로세스 종료
			}
		});
	}

	void Server_start() // // MainServer에서 사용할 수 있도록 default로 설정
	{
		try {
			server_Socket = new ServerSocket(port);
			if (portConection) { // 정상포트로 연결요청 될 시에 main_View를 띄움
				try {
					Thread.sleep(500); // 0.5초뒤 실행
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				main_View();
				portConection = false; // 중복실행 방지를 위한 초기화
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			statusArea.append("다시 입력하세요.\n");
		}

		if (server_Socket != null) // 정상적으로 포트가 열렸을 경우
		{
			Connection();
		}
	}

	private void Connection() {
		// 1가지의 스레드에서는 1가지의 일만 처리할 수 있다.
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() { // 스레드에서 처리할 일을 기재한다.

				while (true) {
					try { // 마찬가지로 try-catch
						statusArea.append("사용자 접속 대기중(설정 포트번호 : " + port + ")\n");
						socket = server_Socket.accept(); // 사용자 접속 무한 대기 => 때문에 다른 기능들이 동작하지 않고 죽어버린다. => 멀티 스레드로 해결!
						statusArea.append("사용자 연결 설정 완료!\n");
						connectCk = true; // 로그인을 위해 사용될 변수

						// 사용자가 accept로 접속을 하게되면 UserInfo 객체를 생성하게 된다.
						// UserInfo 생성자는 매개변수로 넘어온 socket을 받아주고 UserNetwork()를 실행하여 연결설정을 하게된다.
						UserInfo user = new UserInfo(socket);

						user.start(); // 객체의 스레드를 각각 실행
					} catch (IOException e) {
					}
				} // while문 끝

			}
		});

		th.start();
	}

	public static void main(String[] args) {
		new MainServer();
	}

	@Override
	public void paint(Graphics g) {
		viewImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage, 0, 0, null);
	}

	public void screenDraw(Graphics g) {
		g.drawImage(mainBackgroundImage, 0, 0, null);
		this.paintComponents(g);
		this.repaint();
	}

	// {"ID","Password","이름","생년월일","나이","E-mail","Level","Exp","누적 정답 개수"};
	// 테이블 업데이트를 위한 메소드
	public void tableUppate(Vector allUser_vc) {
		for (int i = 0; i < allUser_vc.size(); i++) {
			User u = (User) allUser_vc.elementAt(i);
			column[i][0] = u.getId();
			column[i][1] = u.getPw();
			column[i][2] = u.getName();
			column[i][3] = u.getDateOfBirth();
			column[i][4] = u.getAge();
			column[i][5] = u.geteMail();
			column[i][6] = u.getLevel();
			column[i][7] = u.getExp();
			column[i][8] = u.getCorAnswer();
		}
	}

	// 인트로 화면용 내부 클래스
	class Intro extends JFrame {
		// Frame, Panel
		private JFrame introView = new JFrame("Server"); // 인트로 프레임 (포트번호를 입력하여 서버를 실행하면 메인 프레임 창을 띄워 넘어가는 구조)
		private JLabel port_lb = new JLabel("포트 번호 : ");
		private JLabel introMenuBar = new JLabel();

		// 각종 변수
		private Image viewImage; // 이미지 저장용 변수
		private Graphics viewGraphics; // 그래픽 저장용 변수
		private int mouseX, mouseY; // 마우스 좌표용 변수
		private boolean state = true; // 서버 중복 실행 방지용

		// Image
		// #MainView 배경
		private Image introBackgroundImage = new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
		// #버튼 이미지
		private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("/images/exit_entered.png"));
		private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("/images/exit_basic.png"));

		// Button
		private JButton start_btn = new JButton("서버 실행"); // 서버 실행 버튼
		private JButton stop_btn = new JButton("서버 중지"); // 서버 중지 버튼
		private JButton exitButton = new JButton(exitButtonBasicImage); // 나가기 버튼

		Intro() {
			Font font = new Font("Inconsolata", Font.BOLD, 12);
			setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정
			setTitle("Server"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
			setSize(280, 450); // Main에서 고정시킨 화면 해상도를 사용
			setResizable(false); // 프레임 크기 고정
			setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
			setBackground(new Color(0, 0, 0, 0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
			setVisible(true); // 윈도우를 볼 수 있음.
			setLayout(null);

			// ScrollPane
			statusView.setBounds(10, 20, 260, 330);
			statusView.setBackground(new Color(0, 0, 0, 0));
			statusView.getVerticalScrollBar().setValue(statusView.getVerticalScrollBar().getMaximum());
			statusArea.setBackground(new Color(80, 80, 80, 0));
			statusArea.setFont(font);
			statusArea.setForeground(Color.white);
			statusArea.setLineWrap(true); // 자동 줄바꿈
			statusView.setViewportView(statusArea);
			/* 이하 코드는 쓰레드 환경에서도 자동 스크롤이 되게하려는 메소드이다. */
			statusView.addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					scrollpanemove = true;
				}
			});
			statusView.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) { // 수정리스너에서 변수(휠의 길이,위치)가 변경될시 메소드 작성
					if (scrollpanemove) { // 만약 스크롤 무브가 허용되있을시
						scrollpanemove = false; // 밑으로 내리는 것을 하지않고, 비허용으로 바꾼다.
					} else {
						JScrollBar src = (JScrollBar) e.getSource();
						src.setValue(src.getMaximum());
					}
				}
			});
			add(statusView);

			// label
			// #포트번호
			port_lb.setBounds(10, 360, 100, 30);
			add(port_lb);

			// Button
			// #시작 버튼
			start_btn.setBounds(10, 400, 100, 30);
			add(start_btn);
			start_btn.addMouseListener(new MouseAdapter() {
				// 마우스를 버튼에 올려놨을 때 이벤트
				@Override
				public void mouseEntered(MouseEvent e) {
//					start_btn.setIcon(); // 마우스를 올려놨을때 이미지 변경(Entered Image)
					start_btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
				}

				// 마우스를 버튼에서 떼었을때 이벤트
				@Override
				public void mouseExited(MouseEvent e) {
//					start_btn.setIcon(); // 마우스를 떼었을때 이미지 변경(Basic Image)
					start_btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
				}

				// 마우스로 버튼을 눌렀을 때 이벤트
				@Override
				public void mousePressed(MouseEvent e) {
					if (state) {
						System.out.println("스타트 버튼 클릭");
						/*
						 * portCheck() 메소드를 통해 음수를 반환 받을 경우 서버를 가동하지않음. 음수가 반환 되었다는 것은 정상적인 포트번호가 아님을
						 * 의미.
						 */
						if (portCheck(port_tf.getText().trim()) >= 0) {
							portConection = true;
							System.out.println("포트번호 : " + port);
							Server_start(); // 소켓 생성 및 사용자 접속 대기
							state = false; // 중복 실행 방지를 위해 false로 변경

						}
					} else {
						statusArea.append("서버가 이미 실행중 입니다.\n");
					}
				}
			});

			// #중지 버튼
			stop_btn.setBounds(170, 400, 100, 30);
			add(stop_btn);
			stop_btn.addMouseListener(new MouseAdapter() {
				// 마우스를 버튼에 올려놨을 때 이벤트
				@Override
				public void mouseEntered(MouseEvent e) {
//					stop_btn.setIcon(); // 마우스를 올려놨을때 이미지 변경(Entered Image)
					stop_btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
				}

				// 마우스를 버튼에서 떼었을때 이벤트
				@Override
				public void mouseExited(MouseEvent e) {
//					stop_btn.setIcon(); // 마우스를 떼었을때 이미지 변경(Basic Image)
					stop_btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
				}

				// 마우스로 버튼을 눌렀을 때 이벤트
				@Override
				public void mousePressed(MouseEvent e) {
					state = true; // 서버 실행이 가능한 상태로 변경
					System.out.println("서버 스탑 버튼 클릭");
				}
			});

			// #나가기 버튼
			exitButton.setBounds(125, 400, 30, 30);
			exitButton.setBorderPainted(false);
			exitButton.setContentAreaFilled(false);
			exitButton.setFocusPainted(false);
			exitButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					exitButton.setIcon(exitButtonEnteredImage);
					exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
					// 마우스를 올렸을 때 손모양으로 나타나게

				}

				@Override
				public void mouseExited(MouseEvent e) {
					exitButton.setIcon(exitButtonBasicImage);
					exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// 마우스를 때면 원래 상태로
				}

				@Override
				public void mousePressed(MouseEvent e) {
					try {
						Thread.sleep(500); // 0.5초뒤 종료
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					System.exit(0);
				}
			});
			add(exitButton);

			// TextField
			// 포트번호 입력
			port_tf = new JTextField("12345"); // 포트번호 기본값 12345
			port_tf.setBounds(100, 360, 170, 30);
			add(port_tf);
			port_tf.setColumns(10);

			// 메뉴바
			introMenuBar.setBounds(0, 0, 280, 30);
			introMenuBar.addMouseListener(new MouseAdapter() {
				// 마우스를 버튼에 올려놨을 때 이벤트
				@Override
				public void mouseEntered(MouseEvent e) {
					introMenuBar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
				}

				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			introMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
				// #매뉴바 드래그 시, 움직일 수 있게 한다.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
			add(introMenuBar);

		}

		@Override
		public void paint(Graphics g) {
			viewImage = createImage(280, 450);
			viewGraphics = viewImage.getGraphics();
			screenDraw(viewGraphics);
			g.drawImage(viewImage, 0, 0, null);
		}

		public void screenDraw(Graphics g) {
			g.drawImage(introBackgroundImage, 0, 0, null);
			this.paintComponents(g);
			this.repaint();
		}

		// 유효 포트번호 체크 메소드
		private int portCheck(String str) {
			try {
				// int형으로 변경이 가능한 문자열이 입력되었는지 try-catch로 확인
				int tempPort = Integer.parseInt(str);
				// 가능하다면 유효 포트번호를 입력하였는지 확인.
				if (tempPort >= 0 && tempPort <= 65535) {
					return port = Integer.parseInt(str); // 포트번호 범위는 0~65535
				} else { // 유효 포트번호가 아니면 음수를 반환
					statusArea.append("잘못된 포트번호입니다. 다시 입력하세요.\n");
					return -1;
				}
			} catch (NumberFormatException e) { // 예외 처리 대상의 경우에도 음수를 반환
				statusArea.append("잘못된 포트번호입니다. 다시 입력하세요.\n");
				return -1;
			}
		}
	}

	// 접속자 별로 쓰레드를 진행하기 위한 클래스
	class UserInfo extends Thread {
		private OutputStream os;
		private InputStream is;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private DataOutputStream dos;
		private DataInputStream dis;

		private Socket user_socket; // 사용자의 ID를 받는다.
		private String userID = null; // 사용자의 ID 저장
		// id를 기준으로 User 객체의 정보를 찾아, 아래 정보만 가져와 저장한다.
		private int level; // 레벨
		private int exp; // 경험치
		private int corAnswer; // 누적 정답 개수
		private int room_No; // 게임방 번호
		private boolean loginState = false; // 로그인 상태

		private boolean RoomCh = true;

		UserInfo(Socket socket) // 생성자 메소드
		{
			this.user_socket = socket;
			UserNetwork();
		}

		/*
		 * [Protocol 정리] - 사용자 추가 = NewUser/사용자ID - 기존 사용자 = OldUser/사용자ID - 쪽지 =
		 * Note/User@내용 Client 입장 => Note/받는사람@내용 Server 입장 => Note/받는사람@내용 (메세지 들어올때)
		 * => Note/보낸사람@내용 (메세지 나갈때) => Client 입장 => Note/보낸사람@내용 (메세지 받을때)
		 */
		private void UserNetwork() // 네트워크 자원 설정
		{
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);

				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);

			} catch (IOException e) {
			}
		}

		// 유저가 접속했을 때, 기존 사용자들에게 알리고 리스트를 추가하기 위한 메소드
		private void userAdd(String str) {
			// 연결 설정 후에 사용자의 닉네임을 받아들인다.
			userID = str;
			System.out.println(userID);
			statusArea.append(userID + " : 사용자 접속!\n");

			// 사용자 접속이 완료되면, 해당 계정의 정보를 로드해온다.
			for (int i = 0; i < allUser_vc.size(); i++) {
				// 모든 유저 객체를 하나 가져와서
				User u = (User) allUser_vc.elementAt(i);
				// 접속자의 아이디와 같은 아이디를 찾고, 로그인 상태가 false면 접속을 허용한다.
				if (u.getId().equals(userID)) {
					// 각각의 정보를 저장하고
					level = u.getLevel();
					exp = u.getExp();
					corAnswer = u.getCorAnswer();
					// 로그인 상태로 전환
					loginState = true;
					// 자신에게 그 정보를 알린다.
					send_Message("WaitingRoom/pass/UserInfo@" + userID + "@" + level + "@" + exp + "@" + corAnswer);
					break; // 사용자의 정보를 찾았으므로 반복문 종료
				} 
			}

			// 현재 접속중인 사용자의 리스트를 자신에게 알림
			for (int i = 0; i < wRoom_vc.size(); i++) {
				// 기존 접속자 유저 객체를 하나 가져와서
				UserInfo u = (UserInfo) wRoom_vc.elementAt(i);

				/*
				 * 기존 사용자의 정보를 읽어온다. 서버가 연결된 사용자에게 보내는 부분
				 */
				String msg = "";
				if (i == wRoom_vc.size() - 1) {
					msg = "WaitingRoom/pass/OldUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + u.corAnswer + "@"
							+ "last";
				} else {
					msg = "WaitingRoom/pass/OldUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + u.corAnswer
							+ "@_";
				}
				send_Message(msg);
			}

			// 현재 개설된 방의 리스트를 자신에게 알림
			for (int i = 0; i < room_vc.size(); i++) {
				// 기존 개설된 방 객체를 하나 가져와서
				RoomInfo r = (RoomInfo) room_vc.elementAt(i);

				/*
				 * 기존에 개설된 방의 정보를 읽어온다. 서버가 연결된 사용자에게 보내는 부분
				 */
				String msg = "";
				if (i == room_vc.size() - 1) {
					msg = "WaitingRoom/pass/OldRoom@" + userID + "@" + r.room_No + "@" + r.room_name + "@" + r.room_PW
							+ "@" + r.fixed_User + "@" + r.Room_user_vc.size() + "@last";
				} else {
					msg = "WaitingRoom/pass/OldRoom@" + userID + "@" + r.room_No + "@" + r.room_name + "@" + r.room_PW
							+ "@" + r.fixed_User + "@" + r.Room_user_vc.size() + "@_";
				}
				send_Message(msg);
			}

			// 기존 사용자들에게 새로운 사용자 알림(broadcast)
			BroadCast("WaitingRoom/pass/NewUser@" + userID + "@" + level + "@" + exp + "@" + corAnswer); // 기존 사용자에게 자신을
																											// 알린다. 프로토콜
																											// 사용 [
																											// NewUser/사용자ID
																											// ]

			user_vc.add(this); // 사용자에게 알린 후 Verctor에 자신을 추가
			// Vector는 동적으로 늘어나는 배열로 이해하면 되는데, 객체에 저장한 사용자 정보를 Vector에 저장한다.
			wRoom_vc.add(this); // 대기실 유저 리스트에도 적용
			statusArea.append("현재 접속된 사용자 수 : " + user_vc.size() + "\n");
		}

		// 유저가 로그아웃할 경우, 실행될 메소드로 아직 미적용
		private void userSub(String str) {
			// 유저가 나가게 될 경우 사용자의 닉네임을 받아들인다.
			userID = str;
			System.out.println(userID);
			statusArea.append(userID + " : 사용자 접속종료!");

			// 기존 사용자들에게 사용자 접속 종료 알림(broadcast)
			BroadCast("WaitingRoom/pass/SubUser@" + userID);

			// 현재 접속중인 사용자의 리스트를 자신에게 알림
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo u = (UserInfo) user_vc.elementAt(i);
				// 서버가 연결된 사용자에게 보내는 부분
				send_Message("WaitingRoom/pass/OldUser@" + u.userID);
			}
		}

		@Override
		public void run() // Thread에서 처리할 내용
		{
			while (true) {
				try {
					// 스트림설정을 안했기 때문에 해당 스트림 설정을 생성자에서 실시
					String msg = dis.readUTF();
					if (connectCk) { // 이것이 true이면 연결하고 로그인/회원가입을 하기위한 상태
						statusArea.append("사용자로부터 들어온 메시지 : \n" + msg + "\n");
					} else {
						statusArea.append("[" + userID + "]님 으로부터 들어온 메세지 : \n" + msg + "\n");
					}
					Inmessage(msg);
				} catch (IOException e) {
				}
			}
		} // run 메소드 끝

		private String getUserID() {
			return userID;
		}

		/*
		 * [가독성을 위해 if문을 사용했으나, switch문으로 변경] if문은 모든 조건문에 대해서 cmp(Compare;비교) 과정을 거치므로
		 * 조건을 확인하기 위한 인스트럭션이 계속해서 필요해진다. switch문은 일정 조건 수가 넘어가면 Jump Table을 만들어 그 안에서
		 * 값을 확인하고 바로 해당 코드로 넘어가는 방식으로 작동한다. 때문에 입력받은 값을 확인하는 인스트럭션만 있으면 된다. Jump Table을
		 * 생성하는데 오버헤드가 있으므로 제한이 있지만 Protocol에서는 오버헤드를 발생시킬 양을 사용하지 않으므로 switch문으로 작성한다.
		 */
		// 클라이언트로부터 들어오는 메세지 처리
		private void Inmessage(String str) {
			System.out.println(str);
			st = new StringTokenizer(str, "/"); // 넘기는 구조를 눈에 띄게 하기위해 /와 @로 나누었음

			String protocol = st.nextToken();
			String mUserId = st.nextToken(); // 보통 유저id가 저장된다.

			System.out.println("프로토콜 : " + protocol);
			System.out.println("메세지 : " + mUserId);

			// protocol 요청 처리
			switch (protocol) {

			// #로그인 요청이 들어왔을 때
			case "LoginCheck":
				String pw = st.nextToken();
				// 아이디와 패스워드가 맞는지 확인한다.
				boolean findID = false; // 일치하는 계정이 있는지 체크하는 변수
				for (int i = 0; i < allUser_vc.size(); i++) {
					User user = (User) allUser_vc.elementAt(i);
					 // ID와 PW와 일치하고 현재 로그인 상태가 아닌지를 확인한다
					if (user.getId().equals(mUserId) && user.getPw().equals(pw) && !user.isLoginState())
					{
						userID = mUserId;
						connectCk = false;
						findID = true;
						
						// 로그인 상태로 전환
						user.setLoginState(true);
						
						send_Message("LoginOK/ok");
						userAdd(userID);
						UserNetwork();
					// 만약 현재 로그인 상태라면 
					} else if (user.getId().equals(mUserId) && user.getPw().equals(pw) && user.isLoginState()) {
						findID = true;
						// 현재 로그인 중이라는 메시지를 보낸다.
						send_Message("SigningIn/"+userID);
					}
				}
				if (!findID)
					send_Message("LoginFail/fail"); // 찾지 못하였으면 해당 메시지를 보낸다.
				break;

			// #쪽지 보내기 요청이 들어왔을 떄
			case "Note":
				// protocol = Note
				// message = user
				// note = 받는 내용
				String note = st.nextToken();

				System.out.println("받는 사람 : " + mUserId);
				System.out.println("보낼 내용 : " + note);

				// 벡터에서 해당 사용자를 찾아서 메세지 전송
				for (int i = 0; i < wRoom_vc.size(); i++) {
					UserInfo u = (UserInfo) wRoom_vc.elementAt(i);

					if (u.userID.equals(mUserId)) {
						u.send_Message("Note/" + userID + "/" + note);
						// Note/User1/~~~
					}
				}
				break;

			// #방 생성 요청이 들어왔을 때
			case "CreateRoom":
				// 전달받은 메시지를 토크나이징을 하여 각각의 값을 저장
				String title = st.nextToken();
				String state = st.nextToken();
				String roomPW = st.nextToken();
				int fixed_User = Integer.parseInt(st.nextToken());
				int roomNo = 0; // 방번호는 1~999이므로 0을 초기값으로 설정했음.

				// 방번호를 생성한다.(랜덤) 기존에 생성된 방들과 비교하여 같은 번호가 있을 경우 재생성한다.
				Pointer: while (true) {
					roomNo = (int) (Math.random() * 999) + 1; // 방은 24개까지 할 예정이지만 번호는 999까지 할당!

					for (int i = 0; i < room_vc.size(); i++) {
						RoomInfo r = (RoomInfo) room_vc.elementAt(i);
						if (r.room_No == roomNo) { // 같은 방 번호가 존재할 시
							continue Pointer; // 다시 번호 뽑기
						}
					} // for문 끝
					break; // continue에 도달하지 않는다는건 같은 방번호가 존재하지 않는다는 것.
				}

				// 전달받은 정보로 RoomInfo객체를 생성하고 room_vc에 추가
				RoomInfo new_room = new RoomInfo(roomNo, title, roomPW, fixed_User);
				// 방을 생성한 유저이므로 방장 id를 저장시킨다.
				new_room.roomCaptainID = mUserId;
				room_vc.add(new_room); // 전체 채팅 방 Vector에 방을 추가

				// 방이 만들어졌을때 BroadCast로 알린다.
				BroadCast("WaitingRoom/pass/NewRoom@" + mUserId + "@" + roomNo + "@" + title + "@" + roomPW + "@"
						+ fixed_User + "@" + new_room.Room_user_vc.size() + "@");
				// 방을 생성한 유저에게 방 개설이 가능함을 알리고 할당한 방 번호를 넘겨준다.
				send_Message("WaitingRoom/pass/CreateRoom@" + mUserId + "@" + roomNo);
				// MainView에 Paint창을 띄우라고 알린다.
				send_Message("EntryGameRoom/" + mUserId + "/" + roomNo);

				break;

			// #방 입장 요청
			case "EnterRoom":
				int room_No = Integer.parseInt(st.nextToken()); // 방번호

				// room_vc에서 해당 방 번호와 일치하는 객체를 찾는다.
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.get(i);
					// 방번호가 같은 객체를 찾았다면 pw가 존재하는지 확인한다.
					/* room_PW의 값은 null이여도 넘어올때 String 값으로 받는다. 때문에 비교문도 "null"로 비교한다. */
					if (r.room_No == room_No && !(r.room_PW.equals("null"))) {
						// 비밀번호가 null이 아니라면 pw를 입력하라는 메시지를 보낸다.(비밀번호도 같이 보내서 해당 창에서 빠르게 체크하도록한다.)
						send_Message("WaitingRoom/pass/InputPW@" + mUserId + "@" + room_No + "@" + r.room_PW);
						break; // 작업을 완료했으므로 for문을 탈출한다.
						// 비밀번호가 null일 경우
					} else if (r.room_No == room_No && r.room_PW.equals("null")) {
						// 비밀번호가 null이라면 바로 입장하도록 한다.
//										// 입장 허가받은 유저를 Vector에 추가
//										r.Room_user_vc.add(this);
						// 유저에게 방 입장을 허가받았다 알림. 유저는 이 메시지로 WaitingRoom창을 종료한다.
						send_Message("WaitingRoom/pass/EntryRoom@" + mUserId);

						// 이어서 MainView로 방 번호를 넘기면서 Paint창을 열도록 지시한다.
						send_Message("EntryGameRoom/" + mUserId + "/" + r.room_No);

						// 모든 유저에게 해당 유저가 게임방에 들어갔으니 리스트에서 제거하라고 알린다.
						BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);
						/* 비밀번호 없이 바로 입장하는 코드 */

						break; // 작업을 완료했으므로 for문을 탈출한다.
					}
				}
				// 객체를

				break;

			// #비밀번호를 맞게 입력했을 경우
			case "PassPW":
				room_No = Integer.parseInt(st.nextToken()); // 방번호

				// room_vc에서 해당 방 번호와 일치하는 객체를 찾는다.
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.get(i);
					// 방번호가 같은 객체를 찾아서 입장 처리를 한다.
					if (r.room_No == room_No) {
//										// 입장 허가받은 유저를 Vector에 추가
//										 r.Room_user_vc.add(this);

						// 유저에게 방 입장을 허가받았다 알림. 유저는 이 메시지로 WaitingRoom창을 종료한다.
						send_Message("WaitingRoom/pass/EntryRoom@" + mUserId);

						// 이어서 MainView로 방 번호를 넘기면서 Paint창을 열도록 지시한다.
						send_Message("EntryGameRoom/" + mUserId + "/" + r.room_No);

						// 모든 유저에게 해당 유저가 게임방에 들어갔으니 리스트에서 제거하라고 알린다.
						BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);

						break; // 작업을 완료했으므로 for문을 탈출한다.
					}
				}
				break;

			// #방 입장 알림
			case "EntryRoom":
				// 게임방에 입장하여 현재 대기실 유저에서 제거되어야 된다.
				for (int i = 0; i < wRoom_vc.size(); i++) {
					UserInfo u = (UserInfo) wRoom_vc.get(i);
					// 해당 유저아이디를 찾는다.
					if (u.getUserID().equals(mUserId)) {
						// 해당 아이디를 대기실 유저에서 지운다.
						wRoom_vc.remove(i);
						// 프로토콜이 EntryRoom이면
						/*
						 * 해당 유저를 게임룸 유저목록에 추가한다.
						 */

						// 해당 유저를 리스트에서 제거하라는 브로드캐스트를 보낸다.
						BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);
						break; // 작업을 완료했으므로 for문을 탈출한다.
					}
				}
				break;

			// #유저 로그아웃
			case "UserLogout":
				// 로그아웃 하여 현재 접속 사용자 리스트에서 제거되어야 된다.
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.get(i);
					// 해당 유저아이디를 찾는다.
					if (u.getUserID().equals(mUserId)) {
						// 해당 아이디를 접속자 유저에서 지운다.
						user_vc.remove(i);
						break; // 작업을 완료했으므로 for문을 탈출한다.
					}
				}
				// 로그아웃 하여 현재 대기실 리스트에서 제거되어야 된다.
				for (int i = 0; i < wRoom_vc.size(); i++) {
					UserInfo u = (UserInfo) wRoom_vc.get(i);
					// 해당 유저아이디를 찾는다.
					if (u.getUserID().equals(mUserId)) {
						// 해당 아이디를 대기실 유저에서 지운다.
						wRoom_vc.remove(i);
						break; // 작업을 완료했으므로 for문을 탈출한다.
					}

				}
				
				// 끝으로 alluser에서 해당 계정을 로그아웃 처리한다.
				for(int i=0; i<allUser_vc.size(); i++) {
					User user = (User) allUser_vc.elementAt(i);
					// 현재 상태를 로그아웃 상태로 바꾼다.
					user.setLoginState(false);
				}
				
				// 해당 유저를 리스트에서 제거하라는 브로드캐스트를 보낸다.
				BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);
				break;

			// #대기실 채팅 요청이 들어왔을 때
			case "ChattingWR":
				st = new StringTokenizer(str, "/", true); // 구획문자"/"도 토큰으로 간주한다.
				for (int i = 0; i < 4; i++) {
					st.nextToken(); // 토큰 제거용
				}
				ArrayList<String> chattingMsgList = new ArrayList<String>(); // 채팅메시지 저장할 리스트
				String totalChattingMsg = ""; // 전체 채팅 메시지 저장 변수
				String tempMsg = "";
//								String chattingMsg = st.nextToken(); // 메세지 부분을 잘라서 저장
				while (st.hasMoreTokens()) { // 리턴할 다음 토큰이 있으면 true를 없으면 false를 리턴한다.
					tempMsg = st.nextToken();
					System.out.println("채팅 토큰들 출력:" + tempMsg);
					chattingMsgList.add(tempMsg); // 메시지 토큰을 ArrayList에 추가
				}

				for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList의 모든 메시지를 totalChattingMsg에 저장한다.
					totalChattingMsg += chattingMsgList.get(i);
				}

				System.out.println("MainServer Inmessage에서 protocol이 ChattingWR 일때 들어온 아이디:" + mUserId);
				BroadCast("WaitingRoom/pass/ChattingWR@" + mUserId + "@" + totalChattingMsg);
				break;

			// #게임방 채팅 요청이 들어왔을 때
			case "ChattingPA":
				// 우선 방번호를 받는다.
				room_No = Integer.parseInt(st.nextToken());
				
				// RoomInfo 객체 저장용 => 정답 체크를 위해서 사용
				RoomInfo r = null;
				for (int i = 0; i < room_vc.size(); i++) {
					 r = (RoomInfo) room_vc.get(i);
					// 방번호가 같은 객체를 찾아서 저장한 후 break;
					if (r.room_No == room_No) {
						break;
					}
				}
				
				st = new StringTokenizer(str, "/", true); // 구획문자"/"도 토큰으로 간주한다.
				for (int i = 0; i < 6; i++) {
					st.nextToken(); // 토큰 제거용
				}
				chattingMsgList = new ArrayList<String>(); // 채팅메시지 저장할 리스트
				totalChattingMsg = ""; // 전체 채팅 메시지 저장 변수
				tempMsg = "";
//								String chattingMsg = st.nextToken(); // 메세지 부분을 잘라서 저장
				while (st.hasMoreTokens()) { // 리턴할 다음 토큰이 있으면 true를 없으면 false를 리턴한다.
					tempMsg = st.nextToken();
					System.out.println("채팅 토큰들 출력:" + tempMsg);
					chattingMsgList.add(tempMsg); // 메시지 토큰을 ArrayList에 추가
				}

				for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList의 모든 메시지를 totalChattingMsg에 저장한다.
					totalChattingMsg += chattingMsgList.get(i);
				}

				System.out.println("MainServer Inmessage에서 protocol이 ChattingPA 일때 들어온 아이디:" + mUserId);
				gBroadCast(room_No, "Paint/pass/ChattingPA@" + mUserId + "@" + totalChattingMsg);
				
				// 현재 게임중일 경우 제시어와 일치하는 확인한다.
				if(r.state && r.suggest.equals(totalChattingMsg)) {
					// 해당 방 유저들에게 정답자의 아이디를 보내면서 라운드가 끝났음을 알린다.(라운드를 같이 보냄)
					gBroadCast(room_No, "Paint/pass/EndRound@" + mUserId + "@" + r.descriptor + "@" + r.round);
					r.state = false; // 라운드 종료
					
					// 출제자와 정답자에게 경험치 10을 증가시키고, 누적 정답 개수 하나를 증가시킨다.
					userExpUpdate(r.descriptor, mUserId, room_No);
				}
				break;

			// #초기 게임방에 입장했을때 정보를 요구한다
			case "GameRoomCheck":
				/*
				 * 사용자가 처음에 게임방에 입장했을 때 필요한 부분이다. 기존 방에 입장하고 있는 다른 사용자의 정보와 게임방의 정보를 다시 보내준다.
				 */
				room_No = Integer.parseInt(st.nextToken());

				// 제일 처음, 방의 정보를 보내준다.
				for (int i = 0; i < room_vc.size(); i++) {
					r = (RoomInfo) room_vc.elementAt(i);
					if (r.room_No == room_No) { // 같은 방 번호가 존재할 시

						send_Message("Paint/pass/RoomInfo@" + mUserId + "@" + r.room_name + "@" + r.room_PW + "@"
								+ r.fixed_User + "@" + r.Room_user_vc.size() + "@" + r.roomCaptainID);
						break;
					}
				}

//	                        // 이미 방에 접속해 있는 유저에게 자신의 정보를 보낸다.
	            for (int i = 0; i < user_vc.size(); i++) {
	               UserInfo u = (UserInfo) user_vc.elementAt(i);
	               // 사용자의 아이디와 같은 아이디를 찾아
	               if (mUserId.equals(u.userID)) {
	                  // 해당 유저의 정보를 그 방에 있는 모두에게 보낸다.
	                  gBroadCast(room_No,
	                        "Paint/pass/NewUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + u.corAnswer);
	               }
	            }

	            // 해당 유저를 게임방 Room_user_vc에 추가한다.
	            Pointer:
	            for (int i = 0; i < room_vc.size(); i++) {
	               r = (RoomInfo) room_vc.get(i);
	               for (int j = 0; j < user_vc.size(); j++) {
	                  UserInfo u = (UserInfo) user_vc.get(j);
	                  // 해당 아이디를 찾으면
	                  if (mUserId.equals(u.getUserID())) {
	                     // 해당 유저를 방 유저 리스트에 추가한다.
	                     System.out.println("asdasdsadsadas");
	                     r.Room_user_vc.add(u);
	                     break Pointer;
	                  }
	               }
	            }

	            // 이미 입장 중인 유저가 있으면 해당 유저들의 정보를 먼저 보낸다.(여기에 자신의 정보도 포함되어 있음)
	            for (int i = 0; i < room_vc.size(); i++) {
	               r = (RoomInfo) room_vc.elementAt(i);
	               if (r.room_No == room_No) { // 같은 방 번호가 존재할 시
	                  // 해당 방에 있는 유저들의 정보를 보낸다.
	                  for (int j = 0; j < r.Room_user_vc.size(); j++) {
	                     UserInfo u = (UserInfo) r.Room_user_vc.get(j);
	                     send_Message(
	                           "Paint/pass/OldUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + u.corAnswer);

	                     // 테스트 코드
	                     System.out.println("최대 :" + r.fixed_User + ", 현재 : " + r.Room_user_vc.size());

	                     // 최대 인원과 현재 인원이 같을 경우, 메시지를 보내는 마지막에 방 전체 인원을 대상으로 게임 시작을 알린다.
	                     if (r.fixed_User == r.Room_user_vc.size() && j == r.Room_user_vc.size() - 1) {
	                        gBroadCast(room_No, "Paint/pass/GameStart@pass@");
	                     }
	                  }
	                  break;
	               }
	            }
	            
	            // 유저의 입장이 끝나면 대기실에도 인원의 변동을 알려줘야 한다.
	            for (int i = 0; i < room_vc.size(); i++) {
	               r = (RoomInfo) room_vc.elementAt(i);
	               if (r.room_No == room_No) { // 같은 방 번호가 존재할 시
	                  // 해당 브로드 캐스트를 받는거 만으로 게임방 패널을 갱신한다.
	            	   BroadCast("WaitingRoom/pass/RoomInfoUpdate@pass");
	               }
	            }
	            
				break;
				
				// # 라운드를 진행한다.
			case "RoundStart":
				// 라운드 진행을 요청한 게임방의 번호를 받는다.
				room_No = Integer.parseInt(st.nextToken());
				UserInfo nextU = null;

				// 해당 방의 정보를 가져온다.
				r = null;
				for (int i = 0; i < room_vc.size(); i++) {
					r = (RoomInfo) room_vc.get(i);
					// 같은 방번호를 찾으면 저장한 후 break
					if (r.room_No == room_No) {
						break;
					}
				}
				r.state = true; // 게임중 상태로 전환
 
				r.round++; // round 1을 가산
				// 라운드가 12 이하면 아직 게임이 끝나지않음
				if (r.round <= 12) {
					if(r.round == 1) { // 1라운드 일떄, 
						r.question = new Question(); // 방 객체에 Question 객체를 생성
					}
					
					// 제시어로 뽑힌 문자를 저장한다. 1라운드부터 시작하므로 인수는 1
					r.suggest = r.question.selQuestion(r.round);
					
					// 순서를 알아내기위해 다음을 계산한다. (모든 라운드에 적용가능)
					int index = r.Room_user_vc.size() - (12 - r.round) % r.Room_user_vc.size() - 1;
					
					// 알아낸 인덱스 번호로 유저를 가져온다.
					UserInfo u = (UserInfo) r.Room_user_vc.get(index);
					
					// 해당 유저가 descriptor라는 것을 저장한다.
					r.descriptor = u.userID;
					
					if(r.round != 12) {
						// 다음 유저 정보를 보내기 위함.
						int nextIndex = r.Room_user_vc.size() - (12 - r.round + 1) % r.Room_user_vc.size() - 1;
						nextU = (UserInfo) r.Room_user_vc.get(nextIndex);
						
						// 해당 유저에게만 자신의 순서임을 알리고 나머지 유저들에게는 문제를 맞추도록 한다.(문제를 내는 사람한테만 제시어를 넘김)
						gSelectiveCast(room_No, u.userID, true,
								"Paint/pass/YourTurn@pass@" + u.userID + "@" + nextU.userID + "@" + r.suggest); // 순서인 유저에게만

						gSelectiveCast(room_No, u.userID, false,
								"Paint/pass/Solve@pass@" + u.userID + "@" + nextU.userID); // 나머지 유저에게만

					} else { // 마지막 라운드일 경우
						gSelectiveCast(room_No, u.userID, true,
								"Paint/pass/YourTurn@pass@" + u.userID + "@[마지막라운드]@" + r.suggest); // 순서인 유저에게만
						gSelectiveCast(room_No, u.userID, false, "Paint/pass/Solve@pass@" + u.userID + "@[마지막라운드]"); // 나머지 유저에게만

					}
					
				} else {

					/* 게임이 끝났음을 알리는 코드 */

				}

				break;
				
			// #사용자가 그림을 그리면
			case "GameRoomPaint":
				int gameRoomNo = Integer.parseInt(st.nextToken());
				String mouseState = st.nextToken();
				if (mouseState.equals("mousePress")) {
					String receiveColor = st.nextToken();
					System.out.println("MainServer에서 받은 방번호: " + gameRoomNo + "펜 컬러:" + receiveColor);
					gSelectiveCast(gameRoomNo,mUserId,false,"GameRoomPaint/pass/mousePress/"+receiveColor);
				} else if (mouseState.equals("mouseRelease"))
					gSelectiveCast(gameRoomNo,mUserId,false,"GameRoomPaint/pass/mouseRelease");
				else if (mouseState.equals("mouseDrag")) {
					String pointX1 = st.nextToken();
					String pointY1 = st.nextToken();
					String pointX2 = st.nextToken();
					String pointY2 = st.nextToken();
					String receiveThick = st.nextToken();
					String receiveEraserSel = st.nextToken();

					System.out.println("MainServer에서 받은 방번호:" + gameRoomNo + ", x1좌표:" + pointX1 + ", y1좌표:" + pointY1
							+ ", x2좌표:" + pointX2 + ", y2좌표:" + pointY2 + ", 펜 굵기: " + receiveThick + ", 지우개 선택여부: "
							+ receiveEraserSel);
					gSelectiveCast(gameRoomNo, mUserId,false, "GameRoomPaint/pass/mouseDrag/" + pointX1 + "/" + pointY1 + "/" + pointX2
							+ "/" + pointY2 + "/" + receiveThick + "/" + receiveEraserSel);

					
				} else if (mouseState.equals("canvasClear"))
					gSelectiveCast(gameRoomNo, mUserId, false, "GameRoomPaint/pass/canvasClear");
				break;
			}
		}

		// 문자열 전송 메소드
		// 대기실 사용자에게 메시지를 보내는 부분
		private void BroadCast(String str) {
			for (int i = 0; i < wRoom_vc.size(); i++) // 현재 접속된 사용자에게 전송
			{
				UserInfo u = (UserInfo) wRoom_vc.elementAt(i); // i번쨰에 있는 사용자에게 메세지를 전송
				System.out.println("[" + u.getUserID() + "]에게 : " + str);
				u.send_Message(str);
			}
		}

		// 게임방에 있는 유저들에게 메시지를 보내는 부분
		private void gBroadCast(int room_No, String str) {
			for (int i = 0; i < room_vc.size(); i++) // 게임방에 있는 사용자에게 전송
			{
				RoomInfo r = (RoomInfo) room_vc.elementAt(i); // i번째에 있는 방을 찾아
				// 입력받은 방 번호와 일치하는 방 번호를 찾으면
				if (room_No == r.room_No) {
					// 해당 방에 있는 유저 전원에게 메시지를 보낸다.
					for (int j = 0; j < r.Room_user_vc.size(); j++) {
						// 게임방의 있는 유저 한명을 찾아서
						UserInfo u = (UserInfo) r.Room_user_vc.get(j);
						System.out.println("[" + u.getUserID() + "]에게 : " + str);
						// 메시지를 보낸다.
						u.send_Message(str);
					}
				}
			}
		}
		
		// 인게임에서 그림을 그리는 사람, 혹은 방장 등 특별한 인원을 제외하고 혹은 그 인원만 선택적으로 할때 사용
		/*
		 * [메소드 설명] room_No은 해당 게임방을 찾을 때 사용하는 인수 
		 * id는 선택적인 옵션에 적용할 대상 id 
		 * flag가 'true'이면 해당 유저에게만, 
		 *       'false'이면 해당유저를 제외한 나머지에게
		 * str로 받은 메시지를 전달한다.
		 */
		private void gSelectiveCast(int room_No, String id, boolean flag, String str) {
			Pointer: for (int i = 0; i < room_vc.size(); i++) // 게임방에 있는 사용자에게 전송
			{
				RoomInfo r = (RoomInfo) room_vc.elementAt(i); // i번째에 있는 방을 찾아
				// 입력받은 방 번호와 일치하는 방 번호를 찾으면
				if (room_No == r.room_No) {
					// 해당 방에 있는 유저에게 메시지를 보낸다.
					for (int j = 0; j < r.Room_user_vc.size(); j++) {
						// 게임방의 있는 유저 한명을 찾아서
						UserInfo u = (UserInfo) r.Room_user_vc.get(j);

						// 해당 유저에게만 메시지를 보낼 때
						if (id.equals(u.getUserID()) && flag) {
							System.out.println("[" + u.getUserID() + "]에게 : " + str);
							// 메시지를 보낸다.
							u.send_Message(str);
							break Pointer; // 해당 유저에게만 메시지를 보내면 되므로 전체 break 처리
						}
						// 해당 유저를 제외한 나머지 인원에게 메시지를 보낼 떄
						else if (!id.equals(u.getUserID()) && !flag) {
							System.out.println("[" + u.getUserID() + "]에게 : " + str);
							// 메시지를 보낸다.
							u.send_Message(str);
						}

					}
				}
			}
		}

		// 서버쪽에서도 클라이언트와 대화할 수 있는 메소드
		private void send_Message(String str) // 문자열을 받아서 전송
		{
			try {
				dos.writeUTF(str);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		// 출제자와 정답자의 경험치와 정답 개수를 늘려주기 위한 메소드
		private void userExpUpdate(String descriptor, String userID, int room_No) {
			boolean levelUp = false;
			User desUser = null;
			User user = null;
			
			// 회원 전체 유저들 중에서
			for(int i=0; i<allUser_vc.size(); i++) {
				user = (User)allUser_vc.get(i);
				//해당 유저를 찾아서
				if(userID.equals(user.getId())){
					// 레벨업을 확인한다. 
					levelUp = user.expUpdate();
					allUser_vc.set(i, user); // 적용
				}
			}
			for(int i=0; i<allUser_vc.size(); i++) {
				desUser = (User)allUser_vc.get(i);
				//해당 유저를 찾아서
				if(descriptor.equals(desUser.getId())){
					// 레벨업을 확인한다. 
					levelUp = desUser.expUpdate();
					allUser_vc.set(i, desUser); // 적용
				}
			}
			// 둘중 한 명이라도 레벨업을 할 시, 레벨업 이벤트를 진행
			
			
			// 입력받은 방번호로 방을 찾는다.
			for(int i=0; i<room_vc.size(); i++) {
				RoomInfo r = (RoomInfo)room_vc.get(i);
				// 일치하는 방을 찾으면
				if(room_No == r.room_No) {
					
					// 정답자
					for(int j=0; j<r.Room_user_vc.size(); j++) {
						UserInfo ui = (UserInfo)r.Room_user_vc.get(j);
						//해당 유저를 찾아서
						if(userID.equals(ui.userID)) {
							//경험치가 증가되었음을 알린다.(DB랑 연관있는 벡터로부터 가져온다. 유사 DB)
							gBroadCast(room_No, "Paint/pass/ExpUpdate@"+ui.userID+"@"+user.getLevel()+"@"+user.getExp()+"@"+user.getCorAnswer());
						}
					}
					
					// 출제자
					for(int j=0; j<r.Room_user_vc.size(); j++) {
						UserInfo ui = (UserInfo)r.Room_user_vc.get(j);
						//해당 유저를 찾아서
						if(descriptor.equals(ui.userID)) {
							//경험치가 증가되었음을 알린다.(DB랑 연관있는 벡터로부터 가져온다. 유사 DB)
							gBroadCast(room_No, "Paint/pass/ExpUpdate@"+ui.userID+"@"+desUser.getLevel()+"@"+desUser.getExp()+"@"+desUser.getCorAnswer());
							// 레벨업을 하였을 경우
							
						}
					}
					
					// 레벨업을 하였을 경우
					if(levelUp) {
						gBroadCast(room_No, "Paint/pass/UserLevelUp@pass@");
					}
				}
			}
			
		}
		
		
	} // UserInfo class 끝

	// 게임방
	class RoomInfo {
	      private int room_No; // 게임방 번호
	      private String room_name; // 게임방 이름
	      private String room_PW; // 게임방 비밀번호(공개일 경우 null)
	      private int fixed_User; // 유저 정원

	      
	      private Question question; // 제시어 객체 저장용 변수
	      private String roomCaptainID; // 방장 id
	      private String descriptor; // 문제를 설명해주는 사람
	      private String trun; // 현재 그리는 유저id
	      private String suggest; // 현재 제시어
	      private int round = 0; // 현재 게임 round(초기값 0)
	      private boolean state = false; // 게임 진행 상태를 확인한다.(true - 게임중 )
	      
	      
	      /*
	       * 생성할 때 생성한 유저의 객체를 전달받아 리스트에 저장하고 입장하는 유저들의 유저 객체를 user_vc에서 제거하고 room_vc로 옮기는
	       * 작업을 한다. 그리고 해당 방의 유저 수를 리턴해야할 때, Room_user_vc의 사이즈를 리턴한다.
	       */
	      private Vector<UserInfo> Room_user_vc = new Vector<UserInfo>(); // 게임방 유저 Vector

	      RoomInfo(int room_No, String room_name, String room_PW, int fixed_User) // 방번호를 기준으로!
	      {
	         this.room_No = room_No;
	         this.room_name = room_name;
	         this.room_PW = room_PW;
	         this.fixed_User = fixed_User;
	      }
	      
//	      public void BroadCast_Room(String str) // 현재 방의 모든 사람에게 알린다.
//	      {
//	         for (int i = 0; i < Room_user_vc.size(); i++) {
//	            UserInfo u = (UserInfo) Room_user_vc.elementAt(i);
	//
//	            u.send_Message(str); // 넘어온 문자열을 보내준다.
//	         }
//	      }
	   }
}
