package com.lge.concoleutils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

class Frame extends JFrame implements ActionListener {

	private Dimension dimen, dimen1;
	private int xpos, ypos;
	private JLabel lb = new JLabel("파일 선택");

	private JTextArea ta = new JTextArea();

	private JButton btFind = new JButton("검색");
	private JButton btMerge = new JButton("머지 시작");
	private ArrayList<String> path_list = new ArrayList<String>();

	public Frame() {
		super("AndroidLogMerger");
		init();
		start();
		setSize(450, 320);

		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = getSize();

		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);

		setLocation(xpos, ypos);
		setVisible(true);

	}

	public void init() {
		// 화면 구성 넣을 부분
		setLayout(null);

		add(lb);

		add(ta);

		add(btFind);

		add(btMerge);

		lb.setBounds(20, 10, 90, 60);
		ta.setBounds(20, 70, 400, 150);
		ta.setAutoscrolls(true);

		btFind.setBounds(320, 30, 100, 20);
		btMerge.setBounds(20, 240, 400, 30);

		btFind.addActionListener(this);
		btMerge.addActionListener(this);
	}

	public void start() {
		// Event나 Thread 처리할 부분
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object obj = arg0.getSource();

		if (obj == btFind) {
			JFileChooser chooser = new JFileChooser();
			int result = chooser.showOpenDialog(this);

			File file = chooser.getSelectedFile();
			String path = file.getAbsolutePath();
			path_list.add(path);
			ta.setText(ta.getText() + path + '\n');
		} else if (obj == btMerge) {
			String[] pathArr = new String[path_list.size()];

			pathArr = path_list.toArray(pathArr);

			LogMerger m = new LogMerger(pathArr);
		}
	}
}

public class MainFrame {
	public static void main(String ar[]) {
		Frame f = new Frame();
	}
}
