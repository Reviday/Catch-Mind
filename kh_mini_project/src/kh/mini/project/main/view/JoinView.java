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
	JFrame joinView = new JFrame("Join"); // ���� ������
	JPanel joinPanel = new JPanel(); // ���� ������(�г�) - ȸ������ �г�
	
// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����	
	
	/* ȸ���������� �޾Ƶ��� ������ ���� ������. */
	
//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)

//Button
	private JButton exitButton = new JButton("������"); // ������ ��ư
	private JButton joinButton = new JButton("�����ϱ�"); // ���� ��ư	
	
	
	
	public JoinView() {
	// JFrame mainView
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
		setTitle("Catch Mind"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(341, 384); // Main���� ������Ų ȭ�� �ػ󵵸� ���
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null);		
		
	// Button
		// #������ ��ư
		exitButton.setBounds(170, 320, 150, 50);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
//				exitButton.setIcon(); // ���콺�� �÷������� �̹��� ����(Entered Image)
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
//				exitButton.setIcon(); // ���콺�� �������� �̹��� ����(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				dispose(); // �ϳ��� �����Ӹ� �����ϱ� ���� �޼ҵ�
			}
		});
		
		// #���� ��ư
		joinButton.setBounds(20, 320, 150, 50);
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
//				joinButton.setIcon(); // ���콺�� �÷������� �̹��� ����(Entered Image)
				joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
//				joinButton.setIcon(); // ���콺�� �������� �̹��� ����(Basic Image)
				joinButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				/* �Է��� ������ �����ϰ�, 
				 * ȸ������ ó�� �Ϸ��ϰ� �� 
				 */
			}
			
		});
		
		
	// JPanel loginView
		joinPanel.setBounds(20, 20, 300, 300);
		add(joinPanel);
	}
	
	
	
	/* �Ʒ� paint() �޼ҵ�� GUI Application�� ����ǰų� 
	 * Ȱ��/��Ȱ������ ���� ���� ������ ����������, ����Ǵ� �޼ҵ��̴�. */
	
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
