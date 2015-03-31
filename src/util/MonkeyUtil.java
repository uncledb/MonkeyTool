package util;

import pojo.Monkey;

/**
 * 采用批处理的方式进行
 * 
 * @author admin
 * 
 */
public class MonkeyUtil {
	// 运行monkey的临时文件
	public static final String BAT_PATH = "c:/exec.bat";
	public static final String SH_PATH = "c:/cmd.sh";
	// 停止monkey的临时文件
	public static final String STOP_BAT_PATH = "c:/stop_exec.bat";

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
		// v2.0版本 2015-03-31
		ADBUtil.deletePcFile(BAT_PATH, SH_PATH);
		writeRunBat("adb shell \"" + monkeyCmd + "\"");
		return runBat(BAT_PATH);

		// 后续版本完善 存在一直卡住的问题 本版本未采用 < 定向sh文件
		// writeRunCh(monkeyCmd);
		// writeRunCh("exit");
		// writeRunBat("adb shell < " + SH_PATH);
		// writeRunBat("pause");
		// writeRunBat("exit");
		// return ADBUtil.runBat(BAT_PATH, SH_PATH);
	}

	/**
	 * 停止运行monkey
	 * 
	 * @return
	 */
	public static String stopMonkey() {
		String stopCmd = "for /f \"tokens=2\" %%a in ('adb shell ps ^|findstr \"monkey\" ') do adb shell kill %%a";
		System.out.println(stopCmd);
		ADBUtil.deletePcFile(STOP_BAT_PATH, null);
		writeStopBat(stopCmd);
		return runBat(STOP_BAT_PATH);
	}

	/**
	 * 写bat文件
	 */
	public synchronized static void writeRunBat(String cmd) {
		ADBUtil.writeBat(BAT_PATH, cmd);
	}

	/**
	 * 写sh文件
	 */
	public synchronized static void writeRunCh(String cmd) {
		ADBUtil.writeCh(SH_PATH, cmd);
	}

	/**
	 * 写bat文件 stopmonkey
	 */
	public synchronized static void writeStopBat(String cmd) {
		ADBUtil.writeBat(STOP_BAT_PATH, cmd);
	}

	/**
	 * 运行bat 并删除bat
	 */
	public static String runBat(String batPath) {
		return ADBUtil.runBat(batPath, null);
	}

}
