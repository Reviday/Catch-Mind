package kh.mini.project.db;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import kh.mini.project.main.view.Main;
import kh.mini.project.model.vo.User;


public class UserController {
		
		
	//userDB�� ��� ���� �����͸� �����ϴ� �޼ҵ�
	public int dataSaveAll(Vector allUser_vc) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("userDB.dat"))) {
			User [] users = new User[allUser_vc.size()]; //allUser_vc�� ũ�⸸ŭ User ��ü �迭�� ����
			// user_vc�� �ִ� User ��ü ������ users�� �����Ѵ�.
			for(int i=0; i<users.length; i++) {
				users[i] = (User)allUser_vc.get(i);
			}
			// users�� ����� User ��ü�� userDB.dat�� �����Ѵ�.
			for(User u : users) {
				oos.writeObject(u);
			}
			return 1; // ���� �ε� �Ǿ��� ��� 1�� ����
		} catch (FileNotFoundException e) {
			e.printStackTrace(); return -1; 
			// ���ܰ� �߻��� ��� -1�� �����Ѵ�.
		} catch (IOException e) {
			e.printStackTrace(); return -1;
		}
	}
	
	//userDB�� �����͸� ã�ƿ��� �޼ҵ�
	public User dataSearch(String id) {
		Vector users = dataLoadAll();
		for(int i=0; i<users.size(); i++) 
		{
			User u = (User)users.elementAt(i);
			if(u.getId().equals(id)) {
				return u;
			}
		}
		return null;
	}
	
	//userDB�� ��� �����͸� �ҷ����� �޼ҵ�
	public Vector dataLoadAll() {
		Vector users = new Vector();
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("userDB.dat"))) {
			User user = new User();
			while((user=(User)(ois.readObject()))!=null) {
				users.add(user);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (EOFException e) {
			System.out.println("��� ȸ�� ���� �ε� �Ϸ�!");
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
		return users;
	}
	// ���� �߰� �׽�Ʈ
//	public static void main(String[] args) {
//		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("userDB.dat"))){
//			User admin = new User("admin", "admin", "admin", "900101", "null", 0, ' ', 13, 999999, 99999);
//			User test = new User("test","test","test","900101","null",0,' ',1,0,10);
//			User test1 = new User("test1","test1","test1","900101","null",0,' ',2,0,20);
//			User test2 = new User("test2","test2","test2","900101","null",0,' ',3,0,30);
//			
//			oos.writeObject(admin);
//			oos.writeObject(test);
//			oos.writeObject(test1);
//			oos.writeObject(test2);
//			System.out.println("���� ����Ϸ�!");
//		} catch (Exception e) {
//		}
//	}
}
