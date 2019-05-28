package gec.scf.dummy.bank.kbankws.core.config;

public class DelayProcess {

	// log4j variable
	public static void delay(int delay) {
		if (delay > 0) {
			// check configuration for delay time
			try {
				delay = delay > 360 ? 360 : delay;
				Thread.sleep(delay * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
