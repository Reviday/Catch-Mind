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
import kh.mini.project.model.vo.User;
import kh.mini.project.model.vo.UserInfo;

public class WaitingRoom extends JFrame{
// Frame, Panel
	private JFrame WaitingRoomView = new JFrame("Waiting Room"); // ¸ŞÀÎ ÇÁ·¹ÀÓ
	private JScrollPane chattingView = new JScrollPane(); // Ã¤ÆÃÀ» º¸ÀÌ°ÔÇÏ´Â ½ºÅ©·Ñ ÆäÀÎ
	private JPanel userInfoView = new JPanel(); // À¯Àú Á¤º¸ ÆĞ³Î
	private JPanel userListView = new JPanel(); // À¯Àú ¸®½ºÆ® ÆĞ³Î
	private JPanel gameRoomView = new JPanel(); // °ÔÀÓ¹æ ÆĞ³Î
	private JPanel[] gameRoom = new JPanel[6]; // 24°³ÀÇ ¹æÀ» °³¼³ => ¹öÆ°À¸·Î ÇØº¼±î??
	private JPanel[] userList = new JPanel[10]; // 50¸íÀÇ À¯Àú ¸®½ºÆ®¸¦ ¶ç¿ì´Â ÆĞ³Î
	
// Label
	private JLabel mainMenuBar = new JLabel();
//	private JLabel userList_Label = new JLabel("User List");
	private JLabel gameRoom_Label = new JLabel();
	private JLabel[] userID_lb = new JLabel[userList.length]; 		// À¯Àú ID ¶óº§ ¹è¿­
	private JLabel[] gameRoomNumber_lb = new JLabel[gameRoom.length]; 		// °ÔÀÓ¹æ ³Ñ¹ö ¶óº§ ¹è¿­, gameRoom ¹è¿­ÀÇ Å©±â¸¸Å­ »ı¼º
	private JLabel[] gameRoomTitle_lb = new JLabel[gameRoom.length]; 		// °ÔÀÓ¹æ Á¦¸ñ ¶óº§ ¹è¿­, gameRoom ¹è¿­ÀÇ Å©±â¸¸Å­ »ı¼º
	private JLabel[] gameRoomPlayerCount_lb = new JLabel[gameRoom.length];  // °ÔÀÓ¹æ ÀÎ¿ø¼ö ¶óº§ ¹è¿­, gameRoom ¹è¿­ÀÇ Å©±â¸¸Å­ »ı¼º
	
	
// Textfield	
	private JTextField chatting_tf; // Ã¤ÆÃ ³»¿ëÀ» ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ®ÇÊµå	
	private JTextArea chattingArea = new JTextArea(); // Ã¤ÆÃ ½ºÅ©·Ñ ÆäÀÎ¿¡ ¿Ã·Á³õÀ» Ã¤ÆÃ TextArea
	
// list
	private JList user_Li = new JList();
//	private JList gameRoom_List = new JList();
	
// Network ÀÚ¿ø º¯¼ö
	private Socket socket;// »ç¿ëÀÚ ¼ÒÄÏ
	private int port; // Æ÷Æ®¹øÈ£		
	private String id =""; 
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
// °¢Á¾ º¯¼ö
	private Image viewImage; // ÀÌ¹ÌÁö ÀúÀå¿ë º¯¼ö
	private Graphics viewGraphics; // ±×·¡ÇÈ ÀúÀå¿ë º¯¼ö	
	private int mouseX, mouseY; // ¸¶¿ì½º ÁÂÇ¥¿ë º¯¼ö
	private StringTokenizer st; // ÇÁ·ÎÅäÄİ ±¸ÇöÀ» À§ÇØ ÇÊ¿äÇÔ. ¼ÒÄÏÀ¸·Î ÀÔ·Â¹ŞÀº ¸Ş½ÃÁö¸¦ ºĞ¸®ÇÏ´Âµ¥ ¾²ÀÓ.
	private UserInfo userInfo; // Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ ÀúÀåÇÏ´Â °´Ã¼(ÀÌ ÀÌ¸§À¸·Î Á¢±ÙÇÏ¸é »ç¿ëÀÚ º»ÀÎÀÇ Á¤º¸¿¡ Á¢±ÙÇÒ ¼ö ÀÖ´Ù.)
//	private Vector user_list = new Vector(); // À¯Àú ¸®½ºÆ® Vector
	private Vector room_list = new Vector(); // ¹æ ¸®½ºÆ® Vector
	private Vector gUser_list = new Vector(); // °ÔÀÓ¹æ À¯Àú ¸®½ºÆ® Vector
	private Vector<UserInfo> user_list = new Vector<UserInfo>();
	private Toolkit tk = Toolkit.getDefaultToolkit();
	Image img = tk.getImage(Main.class.getResource("/images/Ä¿¼­Å×½ºÆ®.png"));
	Cursor myCursor = tk.createCustomCursor(img, new Point(10,10), "dynamite stick");
	// ¹æ ¸¸µé±â¿¡ ÇÊ¿äÇÑ º¯¼ö
	private String title; // ¹æÁ¦¸ñ
	private String roomPW; // ¹æ ºñ¹Ğ¹øÈ£
	private int uCount; // ¹æ ÀÎ¿ø¼ö
	private int roomNo; // ¹æ ¹øÈ£
	
	
//Image	
	// #MainView ¹è°æ
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/WaitingRoom_Background.png")).getImage();
			//Main Å¬·¡½ºÀÇ À§Ä¡¸¦ ±âÁØÀ¸·Î ÀÌ¹ÌÁö ÆÄÀÏÀÇ À§Ä¡¸¦ Ã£Àº ´ÙÀ½¿¡ ÀÌ¹ÌÁö ÀÎ½ºÅÏ½º¸¦ ÇØ´ç º¯¼ö¿¡ ÃÊ±âÈ­ ÇØÁÜ(»ó´ë°æ·Î °°Àº Àı´ë°æ·Î)	
	
	// Button Icon (basic : ¹öÆ°ÀÇ ±âº» »óÅÂ, Entered : ¹öÆ°¿¡ ¸¶¿ì½º¸¦ °¡Á®°£ »óÅÂ) 
	// => ¹öÆ° ±âº»»óÅÂ, ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ» ¶§ »óÅÂ, ´­·¶À» ¶§ »óÅÂ 3°¡Áö °¡´É?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon createRoomBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon createRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon rightRBasicImage = new ImageIcon(Main.class.getResource("/images/È­»ìÇ¥1_R_basic.png"));
	private ImageIcon rightREnteredImage = new ImageIcon(Main.class.getResource("/images/È­»ìÇ¥1_R_entered.png")); 
	private ImageIcon leftRBasicImage = new ImageIcon(Main.class.getResource("/images/È­»ìÇ¥1_L_basic.png"));
	private ImageIcon leftREnteredImage = new ImageIcon(Main.class.getResource("/images/È­»ìÇ¥1_L_entered.png")); 
	private ImageIcon gamgeRoomBasicImage = new ImageIcon(Main.class.getResource("/images/gameroom.png")); 
	private ImageIcon gamgeRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/gameroomEntered1.png")); 
	private ImageIcon gamgeRoomPressedImage = new ImageIcon(Main.class.getResource("/images/gameroomPressed.png"));
	private ImageIcon userInfoPanelImage = new ImageIcon(Main.class.getResource("/images/userInfoPanel.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ³ª°¡±â ¹öÆ°
	private JButton createRoomButton = new JButton(createRoomBasicImage); // ¹æ¸¸µé±â ¹öÆ°
	private JButton rightRButton = new JButton(rightRBasicImage); // ¹æ ¿À¸¥ÂÊ ³Ñ±â±â ¹öÆ°
	private JButton leftRButton = new JButton(leftRBasicImage); // ¹æ ¿ŞÂÊ ³Ñ±â±â ¹öÆ°
	
	public WaitingRoom() {
		//½ÇÇà°ú µ¿½Ã¿¡ ³×Æ®¿öÅ© ÀÚ¿ø°ú id¸¦ MainView·ÎºÎÅÍ ÀÌ¾î¹Ş¾Æ¿Â´Ù.
		id = MainView.getId();
		dis = MainView.getDis();
		dos = MainView.getDos();
		
		Font font = new Font("Inconsolata",Font.BOLD,15); // ÆùÆ® ¼³Á¤
		
		setUndecorated(true); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù Á¦°Å(À©µµ¿ì¸¦ Á¦°ÅÇÔ)
		setTitle("Catch Mind"); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù ÀÌ¸§(Å¸ÀÌÆ² ¹Ù¸¦ ¾ø¾Ù ¿¹Á¤ÀÌ±â ¶§¹®¿¡ ¾ø¾îµµ µÇ´Â ÄÚµå)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main¿¡¼­ °íÁ¤½ÃÅ² È­¸é ÇØ»óµµ¸¦ »ç¿ë
		setResizable(false); // ÇÁ·¹ÀÓ Å©±â °íÁ¤
		setLocationRelativeTo(null); // À©µµ¿ì¸¦ È­¸é Á¤Áß¾Ó¿¡ ¶ç¿ì±â À§ÇÔ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // À©µµ¿ì Á¾·á½Ã ³²¾ÆÀÖ´Â ÇÁ·Î¼¼½ºµµ ±ú²ıÇÏ°Ô Á¾·áÇÏ±â À§ÇÔ
		setBackground(new Color(0,0,0,0)); // ¹è°æ»öÀ» Åõ¸íÇÏ°Ô ÇÑ´Ù.(paint()¸Ş¼Òµå·Î ±×¸®´Â ¹è°æÀ» º¸ÀÌ°Ô ÇÏ±â À§ÇÔ)
		setVisible(true); // À©µµ¿ì¸¦ º¼ ¼ö ÀÖÀ½.
		setLayout(null);
		setCursor(myCursor);
		
	// Label
		// #¸Ş´º¹Ù
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				mainMenuBar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		mainMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
			// #¸Å´º¹Ù µå·¡±× ½Ã, ¿òÁ÷ÀÏ ¼ö ÀÖ°Ô ÇÑ´Ù.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(mainMenuBar);
		
		// #À¯Àú ¸®½ºÆ® 
//		userList_Label.setBounds(30, 20, 200, 30);
//		userList_Label.setBackground(new Color(40,40,40,40));
//		add(userList_Label);
		
				
	// JScrollPane
		// #Ã¤ÆÃºä
		chattingView.setBounds(240, 490, 768, 200);
		chattingView.setBackground(new Color(40,40,40,40));
		chattingView.setViewportView(chattingArea);
		chattingArea.setBackground(new Color(0,0,0,0)); 
		chattingArea.setFont(font);
		chattingArea.setForeground(Color.BLACK);
		chattingArea.setEditable(false); // ÇØ´ç ÇÊµå¸¦ ¼öÁ¤ÇÒ ¼ö ¾øÀ½
		add(chattingView); 
		
		// #À¯Àú Á¤º¸(ÀÚ½Å) ºä
		userInfoView.setBounds(30, 250, 190, 68);
		userInfoView.setBackground(new Color(40,40,40,40));
		add(userInfoView); 
		
		
		// #À¯Àú ¸®½ºÆ® ºä
		userListView.setBounds(30, 330, 190, 374);
		userListView.setLayout(new FlowLayout(FlowLayout.CENTER));
		userListView.setBackground(new Color(40,40,40,40));
		allocationUserInfo();
		add(userListView); 
		
		// #°ÔÀÓ¹æ ºä
		gameRoomView.setBounds(240, 110, 768, 370);
		gameRoomView.setLayout(new FlowLayout(FlowLayout.CENTER));
		gameRoomView.setBackground(new Color(40,40,40,40));
		allocationRoom(); // ´ë±â½Ç¿¡ °ÔÀÓ¹æÀÌ º¸ÀÌµµ·Ï ÇÏ´Â ¸Ş¼Òµå
		add(gameRoomView); 
		
		
	// TextField
		chatting_tf = new JTextField(); 
		chatting_tf.setBounds(260, 690, 728, 30);
		chatting_tf.setBackground(new Color(40,40,40,40));
		add(chatting_tf);
		chatting_tf.setDocument(new JTextFieldLimit(45)); // Ã¤ÆÃ 45ÀÚ Á¦ÇÑ 	 
		chatting_tf.setFont(font);
		chatting_tf.setForeground(Color.BLACK);
		chatting_tf.addKeyListener(new keyAdapter());
			
	// Button
		// #³ª°¡±â ¹öÆ°
		exitButton.setBounds(440, 60, 180, 50);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
//				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
				exitButton.setCursor(myCursor);
			}
			
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					System.exit(0); // ÇÁ·Î¼¼½º Á¾·á
				}
			}
		});
		
		// #¹æ¸¸µé±â ¹öÆ°
		createRoomButton.setBounds(260, 60, 180, 50);
		add(createRoomButton);
		createRoomButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				createRoomButton.setIcon(createRoomEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				createRoomButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}

			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseExited(MouseEvent e) {
				createRoomButton.setIcon(createRoomBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				createRoomButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					new CreateRoom();
				}
			}
		});
		
		// #¹æ ¿À¸¥ÂÊ ³Ñ±â±â ¹öÆ°
		rightRButton.setBounds(640, 450, 60, 40);
		add(rightRButton);
		rightRButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				rightRButton.setIcon(rightREnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				rightRButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}

			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseExited(MouseEvent e) {
				rightRButton.setIcon(rightRBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				rightRButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});

		// #¹æ ¿ŞÂÊ ³Ñ±â±â ¹öÆ°
		leftRButton.setBounds(540, 450, 60, 40);
		add(leftRButton);
		leftRButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				leftRButton.setIcon(leftREnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				leftRButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}

			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseExited(MouseEvent e) {
				leftRButton.setIcon(leftRBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				leftRButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});
		
		//´ë±â½Ç ÀÔÀå½Ã, È¯¿µÇÏ´Â ¹®±¸ Ãâ·Â
		chattingArea.append("["+id+"]´Ô È¯¿µÇÕ´Ï´Ù! \n");
	} // WaitingRoom() »ı¼ºÀÚ ³¡

	private void Inmessage(String str) // ¼­¹ö·ÎºÎÅÍ µé¾î¿À´Â ¸ğµç ¸Ş¼¼Áö
	{
		st = new StringTokenizer(str, "@");  // ¾î¶² ¹®ÀÚ¿­À» »ç¿ëÇÒ °ÍÀÎÁö, ¾î¶² ¹®ÀÚ¿­·Î ÀÚ¸¦ °ÍÀÎÁö =>  [ NewUser/»ç¿ëÀÚID ] ÇüÅÂ·Î µé¾î¿È
		
		String protocol = st.nextToken(); // ÇÁ·ÎÅäÄİÀ» ÀúÀåÇÑ´Ù.
		String Message = st.nextToken(); // ¸Ş½ÃÁö¸¦ ÀúÀåÇÑ´Ù.
		
		System.out.println("ÇÁ·ÎÅäÄİ : " + protocol);
		System.out.println("³»¿ë : " + Message);
		
		
		if(protocol.equals("NewUser")) // »õ·Î¿î Á¢¼ÓÀÚ
		{
			// »õ·Î¿î Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ °¡Á®¿Í ÀúÀåÇÑ´Ù.
			int level = Integer.parseInt(st.nextToken());
			int exp = Integer.parseInt(st.nextToken());
			int corAnswer = Integer.parseInt(st.nextToken());
			// °¡Á®¿Â Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			UserInfo u = new UserInfo(Message, level, exp, corAnswer);
			// ÇØ´ç °´Ã¼¸¦ Vector¿¡ Ãß°¡
			user_list.add(u);
			testMethod();
//			allocationUserInfo(); // À¯Àú¸®½ºÆ® °»½Å
			updateUserInfo();
		}
		else if(protocol.equals("UserInfo")) // Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ °¡Á®¿Í ÀúÀå
		{
			// »ç¿ëÀÚÀÇ Á¤º¸¸¦ °¡Á®¿Í ÀúÀåÇÑ´Ù.
			int level = Integer.parseInt(st.nextToken());
			int exp = Integer.parseInt(st.nextToken());
			int corAnswer = Integer.parseInt(st.nextToken());
			// °¡Á®¿Â Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			userInfo = new UserInfo(Message, level, exp, corAnswer);
			// ÇØ´ç °´Ã¼¸¦ Vector¿¡ Ãß°¡
			user_list.add(userInfo);
//			allocationUserInfo(); // À¯Àú¸®½ºÆ® °»½Å
			updateUserInfo();
		}
		else if(protocol.equals("OldUser")) // Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ ¹Ş¾Æ¿Â´Ù.(ÃÊ±â ¼¼ÆÃ ÀÛ¾÷)
		{ 
			// ÇØ´ç ÇÁ·ÎÅäÄİ·Î ¹Ş´Â »ç¿ëÀÚ Á¤º¸´Â Ã¹ ·Î±×ÀÎ ÇÑ ¹ø¸¸ Àû¿ëµÈ´Ù.
			// ÀÌÀü¿¡ Á¢¼ÓÁßÀÌ´ø ¸ğµç À¯ÀúÀÇ Á¤º¸¸¦ °¡Á®¿Â´Ù.
			int level = Integer.parseInt(st.nextToken());
			int exp = Integer.parseInt(st.nextToken());
			int corAnswer = Integer.parseInt(st.nextToken());
			// °¡Á®¿Â Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			UserInfo u = new UserInfo(Message, level, exp, corAnswer);
			// ÇØ´ç °´Ã¼¸¦ Vector¿¡ Ãß°¡
			user_list.add(u);
			
			// ¸¶Áö¸· ÅäÅ«ÀÌ lastÀÏ °æ¿ì ¸®½ºÆ®¸¦ °»½ÅÇÑ´Ù.
			String lastCheck = st.nextToken();
			if(lastCheck.equals("last")) 
//				allocationUserInfo();
				updateUserInfo();
		}
		else if(protocol.equals("Note")) // ÂÊÁö
		{
			String note = st.nextToken(); // ¹ŞÀº ³»¿ë
			
			System.out.println(Message+" »ç¿ëÀÚ·ÎºÎÅÍ ¿Â ÂÊÁö "+note);
			
			JOptionPane.showMessageDialog(null, note, Message+"´ÔÀ¸·Î ºÎÅÍ ÂÊÁö", JOptionPane.CLOSED_OPTION);
		}
		else if(protocol.equals("user_list_update"))
		{
//			User_list.setListData(user_list);
		}
		else if(protocol.equals("CreateRoom")) // ¹æÀ» ¸¸µé¾úÀ» ¶§
		{
			/* ¹æÀ» »ı¼ºÇÔ°ú µ¿½Ã¿¡, °ÔÀÓÃ¢À¸·Î ³Ñ¾î°¡µµ·Ï ÇÑ´Ù. */ 
		}
		else if(protocol.equals("New_Room")) // »õ·Î¿î ¹æÀ» ¸¸µé¾úÀ» ¶§
		{
			/* ¸ğµç ¹æ Á¤º¸¸¦ ¹Ş¾Æ¼­ »ı¼ºÇÏ°í, ´Ù½Ã ÆĞ³Î¿¡ ¶ç¿ì´Â ÀÛ¾÷ÀÌ ÇÊ¿ä. */
			roomNo = Integer.parseInt(st.nextToken()); // ¹æ¹øÈ£
			room_list.add(Message);
//			room_list.add(Message);
//			Room_list.setListData(room_list);
		}
		else if(protocol.equals("ChattingWR"))
		{
			String msg = st.nextToken(); 
			System.out.println("³»¿ë : " + msg);
			chattingArea.append("["+Message+"] : "+msg+"\n");
		}
	}
	
	void testMethod() {
		for(int i=0; i<user_list.size(); i++) 
		{
			UserInfo u = (UserInfo)user_list.elementAt(i);
			System.out.println("test À¯Àú Á¤º¸ ["+u.getUserID()+ "] : "+ u);
		}
	}
	
	private void send_message(String str) // ¼­¹ö¿¡°Ô ¸Ş¼¼Áö¸¦ º¸³»´Â ºÎºĞ
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ¿¡·¯ Ã³¸® ºÎºĞ
		{
			e.printStackTrace();
		}
	}
	
	// MainView Å¬·¡½º¿¡¼­ WaitingRoom Å¬·¡½º·Î ¸Ş½ÃÁö¸¦ Àü´ŞÇÏ±â À§ÇØ »ç¿ëÇÏ´Â ¸Ş¼Òµå
	public void wr_Inmessage(String str) {
		Inmessage(str);
	}
	
	// ´ë±â½Ç 24°³ÀÇ ¹æÀ» »ı¼ºÇÏ±â À§ÇÑ ¸Ş¼Òµå (¹æ °³¼öÀÇ º¯µ¿ÀÌ »ı±æ¶§¸¶´Ù µ¿ÀÛ)
	private void allocationRoom() {
		// ÃßÈÄ »ı¼ºµÈ ¹æ¿¡°Ô¸¸ ÀÌº¥Æ®¸¦ ÇÒ´çÇÏµµ·Ï º¯°æ
		for(int i=0; i<gameRoom.length; i++) {
			// ³»ºÎ Å¬·¡½º GameRoomPanel Å¬·¡½º¸¦ ÀÌ¿ëÇØ¼­ gameRoom PanelÀ» »ı¼º
			GameRoomPanel grp = new GameRoomPanel(gamgeRoomBasicImage.getImage());
			// ÇöÀç »ı¼ºµÈ ¹æÀÇ °³¼ö¸¸Å­ ¸®½º³Ê¸¦ Àû¿ë½ÃÅ²´Ù
			if(i<gameRoom.length) {
				grp.addMouseListener( new MouseAdapter() {
					// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
					@Override
					public void mouseEntered(MouseEvent e) {
						grp.setGRImage(gamgeRoomEnteredImage.getImage()); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
						grp.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
					}
					// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
					@Override  
					public void mouseExited(MouseEvent e) {
						grp.setGRImage(gamgeRoomBasicImage.getImage()); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
						grp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
					}
					// ¸¶¿ì½º·Î ¹öÆ°À» ´­·¶À» ¶§ ÀÌº¥Æ®
					@Override 
					public void mousePressed(MouseEvent e) {
						if(e.getButton()==1) {
							grp.setGRImage(gamgeRoomPressedImage.getImage()); // ¸¶¿ì½º¸¦ ´­·¶À» ¶§ ÀÌ¹ÌÁö º¯°æ(Pressed Image)
							
						}
					}
					// ¸¶¿ì½º·Î ¹öÆ°À» ´©¸¥ ÈÄ ¶¼¾úÀ» ¶§ ÀÌº¥Æ®
					@Override
					public void mouseReleased(MouseEvent e) {
						if(e.getButton()==1) {
							grp.setGRImage(gamgeRoomEnteredImage.getImage()); // ´©¸¥ ¹öÆ°ÀÌ ¶¼¾îÁ³À» ¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image) - ¸¶¿ì½º´Â ÀÌ¹Ì ÆĞ³Î¿¡ ¿Ã·Á³õ¿©Áø »óÅÂÀÌ±â ¶§¹®¿¡
							
						}
					}
				});
			}
			gameRoom[i] = grp;
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	
	// ¹æÀ» »ı¼ºÇÏ´Â ¸Ş¼Òµå
	private void createRoom(String title) {
		Font roomFont = new Font("Inconsolata",Font.BOLD,17); // ÆùÆ® ¼³Á¤
//		int roomNo = 0;
		// 1ºÎÅÍ »ı¼º°¡´ÉÇÑ ¹æÀÇ ÃÖ´ë °³¼ö¸¸Å­ÀÇ ¹üÀ§¿¡¼­ ·£´ıÇÏ°Ô ¹øÈ£¸¦ ÇÑ °³ ÇÒ´çÇÑ´Ù.(Áßº¹À» Çã¿ëÇÏÁö ¾Ê´Â´Ù.)
//		int roomNo = (int)(Math.random()*gameRoom.length) + 1;  // ÀÌ ±â´ÉÀ» Ãß°¡ÇÏ´Â°Ç ³ªÁß¿¡.. ÀÏ´Ü ¼øÂ÷ÀûÀ¸·Î ¹øÈ£ ÇÒ´çÇÏ´Â ¼öÁØ ºÎÅÍ ½ÃÀÛ.
		
		// °ÔÀÓ¹æ ¹øÈ£¸¦ ¼³Á¤ÇÒ JLabelÀ» ÇÒ´ç
		gameRoomNumber_lb[roomNo-1] = new JLabel();
		String number = "";
		// ¹æ¹øÈ£ ¼³Á¤. roomNo°ª¿¡ µû¶ó ÇØ´ç ¹æ¹øÈ£¸¦ ÁöÁ¤. 000 ÀÇ ÇüÅÂ·Î ¼³Á¤. 
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
		
		// °ÔÀÓ¹æ Á¦¸ñÀ» ¼³Á¤ÇÒ JLabelÀ» ÇÒ´ç
		gameRoomTitle_lb[roomNo-1] = new JLabel();
		gameRoomTitle_lb[roomNo-1].setText("No Title");
		gameRoomTitle_lb[roomNo-1].setFont(roomFont);
		gameRoomTitle_lb[roomNo-1].setBounds(90, 23, 200, 20);
		gameRoomTitle_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoom[roomNo-1].add(gameRoomTitle_lb[roomNo-1]);
		
		// °ÔÀÓ¹æ ÀÎ¿ø¼ö¸¦ ¼³Á¤ÇÒ JLabelÀ» ÇÒ´ç
		gameRoomPlayerCount_lb[roomNo-1] = new JLabel();
		gameRoomPlayerCount_lb[roomNo-1].setText("0 / 6");
		gameRoomPlayerCount_lb[roomNo-1].setFont(roomFont);
		gameRoomPlayerCount_lb[roomNo-1].setBounds(275, 68, 50, 20);
		gameRoomPlayerCount_lb[roomNo-1].setForeground(Color.DARK_GRAY);
		gameRoom[roomNo-1].add(gameRoomPlayerCount_lb[roomNo-1]);
		
		// ÀÌ·¸°Ô »ı¼ºÇÑ ÆĞ³ÎÀ» ºä¿¡ Ãß°¡ÇÑ´Ù.
		gameRoomView.add(gameRoom[roomNo-1]);
	}
	
	// ¹æÀÌ »ı¼ºµÉ ¶§¸¶´Ù ¹æ¹øÈ£¿¡ µû¶ó ¼ø¼­¸¦ Àç¹èÄ¡ÇÏ´Â ¸Ş¼Òµå
	public void relocationRoom() {
		
		
		
		// Àç¹èÄ¡µÈ ¹æÀ» ´Ù½Ã ºä¿¡ Àû¿ë
		for(int i=0; i<gameRoom.length; i++) {
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	// 50¸íÀÇ À¯Àú ¸®½ºÆ®¸¦ »ı¼ºÇÏ±â À§ÇÑ ¸Ş¼Òµå
	private void allocationUserInfo() {
		Font infoFont = new Font("Inconsolata",Font.BOLD,17); // ÆùÆ® ¼³Á¤
		for(int i=0; i<userList.length; i++) {
			// ³»ºÎ Å¬·¡½º GameRoomPanel Å¬·¡½º¸¦ ÀÌ¿ëÇØ¼­ User List PanelÀ» »ı¼º
			GameRoomPanel grp = new GameRoomPanel(userInfoPanelImage.getImage());
//			grp.addMouseListener( new MouseAdapter() {
//			});
			userList[i] = grp;
			
			// JPanel,JLabelÀ» ¼±¾ğ ¹× ÇÒ´ç
//			userList[i] = new JPanel();
//			userList[i].setSize(180,32);
			userID_lb[i] = new JLabel();
			// ÇöÀç Á¢¼ÓµÈ À¯ÀúÀÇ ¸®½ºÆ® ¸¸Å­ ÅØ½ºÆ®¸¦ ÁöÁ¤ÇØÁØ´Ù. (ÀúÀå°ªÀº À¯Àúid)
//			if(i<user_list.size()) {
//				UserInfo u = (UserInfo)user_list.get(i);
//				userID_lb[i].setText(u.getUserID());
//			} else {
//				userID_lb[i].setText("no userID");
//			}
			// userID_lb ¼ÂÆÃ
			userID_lb[i].setFont(infoFont);
//			System.out.println(userID_lb[i].getText());
			userID_lb[i].setBounds(40, 1, 160, 30);
			userID_lb[i].setForeground(Color.black);
			userID_lb[i].setLayout(null);
			userList[i].add(userID_lb[i]);
			userListView.add(userList[i]);
		}
//		add(userListView);
		revalidate();
		repaint();
	}
	
	private void updateUserInfo() {
		Font infoFont = new Font("Inconsolata",Font.BOLD,17); // ÆùÆ® ¼³Á¤
		for(int i=0; i<userList.length; i++) {
			// JPanel,JLabelÀ» ¼±¾ğ ¹× ÇÒ´ç
			// ÇöÀç Á¢¼ÓµÈ À¯ÀúÀÇ ¸®½ºÆ® ¸¸Å­ ÅØ½ºÆ®¸¦ ÁöÁ¤ÇØÁØ´Ù. (ÀúÀå°ªÀº À¯Àúid)
			if(i<user_list.size()) {
				UserInfo u = (UserInfo)user_list.get(i);
				userID_lb[i].setText(u.getUserID());
			} else {
//				userID_lb[i].setText("no userID");
			}
			// userID_lb ¼ÂÆÃ
			userID_lb[i].setFont(infoFont);
			System.out.println(userID_lb[i].getText());
			userList[i].add(userID_lb[i]);
			userListView.add(userList[i]);
		}
//		add(userListView);
		userListView.revalidate();
		userListView.repaint();
//		gameRoomView.revalidate();
//		gameRoomView.repaint();
	}
	
	
	// ÅØ½ºÆ® ÇÊµå ±ÛÀÚ ¼ö Á¦ÇÑÀ» À§ÇÑ Å¬·¡½º ¹× ¸Ş¼Òµå
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
	} // JTextFieldLimit class ³¡
	
	// Å° ÀÌº¥Æ®¸¦ ÁÖ±âÀ§ÇÑ Å¬·¡½º
	public class keyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// ¿£ÅÍ¸¦ ´©¸£¸é Àü¼ÛÀÌ µÇ°Ô ÇÏ±âÀ§ÇÑ ¸Ş¼Òµå
				String message = chatting_tf.getText();
				if(message.equals("")) { //¾Æ¹«°Íµµ ÀÔ·ÂÇÏÁö ¾Ê¾ÒÀ» ½Ã ¾Ë¸²Ã¢À» ¶ç¿ò
					JOptionPane.showMessageDialog(null, 
							"³»¿ëÀ» ÀÔ·ÂÇÏ½Ã±â ¹Ù¶ø´Ï´Ù.","¾Ë¸²",JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingWR/"+id+"/"+message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class ³¡
	
	
	/* ¾Æ·¡ paint() ¸Ş¼Òµå´Â GUI ApplicationÀÌ ½ÇÇàµÇ°Å³ª 
	 * È°¼º/ºñÈ°¼ºÀ¸·Î ÀÎÇÑ º¯µ¿ ¿µ¿ªÀ» °¨ÁöÇßÀ»¶§, ½ÇÇàµÇ´Â ¸Ş¼ÒµåÀÌ´Ù. */
	
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
	
	
	// ¹æ¸¸µé±â ÇÁ·¹ÀÓ
	class CreateRoom extends JFrame{
	// TextField
		private JTextField roomTitel_tf = new JTextField(); // ¹æ Á¦¸ñ
		private JTextField roomPw_tf = new JTextField();  // ¹æ ºñ¹Ğ¹øÈ£
	
	// ComboBox
		private String[] state = {"°ø°³","ºñ°ø°³"};
		private Integer[] player = {2,3,4,5,6}; // ÃÖ´ëÀÎ¿ø 6¸íÀ¸·Î ¼³Á¤
		private JComboBox<String> roomState_tf = new JComboBox<String>(state); // °ø°³/ºñ°ø°³ ¼³Á¤À» À§ÇÑ ÄŞº¸¹Ú½º
		private JComboBox<Integer> rPlayer_tf = new JComboBox<Integer>(player); // ÀÎ¿ø¼ö ¼³Á¤À» À§ÇÑ ÄŞº¸¹Ú½º
		
	// °¢Á¾ º¯¼ö º¯¼ö
		private Image viewImage; // ÀÌ¹ÌÁö ÀúÀå¿ë º¯¼ö
		private Graphics viewGraphics; // ±×·¡ÇÈ ÀúÀå¿ë º¯¼ö	
		private int mouseX; // ¸¶¿ì½º ÁÂÇ¥ º¯¼ö
		private int mouseY; // ¸¶¿ì½º ÁÂÇ¥ º¯¼ö
		
	// Image
		// # CreateRoom ¹è°æ
		private Image crbackgroundImage = 
				new ImageIcon(Main.class.getResource("/images/CreateRoom.png")).getImage();
		// #¹æ¸¸µé±â ÇÁ·¹ÀÓ ³»ºÎ ¹öÆ°¿ë ÀÌ¹ÌÁö
		private ImageIcon crCancelBasicImage = new ImageIcon(Main.class.getResource("/images/cancelButtonBasic.png"));
		private ImageIcon crCancelEnteredImage = new ImageIcon(Main.class.getResource("/images/cancelButtonEntered.png")); 
		private ImageIcon createBasicImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonBasic.png"));
		private ImageIcon createEnteredImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonEntered.png"));
		// Button
		private JButton cancelButton = new JButton(crCancelBasicImage); // Ãë¼Ò ¹öÆ°
		private JButton createButton = new JButton(createBasicImage); // ¹æ¸¸µé±â ¹öÆ°
		
		
		public CreateRoom() {
			Font font = new Font("Inconsolata",Font.PLAIN,11); 
			setUndecorated(true); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù Á¦°Å(À©µµ¿ì¸¦ Á¦°ÅÇÔ) - ±â´É ¿Ï¼º ÈÄ Ãß°¡ ¿¹Á¤
			setSize(360,213); // nullÀº ÃÖ´ñ°ª
			setBackground(new Color(0,0,0,0));
			setPreferredSize(new Dimension(crbackgroundImage.getWidth(null), crbackgroundImage.getHeight(null)));
			setResizable(false); // ÇÁ·¹ÀÓ Å©±â °íÁ¤
			setLocationRelativeTo(null); // À©µµ¿ì¸¦ È­¸é Á¤Áß¾Ó¿¡ ¶ç¿ì±â À§ÇÔ
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // À©µµ¿ì Á¾·á½Ã ³²¾ÆÀÖ´Â ÇÁ·Î¼¼½ºµµ ±ú²ıÇÏ°Ô Á¾·áÇÏ±â À§ÇÔ
			setVisible(true); // À©µµ¿ì¸¦ º¼ ¼ö ÀÖÀ½.
			setLayout(null);	
			
			// ¸¶¿ì½º·Î Ã¢À» ¿òÁ÷ÀÏ ¼ö ÀÖ´Ù.
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				// #¸Å´º¹Ù µå·¡±× ½Ã, ¿òÁ÷ÀÏ ¼ö ÀÖ°Ô ÇÑ´Ù.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
		
		// TextField / ComboBox
			// # Á¦¸ñ ÀÔ·Â
			roomTitel_tf.setBounds(88,47,244,20);
			roomTitel_tf.setFont(font);
			roomTitel_tf.setDocument(new JTextFieldLimit(20)); // Á¦¸ñ 20ÀÚ Á¦ÇÑ 	 
			add(roomTitel_tf);
			
			// # °ø°³/ºñ°ø°³ »óÅÂ ÄŞº¸¹Ú½º
			roomState_tf.setBounds(88,78,78,20);
			roomState_tf.setFont(font);
			add(roomState_tf);
			roomState_tf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String state = roomState_tf.getSelectedItem().toString();
					if(state.equals("°ø°³")) {
						roomPw_tf.setEnabled(false);
					} else if (state.equals("ºñ°ø°³")) {
						roomPw_tf.setEnabled(true);
					}
				}
			});
			
			// # ¹æ ºñ¹Ğ¹øÈ£ ÀÔ·Â(ÄŞº¸¹Ú½º ÀÌº¥Æ®¿¡ µû¶ó È°¼³/ºñÈ°¼º)
			roomPw_tf.setBounds(88,103,78,20);
			roomPw_tf.setFont(font);
			roomPw_tf.setDocument(new JTextFieldLimit(10)); // ºñ¹Ğ¹øÈ£ 10ÀÚ Á¦ÇÑ 	
			roomPw_tf.setEnabled(false); // ÃÊ±â¿¡ "°ø°³"¼³Á¤ÀÌ±â¶§¹®¿¡ ºñÈ°¼º »óÅÂ·Î µĞ´Ù.
			add(roomPw_tf);
			
			// # ÀÎ¿ø¼ö ¼³Á¤ ÄŞº¸¹Ú½º
			rPlayer_tf.setBounds(88,128,78,20);
			roomPw_tf.setFont(font);
			add(rPlayer_tf);
			
			
		// Button
			// #¯M¼Ò ¹öÆ°
			cancelButton.setBounds(187, 180, 72, 24);
			add(cancelButton);
			cancelButton.addMouseListener(new MouseAdapter() {
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
				@Override
				public void mouseEntered(MouseEvent e) {
					cancelButton.setIcon(crCancelEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
					cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
				}
				
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override  
				public void mouseExited(MouseEvent e) {
					cancelButton.setIcon(crCancelBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
					cancelButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						dispose(); 
					}
				}
			});
			
			// #¸¸µé±â ¹öÆ°
			createButton.setBounds(101, 180, 72, 24);
			add(createButton);
			createButton.addMouseListener(new MouseAdapter() {
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
				@Override
				public void mouseEntered(MouseEvent e) {
					createButton.setIcon(createEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
					createButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
				}
				
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override  
				public void mouseExited(MouseEvent e) {
					createButton.setIcon(createBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
					createButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
				}
				// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						// ¸¸µé±â ¹öÆ°À» ´©¸£¸é ¼³Á¤»çÇ×À» ±×´ë·Î ¼­¹ö¿¡ Àü¼ÛÇÑ´Ù.
						title = roomTitel_tf.getText().trim(); // ¹æ Á¦¸ñ
						String state = roomState_tf.getSelectedItem().toString(); // °ø°³/ºñ°ø°³
						roomPW = null; // ¹æ ºñ¹Ğ¹øÈ£ (°ø°³¸é null·Î, ºñ°ø°³¸é ÀÔ·Â¹ŞÀº ÆĞ½º¿öµå¸¦ ÀúÀå)
						if(state.equals("ºñ°ø°³")) {
							roomPW = roomPw_tf.getText().trim();
						} 
						uCount = Integer.parseInt(rPlayer_tf.getSelectedItem().toString()); // rPlayer_tfÀÇ Á¦³×¸¯À» Integer·Î ÇØ³õÀ½
						
						// ÀÔ·Â¹ŞÀº °ªÀ» ¼­¹ö¿¡ Àü¼ÛÇÑ´Ù.
						send_message("CreateRoom/"+id+"/"+title+"/"+state+"/"+roomPW+"/"+uCount);
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
	
	// °ÔÀÓ¹æ&À¯Àúinfo ÇÏ³ªÇÏ³ª¸¦ JPanelÀ» »ó¼Ó¹ŞÀº GameRoomPanel Å¬·¡½º·Î »ı¼ºÇÑ´Ù.
	class GameRoomPanel extends JPanel{
		private Image img;
		
		public GameRoomPanel(Image img) {
			this.img = img;
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // nullÀº ÃÖ´ñ°ª
			setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
			setLayout(null); // ÆĞ³Î¿¡ Ãß°¡ÇÏ´Â ¿ä¼ÒµéÀÇ À§Ä¡¸¦ ÀÚÀ¯·Ó°Ô ¼³Á¤ÇÏ±â À§ÇØ LayoutÀ» null·Î ÇØÁØ´Ù.
		}
		
		// ÆĞ³ÎÀ» ¿­¾úÀ» ¶§ ÀÚµ¿À¸·Î ÀÌ¹ÌÁö¸¦ ±×·ÁÁÖ´Â ¸Ş¼Òµå
		public void paintComponent(Graphics g)  {
			g.drawImage(img, 0, 0, null);
		}
		
		// ÀÌ¹ÌÁö¸¦ ¹Ù²ãÁÖ±â À§ÇÑ ¸Ş¼Òµå
		public void setGRImage(Image img) {
			this.img = img;
		}
	}
}
