package kh.mini.project.model.vo;
//Server�ʹ� �ٸ��� �����κ��� ���� �������� ������ �ӽ������� �����ϱ� ���� Ŭ����(���� ����� Ŭ����)
public class UserInfo {
	/* �ش� Ŭ������ ������ ������ Ŭ���̾�Ʈ���� ����ϴ� �뵵��
	 * ������ ������ �ִ� �������� �޾ƿ� �ʿ��� ������ ����ϱ� ���� Ŭ�����̴�.
	 * ������ �����ϸ鼭 ������Ʈ �Ǵ� �κа� ������������ ����ġ �� ���� �����ϱ� ���� Ŭ�����̱⵵ �ϴ�.
	 */
	
	private String userID; // ������� ID 
	private int level; // ����
	private int exp; // ����ġ
	private int corAnswer; // ���� ���� ����
	
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
	
	// ���� ����ġ�� �����Ͽ� �������� �� �� ������ ���� �޼ҵ�
	public void levelUp() {
		
	}

	@Override
	public String toString() {
		return "UserInfo [userID=" + userID + ", level=" + level + ", exp=" + exp + ", corAnswer=" + corAnswer + "]";
	}
	
	
}
