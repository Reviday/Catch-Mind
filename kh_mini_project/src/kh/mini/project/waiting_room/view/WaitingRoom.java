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
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.main.view.Main;
import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.RoomInfo;
import kh.mini.project.model.vo.UserInfo;

public class WaitingRoom extends JFrame{
// Frame, Panel
	private JScrollPane chattingView = new JScrollPane(); // ä���� ���̰��ϴ� ��ũ�� ����
	private JPanel userInfoView = new JPanel(); // ���� ���� �г�
	private JPanel userListView = new JPanel(); // ���� ����Ʈ �г�
	private JPanel gameRoomView = new JPanel(); // ���ӹ� �г�
	private JPanel[] gameRoom = new JPanel[6]; // 24���� ���� ���� => ��ư���� �غ���??
	private JPanel[] userList = new JPanel[10]; // 50���� ���� ����Ʈ�� ���� �г�
	
// Label
	private JLabel mainMenuBar = new JLabel();
//	private JLabel[] userID_lb = new JLabel[userList.length]; 		// ���� ID �� �迭
	
// Textfield	
	private JTextField chatting_tf; // ä�� ������ �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�	
	private JTextArea chattingArea = new JTextArea(); // ä�� ��ũ�� ���ο� �÷����� ä�� TextArea
	
// Network �ڿ� ����
	private String id =""; 
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����	
	private int mouseX, mouseY; // ���콺 ��ǥ�� ����
	private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
	private UserInfo userInfo; // �������� ������ �����ϴ� ��ü(�� �̸����� �����ϸ� ����� ������ ������ ������ �� �ִ�.)
	private Vector<RoomInfo> room_list = new Vector<RoomInfo>(); // �� ����Ʈ Vector
	private Vector<UserInfo> user_list = new Vector<UserInfo>();
	private Toolkit tk = Toolkit.getDefaultToolkit();
	// Ŀ�� �׽�Ʈ
	Image img = tk.getImage(Main.class.getResource("/images/cursorBasic.png"));
	Cursor myCursor = tk.createCustomCursor(img, new Point(10,10), "dynamite stick");
	// �� ����⿡ �ʿ��� ����
	private String room_name; // ������
	private String roomPW; // �� ��й�ȣ
	private int fixed_User; // �� �ο���
	private int room_No; // �� ��ȣ
	private boolean scrollpanemove = false;  // ��ũ�� ���ο� ���Ǵ� ����(��ũ�� ��� ����)
	private RoomInfo roomInfo; // ����ڰ� ������ ���̳� �����Ϸ��� ���� ��ü
	private Font wrFont = new Font("�޸�����ü", Font.PLAIN,18 ); //��Ʈ����
	
	
//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/WaitingRoom_Background.png")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)	
	
	// Button Icon (basic : ��ư�� �⺻ ����, Entered : ��ư�� ���콺�� ������ ����) 
	// => ��ư �⺻����, ���콺�� �÷����� �� ����, ������ �� ���� 3���� ����?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon createRoomBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon createRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon rightRBasicImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_R_basic.png"));
	private ImageIcon rightREnteredImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_R_entered.png")); 
	private ImageIcon leftRBasicImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_L_basic.png"));
	private ImageIcon leftREnteredImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_L_entered.png")); 
	private ImageIcon gamgeRoomBasicImage = new ImageIcon(Main.class.getResource("/images/gameroom.png")); 
	private ImageIcon gamgeRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/gameroomEntered1.png")); 
	private ImageIcon gamgeRoomPressedImage = new ImageIcon(Main.class.getResource("/images/gameroomPressed.png"));
	private ImageIcon userInfoPanelImage = new ImageIcon(Main.class.getResource("/images/userInfoPanel.png")); 
	private ImageIcon inputPwPanelImage = new ImageIcon(Main.class.getResource("/images/inputPwBackground.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư
	private JButton createRoomButton = new JButton(createRoomBasicImage); // �游��� ��ư
	private JButton rightRButton = new JButton(rightRBasicImage); // �� ������ �ѱ�� ��ư
	private JButton leftRButton = new JButton(leftRBasicImage); // �� ���� �ѱ�� ��ư
	
	public WaitingRoom() {
		//����� ���ÿ� ��Ʈ��ũ �ڿ��� id�� MainView�κ��� �̾�޾ƿ´�.
		id = MainView.getId();
		dos = MainView.getDos();
		
		Font font = new Font("Inconsolata",Font.BOLD,15); // ��Ʈ ����
		
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������)
		setTitle("Catch Mind"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main���� ������Ų ȭ�� �ػ󵵸� ���
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null);
		setCursor(myCursor);
		
	// Label
		// #�޴���
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				mainMenuBar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		mainMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
			// #�Ŵ��� �巡�� ��, ������ �� �ְ� �Ѵ�.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(mainMenuBar);
		
	// JScrollPane
		// #ä�ú�
		chattingView.setBounds(240, 490, 768, 200);
		chattingView.setBackground(new Color(40,40,40,40));
		chattingView.setViewportView(chattingArea);
		chattingArea.setBackground(new Color(0,0,0,0)); 
		chattingArea.setFont(font);
		chattingArea.setForeground(Color.BLACK);
		chattingArea.setEditable(false); // �ش� �ʵ带 ������ �� ����
		chattingArea.setLineWrap(true); // �ڵ� �ٹٲ�
		chattingView.setViewportView(chattingArea);
		/* ���� �ڵ�� ������ ȯ�濡���� �ڵ� ��ũ���� �ǰ��Ϸ��� �޼ҵ��̴�. */
		chattingView.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				scrollpanemove = true;
			}
		});
		chattingView.getVerticalScrollBar().addAdjustmentListener(new	AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) { // ���������ʿ��� ����(���� ����,��ġ)�� ����ɽ� �޼ҵ� �ۼ�
				if (scrollpanemove) { // ���� ��ũ�� ���갡 ����������
					scrollpanemove = false; // ������ ������ ���� �����ʰ�, ��������� �ٲ۴�.
				} else {
					JScrollBar src = (JScrollBar) e.getSource();
					src.setValue(src.getMaximum());
				}
			}
		});
		add(chattingView);
		
		// #���� ����(�ڽ�) ��
		userInfoView.setBounds(30, 250, 190, 68);
		userInfoView.setBackground(new Color(40,40,40,40));
		add(userInfoView); 
		
		
		// #���� ����Ʈ ��
		userListView.setBounds(30, 330, 190, 374);
		userListView.setLayout(new FlowLayout(FlowLayout.CENTER));
		userListView.setBackground(new Color(40,40,40,40));
		allocationUserInfo();
		add(userListView); 
		
		// #���ӹ� ��
		gameRoomView.setBounds(240, 110, 768, 370);
		gameRoomView.setLayout(new FlowLayout(FlowLayout.CENTER));
		gameRoomView.setBackground(new Color(40,40,40,40));
		allocationRoom(); // ���ǿ� ���ӹ��� ���̵��� �ϴ� �޼ҵ�
		add(gameRoomView); 
		
		
	// TextField
		chatting_tf = new JTextField(); 
		chatting_tf.setBounds(260, 690, 728, 30);
		chatting_tf.setBackground(new Color(40,40,40,40));
		add(chatting_tf);
		chatting_tf.setDocument(new JTextFieldLimit(45)); // ä�� 45�� ���� 	 
		chatting_tf.setFont(font);
		chatting_tf.setForeground(Color.BLACK);
		chatting_tf.addKeyListener(new keyAdapter());
			
	// Button
		// #������ ��ư
		exitButton.setBounds(440, 60, 180, 50);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
//				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				exitButton.setCursor(myCursor);
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					// ������ �ش� ������ �α׾ƿ� �Ͽ����� �˸���
					send_message("UserLogout/"+userInfo.getUserID());
					// ���μ��� ����
					System.exit(0); 
				}
			}
		});
		
		// #�游��� ��ư
		createRoomButton.setBounds(260, 60, 180, 50);
		add(createRoomButton);
		createRoomButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				createRoomButton.setIcon(createRoomEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				createRoomButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}

			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override
			public void mouseExited(MouseEvent e) {
				createRoomButton.setIcon(createRoomBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				createRoomButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			// ��ư�� �������� �̺�Ʈ
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					new CreateRoom();
				}
			}
		});
		
		// #�� ������ �ѱ�� ��ư
		rightRButton.setBounds(640, 450, 60, 40);
		add(rightRButton);
		rightRButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				rightRButton.setIcon(rightREnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				rightRButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}

			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override
			public void mouseExited(MouseEvent e) {
				rightRButton.setIcon(rightRBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				rightRButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			// ��ư�� �������� �̺�Ʈ
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});

		// #�� ���� �ѱ�� ��ư
		leftRButton.setBounds(540, 450, 60, 40);
		add(leftRButton);
		leftRButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				leftRButton.setIcon(leftREnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				leftRButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}

			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override
			public void mouseExited(MouseEvent e) {
				leftRButton.setIcon(leftRBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				leftRButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			// ��ư�� �������� �̺�Ʈ
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});
		
		//���� �����, ȯ���ϴ� ���� ���
		chattingArea.append("["+id+"]�� ȯ���մϴ�! \n");
	} // WaitingRoom() ������ ��

	private void Inmessage(String str) // �����κ��� ������ ��� �޼���
	{
		st = new StringTokenizer(str, "@");  // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ =>  [ NewUser/�����ID ] ���·� ����
		
		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String mUserId = st.nextToken(); // �޽����� �����Ѵ�.
		
		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + mUserId);
		
		// protocol ���� ó��
		switch(protocol) {
		
		// #���ο� ������ �˸�
		case "NewUser": 
			// ���ο� �������� ������ ������ �����Ѵ�.
			int level = Integer.parseInt(st.nextToken()); //����
			int exp = Integer.parseInt(st.nextToken()); //����ġ
			int corAnswer = Integer.parseInt(st.nextToken()); //���� �����
			
			// ������ ������ ��ü�� ����
			UserInfo u = new UserInfo(mUserId, level, exp, corAnswer);
			
			// �ش� ��ü�� Vector�� �߰�
			user_list.add(u);
			updateUserInfo();  	// ��������Ʈ ����
			break;
		
		// #������ ���� �޾ƿ���(����� ������ ����)
		case "UserInfo":
			// ������� ������ ������ �����Ѵ�.
			level = Integer.parseInt(st.nextToken()); //����
			exp = Integer.parseInt(st.nextToken()); //����ġ
			corAnswer = Integer.parseInt(st.nextToken()); //���� �����
			
			// ������ ������ ��ü�� ����
			userInfo = new UserInfo(mUserId, level, exp, corAnswer);
			
			// �ش� ��ü�� Vector�� �߰�
			user_list.add(userInfo);
			updateUserInfo(); 	// ��������Ʈ ����
			break;
		
		// #���� �������� ������ �޾ƿ´�.(�ʱ� ���� �۾�)
		case "OldUser":
			// �ش� �������ݷ� �޴� ����� ������ ù �α��� �� ���� ����ȴ�.
			// ������ �������̴� ��� ������ ������ �����´�.
			level = Integer.parseInt(st.nextToken()); //����
			exp = Integer.parseInt(st.nextToken()); //����ġ
			corAnswer = Integer.parseInt(st.nextToken()); //���� �����
			
			// ������ ������ ��ü�� ����
			u = new UserInfo(mUserId, level, exp, corAnswer);
			
			// �ش� ��ü�� Vector�� �߰�
			user_list.add(u);

			// ������ ��ū�� last�� ��� ����Ʈ�� �����Ѵ�.
			String lastCheck = st.nextToken();
			if (lastCheck.equals("last"))
				updateUserInfo();
			break;
			
		// #���ӹ� ����, Ȥ�� �α׾ƿ��������� �ش� ������ ���� ����Ʈ���� �����Ѵ�.
		case "RemoveUser":
			for(int i=0; i<user_list.size(); i++) {
				UserInfo rUser = (UserInfo)user_list.get(i);
				// �ش� ���� ���̵� ã�´�.
				if(rUser.getUserID().equals(mUserId)) {
					// �ش� ���̵� ���� �������� �����.
					user_list.remove(i); 
					// ���� ����Ʈ�� ������Ʈ�Ͽ� �гο� �����Ų��.
					updateUserInfo();
					break;
				}
			}
			
			break;
			
		// #���� ������
		case "Note":
			String note = st.nextToken(); // ���� ����
			System.out.println(mUserId+" ����ڷκ��� �� ���� "+note);
			JOptionPane.showMessageDialog(null, note, mUserId+"������ ���� ����", JOptionPane.CLOSED_OPTION);
			break;
			
		// #�� ����
		case "CreateRoom":
			int room_No = Integer.parseInt(st.nextToken()); // �� ��ȣ
			
			send_message("EntryRoom/"+userInfo.getUserID());
			// WaitingRoom â�� �����ϰ� ����â�� ����. �̶�, �� ����� �� ��ȣ�� ���� ������.
			dispose();
//			paint = new PaintEx(room_No);
			
			break;
		
		// #������ ������ ���� ������ �Ѱܹ���(�ʱ� ���� �۾�)
		case "OldRoom":
			// �ش� �������ݷ� �޴� �� ������ ù �α��� �� ���� ����ȴ�.
			// ������ ������ ��� ���� ������ �����´�.
			room_No = Integer.parseInt(st.nextToken()); // �� ��ȣ
			String room_name = st.nextToken(); // �� �̸�
			String room_PW = st.nextToken(); // �� ��й�ȣ
			int fixed_User = Integer.parseInt(st.nextToken()); // ���� ����
			// ���ӹ濡 ���ִ� ���� ��ü�� �������� �ʿ�����Ƿ� �ο� ���� �޴´�.
			int room_UCount = Integer.parseInt(st.nextToken()); // ���� ������
			
			//�Ѱ� ���� ������ ��ü�� ����
			RoomInfo oldRoom = new RoomInfo(room_No, room_name, room_PW, room_UCount, fixed_User);
			//�ش� ��ü�� room_list�� �߰�
			room_list.add(oldRoom);
			
			// ������ ��ū�� last�� ��� �� �г��� �����Ѵ�.
			String lastroomCheck = st.nextToken();
			if (lastroomCheck.equals("last")) 
				relocationRoom(); //�� ������Ʈ
			break;
			
		// #���ο� �� ���� �˸�
		case "NewRoom":
			room_No = Integer.parseInt(st.nextToken()); // �� ��ȣ
			room_name = st.nextToken(); // �� �̸�
			room_PW = st.nextToken(); // �� ��й�ȣ
			fixed_User = Integer.parseInt(st.nextToken()); // ���� ����
			// ���ӹ濡 ���ִ� ���� ��ü�� �������� �ʿ�����Ƿ� �ο� ���� �޴´�.
			room_UCount = Integer.parseInt(st.nextToken()); // ���� ������
			
			//�Ѱ� ���� ������ ��ü�� ����
			RoomInfo NewRoom = new RoomInfo(room_No, room_name, room_PW, room_UCount, fixed_User);
			//�ش� ��ü�� room_list�� �߰�
			room_list.add(NewRoom);
			
			//�� ������Ʈ
			relocationRoom();
			break;
		
		// #�� ���� �㰡�� ����
		case "EntryRoom":
			// �ش� â�� �����Ѵ�. �濡 ���� ������ �̾ MainView�� �޴´�.
			dispose();
			break;
			
		// #��й�ȣ �Է� ��û
		case "InputPW":
			/*
			 *  �н����� �Է� �� �����ϴ� ����!
			 */
			// ���ȣ�� �н����带 �޴´�.
			room_No = Integer.parseInt(st.nextToken());
			room_PW = st.nextToken();
//			send_message("EntryRoom/"+Message+"/"+room_No);
			new inputPw(room_No,room_PW);
//			JFrame inputPW_fr = new JFrame();
//			inputPW_fr.setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
//			inputPW_fr.setSize(1024,768); // null�� �ִ�
//			inputPW_fr.setBackground(new Color(40,40,40,40));
//			inputPW_fr.setAlwaysOnTop(true); // �׻� ��� ������ ���� ��ġ�ϵ��� ��
//			inputPW_fr.setPreferredSize(new Dimension(1024,768));
//			inputPW_fr.setResizable(false); // ������ ũ�� ����
//			inputPW_fr.setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
//			inputPW_fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
//			inputPW_fr.setVisible(true); // �����츦 �� �� ����.
//			inputPW_fr.setLayout(null);
//			
//			
//			JLabel inputPW = new JLabel(inputPwPanelImage);
//			inputPW.setBounds(100, 100, 500, 500);
//			inputPW_fr.add(inputPW);
			
//			JPanel inputPW = new GameRoomPanel(inputPwPanelImage.getImage());
//			inputPW.setLocation(0, 0);
//			inputPW_fr.add(inputPW);
			
			
			
			break;
			
		// #ä��
		case "ChattingWR":
			String chattingMsg = st.nextToken(); 
			System.out.println("WaingRoom ä�� ���� : " + chattingMsg);
			chattingArea.append("["+mUserId+"] : "+chattingMsg+"\n");
			break;
		}
	}
	
	private void send_message(String str) // �������� �޼����� ������ �κ�
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ���� ó�� �κ�
		{
			e.printStackTrace();
		}
	}
	
	// MainView Ŭ�������� WaitingRoom Ŭ������ �޽����� �����ϱ� ���� ����ϴ� �޼ҵ�
	public void wr_Inmessage(String str) {
		Inmessage(str);
	}
	
	// ���� 24���� ���� �����ϱ� ���� �޼ҵ� (�� ������ ������ ���涧���� ����)
	private void allocationRoom() {
		// ���� ������ �濡�Ը� �̺�Ʈ�� �Ҵ��ϵ��� ����
		for(int i=0; i<gameRoom.length; i++) {
			// ���� Ŭ���� GameRoomPanel Ŭ������ �̿��ؼ� gameRoom Panel�� ����
			GameRoomPanel grp = new GameRoomPanel(gamgeRoomBasicImage.getImage());
			gameRoom[i] = grp;
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	
	// �гο� ���� �����ϴ� �޼ҵ�(�� ��ȣ�� �ش� �г��� �ε��� ���� �޾Ƽ� �����Ѵ�.)
	// �޼ҵ� ����, �ش� �޼ҵ�� ��ü ���� �����ϴ� ������ �غ���. 
	private void createRoom() {
		Font roomFont = new Font("Inconsolata",Font.BOLD,17); // ��Ʈ ����
		
		// ������ ����� �г��� �����.
		gameRoomView.removeAll();
		
		// ��� gameRoom�� ������ �ڵ�. ����Ʈ�� ��ϵ� ��ü �� ��ŭ �гο� �̺�Ʈ ����
		for(int i=0; i<gameRoom.length; i++) {
			// �濡 ������ �г��� ����� �����ʸ� �����Ų��(������ ����� �гε��� �����ϰ� �ٽ� �׸��� �������� �׸��⶧���� �ٽ� �����Ѵ�.)
			GameRoomPanel grp = new GameRoomPanel(gamgeRoomBasicImage.getImage());
			gameRoom[i] = grp; // �г� ���� ����
			
			// room_list�� ũ�⺸�� ���� ��, room_list�� ������ �濡 �߰��Ѵ�.
			if(i<room_list.size()) {
				// room_list���� ��ü �ϳ���
				RoomInfo r = (RoomInfo)room_list.get(i);
				// �ش� grp�� RoomInfo ��ü�� �����Ų��.(grp�� room_list�� ��ü�� �����Ű�� ����)
				grp.setRoomInfo(r);
				grp.addMouseListener(new MouseAdapter() {
					// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
					@Override
					public void mouseEntered(MouseEvent e) {
						grp.setGRImage(gamgeRoomEnteredImage.getImage()); // ���콺�� �÷������� �̹��� ����(Entered Image)
						grp.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
					}
					
					// ���콺�� ��ư���� �������� �̺�Ʈ
					@Override
					public void mouseExited(MouseEvent e) {
						grp.setGRImage(gamgeRoomBasicImage.getImage()); // ���콺�� �������� �̹��� ����(Basic Image)
						grp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
					}
					
					// ���콺�� ��ư�� ������ �� �̺�Ʈ
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == 1) {
							grp.setGRImage(gamgeRoomPressedImage.getImage()); // ���콺�� ������ �� �̹��� ����(Pressed Image)
							
						}
					}
					
					// ���콺�� ��ư�� ���� �� ������ �� �̺�Ʈ
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == 1) {
							grp.setGRImage(gamgeRoomEnteredImage.getImage()); // ���� ��ư�� �������� �� �̹��� ����(Entered Image) - ���콺�� �̹� �гο� �÷������� �����̱� ������
							// �ش� �гο� ����� ��ü ������ �����ͼ� roomInfo�� �����Ѵ�.
							RoomInfo roomInfo = grp.getRoomInfo();
							// ���������� EnterRoom���� ���� id�� �� ��ȣ�� ������ ������.
							send_message("EnterRoom/"+userInfo.getUserID()+"/"+roomInfo.getRoom_No());
						}
					}
				});
				gameRoom[i] = grp; // ���ŵ� �гη� �ٽ� ����
				
				// ���ӹ� ��ȣ�� ������ JLabel�� �Ҵ�
				JLabel gameRoomNumber_lb = new JLabel();
				// ���ȣ ����. room_No���� ���� �ش� ���ȣ�� ����. 000 �� ���·� ����. 
				String number = "";
				int temp = r.getRoom_No(), tempI;
				for (int j = 1; j <= 3; j++) {
					if ((tempI=temp % 10)==0) {
						number = "" + 0 + number;
					} else {
						number = "" + tempI + number;
					}
					temp /= 10;
				}
				// gameRoomNumber_lb�� �����Ѵ�.
				gameRoomNumber_lb.setText(number);
				gameRoomNumber_lb.setFont(roomFont);
				gameRoomNumber_lb.setBounds(32, 23, 40, 20);
				gameRoomNumber_lb.setForeground(Color.DARK_GRAY);
				gameRoomNumber_lb.setLayout(null);
				gameRoom[i].add(gameRoomNumber_lb);
				
				// ���ӹ� ������ ������ JLabel�� �Ҵ�
				JLabel gameRoomTitle_lb = new JLabel();
				gameRoomTitle_lb.setText(r.getRoom_name());
				gameRoomTitle_lb.setFont(roomFont);
				gameRoomTitle_lb.setBounds(90, 23, 200, 20);
				gameRoomTitle_lb.setForeground(Color.DARK_GRAY);
				gameRoom[i].add(gameRoomTitle_lb);
				
				// ���ӹ� �ο����� ������ JLabel�� �Ҵ�
				JLabel gameRoomPlayerCount_lb = new JLabel();
				String userCount = "" + r.getRoom_UCount() + " / " + r.getFixed_User();
				gameRoomPlayerCount_lb.setText(userCount);
				gameRoomPlayerCount_lb.setFont(roomFont);
				gameRoomPlayerCount_lb.setBounds(275, 68, 50, 20);
				gameRoomPlayerCount_lb.setForeground(Color.DARK_GRAY);
				gameRoom[i].add(gameRoomPlayerCount_lb);
			}
			// �̷��� ������ �г��� �信 �߰��Ѵ�.
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	// #���� ����/���ŵ� ������ ���ȣ�� ���� ������ ���ġ�ϴ� �޼ҵ�(������Ʈ �޼ҵ�)
	public void relocationRoom() {
				
		// room_list�� AscendingRoomInfo ���� ���ǿ� ���� �������Ѵ�.
		Collections.sort(room_list, new AscendingRoomInfo());
		
		// �⺻���� ��� �гθ� ������ �濡 list�� ��ϵ� �游 ŭ ������ �����Ų��.
		createRoom();
		
		System.out.println("�� ���� : " +room_list.size());
		
		// �г��� ��������� �����ϱ����� �޼ҵ�
		gameRoomView.revalidate(); // ���̾ƿ� ��ȭ�� ��Ȯ�� ��Ų��.
		gameRoomView.repaint(); // removeAll()�� ���� ���� �� ������ �ڽ��� �̹����� ����� �� �ʿ��ϴ�.
	}
	
	// #50���� ���� ����Ʈ�� �����ϱ� ���� �޼ҵ�
	private void allocationUserInfo() {
		for(int i=0; i<userList.length; i++) {
			// ���� Ŭ���� GameRoomPanel Ŭ������ �̿��ؼ� User List Panel�� ����
			GameRoomPanel grp = new GameRoomPanel(userInfoPanelImage.getImage());
			// ���콺 ������ �߰� ���� => ������Ʈ �޼ҵ忡�� �ؾ��ҵ�?
//			grp.addMouseListener( new MouseAdapter() {
//			});
			userList[i] = grp;
			
			userListView.add(userList[i]);
		}
	}
	
	// #���� ����Ʈ ������Ʈ �޼ҵ� - ���� ����Ʈ�� ������ ����ԵǸ� �����Ѵ�.
	private void updateUserInfo() {
		Font infoFont = new Font("Inconsolata",Font.BOLD,17); // ��Ʈ ����
		// ������ ����� �г��� �����.
		userListView.removeAll();
		
		for(int i=0; i<userList.length; i++) {
			// ���� Ŭ���� GameRoomPanel Ŭ������ �̿��ؼ� User List Panel�� ����
			GameRoomPanel grp = new GameRoomPanel(userInfoPanelImage.getImage());
			// ���콺 ������ �߰� ���� => ������Ʈ �޼ҵ忡�� �ؾ��ҵ�?
//			grp.addMouseListener( new MouseAdapter() {
//			});
			
			userList[i] = grp;
			
			if(i<user_list.size()) {
				// JPanel,JLabel�� ���� �� �Ҵ�
				// JLabel�� ���� �� �Ҵ�
				JLabel userID_lb = new JLabel();
				// ���� ���ӵ� ������ ����Ʈ ��ŭ �ؽ�Ʈ�� �������ش�. (���尪�� ����id)
				// userID_lb ����
				userID_lb.setFont(infoFont);
				userID_lb.setBounds(40, 1, 160, 30);
				userID_lb.setForeground(Color.black);
				userID_lb.setLayout(null);
				userList[i].add(userID_lb);
				// ���� ���ӵ� ������ ����Ʈ ��ŭ �ؽ�Ʈ�� �������ش�. (���尪�� ����id)
				UserInfo u = (UserInfo) user_list.get(i);
				userID_lb.setText(u.getUserID());
				// userID_lb ����
				userList[i].add(userID_lb);
			}
			userListView.add(userList[i]);
		}
		
		// �г��� ��������� �����ϱ����� �޼ҵ�
		userListView.revalidate(); // ���̾ƿ� ��ȭ�� ��Ȯ�� ��Ų��.
		userListView.repaint(); // removeAll()�� ���� ���� �� ������ �ڽ��� �̹����� ����� �� �ʿ��ϴ�.
	}
	
	// �ؽ�Ʈ �ʵ� ���� �� ������ ���� Ŭ���� �� �޼ҵ�
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
	} // JTextFieldLimit class ��
	
	// Ű �̺�Ʈ�� �ֱ����� Ŭ����
	public class keyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// ���͸� ������ ������ �ǰ� �ϱ����� �޼ҵ�
				String message = chatting_tf.getText();
				if(message.equals("")) { //�ƹ��͵� �Է����� �ʾ��� �� �˸�â�� ���
					JOptionPane.showMessageDialog(null, 
							"������ �Է��Ͻñ� �ٶ��ϴ�.","�˸�",JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingWR/"+id+"/"+message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class ��
	
	
	/* �Ʒ� paint() �޼ҵ�� GUI Application�� ����ǰų� 
	 * Ȱ��/��Ȱ������ ���� ���� ������ ����������, ����Ǵ� �޼ҵ��̴�. */
	
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
		
	}
	
	
	// �游��� ������
	class CreateRoom extends JFrame{
	// TextField
		private JTextField roomTitel_tf = new JTextField(); // �� ����
		private JTextField roomPw_tf = new JTextField();  // �� ��й�ȣ
	
	// ComboBox
		private String[] state = {"����","�����"};
		private Integer[] player = {2,4,6}; // �ִ��ο� 6������ ����(������ ������ ����� ���� 2,4,6�� ���� ����)
		private JComboBox<String> roomState_tf = new JComboBox<String>(state); // ����/����� ������ ���� �޺��ڽ�
		private JComboBox<Integer> rPlayer_tf = new JComboBox<Integer>(player); // �ο��� ������ ���� �޺��ڽ�
		
	// ���� ���� ����
		private Image viewImage; // �̹��� ����� ����
		private Graphics viewGraphics; // �׷��� ����� ����	
		private int mouseX; // ���콺 ��ǥ ����
		private int mouseY; // ���콺 ��ǥ ����
		
	// Image
		// # CreateRoom ���
		private Image crbackgroundImage = 
				new ImageIcon(Main.class.getResource("/images/CreateRoom.png")).getImage();
		// #�游��� ������ ���� ��ư�� �̹���
		private ImageIcon crCancelBasicImage = new ImageIcon(Main.class.getResource("/images/cancelButtonBasic.png"));
		private ImageIcon crCancelEnteredImage = new ImageIcon(Main.class.getResource("/images/cancelButtonEntered.png")); 
		private ImageIcon createBasicImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonBasic.png"));
		private ImageIcon createEnteredImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonEntered.png"));
		// Button
		private JButton cancelButton = new JButton(crCancelBasicImage); // ��� ��ư
		private JButton createButton = new JButton(createBasicImage); // �游��� ��ư
		
		public CreateRoom() {
			Font font = new Font("Inconsolata",Font.PLAIN,11); 
			setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
			setSize(360,213); // null�� �ִ�
			setBackground(new Color(0,0,0,0));
			setAlwaysOnTop(true); // �׻� ��� ������ ���� ��ġ�ϵ��� ��
			setResizable(false); // ������ ũ�� ����
			setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
			setVisible(true); // �����츦 �� �� ����.
			setLayout(null);	
			
			// ���콺�� â�� ������ �� �ִ�.
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				// #�ش� â�� �巡�� ��, ������ �� �ְ� �Ѵ�.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
		
		// TextField / ComboBox
			// # ���� �Է�
			roomTitel_tf.setBounds(88,47,244,20);
			roomTitel_tf.setFont(font);
			roomTitel_tf.setDocument(new JTextFieldLimit(20)); // ���� 20�� ���� 	 
			add(roomTitel_tf);
			
			// # ����/����� ���� �޺��ڽ�
			roomState_tf.setBounds(88,78,78,20);
			roomState_tf.setFont(font);
			add(roomState_tf);
			roomState_tf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String state = roomState_tf.getSelectedItem().toString();
					if(state.equals("����")) {
						roomPw_tf.setEnabled(false);
					} else if (state.equals("�����")) {
						roomPw_tf.setEnabled(true);
					}
				}
			});
			
			// # �� ��й�ȣ �Է�(�޺��ڽ� �̺�Ʈ�� ���� Ȱ��/��Ȱ��)
			roomPw_tf.setBounds(88,103,78,20);
			roomPw_tf.setFont(font);
			roomPw_tf.setDocument(new JTextFieldLimit(10)); // ��й�ȣ 10�� ���� 	
			roomPw_tf.setEnabled(false); // �ʱ⿡ "����"�����̱⶧���� ��Ȱ�� ���·� �д�.
			add(roomPw_tf);
			
			// # �ο��� ���� �޺��ڽ�
			rPlayer_tf.setBounds(88,128,78,20);
			roomPw_tf.setFont(font);
			add(rPlayer_tf);
			
			
		// Button
			// #�M�� ��ư
			cancelButton.setBounds(187, 180, 72, 24);
			add(cancelButton);
			cancelButton.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					cancelButton.setIcon(crCancelEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
					cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}
				
				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override  
				public void mouseExited(MouseEvent e) {
					cancelButton.setIcon(crCancelBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
					cancelButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						dispose(); 
					}
				}
			});
			
			// #����� ��ư
			createButton.setBounds(101, 180, 72, 24);
			add(createButton);
			createButton.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					createButton.setIcon(createEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
					createButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}
				
				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override  
				public void mouseExited(MouseEvent e) {
					createButton.setIcon(createBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
					createButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}
				// ��ư�� �������� �̺�Ʈ
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						// ����� ��ư�� ������ ���������� �״�� ������ �����Ѵ�.
						room_name = roomTitel_tf.getText().trim(); // �� ����
						String state = roomState_tf.getSelectedItem().toString(); // ����/�����
						roomPW = null; // �� ��й�ȣ (������ null��, ������� �Է¹��� �н����带 ����)
						if(state.equals("�����")) {
							roomPW = roomPw_tf.getText().trim();
						} 
						fixed_User = Integer.parseInt(rPlayer_tf.getSelectedItem().toString()); // rPlayer_tf�� ���׸��� Integer�� �س���
						
						// �Է¹��� ���� ������ �����Ѵ�.
						send_message("CreateRoom/"+id+"/"+room_name+"/"+state+"/"+roomPW+"/"+fixed_User);
						
						/* �ش� ���� ������ RoomInfo ��ü�� �����Ѵ�. �̶�, ���ȣ�� 0������ �ʱ�ȭ�Ͽ� ����
						 * �̶�, room_list���� ������� �ʴ´�.(�� ��ȣ�� �Ҵ� �ް� ���)	 */
						roomInfo = new RoomInfo(0,room_name, roomPW, fixed_User, userInfo); 
						dispose();
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
	
	
	class inputPw extends JFrame {
//		JPanel inputPW = new GameRoomPanel(inputPwPanelImage.getImage());
		private JLabel inputPW_lb;
		private JButton inputPW_bt;
		private JButton cancel_bt;
		private JPasswordField inputPW_tf;
		
		// ���� ���� ����
//		private Image viewImage; // �̹��� ����� ����
//		private Graphics viewGraphics; // �׷��� ����� ����	
		private String input_Pw; // �� ��й�ȣ 
		
		public inputPw(int room_No, String room_Pw) {
			setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
			setSize(1024,768); // null�� �ִ�
			setBackground(new Color(40,40,40,40));
//			setAlwaysOnTop(true); // �׻� ��� ������ ���� ��ġ�ϵ��� ��
			setPreferredSize(new Dimension(1024,768));
			setResizable(false); // ������ ũ�� ����
			setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
			setVisible(true); // �����츦 �� �� ����.
			setLayout(null);

			inputPW_lb = new JLabel();
			inputPW_lb.setBounds(430, 310,200,30);
			inputPW_lb.setText("��й�ȣ�� �Է��ϼ���.");
			inputPW_lb.setFont(wrFont);
			add(inputPW_lb);
			
			inputPW_tf  = new JPasswordField();
			inputPW_tf.setBounds(410, 350,200,30);
			inputPW_tf.setBorder(null); // �׵θ� ����
			inputPW_tf.setBackground(new Color(40,40,40,40)); // ��� ������
			inputPW_tf.setEchoChar('*'); // ȭ�鿡 ǥ���� ���ڸ� '*'�� ����
			inputPW_tf.setFont(wrFont);
			add(inputPW_tf);
			
			
			inputPW_bt = new JButton();
			inputPW_bt.setText("�Է�");
			inputPW_bt.setBounds(412, 400,80,30);
			inputPW_bt.setBorder(null); // �׵θ� ����
			inputPW_bt.setBackground(new Color(0,0,0,0)); // ��� ����
			inputPW_bt.setFont(wrFont);
			add(inputPW_bt);
			inputPW_bt.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					inputPW_bt.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}
				
				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override  
				public void mouseExited(MouseEvent e) {
					inputPW_bt.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}
//				
				// �ش� ��ư�� Ŭ������ ��
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						
						//JPasswordField�� getText()�޼ҵ带 ������ �ʴ´� �Ͽ� �Ʒ��� ���� ������� ����
						input_Pw = "";
						char[] tempPw = inputPW_tf.getPassword();
						for(char a : tempPw) {
							input_Pw += a;
						}
						
						// pw�� �Է��� ���� ������
						if(input_Pw.equals(inputPW_tf.getText())) {
							// �濡 �������� �������� �˸���.
							send_message("EntryRoom/"+userInfo.getUserID());
							// WaitingRoom â�� �����ϰ� ����â�� ����. �̶�, �� ����� �� ��ȣ�� ���� ������.
							dispose();
//							new PaintEx(room_No);
						}
					}
				}
			});
			
			
			cancel_bt = new JButton();
			cancel_bt.setText("���");
			cancel_bt.setBounds(532, 400,80,30);
			cancel_bt.setBorder(null); // �׵θ� ����
			cancel_bt.setBackground(new Color(0,0,0,0)); // ��� ����
			cancel_bt.setFont(wrFont);
			add(cancel_bt);
			cancel_bt.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					cancel_bt.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}
				
				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override  
				public void mouseExited(MouseEvent e) {
					cancel_bt.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}
				
//				// ��ư�� Ŭ���� ��	
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						dispose(); // â ����
					}
				}
			});
			
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(inputPwPanelImage.getImage(), 262, 268, null);
			paintComponents(g);
			this.repaint();
		}
	}
	
	
	// ���ӹ�&����info �ϳ��ϳ��� JPanel�� ��ӹ��� GameRoomPanel Ŭ������ �����Ѵ�.
	class GameRoomPanel extends JPanel{
		private Image img;
		private RoomInfo roomInfo;
		
		public GameRoomPanel(Image img) {
			this.img = img;
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // null�� �ִ�
			setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
			setOpaque(false);
			setLayout(null); // �гο� �߰��ϴ� ��ҵ��� ��ġ�� �����Ӱ� �����ϱ� ���� Layout�� null�� ���ش�.
		}
		
		// �г��� ������ �� �ڵ����� �̹����� �׷��ִ� �޼ҵ�
		public void paintComponent(Graphics g)  {
			g.drawImage(img, 0, 0, null);
		}
		
		// �̹����� �ٲ��ֱ� ���� �޼ҵ�
		public void setGRImage(Image img) {
			this.img = img;
		}
		
		public void setRoomInfo(RoomInfo roomInfo) {
			this.roomInfo = roomInfo;
		}
		
		public RoomInfo getRoomInfo() {
			return roomInfo;
		}
	}
	
	// RoomInfo�� Vector�� �������ϱ� ���� Ŭ����(���ȣ�� �������� ������)
	class AscendingRoomInfo implements Comparator<RoomInfo> {

		@Override
		public int compare(RoomInfo o1, RoomInfo o2) {
			//���ȣ ���� ���������� �ߺ��� ������� �ʱ� ������ �� ������ �����Ѵ�.
			if(o1.getRoom_No() > o2.getRoom_No()) return 1;
			else return -1;
		} 
	}
}
