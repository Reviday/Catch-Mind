package kh.mini.project.model.vo;
// Server와는 다르게 서버로부터 받은 유저들의 정보를 임시적으로 저장하기 위한 클래스(정보 저장용 클래스)
public class RoomInfo {
	/* 개설된 게임 방의 정보를 저장하는 용도의 클래스  */
	
	private int room_No; // 게임방 번호
	private String room_name; // 게임방 이름
	private String room_PW; // 게임방 비밀번호(공개일 경우 null)
	private int room_UCount; // 유저 수(User Count)
	
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
