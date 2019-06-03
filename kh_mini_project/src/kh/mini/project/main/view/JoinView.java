package kh.mini.project.main.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kh.mini.project.waiting_room.view.WatingRoom;

public class JoinView extends JFrame{
// Frame, Panel
	JFrame joinView = new JFrame("Join"); // 메인 프레임
	JPanel joinPanel = new JPanel(); // 보조 프레임(패널) - 회원가입 패널
	
// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수	
	
	/* 회원가입으로 받아들일 정보를 아직 못정함. */
	
//Image	
	// #MainView 배경
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
			//Main 클래스의 위치를 기준으로 이미지 파일의 위치를 찾은 다음에 이미지 인스턴스를 해당 변수에 초기화 해줌(상대경로 같은 절대경로)

//Button
	private JButton exitButton = new JButton("나가기"); // 나가기 버튼
	private JButton joinButton = new JButton("가입하기"); // 가입 버튼	
	
	
	
	public JoinView() {
	// JFrame mainView
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정
		setTitle("Catch Mind"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
		setSize(341, 384); // Main에서 고정시킨 화면 해상도를 사용
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setVisible(true); // 윈도우를 볼 수 있음.
		setLayout(null);		
		
	// Button
		// #나가기 버튼
		exitButton.setBounds(170, 320, 150, 50);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
//				exitButton.setIcon(); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
//				exitButton.setIcon(); // 마우스를 떼었을때 이미지 변경(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			
			// 마우스로 버튼을 눌렀을 때 이벤트
			@Override 
			public void mousePressed(MouseEvent e) {
				dispose(); // 하나의 프레임만 종료하기 위한 메소드
			}
		});
		
		// #가입 버튼
		joinButton.setBounds(20, 320, 150, 50);
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// 마우스를 버튼에 올려놨을 때 이벤트
			@Override
			public void mouseEntered(MouseEvent e) {
//				joinButton.setIcon(); // 마우스를 올려놨을때 이미지 변경(Entered Image)
				joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경
			}
			
			// 마우스를 버튼에서 떼었을때 이벤트
			@Override  
			public void mouseExited(MouseEvent e) {
//				joinButton.setIcon(); // 마우스를 떼었을때 이미지 변경(Basic Image)
				joinButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경
			}
			
			// 마우스로 버튼을 눌렀을 때 이벤트
			@Override 
			public void mousePressed(MouseEvent e) {
				/* 입력한 정보를 저장하고, 
				 * 회원가입 처리 완료하게 끔 
				 */
			}
			
		});
		
		
	// JPanel loginView
		joinPanel.setBounds(20, 20, 300, 300);
		add(joinPanel);
	}
	
	
	
	/* 아래 paint() 메소드는 GUI Application이 실행되거나 
	 * 활성/비활성으로 인한 변동 영역을 감지했을때, 실행되는 메소드이다. */
	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(341, 384);
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
