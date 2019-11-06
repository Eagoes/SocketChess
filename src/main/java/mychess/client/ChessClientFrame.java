package mychess.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.naming.InterruptedNamingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mychess.util.ReadProperties;
import mychess.entity.Code;
import mychess.entity.DataMessage;
import mychess.entity.Message;
import mychess.entity.NormalMessage;
import mychess.util.Common;
import mychess.util.HasFinished;
import mychess.util.Internet;
import mychess.util.JudgeMove;


//该类实现象棋的图形界面,并负责通信
public class ChessClientFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private JButton rank=new JButton("排名");
	private JButton history=new JButton("成绩");
	private Image[] pics =new Image[15];//加载象棋图片
	private ChessBoard panel;
	private Internet chessInternet;
	private Internet funcInternet;
	private String userName;
	
	public ChessClientFrame(Internet outer_internet, Internet func_internet, String name) {
		chessInternet = outer_internet;
		funcInternet = func_internet;
		userName = name;

		pics[1]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess11.png");
		pics[2]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess10.png");
		pics[3]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess9.png");
		pics[4]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess8.png");
		pics[5]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess7.png");
		pics[6]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess12.png");
		pics[7]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess13.png");
		pics[8]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess4.png");
		pics[9]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess3.png");
		pics[10]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess2.png");
		pics[11]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess1.png");
		pics[12]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess0.png");
		pics[13]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess5.png");
		pics[14]=Toolkit.getDefaultToolkit().getImage("src/main/resources/images/chess6.png");
		panel=new ChessBoard(pics, chessInternet, funcInternet, userName);
		add(panel,BorderLayout.CENTER);
		JPanel panel2=new JPanel();
		panel2.add(rank);
		panel2.add(history);
		add(panel2,BorderLayout.SOUTH);
		rank.setBackground(new Color(216,196,152));
		history.setBackground(new Color(216,196,152));
		panel2.setBackground(new Color(216,196,152));
		panel.setBackground(new Color(216,196,152));

		this.setTitle("象棋");
		this.setSize(900,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		rank.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.rank();
			}
		});

		history.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.history();
			}
		});
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		// TODO Auto-generated method stub
		if(e.getID()==WindowEvent.WINDOW_CLOSING){
			panel.leave();
		}
		super.processWindowEvent(e);
	}

}
