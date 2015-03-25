package monkey;

public class Monkey {
	private String throttle;// 执行频率
	private String seed;// 事件序列
	private boolean ignoreCrashes;// 是否忽略崩溃
	private String times;// 次数
	private String logFilePath = "/sdcard/monkeyResult.txt 2>&1";// /sdcard/testResult.txt
																	// 2>&1
																	// 日志文件路径
	private String packageName;

	private String device;

	public String getThrottle() {
		return throttle;
	}

	public void setThrottle(String throttle) {
		this.throttle = throttle;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public boolean isIgnoreCrashes() {
		return ignoreCrashes;
	}

	public void setIgnoreCrashes(boolean ignoreCrashes) {
		this.ignoreCrashes = ignoreCrashes;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
