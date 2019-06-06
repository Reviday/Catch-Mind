package kh.mini.project.model.vo;

import java.io.Serializable;
import java.util.Calendar;

public class User implements Serializable{
// ȸ������ �Է�����
	private String id;
	private String pw;
	private String name;
	private String dateOfBirth; // �������(990101)
	private String eMail;
	private int age; 			// dateOfBirth�� ����Ͽ� ����
	private char gender;

// User ����
	private int level; 		// ����
	private int exp; 		// Experience ����ġ
	private int corAnswer;	// Cumulative number of correct answers (���� ���� ����)
	
	public User() {	}
	
	//ȸ�� ���Կ� �Ű����� ������
	public User(String id, String pw, String name, String dateOfBirth, String eMail, char gender) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.eMail = eMail;
		this.gender = gender;
		age = calcAge(dateOfBirth); // �ѱ����̰��
		//ȸ������ �����̹Ƿ� ������ �������� �ʱⰪ����
		level = 0;
		exp = 0;
		corAnswer = 0;
	}

	//���� ���� ������ �Ű����� ������
	public User(String id, String pw, String name, String dateOfBirth, String eMail, int age, char gender, int level,
			int exp, int corAnswer) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.eMail = eMail;
		this.age = age;
		this.gender = gender;
		this.level = level;
		this.exp = exp;
		this.corAnswer = corAnswer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCorAnswer() {
		return corAnswer;
	}

	public void setCorAnswer(int corAnswer) {
		this.corAnswer = corAnswer;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", pw=" + pw + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", eMail=" + eMail
				+ ", age=" + age + ", gender=" + gender + ", level=" + level + ", exp=" + exp + ", corAnswer="
				+ corAnswer + "]";
	}
	
	// �Է¹��� ��������� ���� �ð��� �������� �ѱ����̸� ����ϴ� �޼ҵ�
	public int calcAge(String dateOfBirth) {
		Calendar todayCal = Calendar.getInstance();
		Calendar userCal = Calendar.getInstance();
		int temp = Integer.parseInt(dateOfBirth);
		int birthDay = temp%100; temp /= 100;
		int birthMonth = temp%100; temp /=100;
		if(temp > 50) { // 50���� ������ 2050���� �� �� �����Ƿ�  1900���� 
			temp += 1900;
		} else { // 50���� ������ 1950�⺸�� ���� �� �����Ƿ� 2000���� 
			temp += 2000;
		}
		userCal.set(temp, birthMonth, birthDay);
		
		long tempCal = (todayCal.getTimeInMillis() - userCal.getTimeInMillis())/1000/24/60/60;
		
		return (int)tempCal/365+1; // �ѱ����̰��
	}
	

}
