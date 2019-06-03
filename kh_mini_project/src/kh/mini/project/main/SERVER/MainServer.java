package kh.mini.project.main.SERVER;
// Github 푸쉬할때 자동으로 대문자로 바뀌어서 그냥 대문자로 놓음..
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kh.mini.project.main.view.Main;

public class MainServer extends JFrame {
	private static final long serialVersionUID = 1216070372320522836L;
// Frame, Panel
	private JFrame mainView = new JFrame("Server"); // 메인 프레임
	private JLabel mainMenuBar = new JLabel();
	private JScrollPane statusView = new JScrollPane(); // 포트로 받은 상태 수신창
	private JTextArea statusArea = new JTextArea();
	
	
// Textfield	
	private JTextField port_tf; // 포트번호를 입력받기 위한 텍스트필드
	
// Network 자원
	private ServerSocket server_socket; // 서버 소켓
	private Socket socket; // 사용자로부터 받을 소켓 
	private int port; // 포트번호
	
// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수
	
//Image	
	// #MainView 배경
	private Image mainBackgroundImage = 
			new ImageIcon(Main.class.getResource("/images/testGIF.gif")).getImage();
	
//Button
	
	MainServer() {
		new intro();
	}
	
	

	
	
	
	private void main_View() { // 서버 메인 화면
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정
		setTitle("Server"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main에서 고정시킨 화면 해상도를 사용
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setVisible(true); // 윈도우를 볼 수 있음.
		setLayout(null);
	}
	
	
	
	
	
	public static void main(String[] args) {
		new MainServer();
	}

	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(mainBackgroundImage, 0, 0, null);
		this.paintComponents(g);
		this.repaint();
	}
	
	
	
	//인트로 화면용 내부 클래스
	class intro extends JFrame implements ActionListener{
	// Frame, Panel
		private JFrame introView = new JFrame("Server"); // 인트로 프레임 (포트번호를 입력하여 서버를 실행하면 메인 프레임 창을 띄워 넘어가는 구조)
		private JLabel port_lb = new JLabel("포트 번호 : ");
		private JLabel introMenuBar = new JLabel();
		
	// 각종 변수	
		private Image viewImage; // 이미지 저장용 변수
		private Graphics viewGraphics; // 그래픽 저장용 변수
		private int mouseX, mouseY; // 마우스 좌표용 변수
		private boolean state = true; // 서버 중복 실행 방지용
		
	//Image	
		// #MainView 배경	
		private Image introBackgroundImage = 
				new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
		// #버튼 이미지
		private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("/images/exit_entered.png"));
		private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("/images/exit_basic.png"));
		
	//Button
		private JButton start_btn = new JButton("서버 실행"); // 서버 실행 버튼
		private JButton stop_btn = new JButton("서버 중지"); // 서버 중지 버튼
		private JButton exitButton = new JButton(exitButtonBasicImage); // 나가기 버튼
		
		
		intro() {
			start();
			setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정
			setTitle("Server"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
			setSize(280, 450); // Main에서 고정시킨 화면 해상도를 사용
			setResizable(false); // 프레임 크기 고정
			setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
			setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
			setVisible(true); // 윈도우를 볼 수 있음.
			setLayout(null);
			
		// ScrollPane
			statusView.setBounds(10, 20, 260,330 );
			statusView.setViewportView(statusArea);
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
//					main_View();
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
					//마우스를 올렸을 때 손모양으로 나타나게
				
				}
				@Override
				public void mouseExited(MouseEvent e) {
					exitButton.setIcon(exitButtonBasicImage);
					exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					//마우스를 때면 원래 상태로
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
			//포트번호 입력
			port_tf = new JTextField();
			port_tf.setBounds(100, 360, 170, 30);
			add(port_tf);
			port_tf.setColumns(10);		
			
			// 메뉴바
			introMenuBar.setBounds(0, 0, 280, 30);
			introMenuBar.addMouseListener(new MouseAdapter() {
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
		
		private void start()
		{
			start_btn.addActionListener(this); // ActionListener를 상속받았기 때문에 this라고 써주면된다.
			stop_btn.addActionListener(this);
		}
		
		
		void Server_start() // // MainServer에서 사용할 수 있도록 default로 설정
		{ 		
			try {
				server_socket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			if(server_socket != null) //정상적으로 포트가 열렸을 경우
			{
				Connection();
			}
		}
		
		void Connection() // MainServer에서 사용할 수 있도록 default로 설정
		{	
			// 1가지의 스레드에서는 1가지의 일만 처리할 수 있다.
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() { // 스레드에서 처리할 일을 기재한다.
					
					while(true) {
						try { // 마찬가지로 try-catch
							statusArea.append("사용자 접속 대기중(설정 포트번호 : "+port+")\n");
							socket = server_socket.accept(); // 사용자 접속 무한 대기 => 때문에 다른 기능들이 동작하지 않고 죽어버린다. => 멀티 스레드로 해결!
							statusArea.append("사용자 접속!!\n");
							
							//사용자가 accept로 접속을 하게되면 UserInfo 객체를 생성하게 된다.
							//UserInfo 생성자는  매개변수로 넘어온 socket을 받아주고 UserNetwork()를 실행하여 연결설정을 하게된다.
//							UserInfo user = new UserInfo(socket);
							
//							user.start(); // 객체의 스레드를 각각 실행
							
						} catch (IOException e) {
						} 
						
					} // while문 끝
					
				}
			});
			
			th.start();
		}

		
		
		@Override
		public void paint(Graphics g) {
			viewImage = createImage(280, 450);
			viewGraphics = viewImage.getGraphics();
			screenDraw(viewGraphics);
			g.drawImage(viewImage,0,0, null);
		}
		
		public void screenDraw(Graphics g) {
			g.drawImage(introBackgroundImage, 0, 0, null);
			this.paintComponents(g);
			this.repaint();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == start_btn)
			{ 
				if(state) {
					System.out.println("스타트 버튼 클릭");
						try {
							port = Integer.parseInt(port_tf.getText().trim());
						} catch(NumberFormatException ee) {
							statusArea.append("잘못된 포트번호입니다. 다시 입력하세요.\n");
						} catch(IllegalArgumentException ee) {
							statusArea.append("잘못된 포트번호입니다. 다시 입력하세요.\n");
						}
						Server_start(); // 소켓 생성 및 사용자 접속 대기
				} else {
					statusArea.append("서버가 이미 실행중 입니다.\n");
				}
				
				state = false; // 중복 실행 방지를 위해 false로 변경
				
			}
			else if(e.getSource() == stop_btn)
			{
				System.out.println("서버 스탑 버튼 클릭");
				
			}
		}// Action 이벤트 끝	
			
		
	}
}
