package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ADBUtil {
	/**
	 * 查看设备adb shell
	 */
	public static final String MOBILE_INFO = "getprop ro.product.device";
	/**
	 * 查看品牌 brand model-- adb shell
	 */
	public static final String MOBILE_BRAND = "getprop ro.product.brand";

	/**
	 * brand model
	 */
	public static final String MOBILE_MODEL = "getprop ro.product.model";

	/**
	 * [ro.build.version.release]: [4.2.1] [ro.build.version.sdk]: [17] 系统版本 和
	 * sdk版本
	 */
	public static final String MOBILE_OSVERSION = "getprop ro.build.version.release";
	public static final String MOBILE_SDKAPI = "getprop ro.build.version.sdk";
	/**
	 * 产品序列号 adb shell
	 */
	public static final String MOBILE_NO = "getprop ro.serialno";
	/**
	 * 获得当前连接的设备
	 */
	public static final String DEVICES = "devices";
	/**
	 * 重启手机 指定某个手机重启待验证
	 */
	public static final String REBOOT = "reboot";
	/**
	 * 崩溃标识
	 */
	public static String CRASH = "shortMsg=Process crashed";
	/**
	 * 测试包名和对应测试运行类
	 */
	public static String TESTCLASS_PACKAGE = "com.xxx.xxxx.test/com.xxx.android.test.InstrumentationTestRunner";

	// 选择指定的设备 adb -s XXXX install 1.apk
	/**
	 * 执行命令
	 * 
	 * @param command
	 *            命令,设备号,命令中是否包含shell
	 */
	public static String runCommand(String Command, String device,
			boolean havaShell) {
		System.out.println("执行了runCommand");
		String result = "";
		String error = "";
		StringBuffer sb = new StringBuffer();
		StringBuffer errorsb = new StringBuffer();
		BufferedReader br = null;
		BufferedReader errorbr = null;
		if (null == device || "".equals(device)) {
			device = "";
		} else {
			device = " -s " + device;
		}
		try {
			Process p = null;
			if (havaShell) {
				p = Runtime.getRuntime().exec(
						"adb " + device + " shell " + Command);
				System.out.println("adb " + device + " shell " + Command);
			} else {
				p = Runtime.getRuntime().exec("adb " + device + Command);
			}
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			errorbr = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			while ((result = br.readLine()) != null) {
				// System.out.println("运行用例时得到的命令行返回结果是" + result);
				sb.append(result);
			}
			while ((error = errorbr.readLine()) != null) {
				// System.out.println("运行用例时得到的命令行返回error结果是" + error);
				errorsb.append(error);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("运行命令出错...");
		} finally {
			if (br != null) {
				try {
					br.close();
					errorbr.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("关闭流出错...");
				}
			}
		}

		System.out.println("执行命令" + Command + "的结果是：" + sb.toString());
		System.out
				.println("执行命令" + Command + "的error结果是：" + errorsb.toString());
		return sb.append(errorsb).toString();
	}

	/**
	 * 执行命令
	 * 
	 * @param command
	 */
	public static String runCommand(String command) {
		BufferedReader br = null;
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec(command);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((result = br.readLine()) != null) {
				System.out.println("执行命令返回结果是" + result);
				break;
			}
		} catch (IOException e) {
			System.out.println("运行cmd命令出错...");
		}
		return result;
	}

	/**
	 * 获得所有的设备
	 * 
	 * @return
	 */
	public static List<String> getDevices() {
		List<String> devices = new ArrayList<String>();
		String result = runCommand(DEVICES, null, false);
		if ((!result.contains("error")) && result.lastIndexOf("device") != 8
				&& !result.contains("started")) {
			result = result.replace(" ", "");
			result = result.replace("\t", "");
			result = result.replace("Listofdevicesattached", "");
			result = result.replace("device", ",");
			result = result.replace("offline", ",");
			// System.out.println(result);
			String[] s = result.split(",");
			for (String string : s) {
				// System.out.println(string);
				devices.add(string);
			}
		} else {
			System.out.println("未发现设备");
		}
		return devices;
	}

	/**
	 * 得到设备的序列和名称的键值对
	 * 
	 * @return
	 */
	public static Map<String, String> getDeviceName() {
		Map<String, String> deviceNames = new HashMap<String, String>();
		List<String> devices = getDevices();
		for (String device : devices) {
			String brand = runCommand(MOBILE_BRAND, device, true);
			String model = runCommand(MOBILE_MODEL, device, true);
			String osVersion = runCommand(MOBILE_OSVERSION, device, true);
			String sdkApi = runCommand(MOBILE_SDKAPI, device, true);

			deviceNames.put(device, brand + "_" + model + " Android "
					+ osVersion + " API " + sdkApi);
		}
		return deviceNames;
	}

	/**
	 * 文件导入到手机中
	 */
	public static String pushFile(String device, String pcPath, String phonePath) {
		BufferedReader br = null;
		String result = "";
		if (null == device || "".equals(device)) {
			device = "";
		} else {
			device = " -s " + device;
		}
		try {
			Process p = Runtime.getRuntime().exec(
					"adb " + device + " push " + pcPath + " " + phonePath);
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((result = br.readLine()) != null) {
				System.out.println("导入文件得到的命令行返回结果是" + result);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("向手机中导入文件出错...");
		}
		return result;
	}

	/**
	 * 手机中的文件导入到电脑
	 */
	public static String pullFile(String device, String phonePath, String pcPath) {
		BufferedReader br = null;
		String result = "";
		if (null == device || "".equals(device)) {
			device = "";
		} else {
			device = " -s " + device;
		}
		try {
			Process p = Runtime.getRuntime().exec(
					" adb " + device + " pull " + phonePath + " " + pcPath);
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((result = br.readLine()) != null) {
				System.out.println("文件导入电脑得到的命令行返回结果是" + result);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("向电脑中导入文件出错...");
		}
		if (null == result) {
			result = "";
		}
		return result;
	}

	/**
	 * 命令行工具 运行用例 返回用例的运行结果
	 */
	public static String runTest(String className, String device) {
		System.out.println("执行了runTest方法");
		String result = "";
		String error = "";
		StringBuffer sb = new StringBuffer();
		StringBuffer errorsb = new StringBuffer();
		BufferedReader br = null;
		BufferedReader errorbr = null;
		if (null == device || "".equals(device)) {
			device = "";
		} else {
			device = " -s " + device;
		}
		try {
			Process p = Runtime.getRuntime().exec(
					"adb " + device + " shell am instrument -e class "
							+ className + " -w " + TESTCLASS_PACKAGE);
			System.out.println("adb " + device
					+ " shell am instrument -e class " + className + " -w "
					+ TESTCLASS_PACKAGE);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			errorbr = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			while ((result = br.readLine()) != null) {
				// System.out.println("运行用例时得到的命令行返回结果是" + result);
				sb.append(result);
			}
			while ((error = errorbr.readLine()) != null) {
				// System.out.println("运行用例时得到的命令行返回error结果是" + error);
				errorsb.append(error);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("运行命令出错...");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("关闭流出错...");
				}
			}
		}

		System.out.println("最终得到的result结果是：" + sb.toString());
		System.out.println("最终得到的error结果是：" + errorsb.toString());
		return sb.append(errorsb).toString();
	}

	/**
	 * 将截图复制到电脑上 并且删除文件夹内的所有文件 以免重复导入 /sdcard/Robotium-Screenshots/ 方法待完善
	 * 
	 * @return
	 */
	public static String copyPicture(String device, String phonePath,
			String pcPath) {
		System.out.println("执行了runTest方法");
		String result = "";
		String error = "";
		StringBuffer sb = new StringBuffer();
		StringBuffer errorsb = new StringBuffer();
		BufferedReader br = null;
		BufferedReader errorbr = null;
		if (null == device || "".equals(device)) {
			device = "";
		} else {
			device = " -s " + device;
		}
		try {
			Process p = Runtime.getRuntime().exec("adb " + device + " shell");
			p = Runtime.getRuntime().exec("pull " + phonePath + " " + pcPath);
			p = Runtime.getRuntime().exec("su");
			p = Runtime.getRuntime().exec("cd /sdcard/");
			p = Runtime.getRuntime().exec("rm -r Robotium-Screenshots");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			errorbr = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			while ((result = br.readLine()) != null) {
				// System.out.println("运行用例时得到的命令行返回结果是" + result);
				sb.append(result);
			}
			while ((error = errorbr.readLine()) != null) {
				// System.out.println("运行用例时得到的命令行返回error结果是" + error);
				errorsb.append(error);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("运行命令出错...");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("关闭流出错...");
				}
			}
		}

		System.out.println("最终得到的result结果是：" + sb.toString());
		System.out.println("最终得到的error结果是：" + errorsb.toString());
		return sb.append(errorsb).toString();
	}

	/**
	 * 运行用例 判断是否需要重跑 返回是否运行通过
	 * 
	 * @param TestCase
	 * @return
	 */
	public static boolean runTestCase(String TestCase, String device) {
		boolean isPass = false;
		for (int i = 0; i < 2; i++) {
			String message = runTest(TestCase, device);
			if (message.contains(CRASH)) {
				System.out.println("@@@@@@@@@@@@@@@ 重复执行了:" + TestCase
						+ " @@@@@@@@@@@@@@@@@@");
				continue;
			} else {
				if (message.contains("error") || message.contains("Error")) {
					isPass = false;
					break;
				}
				isPass = true;
				break;
			}
		}
		return isPass;
	}

	/**
	 * 得到静态字段的值 public static final修饰的 没用了这个方法 现在是引入外部xml文件
	 * 
	 * @return
	 */
	public static List<String> getFinalStrings() {
		List<String> list = new ArrayList<String>();
		Field[] fields = ADBUtil.class.getDeclaredFields();
		for (Field field : fields) {
			// 属性的修饰
			String descriptor = Modifier.toString(field.getModifiers());
			descriptor = descriptor.equals("") == true ? "" : descriptor + " ";
			if (descriptor.contains("public static final")) {
				list.add(field.getName());
			}
		}
		return list;
	}

	/**
	 * 写bat文件
	 */
	public synchronized static void writeBat(String path, String cmd) {
		try {
			cmd = cmd + "\n";
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(path, true);
			out.write(cmd.getBytes());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写ch文件
	 */
	public synchronized static void writeCh(String path, String cmd) {
		try {
			cmd = cmd + "\n";
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(path, true);
			out.write(cmd.getBytes());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 运行bat 并删除bat
	 */
	public static String runBat(String batPath, String chPath) {
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			File file = new File(batPath);
			if (file.exists()) {
				file.delete();
			}
			if (!(null == chPath || "".equals(chPath))) {
				file = new File(chPath);
				if (file.exists()) {
					file.delete();
				}
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
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getTime() {
		String time = "";
		Date date = new Date();
		SimpleDateFormat s = new SimpleDateFormat("MMdd_hhmm");
		time = s.format(date);
		System.out.println(time);
		return time;
	}

	/**
	 * 判断String是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		return false;
	}
}
