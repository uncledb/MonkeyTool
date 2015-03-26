package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import pojo.Monkey;

public class MonkeyUtil {

	private static final String BAT_PATH = "c:/exec.bat";

	/**
	 * 命令行工具 运行用例 返回用例的运行结果
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
		// adb shell < cmd.txt
		writeBat("adb shell");
		writeBat(monkeyCmd);
		return runBat(BAT_PATH);
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
	 * 手机中的文件导入到电脑
	 */
	public static String pullXMLResultFile(String outFileName) {
		BufferedReader br = null;
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec(
					" adb pull /sdcard/monkeyResult.txt e:/Robo-Monkey/"
							+ outFileName + ".text");
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((result = br.readLine()) != null) {
				System.out.println("txt文件导入电脑得到的命令行返回结果是" + result);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("向电脑中导入txt文件出错...");
		}
		return result;
	}

	/**
	 * 写bat文件
	 */
	public synchronized static void writeBat(String cmd) {
		try {
			cmd = cmd + "\n";
			File file = new File(BAT_PATH);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream("c:/exec.bat", true);
			out.write(cmd.getBytes());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 运行bat 并删除bat
	 */
	public static String runBat(String batPath, boolean isRedirect) {
		if (isRedirect) {
			// 如果是重定向 复制到手机中
		}
		String result = "";
		String error = "";
		StringBuffer sb = new StringBuffer();
		StringBuffer errorsb = new StringBuffer();
		BufferedReader br = null;
		BufferedReader errorbr = null;
		Process p;
		try {
			p = Runtime.getRuntime().exec(batPath);
			br = new BufferedReader(new InputStreamReader(p.getInputStream(),
					"gbk"));
			errorbr = new BufferedReader(new InputStreamReader(
					p.getErrorStream(), "gbk"));
			while ((result = br.readLine()) != null) {
				sb.append(result);
			}
			while ((error = errorbr.readLine()) != null) {
				errorsb.append(error);
			}
			p.waitFor();

			File file = new File(batPath);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			File file = new File(batPath);
			if (file.exists()) {
				file.delete();
			}
		}
		System.out.println("最终得到的result结果是：" + sb.toString());
		System.out.println("最终得到的error结果是：" + errorsb.toString());

		return sb
				.append("\r\n")
				.append(errorsb.toString() == "" ? "" : "错误信息："
						+ errorsb.toString()).append("\r\n").toString();
	}

	/**
	 * 运行bat 并删除bat 不重定向
	 */
	public static String runBat(String batPath) {
		return runBat(batPath, false);
	}
}
