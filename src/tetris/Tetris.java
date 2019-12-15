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
	/*--- 블락 모양, 십자리는 행 일자리는 열 ---*/
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
		int position[][] = new int[4][2]; // 블록은 4칸이고 행과 열을 가지니 4x2 배열 선언
		
		Block(int startRow, int startCol) {
			position[0][0] = startRow;
			position[0][1] = startCol;
			Random random = new Random();
			type=(random.nextInt(7));
			wide=false;
			calculatePosition();
		}
		/*--- 회전 가능 여부 ---*/
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
		/*--- 가로 세로 변경하는 함수 ---*/
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
		/*--- 한 점을 기준으로 도형 그리기 ---*/
		public void calculatePosition() {
			// 행은 무조건 기준점보다 아래가 되게 설정하여 절댓값 더해 줌, 열은 부호에 따라서 상대적 위치
			// TODO : 가로세로 바꿀 때 이미 블록이 있는 곳이면 못바꾸게
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
					Thread.sleep(200); // TODO : sleep 초를 조절해서 단계 설정 가능하게 할 수 있을 듯
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				/*--- 블록을 새로 만들어야 할 상황 ---*/
				if(b==null || blockOver()) {
					lineOver();
					b = new Block(1,5);
					if(gameOver()) { // 게임이 끝날 때인지 검사
						JOptionPane.showMessageDialog(null, "Game Over"); // alert 띄워 줌
						b=null; // 블록 삭제
						for(int i=0;i<20;i++)
							for(int j=0;j<10;j++)
								btn[i][j].setBackground(Color.WHITE); // 새 판으로 초기화
						continue; // 새 게임 시작
					}
					btn[b.position[0][0]][b.position[0][1]].setBackground(Color.BLACK);
					btn[b.position[1][0]][b.position[1][1]].setBackground(Color.BLACK);
					btn[b.position[2][0]][b.position[2][1]].setBackground(Color.BLACK);
					btn[b.position[3][0]][b.position[3][1]].setBackground(Color.BLACK);
					
				}
				/*--- 기존 블록을 아래로 내리는 상황 ---*/
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
		setTitle("테트리스");
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
						if (e.getKeyCode() == 16 || e.getKeyCode() == 32) { // Shift 눌렀을 때 가로세로 변하도록
							b.switchPosition();
						}
						else if(e.getKeyCode() == 27) { // ESC 누르면 pause
							pauseFlag=true;
							JOptionPane.showMessageDialog(null, "Pause");
							pauseFlag=false;
							// TODO : 스레드 일시정지 어떻게 할까
						}
						else if(e.getKeyCode() == 37) { // 방향키 좌
							goLeft();
						}
						else if(e.getKeyCode() == 39) { // 방향키 우
							goRight();
						}
						// TODO 단계설정이나 속도 높이는 키를 만들까?
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
		t.setDaemon(true);// 위에서 쓰레드 끝나는 조건이 없음. 근데 여기서는 데몬해도 안끝남. 생성자 안에 써서. or
							// x표시 누를때 thread끄게 해주기(이거 안배움. 과제 제출때 신경 ㄴㄴ)
		t.start();
	}
	
	/*--- 메뉴 바 구성 ---*/
	void makeMenu() {
		JMenuItem item;

		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("파일"); // 상위 메뉴
		m1.setMnemonic(KeyEvent.VK_F);
		item = new JMenuItem("새 게임", KeyEvent.VK_N); // 세부 메뉴
		item.addActionListener(this);
		m1.add(item);
		item = new JMenuItem("파일 열기", KeyEvent.VK_O); // 세부 메뉴
		item.addActionListener(this);
		m1.add(item);
		item = new JMenuItem("파일 저장", KeyEvent.VK_S); // 세부 메뉴
		item.addActionListener(this);
		m1.add(item);
		m1.addSeparator();
		item = new JMenuItem("종료", KeyEvent.VK_E); // 세부 메뉴
		item.addActionListener(this);
		m1.add(item);

		mb.add(m1);
		setJMenuBar(mb);
	}
	
	/*--- 블록이 멈출 상황인지 검사 ---*/
	public boolean blockOver() {
		// 바닥까지 내려왔을 경우
		if(b.position[0][0] >= 19 || b.position[1][0] >= 19 || b.position[2][0] >= 19 || b.position[3][0] >= 19)
			return true;
		// 이미 블록인 부분을 만났을 경우
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
	
	/*--- 게임이 끝난 상황인지 검사 ---*/
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
			if(!flag) { // 한줄 전체가 black인 경우
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
		// 이미 블록인 부분을 만났을 경우
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
		// 이미 블록인 부분을 만났을 경우
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

	/*--- 각 메뉴의 동작 정의 ---*/
	public void actionPerformed(ActionEvent e) {
		JMenuItem mi = (JMenuItem) (e.getSource());
		switch (mi.getText()) {
		case "새 게임":
			System.out.println("새 게임");
			for (i = 0; i < 20; i++) {
				for (j = 0; j < 10; j++) {
					btn[i][j].setBackground(Color.WHITE);
				}
			}
			b=null;
			scoreValue=0;
			score.setText(scoreText+scoreValue);
			break;
		case "파일 열기":
			System.out.println("파일 열기"); // TODO : 점수도 저장하고 불러오기
			String inputPath = "C:/Tetris/save_tetris.txt"; // 저장된 파일 불러 옴
			b=null;
			try {
				FileInputStream fileStream = null; // 파일 스트림
		        String color = "";
		        String[] colors = {};
		        fileStream = new FileInputStream( inputPath );// 파일 스트림 생성
		        byte[ ] readBuffer = new byte[fileStream.available()];
		        if(fileStream.read(readBuffer) != -1) {
					color = new String(readBuffer);
					colors = color.split("\r\n");
				}
		        for (i = 0; i < 20; i++) {
					for (j = 0; j < 10; j++) {
						/*--- R G B에 대해서 substring ---*/
						String target = "r=";
						String target2 = "g=";
						String target3 = "b=";
						int target_num = colors[i*10+j].indexOf(target); 
						int target_num2 = colors[i*10+j].indexOf(target2); 
						int target_num3 = colors[i*10+j].indexOf(target3); 
						String result = colors[i*10+j].substring(target_num+2,(colors[i*10+j].substring(target_num).indexOf(",")+target_num));
						String result2 = colors[i*10+j].substring(target_num2+2,(colors[i*10+j].substring(target_num2).indexOf(",")+target_num2));
						String result3 = colors[i*10+j].substring(target_num3+2,(colors[i*10+j].substring(target_num3).indexOf("]")+target_num3));

						// 전체 버튼에 색깔 적용
						//btn[i][j].setBackground(new Color(Integer.parseInt(result), Integer.parseInt(result2), Integer.parseInt(result3))); -> 이렇게 가면 WHITE BLACK으로 검사가 안 됨
						if(Integer.parseInt(result)==0&&Integer.parseInt(result2)==0&&Integer.parseInt(result3)==0)
							btn[i][j].setBackground(Color.BLACK);
						else
							btn[i][j].setBackground(Color.WHITE);
					}
				}
		        fileStream.close(); //스트림 닫기
			} catch (FileNotFoundException e1) { // 파일이 없을 경우
				e1.printStackTrace();
				System.out.println("저장 파일이 없습니다");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "파일 저장":
			System.out.println("파일 저장");
			OutputStream output;
			btn[b.position[0][0]][b.position[0][1]].setBackground(Color.WHITE);
			btn[b.position[1][0]][b.position[1][1]].setBackground(Color.WHITE);
			btn[b.position[2][0]][b.position[2][1]].setBackground(Color.WHITE);
			btn[b.position[3][0]][b.position[3][1]].setBackground(Color.WHITE);
			b = null;
			try {
				String path = "C:/Tetris";
				File folder = new File(path);
				if (!folder.exists()) { // 해당 폴더가 없으면
					try {
						folder.mkdir(); // 폴더 생성
						System.out.println("폴더 생성");
					} catch (Exception e2) {
						e2.getStackTrace();
					}
				}
				path += "/save_tetris.txt";
				output = new FileOutputStream(path);
				for (i = 0; i < 20; i++) {
					for (j = 0; j < 10; j++) {
						byte[] by = (btn[i][j].getBackground().toString() + "\r\n").getBytes(); // 바이트로 저장
						output.write(by);
					}
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "종료":
			System.out.println("종료");
			System.exit(0); // 종료
			break;
		}
	}
	public static void main(String[] args) {
		new Tetris();
	}
}

/*
Issue
1. setBackground를 int 3개로 넣으면 Color.BLACK과 다르다고 인식이 된다.
2. 스레드 일시정지 이슈
3. 불러오기 색깔 적용 이슈(1번과 연관)
4. 블럭 쌓임 및 회전 가능 여부는 인덱스를 벗어나는가 + 기존에 이미 블럭으로 채워졌는가(이 때, 현재 블럭이 아닌 위치로 확인)


*/