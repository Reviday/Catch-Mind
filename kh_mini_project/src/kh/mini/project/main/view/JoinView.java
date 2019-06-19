package kh.mini.project.main.view;

import java.awt.Color;

import java.awt.Cursor;

import java.awt.Font;

import java.awt.Graphics;

import java.awt.Image;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;

import java.awt.event.MouseMotionAdapter;

import java.io.DataOutputStream;

import java.io.IOException;

import java.io.ObjectOutputStream;

import java.net.Socket;

import java.util.Vector;

import java.util.regex.Pattern;

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

import kh.mini.project.db.UserController;

import kh.mini.project.main.view.MainView.JTextFieldLimit;

import kh.mini.project.model.vo.User;

public class JoinView extends JFrame {

// Frame, Panel

	private JPanel joinPanel = new JPanel(); // 보조 프레임(패널) - 회원가입 패널

// Label

	private JLabel id_lb = new JLabel("ID : "); // 임시 id 레이블

	private JLabel pw_lb = new JLabel("PW : "); // 임시 pw 레이블

//	private JLabel pwRe_lb = new JLabel("PW re : ");

	private JLabel name_lb = new JLabel("이름 : "); // 임시 ip 레이블

	private JLabel dateOfBirth_lb = new JLabel("생년월일 : "); // 임시 포트 번호 레이블

	private JLabel eMail_lb = new JLabel("e-Mail : "); // 임시 ip 레이블

	private JLabel gender_lb = new JLabel("성별 : "); // 임시 포트 번호 레이블

// TextField

	private JTextField id_tf; // ID를 입력받기 위한 텍스트 필드

	private JPasswordField pw_tf; // PW를 입력받기 위한 텍스트 필드

	private JPasswordField pwRe_tf;

	private JTextField name_tf; // 이름를 입력받기 위한 텍스트필드

	private JTextField dateOfBirth_tf; // 생년월일을 입력받기 위한 텍스트필드

	private JTextField eMail_tf; // 이메일을 입력받기 위한 텍스트필드

// Network 자원 변수

	private Socket socket;// 사용자 소켓

	private int port; // 포트번호

// 각종 변수

	private Image viewImage; // 이미지 저장용 변수

	private Graphics viewGraphics; // 그래픽 저장용 변수

	private int mouseX; // 마우스 좌표 변수

	private int mouseY; // 마우스 좌표 변수

	private DataOutputStream dos = null;

// 회원가입 입력정보

	private String id = "";

	private String pw = "";

	private String pwRe = ""; // 패스워드 확인용

	private String name = "";

	private String dateOfBirth = ""; // 생년월일(19990101)

	private String eMail = "";

	private int age; // dateOfBirth로 계산하여 저장

	private char gender;

//Image	

	// #MainView 배경

	private Image backgroundImage =

			new ImageIcon(Main.class.getResource("/images/loginmain.png")).getImage();

	// Main 클래스의 위치를 기준으로 이미지 파일의 위치를 찾은 다음에 이미지 인스턴스를 해당 변수에 초기화 해줌(상대경로 같은 절대경로)

//Button

	ImageIcon exitBasic = new ImageIcon(Main.class.getResource("/images/loginExitButtonBasic.png"));

	ImageIcon exitEntered = new ImageIcon(Main.class.getResource("/images/loginExitButtonEntered.png"));

	private JButton exitButton = new JButton(exitBasic); // 나가기 버튼

	ImageIcon joinBasic = new ImageIcon(Main.class.getResource("/images/loginJoinButtonBasic.png"));

	ImageIcon joinEntered = new ImageIcon(Main.class.getResource("/images/loginJoinButtonEntered.png"));

	private JButton joinButton = new JButton(joinBasic); // 가입 버튼

	ImageIcon checkBasic = new ImageIcon(Main.class.getResource("/images/checkButtonBasic.png"));

	ImageIcon checkEntered = new ImageIcon(Main.class.getResource("/images/checkButtonEntered.png"));

	private JButton idCheck = new JButton(checkBasic);

	public JoinView() {

		// 실행과 동시에 socket,port,ID를 MainView로부터 이어받아온다.

		socket = MainView.getSocket();

		port = MainView.getPort();

		dos = MainView.getDos(); // MainView Output스트림을 이어 받아온다.

		Font font = new Font("Inconsolata", Font.BOLD, 15);

		// JFrame mainView

		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함) - 기능 완성 후 추가 예정

		setTitle("Catch Mind"); // 프레임 타이틀 바 이름(타이틀 바를 없앨 예정이기 때문에 없어도 되는 코드)

		setSize(342, 405);

		setResizable(false); // 프레임 크기 고정

		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함

		setBackground(new Color(0, 0, 0, 0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)

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

		// 중복확인버튼

		idCheck.setBounds(255, 62, 34, 17);

		idCheck.setBorder(null);

		idCheck.setBackground(new Color(0, 0, 0, 0));

		add(idCheck);

		idCheck.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseEntered(MouseEvent e) {

				idCheck.setIcon(checkEntered); // 마우스를 올려놨을때 이미지 변경(Entered Image)

				idCheck.setCursor(new Cursor(Cursor.HAND_CURSOR));

			}

			@Override

			public void mouseExited(MouseEvent e) {

				idCheck.setIcon(checkBasic); // 마우스를 올려놨을때 이미지 변경(Entered Image)

				idCheck.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			@Override

			public void mousePressed(MouseEvent e) {

				if (e.getButton() == 1) {

					if (id_tf.getText().length() < 6) {

						JOptionPane.showMessageDialog(null, "아이디는 6~12자만 넣을 수 있습니다.");

					} else {

						Boolean idCheckStatus = false; // 마우스 좌측 클릭 할때마다 false로 초기화

						String idCheck = id_tf.getText(); // ID 입력창(TextField)에서 값 가져와서 idCheck에 저장

						String existId = ""; // DB에 저장된 아이디 저장할 문자열 변수

						System.out.println("입력한 아이디:" + idCheck); // 입력한 ID 출력

						Vector allUser = new UserController().dataLoadAll(); // userDB.dat에서 모든 정보 가져와서 Vector allUser
																				// 변수에 저장

						for (int i = 0; i < allUser.size(); i++) { // allUser(DB에서 가져온 모든 사용자 정보) 만큼 반복문 실행

							existId = ((User) allUser.get(i)).getId(); // allUser의 0번째부터 차례대로 id 가져와서 existId에 저장

							System.out.println(i + "번째 인덱스에 존재하는 아이디:" + existId);

							if (idCheck.equals(existId)) { // 아이디가 중복되면

								idCheckStatus = true; // idCheckStatus true로 바꾸기(대입)

								break; // 중복되는 ID가 있으면 for문(반복문) 나가기(종료), 더 이상 비교할 필요가 없으므로!!

							}

						}

						if (idCheckStatus) { // 위의 for문에서 비교하여 idCheckStatus가 true이면 (아이디가 중복되면)

							JOptionPane.showMessageDialog(null, "중복된 아이디입니다. \n다른 아이디를 입력해주세요.");

						} else { // 위의 for문에서 비교하여 idCheckStatus가 false이면 (아이디가 중복되는 것이 없으면)

							JOptionPane.showMessageDialog(null, "사용 가능한 아이디 입니다.");

						}

					}

				}

			}

		});

		// TextField

		// ID 입력

		id_tf = new JTextField();

		id_tf.setBounds(165, 55, 138, 30);

		id_tf.setBorder(null);

		id_tf.setBackground(new Color(0, 0, 0, 0));

		add(id_tf);

		id_tf.setDocument(new JTextFieldLimit(12)); // 아이디 최대 12자 제한

		// PW 입력

		pw_tf = new JPasswordField();

		pw_tf.setBounds(165, 96, 138, 30);

		pw_tf.setBorder(null);

		pw_tf.setBackground(new Color(0, 0, 0, 0));

		add(pw_tf);

		pw_tf.setDocument(new JTextFieldLimit(12)); // 비밀번호 최대 12자 제한

		// PW RE

		pwRe_tf = new JPasswordField();

		pwRe_tf.setBounds(164, 137, 138, 30);

		pwRe_tf.setBorder(null);

		pwRe_tf.setBackground(new Color(0, 0, 0, 0));

		add(pwRe_tf);

		pwRe_tf.setDocument(new JTextFieldLimit(12));

		// 165, 137, 138, 30

		// 이름 입력

		name_tf = new JTextField();

		name_tf.setBounds(165, 178, 138, 30);

		name_tf.setBorder(null);

		name_tf.setBackground(new Color(0, 0, 0, 0));

		add(name_tf);

		name_tf.setDocument(new JTextFieldLimit(5)); // 이름 최대 5자 제한

		// 생년월일 입력

		dateOfBirth_tf = new JTextField();

		dateOfBirth_tf.setBounds(165, 221, 138, 30);

		dateOfBirth_tf.setBorder(null);

		dateOfBirth_tf.setBackground(new Color(0, 0, 0, 0));

		add(dateOfBirth_tf);

		dateOfBirth_tf.setDocument(new JTextFieldLimit(6)); // 생년월일 최대 6자 제한

		// e-Mail 입력

		eMail_tf = new JTextField();

		eMail_tf.setBounds(165, 262, 138, 30);

		eMail_tf.setBorder(null);

		eMail_tf.setBackground(new Color(0, 0, 0, 0));

		add(eMail_tf);

		eMail_tf.setDocument(new JTextFieldLimit(30)); // 이메일 최대 30자 제한

		// RadioButton

		// 성별 선택

		JRadioButton genderMale = new JRadioButton("남"); // JRadioButton 생성

		genderMale.setFont(font);

		JRadioButton genderFemale = new JRadioButton("여");

		genderFemale.setFont(font);

		ButtonGroup genderGroup = new ButtonGroup(); // 라디오버튼 그룹화를 위한 버튼그룹 설정. 같은 그룹끼리는 그룹중에 1개만 선택된다.

		genderGroup.add(genderMale);

		genderGroup.add(genderFemale); // 그룹에 그룹화시킬 버튼들을 추가

		genderMale.setBounds(165, 305, 50, 30);

		genderMale.setBackground(new Color(0, 0, 0, 0));

		genderMale.setFont(font);

		genderMale.setForeground(Color.BLACK);

		genderMale.setSelected(true);

		add(genderMale);

		genderFemale.setBounds(220, 305, 50, 30);

		genderFemale.setBackground(new Color(0, 0, 0, 0));

		genderFemale.setFont(font);

		genderFemale.setForeground(Color.BLACK);

		add(genderFemale);

		// Button

		// #나가기 버튼

		exitButton.setBounds(210, 350, 88, 35);

		exitButton.setBorder(null);

		exitButton.setBackground(new Color(0, 0, 0, 0));

		add(exitButton);

		exitButton.addMouseListener(new MouseAdapter() {

			// 마우스를 버튼에 올려놨을 때 이벤트

			@Override

			public void mouseEntered(MouseEvent e) {

				exitButton.setIcon(exitEntered); // 마우스를 올려놨을때 이미지 변경(Entered Image)

				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경

			}

			// 마우스를 버튼에서 떼었을때 이벤트

			@Override

			public void mouseExited(MouseEvent e) {

				exitButton.setIcon(exitBasic); // 마우스를 떼었을때 이미지 변경(Basic Image)

				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경

			}

			// 마우스로 버튼을 눌렀을 때 이벤트

			@Override

			public void mousePressed(MouseEvent e) {

				dispose(); // 하나의 프레임만 종료하기 위한 메소드

			}

		});

		// #가입 버튼

		joinButton.setBounds(60, 350, 88, 35);

		joinButton.setBorder(null);

		joinButton.setBackground(new Color(0, 0, 0, 0));

		add(joinButton);

		joinButton.addMouseListener(new MouseAdapter() {

			// 마우스를 버튼에 올려놨을 때(들어왔을 때) 이벤트

			@Override

			public void mouseEntered(MouseEvent e) {

				joinButton.setIcon(joinEntered); // 마우스를 올려놨을때 이미지 변경(Entered Image)

				joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서를 손모양 커서로 변경

			}

			// 마우스가 버튼에서 나갔을 때(벗어 났을 때) 이벤트

			@Override

			public void mouseExited(MouseEvent e) {

				joinButton.setIcon(joinBasic); // 마우스를 떼었을때 이미지 변경(Basic Image)

				joinButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 마우스 커서를 기본 커서로 변경

			}

			@Override

			public void mouseClicked(MouseEvent e) {

				String emailCheck = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+.([a-zA-Z0-9])+$";

				String nuII = "^[\\s]*$";

				String dateCheck = "^[0-9]*$";

				String pwCheck = "^[a-zA-Z0-9]*$";

				boolean ch = false;

				boolean formatcheck = true;

				String msg = null;

				// 회원 가입 입력 값을 변수들에 저장

				// 아이디 Format check

				id = id_tf.getText();

				if (formatcheck && id.matches(nuII)) {

					msg = "아이디에 공백을 넣을 수 없습니다.";

					formatcheck = false;

				} else if (formatcheck && id.length() < 6) {

					msg = "아이디는 6~12자만 넣을 수 있습니다.";

					formatcheck = false;

				}

				// 비밀번호 Format check

				char[] tempPw = pw_tf.getPassword();

				for (char a : tempPw) {

					pw += a;

				}

				char[] tempPwRe = pwRe_tf.getPassword();

				for (char a : tempPwRe) {

					pwRe += a;

				}

				if (formatcheck && pw.length() < 6) {

					msg = "패스워드는 6~12자 소/대문자, 숫자만 넣을 수 있습니다.";

					formatcheck = false;

				} else if (formatcheck && !pw.equals(pwRe)) {

					msg = "패스워드가 일치하지 않습니다.";

					formatcheck = false;

				}

				name = name_tf.getText();

				if (formatcheck && name.matches(nuII)) {

					msg = "이름에 공백을 넣을 수 없습니다.";

					formatcheck = false;

				}

				dateOfBirth = dateOfBirth_tf.getText();

				if (formatcheck && !dateOfBirth.matches(dateCheck) && dateOfBirth.matches(nuII)) {

					msg = "숫자형식으로 입력하십시오.";

					formatcheck = false;

				}

				eMail = eMail_tf.getText();

				if (formatcheck && !eMail.matches(emailCheck)) {

					msg = "이메일 형식에 맞게 입력하십시오.";

					formatcheck = false;

				}

				if (genderMale.isSelected()) {

					gender = (genderMale.getText()).charAt(0);

				} else if (genderFemale.isSelected()) {

					gender = (genderFemale.getText()).charAt(0);

				}

//				

//				// Format check

//				if(pw.equals(pwRe)) {// 패스워드와 패스워드 확인이 일치하면

//					

//				}else if(!pw.equals(pwRe)){// 패스워드와 패스워드 확인이 일치하지 않으면 다이어로그 띄어줌

//					// 초기화 

//					pw="";

//					pwRe="";

//					JOptionPane.showMessageDialog(null, "패스워드가 일치하지 않습니다.");

//				}

//				//이메일형식

//				if(eMail.matches(p)) {

//					

//				}else if(!eMail.matches(p)){

//					JOptionPane.showMessageDialog(null, "이메일형식에 맞게 입력하십시오.");

//				}

//				

//				if(!id.matches(nuII)) {

//					

//				}

//				}else {

//					

//				}

				//

				if (formatcheck) {

					// 입력받은 정보를 서버로 보낸다.

					send_message("JoinRequest/" + id + "/" + pw + "/" + name + "/" + dateOfBirth + "/" + eMail + "/"
							+ gender);

					JOptionPane.showMessageDialog(null, "정상적으로 가입되었습니다.");

					dispose();

				} else {

					JOptionPane.showMessageDialog(null, msg);

					msg = "";

					formatcheck = true;

					pw = "";

					pwRe = "";

				}

			}

		});

		// JPanel loginView

//		joinPanel.setBounds(20, 20, 300, 300);

//		joinPanel.setBackground(new Color(40,40,40,40));

//		add(joinPanel);

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

	/*
	 * 아래 paint() 메소드는 GUI Application이 실행되거나
	 * 
	 * 활성/비활성으로 인한 변동 영역을 감지했을때, 실행되는 메소드이다.
	 */

	@Override

	public void paint(Graphics g) {

		viewImage = createImage(342, 405);

		viewGraphics = viewImage.getGraphics();

		screenDraw(viewGraphics);

		g.drawImage(viewImage, 0, 0, null);

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
