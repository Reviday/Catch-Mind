package kh.mini.project.paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kh.mini.project.paint.StopWatch.timeThread;

import java.awt.BorderLayout;

public class StopWatch extends JFrame{

	private static long currentTime=0l, preTime=0l;
	private int minute;
	private int sec;
	private int mSec;
	
	public StopWatch() {
		timeThread t = new timeThread();
		StopWatch.preTime = System.currentTimeMillis();
		t.start();
		
		setBounds(100,100,300,200);
		//setLayout
		
		setDefaultCloseOperation(3);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);
	}

	class timeThread extends Thread{
		@Override
		public void run() {
			int i=0;
			
			try {
				sleep(1000);
				
				while(true) {
				currentTime=(System.currentTimeMillis() - preTime);	
				i++;
				mSec=(int)currentTime%1000;
				sec=(int)currentTime /1000 %60;
				minute=(int)currentTime /60000 %60;
				
				printTime(minute, sec, mSec);
				//System.out.println(minute + " : " + sec + "." + (mSec/10));

				if(i==10000000)
					break;
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}finally {
				
			}
				
			
			
		}
		
	}

	public void printTime(int minute, int sec, int mSec) {
		
	}
	
	public static void main(String[] args) {
		new StopWatch();
	}

}



	