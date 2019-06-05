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
	private JFrame mainView = new JFrame("CatchMind"); // ���� ������
	private JPanel loginView = new JPanel(); // ���� ������(�г�) - �α��� �г�
	
//Label
	private JLabel mainMenuBar = new JLabel();
	private JLabel serverIp_lb = new JLabel("Server IP : "); // �ӽ�  ip ���̺�
	private JLabel port_lb = new JLabel("Port Number : "); // �ӽ� ��Ʈ ��ȣ ���̺�
	private JLabel id_lb = new JLabel("ID : "); // �ӽ� id ���̺�
	private JLabel pw_lb = new JLabel("PW : "); // �ӽ�  pw ���̺�

// Textfield	
	private JTextField serverIp_tf; // Server IP�� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField port_tf; // ��Ʈ��ȣ�� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField id_tf; // ID�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	private JPasswordField pw_tf; // PW�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	
// Network �ڿ� ����
	private static Socket socket; // ����� ����
	private static int port; // ��Ʈ��ȣ	
	private String ip=""; // 127.0.0.1 �� �ڱ� �ڽ�
	private static String id=""; // ����� ID
	private String pw=""; // ����� PW => ������ ���Ծ��� �α��� �����ϰ� �Ͽ� �׽�Ʈ ����
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����
	private int mouseX, mouseY; // ���콺 ��ǥ�� ����
	private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
	private boolean connetionCheck = false; // Waiting room���� �Ѿ�� ���� Ŀ�ؼ� üũ.(ConnectException �� �߻��ϸ� �α��� ���� �˸��� �߻���Ű�� ����)
	static Vector user_list = new Vector();
	
//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/test.png")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)
	
	//Button Icon (basic : ��ư�� �⺻ ����, Entered : ��ư�� ���콺�� ������ ����) 
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon conncetBasicImage = new ImageIcon(Main.class.getResource("/images/connect.png"));
	private ImageIcon connectEnteredImage = new ImageIcon(Main.class.getResource("/images/connect.png")); 
	private ImageIcon loginBasicImage = new ImageIcon(Main.class.getResource("/images/login.png"));
	private ImageIcon loginEnteredImage = new ImageIcon(Main.class.getResource("/images/login.png")); 
	private ImageIcon joinBasicImage = new ImageIcon(Main.class.getResource("/images/����.png"));
	private ImageIcon joinEnteredImage = new ImageIcon(Main.class.getResource("/images/����.png")); 
	
	
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư
	private JButton connetButton = new JButton(conncetBasicImage); // ���� ��ư
	private JButton loginButton = new JButton(loginBasicImage); // �α��� ��ư
	private JButton joinButton = new JButton(joinBasicImage); // ȸ������ ��ư
	
	
	MainView() {
	// JFrame mainView
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������)
		setTitle("Catch Mind"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main���� ������Ų ȭ�� �ػ󵵸� ���
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null); // ��ġ ������ ����
	
	// Label
		// #�޴���
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
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
	
		// IP (�ӽ�-���߿� ���� ����)
		serverIp_lb.setBounds(400, 470, 100, 30);
		add(serverIp_lb);
		
		// ��Ʈ��ȣ(�ӽ�-���߿� ���� ����)
		port_lb.setBounds(400, 515, 100, 30);
		add(port_lb);
		
		// ID (�ӽ�-���߿� ���� ����)
		id_lb.setBounds(400, 560, 100, 30);
		add(id_lb);
				
		// PW (�ӽ�-���߿� ���� ����)
		pw_lb.setBounds(400, 605, 100, 30);
		add(pw_lb);
		
	// TextField
		//IP �Է�
		serverIp_tf = new JTextField(); 
		serverIp_tf.setBounds(520, 470, 150, 30);
		add(serverIp_tf); 
		serverIp_tf.setDocument(new JTextFieldLimit(15)); //15�� ����(ip �ִ� �Է°��� ũ�� 000.000.000.000)
		serverIp_tf.setText("127.0.0.1"); //IP �⺻�� 127.0.0.1(�ڱ� �ڽ�)
		
		//��Ʈ��ȣ �Է�
		port_tf = new JTextField(); 
		port_tf.setBounds(520, 515, 150, 30);
		add(port_tf);
		port_tf.setDocument(new JTextFieldLimit(5)); // 5�� ���� (��Ʈ��ȣ ���� 0~65535)	 
		port_tf.setText("12345");//��Ʈ��ȣ �⺻�� 12345
		
		//ID �Է�
		id_tf = new JTextField();
		id_tf.setBounds(520, 560, 150, 30);
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //���̵� �ִ� 12�� ����
				
		//PW �Է�
		pw_tf = new JPasswordField();
		pw_tf.setBounds(520, 605, 150, 30);
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // ��й�ȣ �ִ� 12�� ����
		
	// Button
		// #������ ��ư
		exitButton.setBounds(870, 690, 100, 30);
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
				System.exit(0); // ���μ��� ����.
			}
		});
		// #���� ��ư
		connetButton.setBounds(511, 652, 170, 64);
		add(connetButton);
		connetButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				connetButton.setIcon(connectEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				connetButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				connetButton.setIcon(conncetBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				connetButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println("���� ��ư Ŭ��");
				// ���� ��ư�� ������ Server IP, port�� �����Ѵ�.
				ip = serverIp_tf.getText().trim(); // �� ���� ����
				port = Integer.parseInt(port_tf.getText().trim());
				
				// ��Ʈ��ũ ���� ����
				Network();
				
				// connetionCheck ���¿� ���� ������ �� �˸�â ������ȯ
				if(connetionCheck) {
					connetionChecked(connetionCheck);
					connectToLogin();
					JOptionPane.showMessageDialog(null,"������ ���������� �̷�������ϴ�."
							+ "\n���� �α����� �Ͻñ� �ٶ��ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
				} else { // 
					JOptionPane.showMessageDialog(null, 
							"�α��� ����!\nServer Port Number�� ��ġ���� �ʰų�"
							+"\n������ ���������� �ʽ��ϴ�.\n�ٽ� Ȯ�����ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);
				}
			}
					
		});
		
		// #�α��� ��ư
		loginButton.setBounds(511, 652, 170, 64);
		add(loginButton);
		loginButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setIcon(loginEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				loginButton.setIcon(loginBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				loginButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println("�α��� ��ư Ŭ��");
				// �α��� ��ư�� ������ Server IP, port, id, pw�� �����Ѵ�.
				
				id = id_tf.getText().trim();
				//JPasswordField�� getText()�޼ҵ带 ������ �ʴ´� �Ͽ� �Ʒ��� ���� ������� ����
				char[] tempPw = pw_tf.getPassword();
				for(char a : tempPw) {
					pw += a;
				}
					
				if(connetionCheck) {
					dispose(); // MainView�� �����ϰ� 
					new WatingRoom(); // WatingRoom�� �����Ѵ�. 
				} else { // 
					JOptionPane.showMessageDialog(null, 
							"�α��� ����!\n���̵�� �н����带 �ٽ� Ȯ�����ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
		
		// #ȸ������ ��ư
		joinButton.setBounds(341, 652, 170, 64);
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setIcon(joinEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				joinButton.setIcon(joinBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				joinButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				if(connetionCheck) {
					new JoinView();
				} else {
					JOptionPane.showMessageDialog(null, 
							"������ ���� ���� �Ŀ� �õ��Ͻñ� �ٶ��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
	// JPanel loginView
		loginView.setBounds(341, 460, 341, 256);
		add(loginView);
		
		connetionChecked(connetionCheck);
	}
	
	// ���ἳ���� �Ϸ�Ǹ� �α��� ��ư���� �ٲ��.
	private void connectToLogin() {
		connetButton.setEnabled(false);
		connetButton.setVisible(false);
		loginButton.setEnabled(true);
	}
	
	// connetionCheck ���� ���� 
	private void connetionChecked(boolean connetionCheck) {
		if(connetionCheck) { // ���� �����Ǿ� �ִ� ������ ��
			serverIp_tf.setEnabled(false);
			port_tf.setEnabled(false);
			id_tf.setEnabled(true);
			pw_tf.setEnabled(true);
		} else { // ���� ������ �Ǿ��ֱ� ���� ������ ��
			serverIp_tf.setEnabled(true);
			port_tf.setEnabled(true);
			id_tf.setEnabled(false);
			pw_tf.setEnabled(false);
		}
	}
	
	private void Network() 
	{	
		connetionCheck = true; // ���� �������� ture�� �ʱ�ȭ
		try {
			socket = new Socket(ip,port); 
			
			if(socket != null) // ���������� ������ ����Ǿ��� ���
			{
				Connection();
			}
			
		} catch (UnknownHostException e) { // ȣ��Ʈ�� ã�� �� ���� ��
			e.printStackTrace();
		} catch (ConnectException e) {
			connetionCheck = false; // ConnectException�� �߻��ϸ� false�� ����. �α��� ���� �˸�â�� ���.
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void Connection() // �������� �޼ҵ� ����κ�
	{ 
		try 
		{
			is = socket.getInputStream(); // �������� ���� ��Ʈ���� is�� ����
			dis = new DataInputStream(is); // is�� DataInputStream���� dis�� ����
			
			os = socket.getOutputStream(); // �������� ������ ��Ʈ���� os�� ����
			dos = new DataOutputStream(os); // os�� DataOutputStream���� dos�� ����
		} 
		catch (IOException e) // ����ó�� �κ�
		{ 
			
		} // Stream ���� ��
		
		// ó�� ���ӽÿ� ID ����
		send_message(id);	
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) 
				{
					try {
						String msg = dis.readUTF(); // �޼��� ����
						
						System.out.println("�����κ��� ���ŵ� �޼��� : " + msg);
						
						inmessage(msg);
					} catch (IOException e) {
						
					}
				}
				
			}
		});
		
		th.start();
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
	
	
	/* �޽��� �ۼ��� ���¸� ��������(Protocol) ���Ŀ� ���缭 �Ѵ�.
 	 * [Protocol]/[Message] ���·� �޽����� �����Ѵ�.
 	 * ���ŵ� �޽����� "/"�� �������� �����Ͽ� ������ ���̰� 
 	 * Protocol �κ��� NewUser,OldUser,Chatting ������
 	 * ����� ���������� Message�� �����Ѵ�.
	 */
	private void inmessage(String str) // �����κ��� ������ ��� �޼���
	{
		st = new StringTokenizer(str, "/");  // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ =>  [ NewUser/�����ID ] ���·� ����
		
		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String Message = st.nextToken(); // �޽����� �����Ѵ�.
		
		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + Message);
		
		if(protocol.equals("NewUser")) // ���ο� ������
		{
			user_list.add(Message); // ���� ����Ʈ�� ���ο� User ID �߰�
		}
		else if(protocol.equals("OldUser")) // ���� ������
		{
			user_list.add(Message); // ���� ����Ʈ�� ���� User ID �߰�
		}
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
	}
	
	
	// ��Ʈ��ȣ�� �ٸ� Ŭ�������� �˱� ���� �޼ҵ�
	public static int getPort() {
		return port;
	}
	
	// ������ �ٸ� Ŭ�������� �̾�ޱ� ���� �޼ҵ�
	public static Socket getSocket() {
		return socket;
	}
	
	// ID�� �ٸ� Ŭ�������� �̾�ޱ� ���� �޼ҵ�
	public static String getId() {
		return id;
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
}
