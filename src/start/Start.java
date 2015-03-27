package start;

import java.awt.Font;

import javax.swing.UIManager;

import view.MonkeyFrame;

public class Start {
	public static void main(String[] args) {
		Font font = new Font("微软雅黑", Font.BOLD, 12);
		UIManager.put("Button.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("Label.font", font);
		MonkeyFrame monkeyFrame = new MonkeyFrame();
		System.out.println(monkeyFrame.getClass().getSimpleName());
	}
}
