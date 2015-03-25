package monkey;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		FileOutputStream out = new FileOutputStream("c:/exec.bat");
	}
}
