package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import pojo.Monkey;
import util.MonkeyUtil;

public class MonkeyFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	protected JPanel jPanel;
	protected JTextField jTextFeild_package;
	protected JTextField jTextFeild_seed;
	protected JTextField jTextFeild_throttle;
	protected JTextField jTextFeild_times;
	protected JScrollPane jScrollPane1;
	protected JTextArea textArea;

	protected JLabel jLable_package;
	protected JLabel jLable_seed;
	protected JLabel jLable_throttle;
	protected JLabel jLable_times;
	protected JButton btn_run;
	protected JButton btn_stop;
	protected JButton btn_export;
	protected String result = "";
	static Font font = new Font("微软雅黑", Font.BOLD, 12);

	public MonkeyFrame() {
		initComponents();
	}

	private void initComponents() {
		Container con = this.getContentPane();
		jPanel = new JPanel();
		BtnListener btnListener = new BtnListener();
		jLable_package = new JLabel("package:");
		jLable_seed = new JLabel("seed:");
		jLable_throttle = new JLabel("频率 :");
		jLable_times = new JLabel("次数 :");
		jTextFeild_package = new JTextField(41);
		jTextFeild_seed = new JTextField(10);
		jTextFeild_throttle = new JTextField(10);
		jTextFeild_times = new JTextField(10);
		btn_run = new JButton("run  monkey");
		btn_run.addActionListener(btnListener);
		btn_stop = new JButton("stop monkey");
		btn_stop.addActionListener(btnListener);
		btn_export = new JButton("导出结果到E盘/Robo-Monkey/");
		btn_export.addActionListener(btnListener);

		textArea = new JTextArea("提示窗口：" + "\r\n");
		jScrollPane1 = new JScrollPane(textArea);
		textArea.setColumns(40);
		textArea.setRows(15);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		jScrollPane1.setViewportView(textArea);
		textArea.setEditable(false);

		jPanel.add(jLable_package);
		jPanel.add(jTextFeild_package);
		jPanel.add(jLable_throttle);
		jPanel.add(jTextFeild_throttle);
		jPanel.add(jLable_seed);
		jPanel.add(jTextFeild_seed);
		jPanel.add(jLable_times);
		jPanel.add(jTextFeild_times);
		jPanel.add(btn_run);
		jPanel.add(btn_stop);
		jPanel.add(btn_export);
		jPanel.add(jScrollPane1);
		con.add(jPanel);
		setTitle("monkeyTool");
		setSize(600, 400);
		setLocation(400, 200);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);

	}

	class BtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btn_run) {
				System.out.println("点击了run monkey");
				String throttle = jTextFeild_throttle.getText().trim();
				String seed = jTextFeild_seed.getText().trim();
				String times = jTextFeild_times.getText().trim();
				String packageName = jTextFeild_package.getText().trim();
				Monkey monkey = new Monkey();
				monkey.setPackageName(packageName);
				monkey.setThrottle(throttle);
				monkey.setSeed(seed);
				monkey.setTimes(times);
				result = MonkeyUtil.runMonkey(monkey);
				textArea.append("run monkey：" + result + "\r\n");
			} else if (e.getSource() == btn_stop) {
				System.out.println("点击了stop monkey");
				result = MonkeyUtil.stopMonkey();
				textArea.append("stop monkey：" + result + "\r\n");
			} else if (e.getSource() == btn_export) {
				result = MonkeyUtil.pullXMLResultFile("monkeyResult");
				textArea.append("导出文件：" + result + "\r\n");
				System.out.println("导出结果到E盘/Robo-Monkey/");
			}
		}
	}

	public static void main(String[] args) {
		UIManager.put("Button.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("Label.font", font);
		MonkeyFrame monkeyFrame = new MonkeyFrame();
		System.out.println(monkeyFrame.getClass().getSimpleName());
	}

}
