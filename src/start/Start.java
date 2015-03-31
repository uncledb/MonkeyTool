package start;

import java.awt.Font;

import javax.swing.UIManager;

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
		@SuppressWarnings("unused")
		MonkeyFrame monkeyFrame = new MonkeyFrame();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createUI();
			}
		});
	}
}
