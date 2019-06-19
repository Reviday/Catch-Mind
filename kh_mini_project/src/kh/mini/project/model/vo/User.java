package kh.mini.project.model.vo;

import java.io.Serializable;
import java.util.Calendar;

public class User implements Serializable{
	/* �������� ������ �����ϱ� ���� Ŭ������,
	 * ���������� �����Ѵ�.
	 */
	
	private static final long serialVersionUID = 6644685954737096019L;
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
	private int maxExp;  	// ���� ������ �ִ� ����ġ��
	private int corAnswer;	// Cumulative number of correct answers (���� ���� ����)
	
	private boolean loginState = false; // �α��� ����
	
	static {
		
	}
	
	
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
		level = 1;
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
	
	public boolean isLoginState() {
		return loginState;
	}

	public void setLoginState(boolean loginState) {
		this.loginState = loginState;
	}

	
	// ������ �Ǵ� �����ڿ��� ����Ǵ� �޼ҵ�. ����ġ�� �������� ������ ������Ų��.
	public boolean expUpdate(boolean descriptor) {
		// ����ġ�� 10 ���� ��Ű��, ���� ���䰳���� 1 ������Ų��.
		System.out.println(id+"��, ó�� �� ���� : " + level + ", ����ġ : " + exp +", ���� ����� :" + corAnswer );
		exp += 10; 
		if(!descriptor) { // ���� �����ڰ� �ƴ� �������� ��� ���� ������� �ø���.
			corAnswer++;
		}
		System.out.println(id+"��, ó�� �� ���� : " + level + ", ����ġ : " + exp +", ���� ����� :" + corAnswer );
		return levelUpCheck();
	}
	 
	/* ����ġ ������ ���� ������ Ȯ���� ���� �޼ҵ�
	 * ����ġ�� 10���θ� �־����� ������, 
	 * ���� ������ �ִ����ġ���� �����Ͽ� �������� �� ��
	 * �ܿ� ����ġ�� ���� �ʱ� ������ ������� �ʾƵ� �ȴ�.
	 */
	public boolean levelUpCheck() {
		boolean levelUp = false; // ������ üũ�� ���� �Ҹ���
		
		switch (level) {
		case 1:
			maxExp = 10;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
				System.out.println(id+ " ������!!");
			}
			break;
		case 2:
			maxExp = 40;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 3:
			maxExp = 80;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 4:
			maxExp = 130;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 5:
			maxExp = 200;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 6:
			maxExp = 290;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 7:
			maxExp = 400;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 8:
			maxExp = 540;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 9:
			maxExp = 710;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 10:
			maxExp = 910;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 11:
			maxExp = 1150;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 12:
			maxExp = 1430;
			if (exp >= maxExp) { // ���� ����ġ�� �ִ� ����ġ���� �����Ͽ��� ��
				level++; // ������
				exp = 0; // ����ġ�� 0���� �ʱ�ȭ
				levelUp = true;
			}
			break;
		case 13:
			//MaxLevel �̹Ƿ� ������ ó���� �����ʴ´�.
			break;
		}
		
		return levelUp;
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
