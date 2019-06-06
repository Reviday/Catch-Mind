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
	private JFrame joinView = new JFrame("Join"); // ���� ������
	private JPanel joinPanel = new JPanel(); // ���� ������(�г�) - ȸ������ �г�
	
// Label
	private JLabel id_lb = new JLabel("ID : "); // �ӽ� id ���̺�
	private JLabel pw_lb = new JLabel("PW : "); // �ӽ�  pw ���̺�
	private JLabel name_lb = new JLabel("�̸� : "); // �ӽ�  ip ���̺�
	private JLabel dateOfBirth_lb = new JLabel("������� : "); // �ӽ� ��Ʈ ��ȣ ���̺�
	private JLabel eMail_lb = new JLabel("e-Mail : "); // �ӽ�  ip ���̺�
	private JLabel gender_lb = new JLabel("���� : "); // �ӽ� ��Ʈ ��ȣ ���̺�
	
// TextField
	private JTextField id_tf; // ID�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	private JPasswordField pw_tf; // PW�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	private JTextField name_tf; // �̸��� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField dateOfBirth_tf; // ��������� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField eMail_tf; // �̸����� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	
// Network �ڿ� ����
	private Socket socket;// ����� ����
	private int port; // ��Ʈ��ȣ
	
// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����	
	
// ȸ������ �Է�����
	private String id="";
	private String pw="";
	private String name="";
	private String dateOfBirth=""; // �������(19990101)
	private String eMail="";
	private int age; // dateOfBirth�� ����Ͽ� ����
	private char gender;

	
//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)

//Button
	private JButton exitButton = new JButton("������"); // ������ ��ư
	private JButton joinButton = new JButton("�����ϱ�"); // ���� ��ư	
	
	
	
	public JoinView() {
		//����� ���ÿ� socket,port,ID�� MainView�κ��� �̾�޾ƿ´�.
		socket = MainView.getSocket();
		port = MainView.getPort();
		
		Font font = new Font("Inconsolata",Font.BOLD,15);
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
		
	// Label	
		// ID (�ӽ�-���߿� ���� ����)
		id_lb.setBounds(40, 30, 100, 30);
		id_lb.setFont(font);
		add(id_lb);
		
		// PW (�ӽ�-���߿� ���� ����)
		pw_lb.setBounds(40, 80, 100, 30);
		pw_lb.setFont(font);
		add(pw_lb);
		
		// �̸�
		name_lb.setBounds(40, 130, 100, 30);
		name_lb.setFont(font);
		add(name_lb);
		
		// �������
		dateOfBirth_lb.setBounds(40, 180, 100, 30);
		dateOfBirth_lb.setFont(font);
		add(dateOfBirth_lb);
		
		// e-Mail
		eMail_lb.setBounds(40, 230, 100, 30);
		eMail_lb.setFont(font);
		add(eMail_lb);
		
		// ����
		gender_lb.setBounds(40, 280, 100, 30);
		gender_lb.setFont(font);
		add(gender_lb);
				
	// TextField
		//ID �Է�
		id_tf = new JTextField();
		id_tf.setBounds(140, 30, 150, 30);
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //���̵� �ִ� 12�� ����
		
		//PW �Է�
		pw_tf = new JPasswordField();
		pw_tf.setBounds(140, 80, 150, 30);
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // ��й�ȣ �ִ� 12�� ����
		
		//�̸� �Է�
		name_tf = new JTextField();
		name_tf.setBounds(140, 130, 150, 30);
		add(name_tf);
		name_tf.setDocument(new JTextFieldLimit(5)); //�̸� �ִ� 5�� ����
		
		//������� �Է�
		dateOfBirth_tf = new JTextField();
		dateOfBirth_tf.setBounds(140, 180, 150, 30);
		add(dateOfBirth_tf);
		dateOfBirth_tf.setDocument(new JTextFieldLimit(6)); //������� �ִ� 6�� ����
		
		//e-Mail �Է�
		eMail_tf = new JTextField();
		eMail_tf.setBounds(140, 230, 150, 30);
		add(eMail_tf);
		eMail_tf.setDocument(new JTextFieldLimit(30)); //�̸��� �ִ� 30�� ����
	
	// RadioButton
		//���� ����
		JRadioButton  genderMale = new JRadioButton("��"); // JRadioButton ����
		genderMale.setFont(font); 
		JRadioButton  genderFemale = new JRadioButton("��"); 
		genderFemale.setFont(font);
		ButtonGroup  genderGroup = new ButtonGroup(); //������ư �׷�ȭ�� ���� ��ư�׷� ����. ���� �׷쳢���� �׷��߿� 1���� ���õȴ�.
		genderGroup.add(genderMale);  
		genderGroup.add(genderFemale); //�׷쿡 �׷�ȭ��ų ��ư���� �߰�	
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
				// ȸ�� ���� �Է� ���� �����鿡 ����
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
				//�Է� ������ �������� User ��ü�� ����
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
	
	public static void main(String[] args) {
		new JoinView();
	}
}
