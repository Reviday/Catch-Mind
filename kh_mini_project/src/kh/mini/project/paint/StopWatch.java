package kh.mini.project.paint;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class StopWatch extends JFrame implements ActionListener{

	private static long currentTime=0l, preTime=0l;
	private int minute=0;
	private int sec=0;
	private int mSec=0;
	private boolean stop = false;
	private JButton startBt;
	private JButton stopBt;
	private JLabel minuteLb;
	private JLabel secLb;
	private JLabel mSecLb;
	
	timeThread timeT;
	
	public StopWatch() {		
		setBounds(100,100,400,200);
		getContentPane().setLayout(null);
		
		setDefaultCloseOperation(3);
		
		JPanel panel = new JPanel();
		panel.setLocation(22, 10);
		panel.setBackground(Color.WHITE);
		panel.setSize(335,100);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		minuteLb = new JLabel(String.valueOf(minute));
		minuteLb.setBounds(30, 10, 71, 80);
		minuteLb.setFont(new Font("Times", Font.BOLD, 50));
		panel.add(minuteLb);
		minuteLb.setVisible(true);
		
		secLb = new JLabel(String.valueOf(sec));
		secLb.setBounds(143, 10, 71, 80);
		panel.add(secLb);
		secLb.setFont(new Font("Times", Font.BOLD, 50));
		secLb.setVisible(true);
		
		mSecLb = new JLabel(String.valueOf(mSec));
		mSecLb.setBounds(252, 33, 50, 44);
		mSecLb.setFont(new Font("Times", Font.BOLD, 30));
		panel.add(mSecLb);
		
		JLabel label = new JLabel(":");
		label.setBounds(65, 10, 71, 80);
		panel.add(label);
		label.setIcon(new ImageIcon(StopWatch.class.getResource("/images/middle.png")));
		//label.setFont(new Font("Dialog", Font.BOLD, 30));
		
		JLabel label_1 = new JLabel(".");
		label_1.setBounds(185, 0, 71, 80);
		panel.add(label_1);
		label_1.setIcon(new ImageIcon(StopWatch.class.getResource("/images/end.png")));
		//label_1.setFont(new Font("Dialog", Font.BOLD, 30));
		mSecLb.setVisible(true);
		
		
		startBt = new JButton("START");
		startBt.setBounds(70, 120, 103, 32);
		getContentPane().add(startBt);
		startBt.setVisible(true);
		startBt.addActionListener(this);
		
		stopBt = new JButton("STOP");
		stopBt.setBounds(209, 120, 103, 32);
		getContentPane().add(stopBt);
		stopBt.setVisible(true);
		stopBt.addActionListener(this);
		
		setVisible(true);
	}

	class timeThread extends Thread{
		@Override
		public void run() {
			try{	
				while(!Thread.currentThread().isInterrupted()) {
					currentTime=System.currentTimeMillis() - preTime;	
					
					printTime(currentTime);
				}
				
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
			
	}
	
	
	public void printTime(long currentTime){
		
		mSec=(int)currentTime%1000/10;
		sec=(int)currentTime /1000 %60;
		minute=(int)currentTime /60000 %60;
		
		
		minuteLb.setText(String.valueOf(minute));
		secLb.setText(String.valueOf(sec));
		mSecLb.setText(String.valueOf(mSec));
		
	}
	
	

	
	public static void main(String[] args) {
		new StopWatch();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startBt) {
			
			preTime=System.currentTimeMillis();
			
			timeT = new timeThread();
			timeT.start();
			
			
	
		}

		else if(e.getSource()==stopBt) {
			if(timeT.isAlive()){
				timeT.interrupt();
				currentTime=0l;
				printTime(currentTime);
			}
		}
		
	}
}

	



	