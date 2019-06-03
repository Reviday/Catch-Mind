package kh.mini.project.main.SERVER;
// Github Ǫ���Ҷ� �ڵ����� �빮�ڷ� �ٲ� �׳� �빮�ڷ� ����..
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kh.mini.project.main.view.Main;

public class MainServer extends JFrame {
	private static final long serialVersionUID = 1216070372320522836L;
// Frame, Panel
	private JFrame mainView = new JFrame("Server"); // ���� ������
	private JLabel mainMenuBar = new JLabel();
	private JScrollPane statusView = new JScrollPane(); // ��Ʈ�� ���� ���� ����â
	private JTextArea statusArea = new JTextArea();
	
	
// Textfield	
	private JTextField port_tf; // ��Ʈ��ȣ�� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	
// Network �ڿ�
	private ServerSocket server_socket; // ���� ����
	private Socket socket; // ����ڷκ��� ���� ���� 
	private int port; // ��Ʈ��ȣ
	
// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����
	
//Image	
	// #MainView ���
	private Image mainBackgroundImage = 
			new ImageIcon(Main.class.getResource("/images/testGIF.gif")).getImage();
	
//Button
	
	MainServer() {
		new intro();
	}
	
	

	
	
	
	private void main_View() { // ���� ���� ȭ��
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
		setTitle("Server"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main���� ������Ų ȭ�� �ػ󵵸� ���
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null);
	}
	
	
	
	
	
	public static void main(String[] args) {
		new MainServer();
	}

	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(mainBackgroundImage, 0, 0, null);
		this.paintComponents(g);
		this.repaint();
	}
	
	
	
	//��Ʈ�� ȭ��� ���� Ŭ����
	class intro extends JFrame implements ActionListener{
	// Frame, Panel
		private JFrame introView = new JFrame("Server"); // ��Ʈ�� ������ (��Ʈ��ȣ�� �Է��Ͽ� ������ �����ϸ� ���� ������ â�� ��� �Ѿ�� ����)
		private JLabel port_lb = new JLabel("��Ʈ ��ȣ : ");
		private JLabel introMenuBar = new JLabel();
		
	// ���� ����	
		private Image viewImage; // �̹��� ����� ����
		private Graphics viewGraphics; // �׷��� ����� ����
		private int mouseX, mouseY; // ���콺 ��ǥ�� ����
		private boolean state = true; // ���� �ߺ� ���� ������
		
	//Image	
		// #MainView ���	
		private Image introBackgroundImage = 
				new ImageIcon(Main.class.getResource("/images/gifTest.gif")).getImage();
		// #��ư �̹���
		private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("/images/exit_entered.png"));
		private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("/images/exit_basic.png"));
		
	//Button
		private JButton start_btn = new JButton("���� ����"); // ���� ���� ��ư
		private JButton stop_btn = new JButton("���� ����"); // ���� ���� ��ư
		private JButton exitButton = new JButton(exitButtonBasicImage); // ������ ��ư
		
		
		intro() {
			start();
			setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
			setTitle("Server"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
			setSize(280, 450); // Main���� ������Ų ȭ�� �ػ󵵸� ���
			setResizable(false); // ������ ũ�� ����
			setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
			setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
			setVisible(true); // �����츦 �� �� ����.
			setLayout(null);
			
		// ScrollPane
			statusView.setBounds(10, 20, 260,330 );
			statusView.setViewportView(statusArea);
			add(statusView);
			
		// label
			// #��Ʈ��ȣ
			port_lb.setBounds(10, 360, 100, 30);
			add(port_lb);
			
		// Button
			// #���� ��ư 
			start_btn.setBounds(10, 400, 100, 30);
			add(start_btn);
			start_btn.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
//					start_btn.setIcon(); // ���콺�� �÷������� �̹��� ����(Entered Image)
					start_btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}
					
				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override  
				public void mouseExited(MouseEvent e) {
//					start_btn.setIcon(); // ���콺�� �������� �̹��� ����(Basic Image)
					start_btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}
				
				// ���콺�� ��ư�� ������ �� �̺�Ʈ
				@Override 
				public void mousePressed(MouseEvent e) {
//					main_View();
				}
			});
		
			
			// #���� ��ư 
			stop_btn.setBounds(170, 400, 100, 30);
			add(stop_btn);
			stop_btn.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
//					stop_btn.setIcon(); // ���콺�� �÷������� �̹��� ����(Entered Image)
					stop_btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}
					
				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override  
				public void mouseExited(MouseEvent e) {
//					stop_btn.setIcon(); // ���콺�� �������� �̹��� ����(Basic Image)
					stop_btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}
							
				// ���콺�� ��ư�� ������ �� �̺�Ʈ
				@Override 
				public void mousePressed(MouseEvent e) {
					
				}
			});
			
			// #������ ��ư
			exitButton.setBounds(125, 400, 30, 30);
			exitButton.setBorderPainted(false);
			exitButton.setContentAreaFilled(false);
			exitButton.setFocusPainted(false);
			exitButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					exitButton.setIcon(exitButtonEnteredImage);
					exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
					//���콺�� �÷��� �� �ո������ ��Ÿ����
				
				}
				@Override
				public void mouseExited(MouseEvent e) {
					exitButton.setIcon(exitButtonBasicImage);
					exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					//���콺�� ���� ���� ���·�
				}
				@Override
				public void mousePressed(MouseEvent e) {
					try {
						Thread.sleep(500); // 0.5�ʵ� ����
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					System.exit(0);
				}
			});
			add(exitButton);
			
		// TextField
			//��Ʈ��ȣ �Է�
			port_tf = new JTextField();
			port_tf.setBounds(100, 360, 170, 30);
			add(port_tf);
			port_tf.setColumns(10);		
			
			// �޴���
			introMenuBar.setBounds(0, 0, 280, 30);
			introMenuBar.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			introMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
				// #�Ŵ��� �巡�� ��, ������ �� �ְ� �Ѵ�.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
			add(introMenuBar);	
			
		}
		
		private void start()
		{
			start_btn.addActionListener(this); // ActionListener�� ��ӹ޾ұ� ������ this��� ���ָ�ȴ�.
			stop_btn.addActionListener(this);
		}
		
		
		void Server_start() // // MainServer���� ����� �� �ֵ��� default�� ����
		{ 		
			try {
				server_socket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			if(server_socket != null) //���������� ��Ʈ�� ������ ���
			{
				Connection();
			}
		}
		
		void Connection() // MainServer���� ����� �� �ֵ��� default�� ����
		{	
			// 1������ �����忡���� 1������ �ϸ� ó���� �� �ִ�.
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() { // �����忡�� ó���� ���� �����Ѵ�.
					
					while(true) {
						try { // ���������� try-catch
							statusArea.append("����� ���� �����(���� ��Ʈ��ȣ : "+port+")\n");
							socket = server_socket.accept(); // ����� ���� ���� ��� => ������ �ٸ� ��ɵ��� �������� �ʰ� �׾������. => ��Ƽ ������� �ذ�!
							statusArea.append("����� ����!!\n");
							
							//����ڰ� accept�� ������ �ϰԵǸ� UserInfo ��ü�� �����ϰ� �ȴ�.
							//UserInfo �����ڴ�  �Ű������� �Ѿ�� socket�� �޾��ְ� UserNetwork()�� �����Ͽ� ���ἳ���� �ϰԵȴ�.
//							UserInfo user = new UserInfo(socket);
							
//							user.start(); // ��ü�� �����带 ���� ����
							
						} catch (IOException e) {
						} 
						
					} // while�� ��
					
				}
			});
			
			th.start();
		}

		
		
		@Override
		public void paint(Graphics g) {
			viewImage = createImage(280, 450);
			viewGraphics = viewImage.getGraphics();
			screenDraw(viewGraphics);
			g.drawImage(viewImage,0,0, null);
		}
		
		public void screenDraw(Graphics g) {
			g.drawImage(introBackgroundImage, 0, 0, null);
			this.paintComponents(g);
			this.repaint();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == start_btn)
			{ 
				if(state) {
					System.out.println("��ŸƮ ��ư Ŭ��");
						try {
							port = Integer.parseInt(port_tf.getText().trim());
						} catch(NumberFormatException ee) {
							statusArea.append("�߸��� ��Ʈ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.\n");
						} catch(IllegalArgumentException ee) {
							statusArea.append("�߸��� ��Ʈ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.\n");
						}
						Server_start(); // ���� ���� �� ����� ���� ���
				} else {
					statusArea.append("������ �̹� ������ �Դϴ�.\n");
				}
				
				state = false; // �ߺ� ���� ������ ���� false�� ����
				
			}
			else if(e.getSource() == stop_btn)
			{
				System.out.println("���� ��ž ��ư Ŭ��");
				
			}
		}// Action �̺�Ʈ ��	
			
		
	}
}
