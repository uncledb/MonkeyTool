package util;

import pojo.Monkey;

/**
 * 采用批处理的方式进行
 * 
 * @author admin
 * 
 */
public class MonkeyUtil {

	private static final String BAT_PATH = "c:/exec.bat";
	private static final String CH_PATH = "c:/cmd.ch";

	/**
	 * 运行monkey指令
	 */
	public static String runMonkey(Monkey monkey) {
		String monkeyCmd = "monkey -p "
				+ monkey.getPackageName()
				+ " --throttle "
				+ monkey.getThrottle()
				+ " -s "
				+ monkey.getSeed()
				+ " --ignore-crashes --ignore-timeouts --monitor-native-crashes -v -v -v "
				+ monkey.getTimes() + ">" + monkey.getLogFilePath();
		System.out.println(monkeyCmd);
		writeCh(monkeyCmd);
		writeBat("adb shell < " + CH_PATH);
		return ADBUtil.runBat(BAT_PATH, CH_PATH);
	}

	/**
	 * 停止运行monkey
	 * 
	 * @return
	 */
	public static String stopMonkey() {
		String stopCmd = "for /f \"tokens=2\" %%a in ('adb shell ps ^|findstr \"monkey\" ') do adb shell kill %%a";
		System.out.println(stopCmd);
		writeBat(stopCmd);
		return runBat(BAT_PATH);
	}

	/**
	 * 写bat文件
	 */
	public synchronized static void writeBat(String cmd) {
		ADBUtil.writeBat(BAT_PATH, cmd);
	}

	/**
	 * 写ch文件
	 */
	public synchronized static void writeCh(String cmd) {
		ADBUtil.writeCh(CH_PATH, cmd);
	}

	/**
	 * 运行bat 并删除bat
	 */
	public static String runBat(String batPath) {
		return ADBUtil.runBat(batPath, null);
	}
}
