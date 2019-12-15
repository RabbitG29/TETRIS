package tetris;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.swing.*;

public class Tetris extends JFrame implements ActionListener {
	Button btn[][] = new Button[20][10];
	boolean pauseFlag = false;
	Thread t;
	/*--- ��� ���, ���ڸ��� �� ���ڸ��� �� ---*/
	int blocks[][] = {{0,1,2,3},{0,10,11,12},{0,10,-11,-12},{0,1,10,11},{0,1,10,-11},{0,-11,10,11},{0,1,11,12}};
	int blocks2[][] = {{0,10,20,30},{0,1,10,20},{0,10,20,21},{0,1,10,11},{0,10,11,21},{0,10,11,20},{0,10,-11,-21}};
	int i, j;
	Block b;
	static JLabel score;
	static final String scoreText = "Score : ";
	static int scoreValue = 0;
	class Block {
		int type;
		boolean wide;
		int position[][] = new int[4][2]; // ����� 4ĭ�̰� ��� ���� ������ 4x2 �迭 ����
		
		Block(int startRow, int startCol) {
			position[0][0] = startRow;
			position[0][1] = startCol;
			Random random = new Random();
			type=(random.nextInt(7));
			wide=false;
			calculatePosition();
		}
		/*--- ȸ�� ���� ���� ---*/
		public boolean isPossibleSwitch() {
			if(wide) {
				int wideRow1 = position[0][0] + Math.abs(blocks[type][1]/10);
				int wideCol1 = position[0][1] + blocks[type][1]%10;
				int wideRow2 = position[0][0] + Math.abs(blocks[type][2]/10);
				int wideCol2 = position[0][1] + blocks[type][2]%10;
				int wideRow3 = position[0][0] + Math.abs(blocks[type][3]/10);
				int wideCol3 = position[0][1] + blocks[type][3]%10;
				if( wideRow1 < 0 || wideRow1 >= 20 || wideRow2 < 0 || wideRow2 >= 20 || wideRow3 < 0 || wideRow3 >= 20
						|| wideCol1 < 0 || wideCol1 >= 10 || wideCol2 < 0 || wideCol2 >= 10 || wideCol3 < 0 || wideCol3 >= 10) {
					return false;
				}
				if(btn[wideRow1][wideCol1].getBackground()==Color.BLACK) {
					if((wideRow1!=position[1][0]||wideCol1!=position[1][1])&&(wideRow1!=position[2][0]||wideCol1!=position[2][1])
							&&(wideRow1!=position[3][0]||wideCol1!=position[3][1]))
						return false;
				}
				if(btn[wideRow2][wideCol2].getBackground()==Color.BLACK) {
					if((wideRow2!=position[1][0]||wideCol2!=position[1][1])&&(wideRow2!=position[2][0]||wideCol2!=position[2][1])
							&&(wideRow2!=position[3][0]||wideCol2!=position[3][1]))
						return false;
				}
				if(btn[wideRow3][wideCol3].getBackground()==Color.BLACK) {
					if((wideRow3!=position[1][0]||wideCol3!=position[1][1])&&(wideRow3!=position[2][0]||wideCol3!=position[2][1])
							&&(wideRow3!=position[3][0]||wideCol3!=position[3][1]))
						return false;
				}
			}
			else {
				int nonWideRow1 = position[0][0] + Math.abs(blocks2[type][1]/10);
				int nonWideCol1 = position[0][1] + blocks2[type][1]%10;
				int nonWideRow2 = position[0][0] + Math.abs(blocks2[type][2]/10);
				int nonWideCol2 = position[0][1] + blocks2[type][2]%10;
				int nonWideRow3 = position[0][0] + Math.abs(blocks2[type][3]/10);
				int nonWideCol3 = position[0][1] + blocks2[type][3]%10;
				if( nonWideRow1 < 0 || nonWideRow1 >= 20 || nonWideRow2 < 0 || nonWideRow2 >= 20 || nonWideRow3 < 0 || nonWideRow3 >= 20
						|| nonWideCol1 < 0 || nonWideCol1 >= 10 || nonWideCol2 < 0 || nonWideCol2 >= 10 || nonWideCol3 < 0 || nonWideCol3 >= 10) {
					return false;
				}
				if(btn[nonWideRow1][nonWideCol1].getBackground()==Color.BLACK) {
					if((nonWideRow1!=position[1][0]||nonWideCol1!=position[1][1])&&(nonWideRow1!=position[2][0]||nonWideCol1!=position[2][1])
							&&(nonWideRow1!=position[3][0]||nonWideCol1!=position[3][1]))
						return false;
				}
				if(btn[nonWideRow2][nonWideCol2].getBackground()==Color.BLACK) {
					if((nonWideRow2!=position[1][0]||nonWideCol2!=position[1][1])&&(nonWideRow2!=position[2][0]||nonWideCol2!=position[2][1])
							&&(nonWideRow2!=position[3][0]||nonWideCol2!=position[3][1]))
						return false;
				}
				if(btn[nonWideRow3][nonWideCol3].getBackground()==Color.BLACK) {
					if((nonWideRow3!=position[1][0]||nonWideCol3!=position[1][1])&&(nonWideRow3!=position[2][0]||nonWideCol3!=position[2][1])
							&&(nonWideRow3!=position[3][0]||nonWideCol3!=position[3][1]))
						return false;
				}
			}
			
			return true;
		}
		/*--- ���� ���� �����ϴ� �Լ� ---*/
		public void switchPosition() {
			if(isPossibleSwitch()) {
				wide = !wide;
				btn[b.position[0][0]][b.position[0][1]].setBackground(Color.WHITE);
				btn[b.position[1][0]][b.position[1][1]].setBackground(Color.WHITE);
				btn[b.position[2][0]][b.position[2][1]].setBackground(Color.WHITE);
				btn[b.position[3][0]][b.position[3][1]].setBackground(Color.WHITE);
				calculatePosition();
			}
		}
		/*--- �� ���� �������� ���� �׸��� ---*/
		public void calculatePosition() {
			// ���� ������ ���������� �Ʒ��� �ǰ� �����Ͽ� ���� ���� ��, ���� ��ȣ�� ���� ����� ��ġ
			// TODO : ���μ��� �ٲ� �� �̹� ����� �ִ� ���̸� ���ٲٰ�
			if(!wide) {
				position[1][0] = position[0][0] + Math.abs(blocks[type][1]/10);
				position[1][1] = position[0][1] + blocks[type][1]%10;
				position[2][0] = position[0][0] + Math.abs(blocks[type][2]/10);
				position[2][1] = position[0][1] + blocks[type][2]%10;
				position[3][0] = position[0][0] + Math.abs(blocks[type][3]/10);
				position[3][1] = position[0][1] + blocks[type][3]%10;
			}
			else {
				position[1][0] = position[0][0] + Math.abs(blocks2[type][1]/10);
				position[1][1] = position[0][1] + blocks2[type][1]%10;
				position[2][0] = position[0][0] + Math.abs(blocks2[type][2]/10);
				position[2][1] = position[0][1] + blocks2[type][2]%10;
				position[3][0] = position[0][0] + Math.abs(blocks2[type][3]/10);
				position[3][1] = position[0][1] + blocks2[type][3]%10;
			}
		}
	}
	class Down implements Runnable {
		@Override
		public void run() {
			while (true) {
				if(pauseFlag)
					continue;
				try {
					Thread.sleep(200); // TODO : sleep �ʸ� �����ؼ� �ܰ� ���� �����ϰ� �� �� ���� ��
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				/*--- ����� ���� ������ �� ��Ȳ ---*/
				if(b==null || blockOver()) {
					lineOver();
					b = new Block(1,5);
					if(gameOver()) { // ������ ���� ������ �˻�
						JOptionPane.showMessageDialog(null, "Game Over"); // alert ��� ��
						b=null; // ��� ����
						for(int i=0;i<20;i++)
							for(int j=0;j<10;j++)
								btn[i][j].setBackground(Color.WHITE); // �� ������ �ʱ�ȭ
						continue; // �� ���� ����
					}
					btn[b.position[0][0]][b.position[0][1]].setBackground(Color.BLACK);
					btn[b.position[1][0]][b.position[1][1]].setBackground(Color.BLACK);
					btn[b.position[2][0]][b.position[2][1]].setBackground(Color.BLACK);
					btn[b.position[3][0]][b.position[3][1]].setBackground(Color.BLACK);
					
				}
				/*--- ���� ����� �Ʒ��� ������ ��Ȳ ---*/
				else {
					btn[b.position[0][0]++][b.position[0][1]].setBackground(Color.WHITE);
					btn[b.position[1][0]++][b.position[1][1]].setBackground(Color.WHITE);
					btn[b.position[2][0]++][b.position[2][1]].setBackground(Color.WHITE);
					btn[b.position[3][0]++][b.position[3][1]].setBackground(Color.WHITE);
					btn[b.position[0][0]][b.position[0][1]].setBackground(Color.BLACK);
					btn[b.position[1][0]][b.position[1][1]].setBackground(Color.BLACK);
					btn[b.position[2][0]][b.position[2][1]].setBackground(Color.BLACK);
					btn[b.position[3][0]][b.position[3][1]].setBackground(Color.BLACK);
				}
			}
		}
	}

	Tetris() {
		setTitle("��Ʈ����");
		makeMenu();
		JPanel p2 = new JPanel();
		
		score = new JLabel();
		score.setHorizontalAlignment(SwingConstants.CENTER);
		score.setText(scoreText+scoreValue);
		add("North", score);
		p2.setLayout(new GridLayout(20, 10));
		for (i = 0; i < 20; i++) {
			for (j = 0; j < 10; j++) {
				btn[i][j] = new Button();
				final Button b2 = btn[i][j];
				b2.setBackground(Color.WHITE);
				b2.addKeyListener(new KeyListener() {
					public void keyReleased(KeyEvent e) {

					}
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == 16 || e.getKeyCode() == 32) { // Shift ������ �� ���μ��� ���ϵ���
							b.switchPosition();
						}
						else if(e.getKeyCode() == 27) { // ESC ������ pause
							pauseFlag=true;
							JOptionPane.showMessageDialog(null, "Pause");
							pauseFlag=false;
							// TODO : ������ �Ͻ����� ��� �ұ�
						}
						else if(e.getKeyCode() == 37) { // ����Ű ��
							goLeft();
						}
						else if(e.getKeyCode() == 39) { // ����Ű ��
							goRight();
						}
						// TODO �ܰ輳���̳� �ӵ� ���̴� Ű�� �����?
					}
					public void keyTyped(KeyEvent e) {
						

					}
				});
				p2.add(btn[i][j]);
			}
		}
		add(p2);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 1000);
		setVisible(true);

		t = new Thread(new Down());
		t.setDaemon(true);// ������ ������ ������ ������ ����. �ٵ� ���⼭�� �����ص� �ȳ���. ������ �ȿ� �Ἥ. or
							// xǥ�� ������ thread���� ���ֱ�(�̰� �ȹ��. ���� ���⶧ �Ű� ����)
		t.start();
	}
	
	/*--- �޴� �� ���� ---*/
	void makeMenu() {
		JMenuItem item;

		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("����"); // ���� �޴�
		m1.setMnemonic(KeyEvent.VK_F);
		item = new JMenuItem("�� ����", KeyEvent.VK_N); // ���� �޴�
		item.addActionListener(this);
		m1.add(item);
		item = new JMenuItem("���� ����", KeyEvent.VK_O); // ���� �޴�
		item.addActionListener(this);
		m1.add(item);
		item = new JMenuItem("���� ����", KeyEvent.VK_S); // ���� �޴�
		item.addActionListener(this);
		m1.add(item);
		m1.addSeparator();
		item = new JMenuItem("����", KeyEvent.VK_E); // ���� �޴�
		item.addActionListener(this);
		m1.add(item);

		mb.add(m1);
		setJMenuBar(mb);
	}
	
	/*--- ����� ���� ��Ȳ���� �˻� ---*/
	public boolean blockOver() {
		// �ٴڱ��� �������� ���
		if(b.position[0][0] >= 19 || b.position[1][0] >= 19 || b.position[2][0] >= 19 || b.position[3][0] >= 19)
			return true;
		// �̹� ����� �κ��� ������ ���
		if(btn[b.position[0][0]+1][b.position[0][1]].getBackground()==Color.BLACK) {
			if((b.position[0][0]+1 != b.position[1][0] || b.position[0][1] != b.position[1][1]) &&
					(b.position[0][0]+1 != b.position[2][0] || b.position[0][1] != b.position[2][1]) &&
					(b.position[0][0]+1 != b.position[3][0] || b.position[0][1] != b.position[3][1])) {
				return true;
			}
		}
		if(btn[b.position[1][0]+1][b.position[1][1]].getBackground()==Color.BLACK) {
			if((b.position[1][0]+1 != b.position[0][0] || b.position[1][1] != b.position[0][1]) &&
					(b.position[1][0]+1 != b.position[2][0] || b.position[1][1] != b.position[2][1]) &&
					(b.position[1][0]+1 != b.position[3][0] || b.position[1][1] != b.position[3][1])) {
				return true;
			}
		}
		if(btn[b.position[2][0]+1][b.position[2][1]].getBackground()==Color.BLACK) {
			if((b.position[2][0]+1 != b.position[1][0] || b.position[2][1] != b.position[1][1]) &&
					(b.position[2][0]+1 != b.position[0][0] || b.position[2][1] != b.position[0][1]) &&
					(b.position[2][0]+1 != b.position[3][0] || b.position[2][1] != b.position[3][1])) {
				return true;
			}
		}
		if(btn[b.position[3][0]+1][b.position[3][1]].getBackground()==Color.BLACK) {
			if((b.position[3][0]+1 != b.position[1][0] || b.position[3][1] != b.position[1][1]) &&
					(b.position[3][0]+1 != b.position[2][0] || b.position[3][1] != b.position[2][1]) &&
					(b.position[3][0]+1 != b.position[0][0] || b.position[3][1] != b.position[0][1])) {
				return true;
			}
		}					
		return false;
	}
	
	/*--- ������ ���� ��Ȳ���� �˻� ---*/
	public boolean gameOver() {
		if(b==null)
			return false;
		if(btn[b.position[0][0]][b.position[0][1]].getBackground()==Color.BLACK || 
				btn[b.position[1][0]][b.position[1][1]].getBackground()==Color.BLACK ||
				btn[b.position[2][0]][b.position[2][1]].getBackground()==Color.BLACK ||
				btn[b.position[3][0]][b.position[3][1]].getBackground()==Color.BLACK)
			return true;
		
		return false;
	}
	
	public void lineOver() {
		int tempScore = 0;
		for(int i=0;i<20;i++) {
			boolean flag = false;
			for(int j=0;j<10;j++) {
				if(btn[i][j].getBackground()!=Color.BLACK) {
					flag = true;
					break;
				}
			}
			if(!flag) { // ���� ��ü�� black�� ���
				tempScore+=100;
				for(int m=i;m>=1;m--) {
					for(int n=0;n<10;n++) {
						btn[m][n].setBackground(btn[m-1][n].getBackground());
					}
				}
				for(int m=0;m<10;m++)
					btn[0][m].setBackground(Color.WHITE);
			}
		}
		scoreValue += tempScore;
		score.setText(scoreText+scoreValue);
	}
	
	public void goLeft() {
		if(b==null)
			return;
		if(b.position[0][1] < 1 || b.position[1][1] < 1 || b.position[2][1] < 1 || b.position[3][1] < 1)
			return;
		// �̹� ����� �κ��� ������ ���
		if(btn[b.position[0][0]][b.position[0][1]-1].getBackground()==Color.BLACK) {
			if((b.position[0][0] == b.position[1][0] && b.position[0][1]-1 == b.position[1][1]) &&
					(b.position[0][0] == b.position[2][0] && b.position[0][1]-1 == b.position[2][1]) &&
					(b.position[0][0] == b.position[3][0] && b.position[0][1]-1 == b.position[3][1])) {
				return;
			}
		}
		if(btn[b.position[1][0]][b.position[1][1]-1].getBackground()==Color.BLACK) {
			if((b.position[1][0] == b.position[0][0] && b.position[1][1]-1 == b.position[0][1]) &&
					(b.position[1][0] == b.position[2][0] && b.position[1][1]-1 == b.position[2][1]) &&
					(b.position[1][0] == b.position[3][0] && b.position[1][1]-1 == b.position[3][1])) {
				return;
			}
		}
		if(btn[b.position[2][0]][b.position[2][1]-1].getBackground()==Color.BLACK) {
			if((b.position[2][0] == b.position[1][0] && b.position[2][1]-1 == b.position[1][1]) &&
					(b.position[2][0] == b.position[0][0] && b.position[2][1]-1 == b.position[0][1]) &&
					(b.position[2][0] == b.position[3][0] && b.position[2][1]-1 == b.position[3][1])) {
				return;
			}
		}
		if(btn[b.position[3][0]][b.position[3][1]-1].getBackground()==Color.BLACK) {
			if((b.position[3][0] == b.position[1][0] && b.position[3][1]-1 == b.position[1][1]) &&
					(b.position[3][0] == b.position[2][0] && b.position[3][1]-1 == b.position[2][1]) &&
					(b.position[3][0] == b.position[0][0] && b.position[3][1]-1 == b.position[0][1])) {
				return;
			}
		}
		btn[b.position[0][0]][b.position[0][1]--].setBackground(Color.WHITE);
		btn[b.position[1][0]][b.position[1][1]--].setBackground(Color.WHITE);
		btn[b.position[2][0]][b.position[2][1]--].setBackground(Color.WHITE);
		btn[b.position[3][0]][b.position[3][1]--].setBackground(Color.WHITE);
		btn[b.position[0][0]][b.position[0][1]].setBackground(Color.BLACK);
		btn[b.position[1][0]][b.position[1][1]].setBackground(Color.BLACK);
		btn[b.position[2][0]][b.position[2][1]].setBackground(Color.BLACK);
		btn[b.position[3][0]][b.position[3][1]].setBackground(Color.BLACK);
	}
	
	public void goRight() {
		if(b==null)
			return;
		if(b.position[0][1] > 8 || b.position[1][1] > 8 || b.position[2][1] > 8 || b.position[3][1] > 8)
			return;
		// �̹� ����� �κ��� ������ ���
		if(btn[b.position[0][0]][b.position[0][1]+1].getBackground()==Color.BLACK) {
			if((b.position[0][0] == b.position[1][0] && b.position[0][1]+1 == b.position[1][1]) &&
					(b.position[0][0] == b.position[2][0] && b.position[0][1]+1 == b.position[2][1]) &&
					(b.position[0][0] == b.position[3][0] && b.position[0][1]+1 == b.position[3][1])) {
				return;
			}
		}
		if(btn[b.position[1][0]][b.position[1][1]+1].getBackground()==Color.BLACK) {
			if((b.position[1][0] == b.position[0][0] && b.position[1][1]+1 == b.position[0][1]) &&
					(b.position[1][0] == b.position[2][0] && b.position[1][1]+1 == b.position[2][1]) &&
					(b.position[1][0] == b.position[3][0] && b.position[1][1]+1 == b.position[3][1])) {
				return;
			}
		}
		if(btn[b.position[2][0]][b.position[2][1]+1].getBackground()==Color.BLACK) {
			if((b.position[2][0] == b.position[1][0] && b.position[2][1]+1 == b.position[1][1]) &&
					(b.position[2][0] == b.position[0][0] && b.position[2][1]+1 == b.position[0][1]) &&
					(b.position[2][0] == b.position[3][0] && b.position[2][1]+1 == b.position[3][1])) {
				return;
			}
		}
		if(btn[b.position[3][0]][b.position[3][1]+1].getBackground()==Color.BLACK) {
			if((b.position[3][0] == b.position[1][0] && b.position[3][1]+1 == b.position[1][1]) &&
					(b.position[3][0] == b.position[2][0] && b.position[3][1]+1 == b.position[2][1]) &&
					(b.position[3][0] == b.position[0][0] && b.position[3][1]+1 == b.position[0][1])) {
				return;
			}
		}
		btn[b.position[0][0]][b.position[0][1]++].setBackground(Color.WHITE);
		btn[b.position[1][0]][b.position[1][1]++].setBackground(Color.WHITE);
		btn[b.position[2][0]][b.position[2][1]++].setBackground(Color.WHITE);
		btn[b.position[3][0]][b.position[3][1]++].setBackground(Color.WHITE);
		btn[b.position[0][0]][b.position[0][1]].setBackground(Color.BLACK);
		btn[b.position[1][0]][b.position[1][1]].setBackground(Color.BLACK);
		btn[b.position[2][0]][b.position[2][1]].setBackground(Color.BLACK);
		btn[b.position[3][0]][b.position[3][1]].setBackground(Color.BLACK);
	}

	/*--- �� �޴��� ���� ���� ---*/
	public void actionPerformed(ActionEvent e) {
		JMenuItem mi = (JMenuItem) (e.getSource());
		switch (mi.getText()) {
		case "�� ����":
			System.out.println("�� ����");
			for (i = 0; i < 20; i++) {
				for (j = 0; j < 10; j++) {
					btn[i][j].setBackground(Color.WHITE);
				}
			}
			b=null;
			scoreValue=0;
			score.setText(scoreText+scoreValue);
			break;
		case "���� ����":
			System.out.println("���� ����"); // TODO : ������ �����ϰ� �ҷ�����
			String inputPath = "C:/Tetris/save_tetris.txt"; // ����� ���� �ҷ� ��
			b=null;
			try {
				FileInputStream fileStream = null; // ���� ��Ʈ��
		        String color = "";
		        String[] colors = {};
		        fileStream = new FileInputStream( inputPath );// ���� ��Ʈ�� ����
		        byte[ ] readBuffer = new byte[fileStream.available()];
		        if(fileStream.read(readBuffer) != -1) {
					color = new String(readBuffer);
					colors = color.split("\r\n");
				}
		        for (i = 0; i < 20; i++) {
					for (j = 0; j < 10; j++) {
						/*--- R G B�� ���ؼ� substring ---*/
						String target = "r=";
						String target2 = "g=";
						String target3 = "b=";
						int target_num = colors[i*10+j].indexOf(target); 
						int target_num2 = colors[i*10+j].indexOf(target2); 
						int target_num3 = colors[i*10+j].indexOf(target3); 
						String result = colors[i*10+j].substring(target_num+2,(colors[i*10+j].substring(target_num).indexOf(",")+target_num));
						String result2 = colors[i*10+j].substring(target_num2+2,(colors[i*10+j].substring(target_num2).indexOf(",")+target_num2));
						String result3 = colors[i*10+j].substring(target_num3+2,(colors[i*10+j].substring(target_num3).indexOf("]")+target_num3));

						// ��ü ��ư�� ���� ����
						//btn[i][j].setBackground(new Color(Integer.parseInt(result), Integer.parseInt(result2), Integer.parseInt(result3))); -> �̷��� ���� WHITE BLACK���� �˻簡 �� ��
						if(Integer.parseInt(result)==0&&Integer.parseInt(result2)==0&&Integer.parseInt(result3)==0)
							btn[i][j].setBackground(Color.BLACK);
						else
							btn[i][j].setBackground(Color.WHITE);
					}
				}
		        fileStream.close(); //��Ʈ�� �ݱ�
			} catch (FileNotFoundException e1) { // ������ ���� ���
				e1.printStackTrace();
				System.out.println("���� ������ �����ϴ�");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "���� ����":
			System.out.println("���� ����");
			OutputStream output;
			btn[b.position[0][0]][b.position[0][1]].setBackground(Color.WHITE);
			btn[b.position[1][0]][b.position[1][1]].setBackground(Color.WHITE);
			btn[b.position[2][0]][b.position[2][1]].setBackground(Color.WHITE);
			btn[b.position[3][0]][b.position[3][1]].setBackground(Color.WHITE);
			b = null;
			try {
				String path = "C:/Tetris";
				File folder = new File(path);
				if (!folder.exists()) { // �ش� ������ ������
					try {
						folder.mkdir(); // ���� ����
						System.out.println("���� ����");
					} catch (Exception e2) {
						e2.getStackTrace();
					}
				}
				path += "/save_tetris.txt";
				output = new FileOutputStream(path);
				for (i = 0; i < 20; i++) {
					for (j = 0; j < 10; j++) {
						byte[] by = (btn[i][j].getBackground().toString() + "\r\n").getBytes(); // ����Ʈ�� ����
						output.write(by);
					}
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "����":
			System.out.println("����");
			System.exit(0); // ����
			break;
		}
	}
	public static void main(String[] args) {
		new Tetris();
	}
}

/*
Issue
1. setBackground�� int 3���� ������ Color.BLACK�� �ٸ��ٰ� �ν��� �ȴ�.
2. ������ �Ͻ����� �̽�
3. �ҷ����� ���� ���� �̽�(1���� ����)
4. �� ���� �� ȸ�� ���� ���δ� �ε����� ����°� + ������ �̹� ������ ä�����°�(�� ��, ���� ���� �ƴ� ��ġ�� Ȯ��)


*/