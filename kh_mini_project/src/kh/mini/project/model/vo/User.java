package kh.mini.project.model.vo;

import java.io.Serializable;
import java.util.Calendar;

public class User implements Serializable{
// 회원가입 입력정보
	private String id;
	private String pw;
	private String name;
	private String dateOfBirth; // 생년월일(990101)
	private String eMail;
	private int age; 			// dateOfBirth로 계산하여 저장
	private char gender;

// User 정보
	private int level; 		// 레벨
	private int exp; 		// Experience 경험치
	private int corAnswer;	// Cumulative number of correct answers (누적 정답 개수)
	
	public User() {	}
	
	//회원 가입용 매개변수 생성자
	public User(String id, String pw, String name, String dateOfBirth, String eMail, char gender) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.eMail = eMail;
		this.gender = gender;
		age = calcAge(dateOfBirth); // 한국나이계산
		//회원가입 상태이므로 나머지 설정값은 초기값으로
		level = 0;
		exp = 0;
		corAnswer = 0;
	}

	//유저 정보 관리용 매개변수 생성자
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
	
	// 입력받은 생년월일을 현재 시간을 기준으로 한국나이를 계산하는 메소드
	public int calcAge(String dateOfBirth) {
		Calendar todayCal = Calendar.getInstance();
		Calendar userCal = Calendar.getInstance();
		int temp = Integer.parseInt(dateOfBirth);
		int birthDay = temp%100; temp /= 100;
		int birthMonth = temp%100; temp /=100;
		if(temp > 50) { // 50보다 높으면 2050년이 될 수 없으므로  1900년대로 
			temp += 1900;
		} else { // 50보다 작으면 1950년보다 적을 수 없으므로 2000년대로 
			temp += 2000;
		}
		userCal.set(temp, birthMonth, birthDay);
		
		long tempCal = (todayCal.getTimeInMillis() - userCal.getTimeInMillis())/1000/24/60/60;
		
		return (int)tempCal/365+1; // 한국나이계산
	}
	

}
