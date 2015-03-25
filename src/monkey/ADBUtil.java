package monkey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
}
