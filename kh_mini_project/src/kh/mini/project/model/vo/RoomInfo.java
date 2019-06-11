package kh.mini.project.model.vo;
// Server�ʹ� �ٸ��� �����κ��� ���� �������� ������ �ӽ������� �����ϱ� ���� Ŭ����(���� ����� Ŭ����)
public class RoomInfo {
	/* ������ ���� ���� ������ �����ϴ� �뵵�� Ŭ����  */
	
	private int room_No; // ���ӹ� ��ȣ
	private String room_name; // ���ӹ� �̸�
	private String room_PW; // ���ӹ� ��й�ȣ(������ ��� null)
	private int room_UCount; // ���� ��(User Count)
	
	public RoomInfo(int room_No, String room_name, String room_PW, int room_UCount) {
		super();
		this.room_No = room_No;
		this.room_name = room_name;
		this.room_PW = room_PW;
		this.room_UCount = room_UCount;
		
		if(room_PW.equals(null)) {
			
		}
	}

	public int getRoom_No() {
		return room_No;
	}

	public void setRoom_No(int room_No) {
		this.room_No = room_No;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public String getRoom_PW() {
		return room_PW;
	}

	public void setRoom_PW(String room_PW) {
		this.room_PW = room_PW;
	}

	public int getRoom_UCount() {
		return room_UCount;
	}

	public void setRoom_UCount(int room_UCount) {
		this.room_UCount = room_UCount;
	}
	
	
	
}
