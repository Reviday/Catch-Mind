package kh.mini.project.waiting_room.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.main.view.Main;
import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.User;
import kh.mini.project.model.vo.UserInfo;

public class WaitingRoom extends JFrame{
// Frame, Panel
	private JFrame WaitingRoomView = new JFrame("Waiting Room"); // 메인 프레임
	private JScrollPane chattingView = new JScrollPane(); // 채팅을 보이게하는 스크롤 페인
	private JPanel userInfoView = new JPanel(); // 유저 정보 패널
	private JPanel userListView = new JPanel(); // 유저 리스트 패널
	private JPanel gameRoomView = new JPanel(); // 게임방 패널
	private JPanel[] gameRoom = new JPanel[6]; // 24개의 방을 개설 => 버튼으로 해볼까??
	private JPanel[] userList = new JPanel[10]; // 50명의 유저 리스트를 띄우는 패널
	
// Label
	private JLabel mainMenuBar = new JLabel();
//	private JLabel userList_Label = new JLabel("User List");
	private JLabel gameRoom_Label = new JLabel();
	private JLabel[] userID_lb = new JLabel[userList.length]; 		// 유저 ID 라벨 배열
	private JLabel[] gameRoomNumber_lb = new JLabel[gameRoom.length]; 		// 게임방 넘버 라벨 배열, gameRoom 배열의 크기만큼 생성
	private JLabel[] gameRoomTitle_lb = new JLabel[gameRoom.length]; 		// 게임방 제목 라벨 배열, gameRoom 배열의 크기만큼 생성
	private JLabel[] gameRoomPlayerCount_lb = new JLabel[gameRoom.length];  // 게임방 인원수 라벨 배열, gameRoom 배열의 크기만큼 생성
	
	
// Textfield	
	private JTextField chatting_tf; // 채팅 내용을 입력받기 위한 텍스트필드	
	private JTextArea chattingArea = new JTextArea(); // 채팅 스크롤 페인에 올려놓을 채팅 TextArea
	
// list
	private JList user_Li = new JList();
//	private JList gameRoom_List = new JList();
	
// Network 자원 변수
	private Socket socket;// 사용자 소켓
	private int port; // 포트번호		
	private String id =""; 
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수	
	private int mouseX, mouseY; // 마우스 좌표용 변수
	private StringTokenizer st; // 프로토콜 구현을 위해 필요함. 소켓으로 입력받은 메시지를 분리하는데 쓰임.
	private UserInfo userInfo; // 접속자의 정보를 저장하는 객체(이 이름으로 접근하면 사용자 본인의 정보에 접근할 수 있다.)
//	private Vector user_list = new Vector(); // 유저 리스트 Vector
	private Vector room_list = new Vector(); // 방 리스트 Vector
	private Vector gUser_list = new Vector(); // 게임방 유저 리스트 Vector
	private Vector<UserInfo> user_list = new Vector<UserInfo>();
	private Toolkit tk = Toolkit.getDefaultToolkit();
	Image img = tk.getImage(Main.class.getResource("/images/커서테스트.png"));
	Cursor myCursor = tk.createCustomCursor(img, new Point(10,10), "dynamite stick");
	// 방 만들기에 필요한 변수
	private String title; // 방제목
	private String roomPW; // 방 비밀번호
	private int uCount; // 방 인원수
	private int roomNo; // 방 번호
	
	
//Image	
	// #MainView 배경
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/WaitingRoom_Background.png")).getImage();
			//Main 클래스의 위치를 기준으로 이미지 파일의 위치를 찾은 다음에 이미지 인스턴스를 해당 변수에 초기화 해줌(상대경로 같은 절대경로)	
	
	// Button Icon (basic : 버튼의 기본 상태, Entered : 버튼에 마우스를 가져간 상태) 
	// => 버튼 기본상태, 마우스를 올려놨을 때 상태, 눌렀을 때 상태 3가지 가능?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon createRoomBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon createRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon rightRBasicImage = new ImageIcon(Main.class.getResource("/images/화살표1_R_basic.png"));
	private ImageIcon rightREnteredImage = new ImageIcon(Main.class.getResource("/images/화살표1_R_entered.png")); 
	private ImageIcon leftRBasicImage = new ImageIcon(Main.class.getResource("/images/화살표1_L_basic.png"));
	private ImageIcon leftREnteredImage = new ImageIcon(Main.class.getResource("/images/화살표1_L_entered.png")); 
	private ImageIcon gamgeRoomBasicImage = new ImageIcon(Main.class.getResource("/images/gameroom.png")); 
	private ImageIcon gamgeRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/gameroomEntered1.png")); 
	private ImageIcon gamgeRoomPressedImage = new ImageIcon(Main.class.getResource("/images/gameroomPressed.png"));
	private ImageIcon userInfoPanelImage = new ImageIcon(Main.class.getResource("/images/userInfoPanel.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // 나가기 버튼
	private JButton createRoomButton = new JButton(createRoomBasicImage); // 방만들기 버튼
	private JButton rightRButton = new JButton(rightRBasicImage); // 방 오른쪽 넘기기 버튼
	private JButton leftRButton = new JButton(leftRBasicImage); // 방 왼쪽 넘기기 버튼
	
	public WaitingRoom() {
		//실행과 동시에 네트워크 자원과 id를 MainView로부터 이어받아온다.
		id = MainView.getId();
		dis = MainView.getDis();
		dos = MainView.getDos();
		
		Font font = new Font("Inconsolata",Font.BOLD,15); // 폰트 설정
		
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함)
		setTitle("Catch Mind"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main에서 고정시킨 화면 해상도를 사용
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setVisible(true); // 윈도우를 볼 수 있음.
		setLayout(null);
		setCursor(myCursor);
		
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
		
		// #유저 리스트 
//		userList_Label.setBounds(30, 20, 200, 30);
//		userList_Label.setBackground(new Color(40,40,40,40));
//		add(userList_Label);
		
				
	// JScrollPane
		// #채팅뷰
		chattingView.setBounds(240, 490, 768, 200);
		chattingView.setBackground(new Color(40,40,40,40));
		chattingView.setViewportView(chattingArea);
		chattingArea.setBackground(new Color(0,0,0,0)); 
		chattingArea.setFont(font);
		chattingArea.setForeground(Color.BLACK);
		chattingArea.setEditable(false); // 해당 필드를 수정할 수 없음
		add(chattingView); 
		
		// #유저 정보(자신) 뷰
		userInfoView.setBounds(30, 250, 190, 68);
		userInfoView.setBackground(new Color(40,40,40,40));
		add(userInfoView); 
		
		
		// #유저 리스트 뷰
		userListView.setBounds(30, 330, 190, 374);
		userListView.setLayout(new FlowLayout(FlowLayout.CENTER));
		userListView.setBackground(new Color(40,40,40,40));
		allocationUserInfo();
		add(userListView); 
		
		// #게임방 뷰
		gameRoomView.setBounds(240, 110, 768, 370);
		gameRoomView.setLayout(new FlowLayout(FlowLayout.CENTER));
		gameRoomView.setBackground(new Color(40,40,40,40));
		allocationRoom(); // 대기실에 게임방이 보이도록 하는 메소드
		add(gameRoomView); 
		
		
	// TextField
		chatting_tf = new JTextField(); 
		chatting_tf.setBounds(260, 690, 728, 30);
		chatting_tf.setBackground(new Color(40,40,40,40));
		add(chatting_tf);
		chatting_tf.setDocument(new JTextFieldLimit(45)); // 채팅 45자 제한 	 
		chatting_tf.setFont(font);
		chatting_tf.setForeground(Color.BLACK);
		chatting_tf.addKeyListener(new keyAdapter());
			
	// Button
		// #나가기 버튼
		exitButton.setBounds(440, 60, 180, 50);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
//				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
				exitButton.setCursor(myCursor);
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					System.exit(0); // 프로세스 종료
				}
			}
		});
		
		// #방만들기 버튼
		createRoomButton.setBounds(260, 60, 180, 50);
		add(createRoomButton);
		createRoomButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				createRoomButton.setIcon(createRoomEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				createRoomButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}

			// 마우스를 버튼에서 떼었을때 이벤트
			@Override
			public void mouseExited(MouseEvent e) {
				createRoomButton.setIcon(createRoomBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				createRoomButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			// 버튼을 떼었을때 이벤트
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					new CreateRoom();
				}
			}
		});
		
		// #방 오른쪽 넘기기 버튼
		rightRButton.setBounds(640, 450, 60, 40);
		add(rightRButton);
		rightRButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				rightRButton.setIcon(rightREnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				rightRButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}

			// 마우스를 버튼에서 떼었을때 이벤트
			@Override
			public void mouseExited(MouseEvent e) {
				rightRButton.setIcon(rightRBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				rightRButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			// 버튼을 떼었을때 이벤트
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});

		// #방 왼쪽 넘기기 버튼
		leftRButton.setBounds(540, 450, 60, 40);
		add(leftRButton);
		leftRButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				leftRButton.setIcon(leftREnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				leftRButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}

			// 마우스를 버튼에서 떼었을때 이벤트
			@Override
			public void mouseExited(MouseEvent e) {
				leftRButton.setIcon(leftRBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				leftRButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			// 버튼을 떼었을때 이벤트
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});
		
		//대기실 입장시, 환영하는 문구 출력
		chattingArea.append("["+id+"]님 환영합니다! \n");
	} // WaitingRoom() 생성자 끝

	private void Inmessage(String str) // 서버로부터 들어오는 모든 메세지
	{
		st = new StringTokenizer(str, "@");  // 어떤 문자열을 사용할 것인지, 어떤 문자열로 자를 것인지 =>  [ NewUser/사용자ID ] 형태로 들어옴
		
		String protocol = st.nextToken(); // 프로토콜을 저장한다.
		String Message = st.nextToken(); // 메시지를 저장한다.
		
		System.out.println("프로토콜 : " + protocol);
		System.out.println("내용 : " + Message);
		
		
		if(protocol.equals("NewUser")) // 새로운 접속자
		{
			// 새로운 접속자의 정보를 가져와 저장한다.
			int level = Integer.parseInt(st.nextToken());
			int exp = Integer.parseInt(st.nextToken());
			int corAnswer = Integer.parseInt(st.nextToken());
			// 가져온 정보로 객체를 생성
			UserInfo u = new UserInfo(Message, level, exp, corAnswer);
			// 해당 객체를 Vector에 추가
			user_list.add(u);
			testMethod();
//			allocationUserInfo(); // 유저리스트 갱신
			updateUserInfo();
		}
		else if(protocol.equals("UserInfo")) // 접속자의 정보를 가져와 저장
		{
			// 사용자의 정보를 가져와 저장한다.
			int level = Integer.parseInt(st.nextToken());
			int exp = Integer.parseInt(st.nextToken());
			int corAnswer = Integer.parseInt(st.nextToken());
			// 가져온 정보로 객체를 생성
			userInfo = new UserInfo(Message, level, exp, corAnswer);
			// 해당 객체를 Vector에 추가
			user_list.add(userInfo);
//			allocationUserInfo(); // 유저리스트 갱신
			updateUserInfo();
		}
		else if(protocol.equals("OldUser")) // 접속자의 정보를 받아온다.(초기 세팅 작업)
		{ 
			// 해당 프로토콜로 받는 사용자 정보는 첫 로그인 한 번만 적용된다.
			// 이전에 접속중이던 모든 유저의 정보를 가져온다.
			int level = Integer.parseInt(st.nextToken());
			int exp = Integer.parseInt(st.nextToken());
			int corAnswer = Integer.parseInt(st.nextToken());
			// 가져온 정보로 객체를 생성
			UserInfo u = new UserInfo(Message, level, exp, corAnswer);
			// 해당 객체를 Vector에 추가
			user_list.add(u);
			
			// 마지막 토큰이 last일 경우 리스트를 갱신한다.
			String lastCheck = st.nextToken();
			if(lastCheck.equals("last")) 
//				allocationUserInfo();
				updateUserInfo();
		}
		else if(protocol.equals("Note")) // 쪽지
		{
			String note = st.nextToken(); // 받은 내용
			
			System.out.println(Message+" 사용자로부터 온 쪽지 "+note);
			
			JOptionPane.showMessageDialog(null, note, Message+"님으로 부터 쪽지", JOptionPane.CLOSED_OPTION);
		}
		else if(protocol.equals("user_list_update"))
		{
//			User_list.setListData(user_list);
		}
		else if(protocol.equals("CreateRoom")) // 방을 만들었을 때
		{
			/* 방을 생성함과 동시에, 게임창으로 넘어가도록 한다. */ 
		}
		else if(protocol.equals("New_Room")) // 새로운 방을 만들었을 때
		{
			/* 모든 방 정보를 받아서 생성하고, 다시 패널에 띄우는 작업이 필요. */
			roomNo = Integer.parseInt(st.nextToken()); // 방번호
			room_list.add(Message);
//			room_list.add(Message);
//			Room_list.setListData(room_list);
		}
		else if(protocol.equals("ChattingWR"))
		{
			String msg = st.nextToken(); 
			System.out.println("내용 : " + msg);
			chattingArea.append("["+Message+"] : "+msg+"\n");
		}
	}
	
	void testMethod() {
		for(int i=0; i<user_list.size(); i++) 
		{
			UserInfo u = (UserInfo)user_list.elementAt(i);
			System.out.println("test 유저 정보 ["+u.getUserID()+ "] : "+ u);
		}
	}
	
	private void send_message(String str) // 서버에게 메세지를 보내는 부분
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) // 에러 처리 부분
		{
			e.printStackTrace();
		}
	}
	
	// MainView 클래스에서 WaitingRoom 클래스로 메시지를 전달하기 위해 사용하는 메소드
	public void wr_Inmessage(String str) {
		Inmessage(str);
	}
	
	// 대기실 24개의 방을 생성하기 위한 메소드 (방 개수의 변동이 생길때마다 동작)
	private void allocationRoom() {
		// 추후 생성된 방에게만 이벤트를 할당하도록 변경
		for(int i=0; i<gameRoom.length; i++) {
			// 내부 클래스 GameRoomPanel 클래스를 이용해서 gameRoom Panel을 생성
			GameRoomPanel grp = new GameRoomPanel(gamgeRoomBasicImage.getImage());
			// 현재 생성된 방의 개수만큼 리스너를 적용시킨다
			if(i<gameRoom.length) {
				grp.addMouseListener( new MouseAdapter() {
					// 마우스를 버튼에 올려놨을 때 이벤트
					@Override
					public void mouseEntered(MouseEvent e) {
						grp.setGRImage(gamgeRoomEnteredImage.getImage()); // 마우스를 올려놨을때 이미지 변경(Entered Image)
						grp.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
					}
					// 마우스를 버튼에서 떼었을때 이벤트
					@Override  
					public void mouseExited(MouseEvent e) {
						grp.setGRImage(gamgeRoomBasicImage.getImage()); // 마우스를 떼었을때 이미지 변경(Basic Image)
						grp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
					}
					// 마우스로 버튼을 눌렀을 때 이벤트
					@Override 
					public void mousePressed(MouseEvent e) {
						if(e.getButton()==1) {
							grp.setGRImage(gamgeRoomPressedImage.getImage()); // 마우스를 눌렀을 때 이미지 변경(Pressed Image)
							
						}
					}
					// 마우스로 버튼을 누른 후 떼었을 때 이벤트
					@Override
					public void mouseReleased(MouseEvent e) {
						if(e.getButton()==1) {
							grp.setGRImage(gamgeRoomEnteredImage.getImage()); // 누른 버튼이 떼어졌을 때 이미지 변경(Entered Image) - 마우스는 이미 패널에 올려놓여진 상태이기 때문에
							
						}
					}
				});
			}
			gameRoom[i] = grp;
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	
	// 방을 생성하는 메소드
	private void createRoom(String title) {
		Font roomFont = new Font("Inconsolata",Font.BOLD,17); // 폰트 설정
//		int roomNo = 0;
		// 1부터 생성가능한 방의 최대 개수만큼의 범위에서 랜덤하게 번호를 한 개 할당한다.(중복을 허용하지 않는다.)
//		int roomNo = (int)(Math.random()*gameRoom.length) + 1;  // 이 기능을 추가하는건 나중에.. 일단 순차적으로 번호 할당하는 수준 부터 시작.
		
		// 게임방 번호를 설정할 JLabel을 할당
		gameRoomNumber_lb[roomNo-1] = new JLabel();
		String number = "";
		// 방번호 설정. roomNo값에 따라 해당 방번호를 지정. 000 의 형태로 설정. 
		int temp = roomNo, tempI;
		for (int j = 1; j <= 3; j++) {
			if ((tempI=temp % 10)==0) {
				number = "" + 0 + number;
			} else {
				number = "" + tempI + number;
			}
			temp /= 10;
		}
		gameRoomNumber_lb[roomNo-1].setText(number);
		gameRoomNumber_lb[roomNo-1].setFont(roomFont);
		gameRoomNumber_lb[roomNo-1].setBounds(32, 23, 40, 20);
		gameRoomNumber_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoomNumber_lb[roomNo-1].setLayout(null);
		gameRoom[roomNo-1].add(gameRoomNumber_lb[roomNo-1]);
		
		// 게임방 제목을 설정할 JLabel을 할당
		gameRoomTitle_lb[roomNo-1] = new JLabel();
		gameRoomTitle_lb[roomNo-1].setText("No Title");
		gameRoomTitle_lb[roomNo-1].setFont(roomFont);
		gameRoomTitle_lb[roomNo-1].setBounds(90, 23, 200, 20);
		gameRoomTitle_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoom[roomNo-1].add(gameRoomTitle_lb[roomNo-1]);
		
		// 게임방 인원수를 설정할 JLabel을 할당
		gameRoomPlayerCount_lb[roomNo-1] = new JLabel();
		gameRoomPlayerCount_lb[roomNo-1].setText("0 / 6");
		gameRoomPlayerCount_lb[roomNo-1].setFont(roomFont);
		gameRoomPlayerCount_lb[roomNo-1].setBounds(275, 68, 50, 20);
		gameRoomPlayerCount_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoom[roomNo-1].add(gameRoomPlayerCount_lb[roomNo-1]);
		
		// 이렇게 생성한 패널을 뷰에 추가한다.
		gameRoomView.add(gameRoom[roomNo-1]);
	}
	
	// 방이 생성될 때마다 방번호에 따라 순서를 재배치하는 메소드
	public void relocationRoom() {
		
		
		
		// 재배치된 방을 다시 뷰에 적용
		for(int i=0; i<gameRoom.length; i++) {
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	// 50명의 유저 리스트를 생성하기 위한 메소드
	private void allocationUserInfo() {
		Font infoFont = new Font("Inconsolata",Font.BOLD,17); // 폰트 설정
		for(int i=0; i<userList.length; i++) {
			// 내부 클래스 GameRoomPanel 클래스를 이용해서 User List Panel을 생성
			GameRoomPanel grp = new GameRoomPanel(userInfoPanelImage.getImage());
//			grp.addMouseListener( new MouseAdapter() {
//			});
			userList[i] = grp;
			
			// JPanel,JLabel을 선언 및 할당
//			userList[i] = new JPanel();
//			userList[i].setSize(180,32);
			userID_lb[i] = new JLabel();
			// 현재 접속된 유저의 리스트 만큼 텍스트를 지정해준다. (저장값은 유저id)
//			if(i<user_list.size()) {
//				UserInfo u = (UserInfo)user_list.get(i);
//				userID_lb[i].setText(u.getUserID());
//			} else {
//				userID_lb[i].setText("no userID");
//			}
			// userID_lb 셋팅
			userID_lb[i].setFont(infoFont);
//			System.out.println(userID_lb[i].getText());
			userID_lb[i].setBounds(40, 1, 160, 30);
			userID_lb[i].setForeground(Color.black);
			userID_lb[i].setLayout(null);
			userList[i].add(userID_lb[i]);
			userListView.add(userList[i]);
		}
//		add(userListView);
		revalidate();
		repaint();
	}
	
	private void updateUserInfo() {
		Font infoFont = new Font("Inconsolata",Font.BOLD,17); // 폰트 설정
		for(int i=0; i<userList.length; i++) {
			// JPanel,JLabel을 선언 및 할당
			// 현재 접속된 유저의 리스트 만큼 텍스트를 지정해준다. (저장값은 유저id)
			if(i<user_list.size()) {
				UserInfo u = (UserInfo)user_list.get(i);
				userID_lb[i].setText(u.getUserID());
			} else {
//				userID_lb[i].setText("no userID");
			}
			// userID_lb 셋팅
			userID_lb[i].setFont(infoFont);
			System.out.println(userID_lb[i].getText());
			userList[i].add(userID_lb[i]);
			userListView.add(userList[i]);
		}
//		add(userListView);
		userListView.revalidate();
		userListView.repaint();
//		gameRoomView.revalidate();
//		gameRoomView.repaint();
	}
	
	
	// 텍스트 필드 글자 수 제한을 위한 클래스 및 메소드
	public class JTextFieldLimit extends PlainDocument {
		private int limit;
		
		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}
		
		public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
			if (str == null) return;
			
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
				if(message.equals("")) { //아무것도 입력하지 않았을 시 알림창을 띄움
					JOptionPane.showMessageDialog(null, 
							"내용을 입력하시기 바랍니다.","알림",JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingWR/"+id+"/"+message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class 끝
	
	
	/* 아래 paint() 메소드는 GUI Application이 실행되거나 
	 * 활성/비활성으로 인한 변동 영역을 감지했을때, 실행되는 메소드이다. */
	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}	
	
	
	public static void main(String[] args) {
		new WaitingRoom();
	}
	
	
	// 방만들기 프레임
	class CreateRoom extends JFrame{
	// TextField
		private JTextField roomTitel_tf = new JTextField(); // 방 제목
		private JTextField roomPw_tf = new JTextField();  // 방 비밀번호
	
	// ComboBox
		private String[] state = {"공개","비공개"};
		private Integer[] player = {2,3,4,5,6}; // 최대인원 6명으로 설정
		private JComboBox<String> roomState_tf = new JComboBox<String>(state); // 공개/비공개 설정을 위한 콤보박스
		private JComboBox<Integer> rPlayer_tf = new JComboBox<Integer>(player); // 인원수 설정을 위한 콤보박스
		
	// 각종 변수 변수
		private Image viewImage; // 이미지 저장용 변수
		private Graphics viewGraphics; // 그래픽 저장용 변수	
		private int mouseX; // 마우스 좌표 변수
		private int mouseY; // 마우스 좌표 변수
		
	// Image
		// # CreateRoom 배경
		private Image crbackgroundImage = 
				new ImageIcon(Main.class.getResource("/images/CreateRoom.png")).getImage();
		// #방만들기 프레임 내부 버튼용 이미지
		private ImageIcon crCancelBasicImage = new ImageIcon(Main.class.getResource("/images/cancelButtonBasic.png"));
		private ImageIcon crCancelEnteredImage = new ImageIcon(Main.class.getResource("/images/cancelButtonEntered.png")); 
		private ImageIcon createBasicImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonBasic.png"));
		private ImageIcon createEnteredImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonEntered.png"));
		// Button
		private JButton cancelButton = new JButton(crCancelBasicImage); // 취소 버튼
		private JButton createButton = new JButton(createBasicImage); // 방만들기 버튼
		
		public CreateRoom() {
			Font font = new Font("Inconsolata",Font.PLAIN,11); 
			setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정
			setSize(360,213); // null은 최댓값
			setBackground(new Color(0,0,0,0));
			setAlwaysOnTop(true); // 항상 모든 윈도우 위에 위치하도록 함
			setPreferredSize(new Dimension(crbackgroundImage.getWidth(null), crbackgroundImage.getHeight(null)));
			setResizable(false); // 프레임 크기 고정
			setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
			setVisible(true); // 윈도우를 볼 수 있음.
			setLayout(null);	
			
			// 마우스로 창을 움직일 수 있다.
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				// #매뉴바 드래그 시, 움직일 수 있게 한다.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
		
		// TextField / ComboBox
			// # 제목 입력
			roomTitel_tf.setBounds(88,47,244,20);
			roomTitel_tf.setFont(font);
			roomTitel_tf.setDocument(new JTextFieldLimit(20)); // 제목 20자 제한 	 
			add(roomTitel_tf);
			
			// # 공개/비공개 상태 콤보박스
			roomState_tf.setBounds(88,78,78,20);
			roomState_tf.setFont(font);
			add(roomState_tf);
			roomState_tf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String state = roomState_tf.getSelectedItem().toString();
					if(state.equals("공개")) {
						roomPw_tf.setEnabled(false);
					} else if (state.equals("비공개")) {
						roomPw_tf.setEnabled(true);
					}
				}
			});
			
			// # 방 비밀번호 입력(콤보박스 이벤트에 따라 활설/비활성)
			roomPw_tf.setBounds(88,103,78,20);
			roomPw_tf.setFont(font);
			roomPw_tf.setDocument(new JTextFieldLimit(10)); // 비밀번호 10자 제한 	
			roomPw_tf.setEnabled(false); // 초기에 "공개"설정이기때문에 비활성 상태로 둔다.
			add(roomPw_tf);
			
			// # 인원수 설정 콤보박스
			rPlayer_tf.setBounds(88,128,78,20);
			roomPw_tf.setFont(font);
			add(rPlayer_tf);
			
			
		// Button
			// #츼소 버튼
			cancelButton.setBounds(187, 180, 72, 24);
			add(cancelButton);
			cancelButton.addMouseListener(new MouseAdapter() {
				// 마우스를 버튼에 올려놨을 때 이벤트
				@Override
				public void mouseEntered(MouseEvent e) {
					cancelButton.setIcon(crCancelEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
					cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
				}
				
				// 마우스를 버튼에서 떼었을때 이벤트
				@Override  
				public void mouseExited(MouseEvent e) {
					cancelButton.setIcon(crCancelBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
					cancelButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						dispose(); 
					}
				}
			});
			
			// #만들기 버튼
			createButton.setBounds(101, 180, 72, 24);
			add(createButton);
			createButton.addMouseListener(new MouseAdapter() {
				// 마우스를 버튼에 올려놨을 때 이벤트
				@Override
				public void mouseEntered(MouseEvent e) {
					createButton.setIcon(createEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
					createButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
				}
				
				// 마우스를 버튼에서 떼었을때 이벤트
				@Override  
				public void mouseExited(MouseEvent e) {
					createButton.setIcon(createBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
					createButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
				}
				// 버튼을 떼었을때 이벤트
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						// 만들기 버튼을 누르면 설정사항을 그대로 서버에 전송한다.
						title = roomTitel_tf.getText().trim(); // 방 제목
						String state = roomState_tf.getSelectedItem().toString(); // 공개/비공개
						roomPW = null; // 방 비밀번호 (공개면 null로, 비공개면 입력받은 패스워드를 저장)
						if(state.equals("비공개")) {
							roomPW = roomPw_tf.getText().trim();
						} 
						uCount = Integer.parseInt(rPlayer_tf.getSelectedItem().toString()); // rPlayer_tf의 제네릭을 Integer로 해놓음
						
						// 입력받은 값을 서버에 전송한다.
						send_message("CreateRoom/"+id+"/"+title+"/"+state+"/"+roomPW+"/"+uCount);
					}
				}
			});
			
			
		}
		
		
		@Override
		public void paint(Graphics g) {
			viewImage = createImage(360,213);
			viewGraphics = viewImage.getGraphics();
			screenDraw(viewGraphics);
			g.drawImage(viewImage,0,0, null);
		}
		
		public void screenDraw(Graphics g) {
			g.drawImage(crbackgroundImage, 0, 0, null);
			paintComponents(g);
			this.repaint();
		}	
	}
	
	// 게임방&유저info 하나하나를 JPanel을 상속받은 GameRoomPanel 클래스로 생성한다.
	class GameRoomPanel extends JPanel{
		private Image img;
		
		public GameRoomPanel(Image img) {
			this.img = img;
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // null은 최댓값
			setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
			setLayout(null); // 패널에 추가하는 요소들의 위치를 자유롭게 설정하기 위해 Layout을 null로 해준다.
		}
		
		// 패널을 열었을 때 자동으로 이미지를 그려주는 메소드
		public void paintComponent(Graphics g)  {
			g.drawImage(img, 0, 0, null);
		}
		
		// 이미지를 바꿔주기 위한 메소드
		public void setGRImage(Image img) {
			this.img = img;
		}
	}
}
