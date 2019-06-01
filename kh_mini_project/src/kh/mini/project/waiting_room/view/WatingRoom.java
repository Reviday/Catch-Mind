package kh.mini.project.waiting_room.view;

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

import kh.mini.project.main.view.Main;

public class WatingRoom extends JFrame{
// Frame, Panel
	JFrame WatingRoomView = new JFrame("Wating Room"); // 메인 프레임
	
// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수		
	 
//Image	
	//MainView 배경
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/임시2.jpg")).getImage();
			//Main 클래스의 위치를 기준으로 이미지 파일의 위치를 찾은 다음에 이미지 인스턴스를 해당 변수에 초기화 해줌(상대경로 같은 절대경로)	
	
	//Button Icon (basic : 버튼의 기본 상태, Entered : 버튼에 마우스를 가져간 상태) 
	// => 버튼 기본상태, 마우스를 올려놨을 때 상태, 눌렀을 때 상태 3가지 가능?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // 나가기 버튼
	
	public WatingRoom() {
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함)
		setTitle("Catch Mind"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main에서 고정시킨 화면 해상도를 사용
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setVisible(true); // 윈도우를 볼 수 있음.
		setLayout(null);
		
	// Label
			
			
	// Button
		// 나가기 버튼
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
				dispose(); // 하나의 프레임만 종료하기 위한 메소드
			}
		});

		
		
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
