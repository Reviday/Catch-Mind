package kh.mini.project.paint;

public class Question {
	private String[] question;
	private String[] selected;
	private int i=0;
	
	public Question(){
		question = new String[] {"��ó���", "��������", "īī������", "����ǥ", "���Ÿ�̰���",
				"��Ǫ", "�ҹ��", "���ٹ�", "ī���̼�", "�ﱹ�ô�", "���μ�", "������", "����", "�����",
				"���α�", "�ӼӸ�", "����", "����", "���", "�ʵ��б�", "���л�", "����", "����", "�ڹ�",
				"����", "�ڵ���", "���͸�", "Ŀ��", "�˱��", "�Ǽ�", "�Ϻ���", "���ǰ", "����", "������Ǯ",
				"��۱�", "�δ��", "õ������", "���ξ���", "���߷�", "���̽���ũ", "���ڸ�ä", "������ȭ",
				"�μ��", "���ħ", "�¾�", "��������", "����", "����", "������", "�����", "��ѱ�",
				"�̸�", "��", "ȣ��", "����", "ȭ��", "��", "��ǥ", "�����", "����", "��踻", "����",
				"����", "���̽�ũ��", "�ѱ�", "����", "�̱�", "�߱�", "������", "�����̵�", "ȭ���", "Ű����",
				"���콺", "��Ʈ��", "�̾�޸���", "��ȣ��", "���", "������", "�����", "�Ｚ", "����",
				"īī����", "��Ƽ", "�౸", "�߱�", "����", "�⸰", "��ӵ���", "����", "�λ�", "���ֵ�", "����Ŀ",
				"â��", "ķ�����̾�", "ũ��������", "������", "�������", "�����", "����", "���찳"};
		selected = new String[question.length];
	}
	
	public String selQuestion(int round){
		int count = 0;
		while(true){
			selected[round] = question[(int)(Math.random()*100)];
			
			for(int j=0;j<i;j++){
				if(selected[round].equals(selected[j]))
					count++;
			}
			
			if(count==0)
				return selected[i];
		}
		
	}
	
	public static void main(String[] args) {
		//System.out.println(new Question().question.length);
		Question q = new Question();
		
		for(int i=0;i<10;i++){
			System.out.println(q.selQuestion(i));
		}
		
		
	}
}
