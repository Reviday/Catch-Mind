package kh.mini.project.waiting_room.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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

public class WaitingRoom extends JFrame{
// Frame, Panel
	private JFrame WaitingRoomView = new JFrame("Waiting Room"); // ���� ������
	private JScrollPane chattingView = new JScrollPane(); // ä���� ���̰��ϴ� ��ũ�� ����
	private JPanel userListView = new JPanel(); // ���� ����Ʈ �г�
	private JPanel gameRoomView = new JPanel(); // ���ӹ� �г�
	private JPanel[] gameRoom = new JPanel[6]; // 24���� ���� ���� => ��ư���� �غ���??
	private JPanel[] userList = new JPanel[50]; // 50���� ���� ����Ʈ�� ���� �г�
	
// Label
	private JLabel mainMenuBar = new JLabel();
	private JLabel userList_Label = new JLabel("User List");
	private JLabel gameRoom_Label = new JLabel();
	private JLabel[] gameRoomNumber_lb = new JLabel[gameRoom.length]; 		// ���ӹ� �ѹ� �� �迭, gameRoom �迭�� ũ�⸸ŭ ����
	private JLabel[] gameRoomTitle_lb = new JLabel[gameRoom.length]; 		// ���ӹ� ���� �� �迭, gameRoom �迭�� ũ�⸸ŭ ����
	private JLabel[] gameRoomPlayerCount_lb = new JLabel[gameRoom.length];  // ���ӹ� �ο��� �� �迭, gameRoom �迭�� ũ�⸸ŭ ����
	
// Textfield	
	private JTextField chatting_tf; // ä�� ������ �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�	
	private JTextArea chattingArea = new JTextArea(); // ä�� ��ũ�� ���ο� �÷����� ä�� TextArea
	
// list
//	private JList user_List = new JList();
//	private JList gameRoom_List = new JList();
	
// Network �ڿ� ����
	private Socket socket;// ����� ����
	private int port; // ��Ʈ��ȣ		
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
	private Vector user_list = new Vector(); // ���� ����Ʈ Vector
	private Vector room_list = new Vector(); // �� ����Ʈ Vector
	private Vector gUser_list = new Vector(); // ���ӹ� ���� ����Ʈ Vector

	
//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/�ӽ�5.jpg")).getImage();
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
	private ImageIcon gamgeRoomImage = new ImageIcon(Main.class.getResource("/images/gameroom.png")); 
	
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư
	private JButton createRoomButton = new JButton(createRoomBasicImage); // �游��� ��ư
	private JButton rightRButton = new JButton(rightRBasicImage); // �� ������ �ѱ�� ��ư
	private JButton leftRButton = new JButton(leftRBasicImage); // �� ���� �ѱ�� ��ư
	
	public WaitingRoom() {
		//����� ���ÿ� id�� MainView�κ��� �̾�޾ƿ´�.
		id = MainView.getId();
		
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
		
		// #���� ����Ʈ 
		userList_Label.setBounds(30, 20, 200, 30);
		userList_Label.setBackground(new Color(40,40,40,40));
		add(userList_Label);
		
				
	// JScrollPane
		// #ä�ú�
		chattingView.setBounds(320, 520, 600, 200);
		chattingView.setBackground(new Color(40,40,40,40));
		chattingView.setViewportView(chattingArea);
		chattingArea.setBackground(new Color(0,0,0,0)); 
		chattingArea.setFont(font);
		chattingArea.setForeground(Color.BLACK);
		chattingArea.setEditable(false); // �ش� �ʵ带 ������ �� ����
		add(chattingView); 
		
		// #���� ����Ʈ ��
		userListView.setBounds(30, 400, 180, 350);
		userListView.setBackground(new Color(40,40,40,40));
//		userListView.setViewportView(user_List);
//		user_List.setBackground(new Color(0,0,0,0)); 
		add(userListView); 
		
		// #���ӹ� ��
		gameRoomView.setBounds(240, 110, 760, 370);
		gameRoomView.setLayout(new FlowLayout(FlowLayout.CENTER));
		allocationRoom(); // ���ǿ� ���ӹ��� ���̵��� �ϴ� �޼ҵ�
		
		
		
		gameRoomView.setBackground(new Color(40,40,40,40));
//		gameRoomView.setViewportView(gameRoom_List);
//		gameRoom_List.setBackground(new Color(0,0,0,0)); 
		add(gameRoomView); 
		
	// TextField
		chatting_tf = new JTextField(); 
		chatting_tf.setBounds(320, 720, 600, 30);
		chatting_tf.setBackground(new Color(40,40,40,40));
		add(chatting_tf);
		chatting_tf.setDocument(new JTextFieldLimit(45)); // ä�� 45�� ���� 	 
		chatting_tf.setFont(font);
		chatting_tf.setForeground(Color.BLACK);
		chatting_tf.addKeyListener(new keyAdapter());
			
	// Button
		// #������ ��ư
		exitButton.setBounds(820, 30, 180, 80);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				System.exit(0); // ���μ��� ����
			}
		});
		
		// #�游��� ��ư
		createRoomButton.setBounds(240, 30, 180, 80);
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

			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override
			public void mousePressed(MouseEvent e) {
				new CreateRoom();
			}
		});
		
		// #�� ������ �ѱ�� ��ư
		rightRButton.setBounds(660, 480, 60, 40);
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

			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
		});

		// #�� ���� �ѱ�� ��ư
		leftRButton.setBounds(540, 480, 60, 40);
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

			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
		});
		
		Connection();
	} // WaitingRoom() ������ ��
	
	private void Connection() // �������� �޼ҵ� ����κ�
	{
		dis = MainView.getDis();
		dos = MainView.getDos();
	}
	
	
	
	private void Inmessage(String str) // �����κ��� ������ ��� �޼���
	{
		st = new StringTokenizer(str, "@");  // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ =>  [ NewUser/�����ID ] ���·� ����
		
		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String Message = st.nextToken(); // �޽����� �����Ѵ�.
		
		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + Message);
		
		
		if(protocol.equals("NewUser")) // ���ο� ������
		{
			user_list.add(Message);
		}
		else if(protocol.equals("OldUser")) // ���� ������
		{
			user_list.add(Message);
		}
		else if(protocol.equals("Note")) // ����
		{
			String note = st.nextToken(); // ���� ����
			
			System.out.println(Message+" ����ڷκ��� �� ���� "+note);
			
			JOptionPane.showMessageDialog(null, note, Message+"������ ���� ����", JOptionPane.CLOSED_OPTION);
		}
		else if(protocol.equals("user_list_update"))
		{
//			User_list.setListData(user_list);
		}
		else if(protocol.equals("CreateRoom")) // ���� ������� ��
		{
//			My_Room = Message;
		}
		else if(protocol.equals("CreateRoomFail")) // �� ����� �������� ���
		{
			JOptionPane.showMessageDialog(null, "�� ����� ����","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		else if(protocol.equals("New_Room")) // ���ο� ���� ������� ��
		{
//			room_list.add(Message);
//			Room_list.setListData(room_list);
		}
		else if(protocol.equals("ChattingWR"))
		{
			String msg = st.nextToken(); 
			System.out.println("���� : " + msg);
			chattingArea.append("["+Message+"] : "+msg+"\n");
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
	
	// ���� 24���� ���� �����ϱ� ���� �޼ҵ� 
	private void allocationRoom() {
		Font roomFont = new Font("Inconsolata",Font.BOLD,17); // ��Ʈ ����
		for(int i=0; i<gameRoom.length; i++) {
			// ���� Ŭ���� GameRoomPanel Ŭ������ �̿��ؼ� gameRoom Panel�� ����
			gameRoom[i] = new GameRoomPanel(gamgeRoomImage.getImage());
			
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	private void createRoom(String title) {
		Font roomFont = new Font("Inconsolata",Font.BOLD,17); // ��Ʈ ����
		int roomNo = 0;
		// 1���� ���������� ���� �ִ� ������ŭ�� �������� �����ϰ� ��ȣ�� �� �� �Ҵ��Ѵ�.(�ߺ��� ������� �ʴ´�.)
//		int roomNo = (int)(Math.random()*gameRoom.length) + 1;  // �� ����� �߰��ϴ°� ���߿�.. �ϴ� ���������� ��ȣ �Ҵ��ϴ� ���� ���� ����.
		Pointer:
		while(true) {
			//Vector�� ������ (�ε���, ��);
			for(int i=0; i<=room_list.size(); i++) {
				if(room_list.get(i).equals(null));
			}
			
			break;
		}
		
		
		// ���ӹ� ��ȣ�� ������ JLabel�� �Ҵ�
		gameRoomNumber_lb[roomNo-1] = new JLabel();
		String number = "";
		// ���ȣ ����. roomNo���� ���� �ش� ���ȣ�� ����. 000 �� ���·� ����. 
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
		
		// ���ӹ� ������ ������ JLabel�� �Ҵ�
		gameRoomTitle_lb[roomNo-1] = new JLabel();
		gameRoomTitle_lb[roomNo-1].setText("No Title");
		gameRoomTitle_lb[roomNo-1].setFont(roomFont);
		gameRoomTitle_lb[roomNo-1].setBounds(90, 23, 200, 20);
		gameRoomTitle_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoom[roomNo-1].add(gameRoomTitle_lb[roomNo-1]);
		
		// ���ӹ� �ο����� ������ JLabel�� �Ҵ�
		gameRoomPlayerCount_lb[roomNo-1] = new JLabel();
		gameRoomPlayerCount_lb[roomNo-1].setText("0 / 6");
		gameRoomPlayerCount_lb[roomNo-1].setFont(roomFont);
		gameRoomPlayerCount_lb[roomNo-1].setBounds(275, 68, 50, 20);
		gameRoomPlayerCount_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoom[roomNo-1].add(gameRoomPlayerCount_lb[roomNo-1]);
		
		// �̷��� ������ �г��� �信 �߰��Ѵ�.
		gameRoomView.add(gameRoom[roomNo-1]);
		
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
					//UserID�� ���߿� �߰�����
//					chattingArea.append("["+id+"] : "+message+"\n");
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class ��
	
//	public static Vector getUserList() {
//		return user_list;
//	}
//	
//	public static void addUserList(Vector v) {
//		user_list.add(v);
//	}
//	
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
		new WaitingRoom();
	}
	
	
	// �游��� ������
	class CreateRoom extends JFrame{
	// TextField
		private JTextField roomTitel_tf = new JTextField(); // �� ����
		private JTextField roomPw_tf = new JTextField();  // �� ��й�ȣ
	
	// ComboBox
		private String[] state = {"����","�����"};
		private Integer[] player = {2,3,4,5,6}; // �ִ��ο� 6������ ����
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
			setPreferredSize(new Dimension(crbackgroundImage.getWidth(null), crbackgroundImage.getHeight(null)));
			setResizable(false); // ������ ũ�� ����
			setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
			setBackground(new Color(0,0,0,0));
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
				// #�Ŵ��� �巡�� ��, ������ �� �ְ� �Ѵ�.
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
				// ���콺�� ��ư�� ������ �� �̺�Ʈ
				@Override 
				public void mousePressed(MouseEvent e) {
					dispose(); 
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
				// ���콺�� ��ư�� ������ �� �̺�Ʈ
				@Override 
				public void mousePressed(MouseEvent e) {
					
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
	
	
	
	// ���ӹ� �ϳ��ϳ��� JPanel�� ��ӹ��� GameRoomPanel Ŭ������ �����Ѵ�.
	class GameRoomPanel extends JPanel{
		private Image img;
		
		public GameRoomPanel(Image img) {
			this.img = img;
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // null�� �ִ�
			setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
			setLayout(null); // �гο� �߰��ϴ� ��ҵ��� ��ġ�� �����Ӱ� �����ϱ� ���� Layout�� null�� ���ش�.
		}
		
		// �г��� ������ �� �ڵ����� �̹����� �׷��ִ� �޼ҵ�
		public void paintComponent(Graphics g)  {
			g.drawImage(img, 0, 0, null);
		}
	}
}
