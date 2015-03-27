package view;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import pojo.Monkey;
import util.ADBUtil;
import util.MonkeyUtil;

public class MonkeyFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private Monkey monkey = null;
	private String pcLogPath = "D:\\";
	private String defaultThrottle = "200";
	private String defaultSeed = "20";
	private String defaultTimes = "100";

	protected JFileChooser jFileChooser;
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
		jTextFeild_seed.setText("20");
		jTextFeild_throttle = new JTextField(10);
		jTextFeild_throttle.setText("200");
		jTextFeild_times = new JTextField(10);
		jTextFeild_times.setText("100");
		btn_run = new JButton("run  monkey");
		btn_run.addActionListener(btnListener);
		btn_stop = new JButton("stop monkey");
		btn_stop.addActionListener(btnListener);
		btn_export = new JButton("运行结束 导出结果");
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
		// jPanel.add(jFileChooser);
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
				String packageName = jTextFeild_package.getText().trim();
				if (ADBUtil.isBlank(packageName)) {
					textArea.append("注意：请您这是要测试的包名" + "\r\n");
					return;
				}
				String throttle = jTextFeild_throttle.getText().trim();
				String seed = jTextFeild_seed.getText().trim();
				String times = jTextFeild_times.getText().trim();
				// 设置默认值
				throttle = ADBUtil.isBlank(throttle) == true ? defaultThrottle
						: throttle;
				seed = ADBUtil.isBlank(seed) == true ? defaultSeed : seed;
				times = ADBUtil.isBlank(times) == true ? defaultTimes : times;

				monkey = new Monkey();
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
				if (monkey != null) {
					jFileChooser = new JFileChooser();
					jFileChooser.setFileSelectionMode(1);// 设定只能选择到文件夹
					// 设置文件选择器的默认路径
					jFileChooser
							.setCurrentDirectory(new java.io.File(pcLogPath));
					// 设置窗口标题
					jFileChooser.setDialogTitle("选择保存目录");
					jFileChooser.setFont(new java.awt.Font("宋体", 0, 12));

					int state = jFileChooser.showSaveDialog(null);// 此句是打开文件选择器界面的触发语句
					if (state == 1) {
						return;// 撤销则返回
					} else {
						File f = jFileChooser.getSelectedFile();// f为选择到的目录
						pcLogPath = f.getAbsolutePath();
						System.out.println(f.getAbsolutePath());
						result = ADBUtil.pullFile(null,
								monkey.getShortLogFilePath(), pcLogPath);
						textArea.append("导出文件：" + result + "\r\n");
						System.out.println("导出结果到" + pcLogPath);
					}
				} else {
					textArea.append("提示：请先运行monkey命令" + "\r\n");

				}
			}
		}
	}
}
