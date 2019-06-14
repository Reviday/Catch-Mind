package kh.mini.project.model.vo;
//Server와는 다르게 서버로부터 받은 유저들의 정보를 임시적으로 저장하기 위한 클래스(정보 저장용 클래스)
public class UserInfo {
	/* 해당 클래스는 서버를 제외한 클라이언트에서 사용하는 용도로
	 * 서버가 가지고 있는 정보들을 받아와 필요한 정보만 사용하기 위한 클래스이다.
	 * 게임을 진행하면서 업데이트 되는 부분과 레벨구간마다 경험치 양 등을 지정하기 위한 클래스이기도 하다.
	 */
	
	private String userID; // 사용자의 ID 
	private int level; // 레벨
	private int exp; // 경험치
	private int corAnswer; // 누적 정답 개수
	
	public UserInfo(String userID, int level, int exp, int corAnswer) {
		super();
		this.userID = userID;
		this.level = level;
		this.exp = exp;
		this.corAnswer = corAnswer;
	}

	public String getUserID() {
		return userID;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp += exp;
	}

	public int getCorAnswer() {
		return corAnswer;
	}

	public void setCorAnswer(int corAnswer) {
		this.corAnswer += corAnswer;
	}
	
	// 일정 경험치에 도달하여 레벨업을 할 시 동작을 위한 메소드
	public void levelUp() {
		
	}

	@Override
	public String toString() {
		return "UserInfo [userID=" + userID + ", level=" + level + ", exp=" + exp + ", corAnswer=" + corAnswer + "]";
	}
	
	
}
