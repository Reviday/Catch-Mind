package kh.mini.project.main.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.waiting_room.view.WatingRoom;

public class MainView extends JFrame{
// Frame, Panel
	private JFrame mainView = new JFrame("CatchMind"); // 메인 프레임
	private JPanel loginView = new JPanel(); // 보조 프레임(패널) - 로그인 패널
	
//Label
	private JLabel mainMenuBar = new JLabel();
	private JLabel serverIp_lb = new JLabel("Server IP : "); // 임시  ip 레이블
	private JLabel port_lb = new JLabel("Port Number : "); // 임시 포트 번호 레이블
	private JLabel id_lb = new JLabel("ID : "); // 임시 id 레이블
	private JLabel pw_lb = new JLabel("PW : "); // 임시  pw 레이블

// Textfield	
	private JTextField serverIp_tf; // Server IP를 입력받기 위한 텍스트필드
	private JTextField port_tf; // 포트번호를 입력받기 위한 텍스트필드
	private JTextField id_tf; // ID를 입력받기 위한 텍스트 필드
	private JPasswordField pw_tf; // PW를 입력받기 위한 텍스트 필드
	
// Network 자원 변수
	private static Socket socket; // 사용자 소켓
	private static int port; // 포트번호	
	private String ip=""; // 127.0.0.1 은 자기 자신
	private static String id=""; // 사용자 ID
	private String pw=""; // 사용자 PW => 지금은 가입없이 로그인 가능하게 하여 테스트 진행
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수
	private int mouseX, mouseY; // 마우스 좌표용 변수
	private StringTokenizer st; // 프로토콜 구현을 위해 필요함. 소켓으로 입력받은 메시지를 분리하는데 쓰임.
	private boolean connetionCheck = false; // Waiting room으로 넘어가기 위해 커넥션 체크.(ConnectException 이 발생하면 로그인 실패 알림을 발생시키기 위함)
	static Vector user_list = new Vector();
	
//Image	
	// #MainView 배경
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/test.png")).getImage();
			//Main 클래스의 위치를 기준으로 이미지 파일의 위치를 찾은 다음에 이미지 인스턴스를 해당 변수에 초기화 해줌(상대경로 같은 절대경로)
	
	//Button Icon (basic : 버튼의 기본 상태, Entered : 버튼에 마우스를 가져간 상태) 
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon conncetBasicImage = new ImageIcon(Main.class.getResource("/images/connect.png"));
	private ImageIcon connectEnteredImage = new ImageIcon(Main.class.getResource("/images/connect.png")); 
	private ImageIcon loginBasicImage = new ImageIcon(Main.class.getResource("/images/login.png"));
	private ImageIcon loginEnteredImage = new ImageIcon(Main.class.getResource("/images/login.png")); 
	private ImageIcon joinBasicImage = new ImageIcon(Main.class.getResource("/images/조인.png"));
	private ImageIcon joinEnteredImage = new ImageIcon(Main.class.getResource("/images/조인.png")); 
	
	
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // 나가기 버튼
	private JButton connetButton = new JButton(conncetBasicImage); // 연결 버튼
	private JButton loginButton = new JButton(loginBasicImage); // 로그인 버튼
	private JButton joinButton = new JButton(joinBasicImage); // 회원가입 버튼
	
	
	MainView() {
	// JFrame mainView
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함)
		setTitle("Catch Mind"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main에서 고정시킨 화면 해상도를 사용
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setVisible(true); // 윈도우를 볼 수 있음.
		setLayout(null); // 배치 관리자 삭제
	
	// Label
		// #메뉴바
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
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
	
		// IP (임시-나중에 지울 예정)
		serverIp_lb.setBounds(400, 470, 100, 30);
		add(serverIp_lb);
		
		// 포트번호(임시-나중에 지울 예정)
		port_lb.setBounds(400, 515, 100, 30);
		add(port_lb);
		
		// ID (임시-나중에 지울 예정)
		id_lb.setBounds(400, 560, 100, 30);
		add(id_lb);
				
		// PW (임시-나중에 지울 예정)
		pw_lb.setBounds(400, 605, 100, 30);
		add(pw_lb);
		
	// TextField
		//IP 입력
		serverIp_tf = new JTextField(); 
		serverIp_tf.setBounds(520, 470, 150, 30);
		add(serverIp_tf); 
		serverIp_tf.setDocument(new JTextFieldLimit(15)); //15자 제한(ip 최대 입력가능 크기 000.000.000.000)
		serverIp_tf.setText("127.0.0.1"); //IP 기본값 127.0.0.1(자기 자신)
		
		//포트번호 입력
		port_tf = new JTextField(); 
		port_tf.setBounds(520, 515, 150, 30);
		add(port_tf);
		port_tf.setDocument(new JTextFieldLimit(5)); // 5자 제한 (포트번호 범위 0~65535)	 
		port_tf.setText("12345");//포트번호 기본값 12345
		
		//ID 입력
		id_tf = new JTextField();
		id_tf.setBounds(520, 560, 150, 30);
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //아이디 최대 12자 제한
				
		//PW 입력
		pw_tf = new JPasswordField();
		pw_tf.setBounds(520, 605, 150, 30);
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // 비밀번호 최대 12자 제한
		
	// Button
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
				System.exit(0); // 프로세스 종료.
			}
		});
		// #연결 버튼
		connetButton.setBounds(511, 652, 170, 64);
		add(connetButton);
		connetButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				connetButton.setIcon(connectEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				connetButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
				connetButton.setIcon(conncetBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				connetButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			
			// 마우스로 버튼을 눌렀을 때 이벤트
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println("연결 버튼 클릭");
				// 연결 버튼을 누르면 Server IP, port를 저장한다.
				ip = serverIp_tf.getText().trim(); // 빈 공간 제거
				port = Integer.parseInt(port_tf.getText().trim());
				
				// 네트워크 연결 설정
				Network();
				
				// connetionCheck 상태에 따른 프레임 및 알림창 상태전환
				if(connetionCheck) {
					connetionChecked(connetionCheck);
					connectToLogin();
					JOptionPane.showMessageDialog(null,"연결이 정상적으로 이루어졌습니다."
							+ "\n이제 로그인을 하시기 바랍니다.","알림",JOptionPane.INFORMATION_MESSAGE);
				} else { // 
					JOptionPane.showMessageDialog(null, 
							"로그인 실패!\nServer Port Number가 일치하지 않거나"
							+"\n서버가 실행중이지 않습니다.\n다시 확인해주세요.","알림",JOptionPane.ERROR_MESSAGE);
				}
			}
					
		});
		
		// #로그인 버튼
		loginButton.setBounds(511, 652, 170, 64);
		add(loginButton);
		loginButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setIcon(loginEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
				loginButton.setIcon(loginBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				loginButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			
			// 마우스로 버튼을 눌렀을 때 이벤트
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println("로그인 버튼 클릭");
				// 로그인 버튼을 누르면 Server IP, port, id, pw를 저장한다.
				
				id = id_tf.getText().trim();
				//JPasswordField는 getText()메소드를 권하지 않는다 하여 아래와 같은 방법으로 저장
				char[] tempPw = pw_tf.getPassword();
				for(char a : tempPw) {
					pw += a;
				}
					
				if(connetionCheck) {
					dispose(); // MainView를 종료하고 
					new WatingRoom(); // WatingRoom을 실행한다. 
				} else { // 
					JOptionPane.showMessageDialog(null, 
							"로그인 실패!\n아이디와 패스워드를 다시 확인해주세요.","알림",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
		
		// #회원가입 버튼
		joinButton.setBounds(341, 652, 170, 64);
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setIcon(joinEnteredImage); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
				joinButton.setIcon(joinBasicImage); // 마우스를 떼었을때 이미지 변경(Basic Image)
				joinButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			
			// 마우스로 버튼을 눌렀을 때 이벤트
			@Override 
			public void mousePressed(MouseEvent e) {
				if(connetionCheck) {
					new JoinView();
				} else {
					JOptionPane.showMessageDialog(null, 
							"서버와 연결 설정 후에 시도하시기 바랍니다.","알림",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
	// JPanel loginView
		loginView.setBounds(341, 460, 341, 256);
		add(loginView);
		
		connetionChecked(connetionCheck);
	}
	
	// 연결설정이 완료되면 로그인 버튼으로 바뀐다.
	private void connectToLogin() {
		connetButton.setEnabled(false);
		connetButton.setVisible(false);
		loginButton.setEnabled(true);
	}
	
	// connetionCheck 값에 따라 
	private void connetionChecked(boolean connetionCheck) {
		if(connetionCheck) { // 연결 설정되어 있는 상태일 시
			serverIp_tf.setEnabled(false);
			port_tf.setEnabled(false);
			id_tf.setEnabled(true);
			pw_tf.setEnabled(true);
		} else { // 연결 설정이 되어있기 이전 상태일 시
			serverIp_tf.setEnabled(true);
			port_tf.setEnabled(true);
			id_tf.setEnabled(false);
			pw_tf.setEnabled(false);
		}
	}
	
	private void Network() 
	{	
		connetionCheck = true; // 연결 시작전에 ture로 초기화
		try {
			socket = new Socket(ip,port); 
			
			if(socket != null) // 정상적으로 소켓이 연결되었을 경우
			{
				Connection();
			}
			
		} catch (UnknownHostException e) { // 호스트를 찾을 수 없을 때
			e.printStackTrace();
		} catch (ConnectException e) {
			connetionCheck = false; // ConnectException이 발생하면 false로 변경. 로그인 실패 알림창을 띄움.
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void Connection() // 실제적인 메소드 연결부분
	{ 
		try 
		{
			is = socket.getInputStream(); // 소켓으로 들어온 스트림을 is에 저장
			dis = new DataInputStream(is); // is를 DataInputStream으로 dis에 저장
			
			os = socket.getOutputStream(); // 소켓으로 내보낼 스트림을 os에 저장
			dos = new DataOutputStream(os); // os를 DataOutputStream으로 dos에 저장
		} 
		catch (IOException e) // 에러처리 부분
		{ 
			
		} // Stream 설정 끝
		
		// 처음 접속시에 ID 전송
		send_message(id);	
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) 
				{
					try {
						String msg = dis.readUTF(); // 메세지 수신
						
						System.out.println("서버로부터 수신된 메세지 : " + msg);
						
						inmessage(msg);
					} catch (IOException e) {
						
					}
				}
				
			}
		});
		
		th.start();
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
	
	
	/* 메시지 송수신 형태를 프로토콜(Protocol) 형식에 맞춰서 한다.
 	 * [Protocol]/[Message] 형태로 메시지를 수신한다.
 	 * 수신된 메시지는 "/"를 기준으로 분할하여 저장할 것이고 
 	 * Protocol 부분은 NewUser,OldUser,Chatting 등으로
 	 * 나누어서 개별적으로 Message를 수행한다.
	 */
	private void inmessage(String str) // 서버로부터 들어오는 모든 메세지
	{
		st = new StringTokenizer(str, "/");  // 어떤 문자열을 사용할 것인지, 어떤 문자열로 자를 것인지 =>  [ NewUser/사용자ID ] 형태로 들어옴
		
		String protocol = st.nextToken(); // 프로토콜을 저장한다.
		String Message = st.nextToken(); // 메시지를 저장한다.
		
		System.out.println("프로토콜 : " + protocol);
		System.out.println("내용 : " + Message);
		
		if(protocol.equals("NewUser")) // 새로운 접속자
		{
			user_list.add(Message); // 유저 리스트에 새로운 User ID 추가
		}
		else if(protocol.equals("OldUser")) // 기존 접속자
		{
			user_list.add(Message); // 유저 리스트에 기존 User ID 추가
		}
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
	}
	
	
	// 포트번호를 다른 클래스에서 알기 위한 메소드
	public static int getPort() {
		return port;
	}
	
	// 소켓을 다른 클래스에서 이어받기 위한 메소드
	public static Socket getSocket() {
		return socket;
	}
	
	// ID를 다른 클래스에서 이어받기 위한 메소드
	public static String getId() {
		return id;
	}
	
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
}
