package start;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;

import util.ADBUtil;
import util.MonkeyUtil;
import view.MonkeyFrame;
//com.yonyou.travelmanager2
//com.yonyou.helloworld

public class Start {
	public static void createUI() {
		Font font = new Font("宋体", Font.PLAIN, 12);
		UIManager.put("Button.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("Label.font", font);
		MonkeyFrame monkeyFrame = new MonkeyFrame();
		monkeyFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ADBUtil.deletePcFile(MonkeyUtil.BAT_PATH, MonkeyUtil.SH_PATH,
						MonkeyUtil.STOP_BAT_PATH);
			}
		});
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createUI();
			}
		});
	}
}
