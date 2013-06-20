import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sun.jna.Native;
import com.sun.jna.ptr.ShortByReference;

public class rJavaTest extends java.lang.Thread {
	static FOnNotifyMarketTot fnmt;
	static FOnNotifyKLineData fnkld;
	public static ShortByReference sbr_tick = new ShortByReference((short) -1);
	static List<String[]> tick = new ArrayList<String[]>();
	static int ini;
	static boolean OK = false;
	static SKQuoteLib skquotelib;
	
	public static List<String[]> getTick() {
		return tick;
	}
	
	public static void setTick(String[] input) {
		rJavaTest.tick.add(input);
	}

	private volatile static rJavaTest r;
	
	private rJavaTest(){}
	
	public static rJavaTest getInstance(){
		if (r == null) {
			synchronized (rJavaTest.class){
				if (r == null) {
					r = new rJavaTest();
				}
			}
		}
		return r;
	}
	
	public static String getTickStr() {
		List<String[]> result = rJavaTest.getInstance().getTick();
		String[] tick = result.get(0);
		String tmp = "";
		for(String s: tick)
			tmp += s + ",";
		return tmp;
	}
	
	public static void main(String[] args) {
		rJavaTest.getInstance().start();
		while (!rJavaTest.isOK()){}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rJavaTest.getInstance().connect();
		while (rJavaTest.getTick().size() == 0) {}
		System.out.println(rJavaTest.getInstance().getIni());
		List<String[]> result = rJavaTest.getInstance().getTick();
		System.out.println(rJavaTest.getInstance().getTickStr());
		//String[] sarray = result.get(0);
		//System.out.println(sarray);
		result = rJavaTest.getInstance().getTick();
		rJavaTest.getInstance().close();
	}
	
	public static void runThread() {
		rJavaTest.getInstance().start();
		while (!rJavaTest.isOK()){}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rJavaTest.getInstance().connect();
	}
	
	public void run() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		skquotelib = (SKQuoteLib) Native.loadLibrary(
				"SKQuoteLib", SKQuoteLib.class);
		System.out.println("start tw");
		System.out.println("skquotelib =" + skquotelib);
		ini = skquotelib.SKQuoteLib_Initialize("ID","PASSWD");
		System.out.println("inti " + ini);
		if (ini == 0) {
			// fnkld=new FOnNotifyKLineData();
			// fnmt = new FOnNotifyMarketTot(skquotelib,twse_ohlc);
			// fnq = new FOnNotifyQuote(skquotelib,twse_ohlc);
			// int kline = skquotelib.SKQuoteLib_AttachKLineDataCallBack(fnkld);
			int citime = skquotelib
					.SKQuoteLib_AttchServerTimeCallBack(new FOnNotifyServerTime(
							skquotelib));
			int connectioncb = skquotelib
					.SKQuoteLib_AttachConnectionCallBack(new FOnNotifyConnection(
							skquotelib));
			// int tot = skquotelib.SKQuoteLib_AttachMarketTotCallBack(fnmt);
			int c = skquotelib
					.SKQuoteLib_AttachQuoteCallBack(new FOnNotifyQuote(
							skquotelib));
			skquotelib.SKQuoteLib_EnterMonitor();
		}

		//shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static int getIni() {
		return ini;
	}
	
	public void connect() {
		String tmp = "Status: "
			+ skquotelib.SKQuoteLib_RequestStocks(sbr_tick, "TX00,TSLD");
		System.out.println(tmp);
	}
	
	public void close() {
		String tmp = "Status: " + skquotelib.SKQuoteLib_LeaveMonitor();
		System.out.println(tmp);
	}

	public static boolean isOK() {
		return OK;
	}

	public static void setOK(boolean oK) {
		OK = oK;
	}

}