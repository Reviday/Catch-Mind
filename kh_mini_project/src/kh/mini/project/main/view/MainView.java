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

public class MainView extends JFrame{
// Frame, Panel
	JFrame mainView = new JFrame("CatchMind"); // ���� ������
	JPanel loginView = new JPanel(); // ���� ������(�г�) - �α��� �г�

// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����
	
//Image	
	//MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/test.png")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)
	
	//Button Icon (basic : ��ư�� �⺻ ����, Entered : ��ư�� ���콺�� ������ ����) 
	// => ��ư �⺻����, ���콺�� �÷����� �� ����, ������ �� ���� 3���� ����?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png")); 
	private ImageIcon loginBasicImage = new ImageIcon(Main.class.getResource("/images/login.png"));
	private ImageIcon loginEnteredImage = new ImageIcon(Main.class.getResource("/images/login.png")); 
	private ImageIcon joinBasicImage = new ImageIcon(Main.class.getResource("/images/����.png"));
	private ImageIcon joinEnteredImage = new ImageIcon(Main.class.getResource("/images/����.png")); 
	
//Label
	
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư
	private JButton loginButton = new JButton(loginBasicImage); // �α��� ��ư
	private JButton joinButton = new JButton(joinBasicImage); // ȸ������ ��ư
	
	
	MainView() {
	// JFrame mainView
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
		setTitle("Catch Mind"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main���� ������Ų ȭ�� �ػ󵵸� ���
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null);
	
	// Label
		
		
	// Button
		// ������ ��ư
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
				dispose(); // �ϳ��� �����Ӹ� �����ϱ� ���� �޼ҵ�
			}
		});
		
		// �α��� ��ư
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
				dispose(); // MainView�� �����ϰ� 
				new WatingRoom(); // WatingRoom�� �����Ѵ�. 
			}
			
		});
		
		
		// ȸ������ ��ư
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
				
			}
			
		});
		
	// JPanel loginView => ���ο��� ���� ��(�ʺ�,����)�� ���� �ڵ����� ��ġ�� �������� �������.
		loginView.setBounds(341, 460, 341, 256);
		add(loginView);
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
