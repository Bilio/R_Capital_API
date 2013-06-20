import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.sun.jna.Native;
import com.sun.jna.ptr.ShortByReference;

public class SWTTest extends java.lang.Thread {
	static FOnNotifyMarketTot fnmt;
	static FOnNotifyKLineData fnkld;
	public static ShortByReference sbr_tick = new ShortByReference((short) -1);

	public static void main(String[] args) {
		new SWTTest().start();
	}
	
	public void run() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(300, 200);
		shell.setText("�s�q����API");
		shell.setLayout(new RowLayout());
		final SKQuoteLib skquotelib = (SKQuoteLib) Native.loadLibrary(
				"SKQuoteLib", SKQuoteLib.class);
		System.out.println("start tw");

		final Button button = new Button(shell, SWT.PUSH);
		button.setText("��������");
		final Button close = new Button(shell, SWT.PUSH);
		close.setText("�����s�u");
		final Label connectionlabel = new Label(shell, SWT.SHADOW_IN);
		connectionlabel.setText("Connecting...");
		System.out.println("skquotelib =" + skquotelib);
		final int ini = skquotelib.SKQuoteLib_Initialize("ID","PASSWD");
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

		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				String tmp = "Status: "
						+ skquotelib.SKQuoteLib_RequestStocks(sbr_tick, "TX00,TSLD");
				connectionlabel.setText(tmp);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				String tmp = "Status: "
						+ skquotelib.SKQuoteLib_RequestStocks(sbr_tick, "TX00,TSLD");
				connectionlabel.setText(tmp);
			}
		});

		close.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				String tmp = "Status: " + skquotelib.SKQuoteLib_LeaveMonitor();
				;
				connectionlabel.setText(tmp);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				String tmp = "Status: " + skquotelib.SKQuoteLib_LeaveMonitor();
				;
				connectionlabel.setText(tmp);
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}