package kh.mini.project.waiting_room.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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

public class WaitingRoom extends JFrame{
// Frame, Panel
	private JFrame WaitingRoomView = new JFrame("Waiting Room"); // ���� ������
	private JScrollPane chattingView = new JScrollPane(); // ä���� ���̰��ϴ� ��ũ�� ����
	private JPanel userListView = new JPanel(); // ���� ����Ʈ �г�
	private JPanel gameRoomView = new JPanel(); // ���ӹ� �г�
	private JTextArea chattingArea = new JTextArea(); // ä�� ��ũ�� ���ο� �÷����� ä�� TextArea
	private JPanel[] gameRoom = new JPanel[24]; // 1~24���� ���� ���� 
	private JPanel[] userList = new JPanel[50]; // 50���� ���� ����Ʈ�� ���� �г�
	
// Label
	private JLabel mainMenuBar = new JLabel();
	private JLabel userList_Label = new JLabel("User List");
	private JLabel gameRoom_Label = new JLabel();
	
// Textfield	
	private JTextField chatting_tf; // ä�� ������ �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�	
	
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
	private static Vector user_list = new Vector();

	
//Image	
	//MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/�ӽ�5.jpg")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)	
	
	//Button Icon (basic : ��ư�� �⺻ ����, Entered : ��ư�� ���콺�� ������ ����) 
	// => ��ư �⺻����, ���콺�� �÷����� �� ����, ������ �� ���� 3���� ����?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon createRoomBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon createRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon rightRBasicImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_R_basic.png"));
	private ImageIcon rightREnteredImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_R_entered.png")); 
	private ImageIcon leftRBasicImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_L_basic.png"));
	private ImageIcon leftREnteredImage = new ImageIcon(Main.class.getResource("/images/ȭ��ǥ1_L_entered.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư
	private JButton createRoomButton = new JButton(createRoomBasicImage); // �游��� ��ư
	private JButton rightRButton = new JButton(rightRBasicImage); // �� ������ �ѱ�� ��ư
	private JButton leftRButton = new JButton(leftRBasicImage); // �� ���� �ѱ�� ��ư
	
	public WaitingRoom() {
		//����� ���ÿ� ��Ʈ��ũ ������ id�� MainView�κ��� �̾�޾ƿ´�.
//		socket = MainView.getSocket();
//		port = MainView.getPort();
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
	
	
	public void wr_Inmessage(String str) {
		Inmessage(str);
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
	
	public static Vector getUserList() {
		return user_list;
	}
	
	public static void addUserList(Vector v) {
		user_list.add(v);
	}
	
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
}
