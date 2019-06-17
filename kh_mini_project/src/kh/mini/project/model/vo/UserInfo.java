package kh.mini.project.model.vo;

import javax.swing.ImageIcon;

import kh.mini.project.main.view.Main;

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
	
	private ImageIcon charImg; // ������ ���� ĳ���� �̹���
	private ImageIcon gradeImg; // ������ ���� ��� �̹���
	
	// ��� �̹��� ����
	private static ImageIcon[] gradeImgArr = new ImageIcon[13];
	
	
	static
	{
		// �̹��� �ʱ�ȭ 
		gradeImgArr[0] = new ImageIcon(Main.class.getResource("/images/F.PNG"));
		gradeImgArr[1] = new ImageIcon(Main.class.getResource("/images/Dm.PNG"));
		gradeImgArr[2] = new ImageIcon(Main.class.getResource("/images/D.PNG"));
		gradeImgArr[3] = new ImageIcon(Main.class.getResource("/images/Dp.PNG"));
		gradeImgArr[4] = new ImageIcon(Main.class.getResource("/images/Cm.PNG"));
		gradeImgArr[5] = new ImageIcon(Main.class.getResource("/images/C.PNG"));
		gradeImgArr[6] = new ImageIcon(Main.class.getResource("/images/Cp.PNG"));
		gradeImgArr[7] = new ImageIcon(Main.class.getResource("/images/Bm.PNG"));
		gradeImgArr[8] = new ImageIcon(Main.class.getResource("/images/B.PNG"));
		gradeImgArr[9] = new ImageIcon(Main.class.getResource("/images/Bp.PNG"));
		gradeImgArr[10] = new ImageIcon(Main.class.getResource("/images/Am.PNG"));
		gradeImgArr[11] = new ImageIcon(Main.class.getResource("/images/A.PNG"));
		gradeImgArr[12] = new ImageIcon(Main.class.getResource("/images/Ap.PNG"));
	}
	
	public UserInfo(String userID, int level, int exp, int corAnswer) {
		super();
		this.userID = userID;
		this.level = level;
		this.exp = exp;
		this.corAnswer = corAnswer;
		
		// ������ ���� �̹��� ����
		setCharImg(level);
		setGradeImg(level);
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
	
	public ImageIcon getCharImg() {
		return charImg;
	}

	public void setCharImg(int level) {
		/* ������ ���� ĳ���� �̹��� ���� ��� */
		
	}

	public ImageIcon getGradeImg() {
		return gradeImg;
	}

	public void setGradeImg(int level) {
		/* ������ ���� ��� �̹��� ���� ��� */
	}

	// ���� ����ġ�� �����Ͽ� �������� �� �� ������ ���� �޼ҵ�
	public void levelUp() {
		
	}
	
	// ������ �ش��ϴ� ��� �̹����� ��ȯ�Ѵ�.
	public static ImageIcon getGrade(int level) {
		return gradeImgArr[level-1];
	}
	
	@Override
	public String toString() {
		return "UserInfo [userID=" + userID + ", level=" + level + ", exp=" + exp + ", corAnswer=" + corAnswer + "]";
	}
	
	
}
