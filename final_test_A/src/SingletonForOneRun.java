import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SingletonForOneRun {

	final static String SINGLETON_FILE_NAME = "singletonTTT.dat";
	final static String SINGLETON_FILE_MODE = "rw";
	final static String SINGLETON_MESSAGE = "Only one stage can run. close the running stage\n"
			+ "by clicking on x (not the red square button).";
	final static int INSTANCE = 1;
	final static int NON_INSTANCE = 0;
	private static RandomAccessFile randomSingletonTTT;
	private static File fileSingletonTTT = new File(SINGLETON_FILE_NAME);

	public static int getInstance() {
		int x = 0;
		try {

			randomSingletonTTT = new RandomAccessFile(fileSingletonTTT, SINGLETON_FILE_MODE);
			randomSingletonTTT.seek(0);
			x = randomSingletonTTT.read();
			//System.out.println("x:" + x);
			randomSingletonTTT.close();
		}

		catch (EOFException ex) {

		} catch (IOException ex) {
			System.out.print("Error: getInstance" + ex);
			try {
				randomSingletonTTT.close();
			} catch (IOException e) {
				System.out.println("error closing file getInstance");
			}
			// System.exit(0);
		}

		return x;
	}

	public static void setInstance() {
		try {

			randomSingletonTTT = new RandomAccessFile(fileSingletonTTT, SINGLETON_FILE_MODE);
			randomSingletonTTT.seek(0);
			randomSingletonTTT.write(INSTANCE);
			randomSingletonTTT.close();
		}

		catch (IOException ex) {
			System.out.print("Error: setInstance" + ex);
			try {
				randomSingletonTTT.close();
			} catch (IOException e) {
				System.out.println("error closing file setInstance");
			}
			// System.exit(0);
		}
	}

	public static void resetInstance() throws IOException {
		randomSingletonTTT = new RandomAccessFile(fileSingletonTTT, SINGLETON_FILE_MODE);
		try {
			randomSingletonTTT.seek(0);
			randomSingletonTTT.write(NON_INSTANCE);

		} catch (

		IOException ex) {
			System.out.print("Error: resetInstance" + ex);
			System.exit(0);
		} finally {
			randomSingletonTTT.close();
		}

	}
}