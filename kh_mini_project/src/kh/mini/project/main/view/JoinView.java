package kh.mini.project.main.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.main.view.MainView.JTextFieldLimit;
import kh.mini.project.model.vo.User;

public class JoinView extends JFrame{
// Frame, Panel
	private JFrame joinView = new JFrame("Join"); // 메인 프레임
	private JPanel joinPanel = new JPanel(); // 보조 프레임(패널) - 회원가입 패널
	
// Label
	private JLabel id_lb = new JLabel("ID : "); // 임시 id 레이블
	private JLabel pw_lb = new JLabel("PW : "); // 임시  pw 레이블
	private JLabel name_lb = new JLabel("이름 : "); // 임시  ip 레이블
	private JLabel dateOfBirth_lb = new JLabel("생년월일 : "); // 임시 포트 번호 레이블
	private JLabel eMail_lb = new JLabel("e-Mail : "); // 임시  ip 레이블
	private JLabel gender_lb = new JLabel("성별 : "); // 임시 포트 번호 레이블
	
// TextField
	private JTextField id_tf; // ID를 입력받기 위한 텍스트 필드
	private JPasswordField pw_tf; // PW를 입력받기 위한 텍스트 필드
	private JTextField name_tf; // 이름를 입력받기 위한 텍스트필드
	private JTextField dateOfBirth_tf; // 생년월일을 입력받기 위한 텍스트필드
	private JTextField eMail_tf; // 이메일을 입력받기 위한 텍스트필드
	
// Network 자원 변수
	private Socket socket;// 사용자 소켓
	private int port; // 포트번호
	
// 각종 변수
	private Image viewImage; // 이미지 저장용 변수
	private Graphics viewGraphics; // 그래픽 저장용 변수	
	
// 회원가입 입력정보
	private String id="";
	private String pw="";
	private String name="";
	private String dateOfBirth=""; // 생년월일(19990101)
	private String eMail="";
	private int age; // dateOfBirth로 계산하여 저장
	private char gender;

	
//Image	
	// #MainView 배경
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
			//Main 클래스의 위치를 기준으로 이미지 파일의 위치를 찾은 다음에 이미지 인스턴스를 해당 변수에 초기화 해줌(상대경로 같은 절대경로)

//Button
	private JButton exitButton = new JButton("나가기"); // 나가기 버튼
	private JButton joinButton = new JButton("가입하기"); // 가입 버튼	
	
	
	
	public JoinView() {
		//실행과 동시에 socket,port,ID를 MainView로부터 이어받아온다.
		socket = MainView.getSocket();
		port = MainView.getPort();
		
		Font font = new Font("Inconsolata",Font.BOLD,15);
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
		
	// Label	
		// ID (임시-나중에 지울 예정)
		id_lb.setBounds(40, 30, 100, 30);
		id_lb.setFont(font);
		add(id_lb);
		
		// PW (임시-나중에 지울 예정)
		pw_lb.setBounds(40, 80, 100, 30);
		pw_lb.setFont(font);
		add(pw_lb);
		
		// 이름
		name_lb.setBounds(40, 130, 100, 30);
		name_lb.setFont(font);
		add(name_lb);
		
		// 생년월일
		dateOfBirth_lb.setBounds(40, 180, 100, 30);
		dateOfBirth_lb.setFont(font);
		add(dateOfBirth_lb);
		
		// e-Mail
		eMail_lb.setBounds(40, 230, 100, 30);
		eMail_lb.setFont(font);
		add(eMail_lb);
		
		// 성별
		gender_lb.setBounds(40, 280, 100, 30);
		gender_lb.setFont(font);
		add(gender_lb);
				
	// TextField
		//ID 입력
		id_tf = new JTextField();
		id_tf.setBounds(140, 30, 150, 30);
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //아이디 최대 12자 제한
		
		//PW 입력
		pw_tf = new JPasswordField();
		pw_tf.setBounds(140, 80, 150, 30);
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // 비밀번호 최대 12자 제한
		
		//이름 입력
		name_tf = new JTextField();
		name_tf.setBounds(140, 130, 150, 30);
		add(name_tf);
		name_tf.setDocument(new JTextFieldLimit(5)); //이름 최대 5자 제한
		
		//생년월일 입력
		dateOfBirth_tf = new JTextField();
		dateOfBirth_tf.setBounds(140, 180, 150, 30);
		add(dateOfBirth_tf);
		dateOfBirth_tf.setDocument(new JTextFieldLimit(6)); //생년월일 최대 6자 제한
		
		//e-Mail 입력
		eMail_tf = new JTextField();
		eMail_tf.setBounds(140, 230, 150, 30);
		add(eMail_tf);
		eMail_tf.setDocument(new JTextFieldLimit(30)); //이메일 최대 30자 제한
	
	// RadioButton
		//성별 선택
		JRadioButton  genderMale = new JRadioButton("남"); // JRadioButton 생성
		genderMale.setFont(font); 
		JRadioButton  genderFemale = new JRadioButton("여"); 
		genderFemale.setFont(font);
		ButtonGroup  genderGroup = new ButtonGroup(); //라디오버튼 그룹화를 위한 버튼그룹 설정. 같은 그룹끼리는 그룹중에 1개만 선택된다.
		genderGroup.add(genderMale);  
		genderGroup.add(genderFemale); //그룹에 그룹화시킬 버튼들을 추가	
		genderMale.setBounds(140, 280, 50, 30);
		genderMale.setBackground(new Color(0,0,0,0)); 
		genderMale.setFont(font);
		genderMale.setForeground(Color.WHITE);
		genderMale.setSelected(true);
		add(genderMale);
		genderFemale.setBounds(220, 280, 50, 30);
		genderFemale.setBackground(new Color(0,0,0,0)); 
		genderFemale.setFont(font);
		genderFemale.setForeground(Color.WHITE);
		add(genderFemale);


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
				// 회원 가입 입력 값을 변수들에 저장
				id=id_tf.getText();
				char[] tempPw = pw_tf.getPassword();
				for(char a : tempPw) {
					pw += a;
				}
				name=name_tf.getText();
				dateOfBirth=dateOfBirth_tf.getText();
				eMail=eMail_tf.getText();
				if(genderMale.isSelected()) {
					gender = (genderMale.getText()).charAt(0);
				} else if (genderFemale.isSelected()) {
					gender = (genderFemale.getText()).charAt(0);
				}
				//입력 정보를 바탕으로 User 객체를 생성
				User u = new User(id, pw, name, dateOfBirth, eMail, gender);
				
//				try(ObjectOutputStream oos = new ObjectOutputStream(u)) {
					
//				}
			}
		});
		
		
	// JPanel loginView
		joinPanel.setBounds(20, 20, 300, 300);
		joinPanel.setBackground(new Color(40,40,40,40));
		add(joinPanel);
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
	
	public static void main(String[] args) {
		new JoinView();
	}
}
