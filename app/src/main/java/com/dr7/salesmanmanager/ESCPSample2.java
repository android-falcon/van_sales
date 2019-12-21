package com.dr7.salesmanmanager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.command.ESCPOSConst;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;
import com.sewoo.jpos.request.RequestQueue;

import static com.dr7.salesmanmanager.PrintPayment.pay1;
import static com.dr7.salesmanmanager.PrintPayment.paymentPrinter;
import static com.dr7.salesmanmanager.PrintVoucher.items;
import static com.dr7.salesmanmanager.PrintVoucher.vouch1;
import static com.dr7.salesmanmanager.ReceiptVoucher.payment;
import static com.dr7.salesmanmanager.ReceiptVoucher.paymentsforPrint;
import static com.dr7.salesmanmanager.Reports.CashReport.cash;
import static com.dr7.salesmanmanager.Reports.CashReport.cashPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.credit;
import static com.dr7.salesmanmanager.Reports.CashReport.creditPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.date;
import static com.dr7.salesmanmanager.Reports.CashReport.net;
import static com.dr7.salesmanmanager.Reports.CashReport.returnCash;
import static com.dr7.salesmanmanager.Reports.CashReport.returnCridet;
import static com.dr7.salesmanmanager.Reports.CashReport.total;
import static com.dr7.salesmanmanager.Reports.CashReport.total_cash;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrint;
import static com.dr7.salesmanmanager.SalesInvoice.voucher;

public class ESCPSample2
{
	private ESCPOSPrinter posPtr;
	private final char ESC = ESCPOS.ESC;
	private final char LF = ESCPOS.LF;
	Voucher voucherforPrint;
	List<Item> itemforPrint;
	DatabaseHandler obj;
	DecimalFormat decimalFormat;
	List<Payment>payList;
	Payment payforBank;
	Context context;
	public ESCPSample2(Context context)
	{
		posPtr = new ESCPOSPrinter();
		obj = new DatabaseHandler(context);
		decimalFormat = new DecimalFormat("00.00");
		this.context=context;
//		posPtr = new ESCPOSPrinter("EUC-KR"); // Korean.
//		posPtr = new ESCPOSPrinter("BIG5"); // Big5
	}

	public void receipt() throws UnsupportedEncodingException
	{
		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "Receipt" + LF + LF);
		posPtr.printNormal(ESC + "|rA" + ESC + "|bC" + "TEL (123)-456-7890" + LF);
		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + "Thank you for coming to our shop!" + LF + LF);

		posPtr.printNormal(ESC + "|cA" +"Chicken                   $10.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Hamburger                 $20.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Pizza                     $30.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Lemons                    $40.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Drink                     $50.00" + LF + LF);
		posPtr.printNormal(ESC + "|cA" +"Excluded tax             $150.00" + LF);

		posPtr.printNormal( ESC + "|cA" +ESC + "|uC" + "Tax(5%)                    $7.50" + LF);
		posPtr.printNormal( ESC + "|cA" +ESC + "|bC" + ESC + "|2C" + "Total   $157.50" + LF + LF);
		posPtr.printNormal( ESC + "|cA" +ESC + "|bC" + "Payment                  $200.00" + LF);
		posPtr.printNormal( ESC + "|cA" +ESC + "|bC" + "Change                    $42.50" + LF);
	}

	public int sample() throws UnsupportedEncodingException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		receipt();
		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|4C" + "Thank you" + LF);
//    	posPtr.printNormal("測試");
		posPtr.lineFeed(3);
		return 0;
	}

	public int barcode2d() throws UnsupportedEncodingException
	{
		String data = "ABCDEFGHIJKLMN";
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printString("PDF417\r\n");
		posPtr.printPDF417(data, data.length(), 0, 10, ESCPOSConst.LK_ALIGNMENT_CENTER);
		posPtr.printPDF417(data, data.length(), 4, 3, ESCPOSConst.LK_ALIGNMENT_CENTER);
		posPtr.printString("QRCode\r\n");
		posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);

		posPtr.printString("DDDD\r\n");
//        receipt();
		posPtr.printBarCode("1234567890", LKPrint.LK_BCS_Code39, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
//        posPtr.printNormal(ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "Thank you" + LF);

		posPtr.lineFeed(4);
//	    posPtr.cutPaper();
		return 0;
	}
	public int barcodesample() throws UnsupportedEncodingException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printBarCode("1234567890", LKPrint.LK_BCS_Code39, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("0123498765", LKPrint.LK_BCS_Code93, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("0987654321", LKPrint.LK_BCS_ITF, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("{ACODE 128", LKPrint.LK_BCS_Code128, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("{BCode 128", LKPrint.LK_BCS_Code128, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("{C12345", LKPrint.LK_BCS_Code128, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("A1029384756A", LKPrint.LK_BCS_Codabar, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printNormal(ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "Thank you" + LF);
		posPtr.lineFeed(3);
		return 0;
	}

	public int imageTest() throws IOException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printBitmap("//sdcard//temp//test//logo_s.jpg", LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.printBitmap("//sdcard//temp//test//sample_2.jpg", LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.printBitmap("//sdcard//temp//test//sample_3.jpg", LKPrint.LK_ALIGNMENT_LEFT);
		posPtr.printBitmap("//sdcard//temp//test//sample_4.jpg", LKPrint.LK_ALIGNMENT_RIGHT);

		Bitmap _bitmap = BitmapFactory.decodeFile("//sdcard//temp//test//logo_s.jpg");
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_CENTER, 0, 1);

		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_LEFT, 1);
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_LEFT, 2);
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_LEFT, 3);

		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|4C" + "Thank you" + LF);

		posPtr.lineFeed(3);
		return 0;
	}

	public int imageTest1() throws IOException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printBitmap("//sdcard//temp//test//1512301638.png", LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.lineFeed(3);
		return 0;
	}

	public int invoice() throws UnsupportedEncodingException
	{
/*
    	posPtr.setCharSet("UTF-8");

		// Setting PageMode
		posPtr.setPageMode(true);
    	// 180 DPI or 203 DPI
		// 180 DPI - 7 dot per 1mm
    	// 203 DPI - 8 dot per 1mm
    	posPtr.setDPI(203);
    	// Print direction.
		posPtr.setPrintDirection(ESCPOSConst.DIRECTION_LEFT_RIGHT);
    	// 399 dot x 630 dot.
		posPtr.setPrintingArea(399, 630);

    	// Data
    	// Medium Text (20, 20)
    	posPtr.setAbsoluteVertical(20);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("丟並乾亂佔佪亙", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_2WIDTH | LKPrint.LK_TXT_2HEIGHT);

    	// Large Text
	    posPtr.setAbsoluteVertical(90);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("伋伕佇佈", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

	    // Must be Off Unicode when print Alphabet or print barcode.
	    posPtr.setCharSet("Big5");

	    posPtr.setAbsoluteVertical(190);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("ABCDE", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

	    posPtr.setCharSet("UTF-8");

    	// Small Text
	    posPtr.setAbsoluteVertical(300);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("壓壘壙壚壞壟壢壩壯壺", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH | LKPrint.LK_TXT_1HEIGHT);

	    // Must be Off Unicode when print Alphabet or print barcode.
    	posPtr.setCharSet("Big5");

    	// 1D Barcode
    	posPtr.setAbsoluteVertical(380);
    	posPtr.setAbsoluteHorizontal(0);
    	posPtr.printBarCode("0123456789012345678901", ESCPOSConst.LK_BCS_Code39, 40, 1, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);
//    	    	posPtr.printBarCode("0123498765", ESCPOSConst.LK_BCS_Code93, 40, 2, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);

    	// QRCODE
    	String data = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
    	posPtr.setAbsoluteVertical(450);
    	posPtr.setAbsoluteHorizontal(40);
    	posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);
    	posPtr.setAbsoluteVertical(450);
    	posPtr.setAbsoluteHorizontal(240);
    	posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);

    	// Data
	    posPtr.printPageModeData();
    	posPtr.setPageMode(false);
    	posPtr.lineFeed(4);
    	return 0;
*/
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.setCharSet("Big5");

		// Setting PageMode
		posPtr.setPageMode(true);
		// 180 DPI or 203 DPI
		// 180 DPI - 7 dot per 1mm
		// 203 DPI - 8 dot per 1mm
		posPtr.setDPI(203);
		// Print direction.
		posPtr.setPrintDirection(ESCPOSConst.DIRECTION_LEFT_RIGHT);
		// 399 dot x 630 dot.
		posPtr.setPrintingArea(399, 730); // al

		// Data
		// Medium Text (20, 20)
		posPtr.setAbsoluteVertical(20);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("丟並乾亂佔佪亙", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_2WIDTH | LKPrint.LK_TXT_2HEIGHT);

		// Large Text
		posPtr.setAbsoluteVertical(90);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("伋伕佇佈", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

		// Must be Off Unicode when print Alphabet or print barcode.
		posPtr.setCharSet("Big5");

		posPtr.setAbsoluteVertical(190);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("ABCDE", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

		posPtr.setCharSet("Big5");

		// Small Text
		posPtr.setAbsoluteVertical(300);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("壓壘壙壚壞壟壢壩壯壺桌號菇", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH | LKPrint.LK_TXT_1HEIGHT);

		// Must be Off Unicode when print Alphabet or print barcode.
		//posPtr.setCharSet("Big5");

		// 1D Barcode
		posPtr.setAbsoluteVertical(380);
		posPtr.setAbsoluteHorizontal(0);
		posPtr.printBarCode("0123456789012345678901", ESCPOSConst.LK_BCS_Code39, 40, 1, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);
//    	    	posPtr.printBarCode("0123498765", ESCPOSConst.LK_BCS_Code93, 40, 2, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);

		// QRCODE
		String data = "中華民國萬萬稅1234567890123456789012345678901234567890123456789012345678";
		posPtr.setAbsoluteVertical(450);
		posPtr.setAbsoluteHorizontal(40);
		posPtr.printQRCode(data, data.length(), 5, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);
		posPtr.setAbsoluteVertical(450);
		posPtr.setAbsoluteHorizontal(240);
		posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);

		// Data
		posPtr.printPageModeData();
		posPtr.setPageMode(false);
		posPtr.lineFeed(4);
		return 0;

	}

	public int printDataMatrix() throws UnsupportedEncodingException
	{
		// DataMatrix
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		String data = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		posPtr.printDataMatrix(data, data.length(), 6, ESCPOSConst.LK_ALIGNMENT_CENTER);

		posPtr.lineFeed(4);
		return 0;
	}

	public int printAndroidFont() throws UnsupportedEncodingException
	{
		int nLineWidth = 384;
		String data = "Receipt";
//    	String data = "영수증";
		Typeface typeface = null;

		try
		{
			int sts;

			sts = posPtr.printerCheck();
			if(sts != ESCPOSConst.LK_STS_NORMAL)
			{
				return sts;
			}

			sts = posPtr.status();
			if(sts != ESCPOSConst.LK_STS_NORMAL)
			{
				return sts;
			}

			posPtr.setAsync(false);

			posPtr.printAndroidFont(data, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(2);
			posPtr.printAndroidFont("Left Alignment", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont("Center Alignment", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont("Right Alignment", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, "SANS_SERIF : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, "SERIF : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(typeface.MONOSPACE, "MONOSPACE : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, "SANS : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, "SANS BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, false, "SANS BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, false, true, "SANS ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, true, "SANS BOLD ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, true, true, "SANS B/I/U : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.SERIF, "SERIF : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, "SERIF BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, false, "SERIF BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, false, true, "SERIF ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, true, "SERIF BOLD ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, true, true, "SERIF B/I/U : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.MONOSPACE, "MONOSPACE : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, "MONO BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, false, "MONO BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, false, true, "MONO ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, true, "MONO BOLD ITALIC: 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, true, true, "MONO B/I/U: 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(4);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public int printMultilingualFont() throws UnsupportedEncodingException
	{
		int nLineWidth = 550;
		String Koreandata = "영수증";
		String Turkishdata = "Turkish(İ,Ş,Ğ)";
		String Russiandata = "Получение";
		String Arabicdata = "الإيصال";
		String Greekdata = "Παραλαβή";
		String Japanesedata = "領収書";
		String GB2312data = "收据";
		String BIG5data = "收據";

//		try
//		{
			int sts;

//        	sts = posPtr.printerCheck();
//    		if(sts != ESCPOSConst.LK_STS_NORMAL)
//    		{
//    			return sts;
//    		}
//
//        	sts = posPtr.status();
//    		if(sts != ESCPOSConst.LK_STS_NORMAL)
//    		{
//    			return sts;
//    		}

			posPtr.setAsync(false);

//    		posPtr.printAndroidFont("Korean Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Korean 100-dot size font in android device.
//        	posPtr.printAndroidFont(Koreandata, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);

//			posPtr.printAndroidFont("Turkish Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Turkish 50-dot size font in android device.
//        	posPtr.printAndroidFont(Turkishdata, nLineWidth, 50, ESCPOSConst.LK_ALIGNMENT_CENTER);

//			posPtr.printAndroidFont("Russian Font", 384, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Russian 60-dot size font in android device.
//        	posPtr.printAndroidFont(Russiandata, nLineWidth, 60, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("Arabic Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			// Arabic 100-dot size font in android device.
//			posPtr.printAndroidFont(Arabicdata, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);


//			Log.e("ifEnglish",""+ifEnglish('a'));
//		Log.e("ifEnglish",""+ifEnglish('1'));
//		Log.e("ifEnglish",""+ifEnglish('V'));
//		Log.e("ifEnglish",""+ifEnglish('m'));
//		Log.e("ifEnglish",""+ifEnglish(';'));

//			posPtr.printAndroidFont("Greek Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Greek 60-dot size font in android device.
//        	posPtr.printAndroidFont(Greekdata, nLineWidth, 60, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("Japanese Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//        	// Japanese 100-dot size font in android device.
//        	posPtr.printAndroidFont(Japanesedata, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("GB2312 Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// GB2312 100-dot size font in android device.
//        	posPtr.printAndroidFont(GB2312data, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("BIG5 Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// BIG5 100-dot size font in android device.
//        	posPtr.printAndroidFont(BIG5data, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);

			posPtr.lineFeed(4);
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return 0;
	}


	public void printMultilingualFontEsc(int count) throws UnsupportedEncodingException {
		if (count == 0) {
			voucherforPrint = vouch1;
			itemforPrint = items;
		} else {
			voucherforPrint = voucher;
			itemforPrint = itemForPrint;
		}
		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "";
			switch (voucherforPrint.getVoucherType()) {
				case 504:
					voucherTyp = "فاتورة بيع";
					break;
				case 506:
					voucherTyp = "فاتورة مرتجعات";
					break;
				case 508:
					voucherTyp = "طلب جديد";
					break;
			}

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//			public int printAndroidFont(Typeface typeface, boolean isBold, String textString, int widthDots, int textSize, int alignment)
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

			posPtr.printAndroidFont(null,true,companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"هاتف : " + companyInfo.getcompanyTel()+"    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"رقم الفاتورة : " + voucherforPrint.getVoucherNumber()+"    " + "          التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"اسم العميل   : " + voucherforPrint.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"ملاحظة        : " + voucherforPrint.getRemark() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"نوع الفاتورة : " +voucherTyp+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"طريقة الدفع  : " +  (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا")+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty=0;
				posPtr.printAndroidFont(null,true," السلعة              " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" +  "سعر الوحدة" + "\t\t\t" +"المجموع" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
								space =  space+" " ;
							}
							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 10);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}


					}
				}
			} else {
				total_Qty=0;
				posPtr.printAndroidFont(  null,true," السلعة              " + "العدد" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());


						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							int m=(12 - itemforPrint.get(i).getItemName().length());
							Log.e("true22",""+m);

							for (int g = 0; g <m ; g++) {
								space =  space+"   ";
							}
							char nam=itemforPrint.get(i).getItemName().charAt(0);
							if(!ifEnglish(nam)){
								Log.e("true22",""+nam);
								posPtr.printAndroidFont(null,true,  space + "     " + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
							}else {
								Log.e("false22",""+nam);
								posPtr.printAndroidFont(null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t" +itemforPrint.get(i).getPrice()  + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" +space + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);

							}

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 12);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(12, itemforPrint.get(i).getItemName().length() - 1);

							char nam=itemforPrint.get(i).getItemName().charAt(0);
							if(!ifEnglish(nam)) {
								posPtr.printAndroidFont( null,true,  space + "     "  + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
								Log.e("true233",""+nam);
							}else {
								posPtr.printAndroidFont( null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t"  +itemforPrint.get(i).getPrice()  + "\t\t\t\t" +itemforPrint.get(i).getQty()  + "\t\t\t\t" +space  + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);
								Log.e("false233",""+nam);

							}
						}

					}
				}

			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "اجمالي الكمية  : " + total_Qty + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "المجموع  : " + voucherforPrint.getSubTotal() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الضريبة  : " + voucherforPrint.getTax() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الصافي   : " + voucherforPrint.getNetSales() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true,  "المستلم : ________________ التوقيع : __________" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void printMultilingualFontEsc2(int count) throws UnsupportedEncodingException {
		if (count == 0) {
			voucherforPrint = vouch1;
			itemforPrint = items;
		} else {
			voucherforPrint = voucher;
			itemforPrint = itemForPrint;
		}
		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "";
			switch (voucherforPrint.getVoucherType()) {
				case 504:
					voucherTyp = "فاتورة بيع";
					break;
				case 506:
					voucherTyp = "فاتورة مرتجعات";
					break;
				case 508:
					voucherTyp = "طلب جديد";
					break;
			}

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//			public int printAndroidFont(Typeface typeface, boolean isBold, String textString, int widthDots, int textSize, int alignment)
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

//			posPtr.printAndroidFont(null,true,companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"هاتف : " + companyInfo.getcompanyTel()+"    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"رقم الفاتورة : " + voucherforPrint.getVoucherNumber()+"    " + "          التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"اسم العميل   : " + voucherforPrint.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"ملاحظة        : " + voucherforPrint.getRemark() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"نوع الفاتورة : " +voucherTyp+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"طريقة الدفع  : " +  (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا")+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty=0;
				posPtr.printAndroidFont(null,true," السلعة              " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" +  "سعر الوحدة" + "\t\t\t" +"المجموع" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
								space =  space+" " ;
							}
							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 10);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}


					}
				}
			} else {
				total_Qty=0;
				posPtr.printAndroidFont(  null,true," السلعة              " + "العدد" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());

						posPtr.printAndroidFont(null,true,  itemforPrint.get(i).getItemName()+"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						posPtr.printAndroidFont(null,true,   "   "+convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t\t\t\t\t" +itemforPrint.get(i).getQty() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                        posPtr.printAndroidFont(  null,true,"---------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


//						if (itemforPrint.get(i).getItemName().length() <= 12) {
//							String space = itemforPrint.get(i).getItemName();
//							int m=(12 - itemforPrint.get(i).getItemName().length());
//							Log.e("true22",""+m);
//
//							for (int g = 0; g <m ; g++) {
//								space =  space+"   ";
//							}
//							char nam=itemforPrint.get(i).getItemName().charAt(0);
//							if(!ifEnglish(nam)){
//								Log.e("true22",""+nam);
//								posPtr.printAndroidFont(null,true,  itemforPrint.get(i).getItemName()+"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//								posPtr.printAndroidFont(null,true,   "\t\t\t\t\t\t\t"+itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//							}else {
//								Log.e("false22",""+nam);
//								posPtr.printAndroidFont(null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t" +itemforPrint.get(i).getPrice()  + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" +space + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);
//
//							}
//
//						} else {
//							String space = itemforPrint.get(i).getItemName().substring(0, 12);
////                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
////                            space = " " + space;
////                        }
//							String fullString = itemforPrint.get(i).getItemName().substring(12, itemforPrint.get(i).getItemName().length() - 1);
//
//							char nam=itemforPrint.get(i).getItemName().charAt(0);
//							if(!ifEnglish(nam)) {
//								posPtr.printAndroidFont( null,true,  space + "     "  + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//								Log.e("true233",""+nam);
//							}else {
//								posPtr.printAndroidFont( null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t"  +itemforPrint.get(i).getPrice()  + "\t\t\t\t" +itemforPrint.get(i).getQty()  + "\t\t\t\t" +space  + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);
//								Log.e("false233",""+nam);
//
//							}
//						}

					}
				}

			}

//			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(  null,true, "اجمالي الكمية  : " + total_Qty + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "المجموع  : " + voucherforPrint.getSubTotal() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "الضريبة  : " + voucherforPrint.getTax() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "الصافي   : " + voucherforPrint.getNetSales() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true,  "المستلم : ________________ التوقيع : __________" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	public void printMultilingualFontEsc3(int count, Voucher voucherforPrint, List<Item>itemforPrint) throws UnsupportedEncodingException {

//		if (count == 0) {
//			voucherforPrint = vouch1;
//			itemforPrint = items;
//		} else {
//			voucherforPrint = voucher;
//			itemforPrint = itemForPrint;
//		}


		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "فاتورة بيع";
			switch (voucherforPrint.getVoucherType()) {
				case 504:
					voucherTyp = "فاتورة بيع";
					break;
				case 506:
					voucherTyp = "فاتورة مرتجعات";
					break;
				case 508:
					voucherTyp = "طلب جديد";
					break;
			}

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			if(companyInfo.getLogo()!=null) {
				posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER);
				Log.e("12222print","pyyy");
			}
			posPtr.printAndroidFont(null,true,companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"هاتف : " + companyInfo.getcompanyTel()+"    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"رقم الفاتورة : " + voucherforPrint.getVoucherNumber()+"    " + "          التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"اسم  المندوب    : " +obj.getAllSettings().get(0).getSalesMan_name() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"اسم العميل   : " + voucherforPrint.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"ملاحظة        : " + voucherforPrint.getRemark() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"نوع الفاتورة : " +voucherTyp+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"طريقة الدفع  : " +  (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا")+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty=0;
				posPtr.printAndroidFont(null,true," السلعة                          " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" +  "سعر الوحدة" + "\t\t\t" +"المجموع" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
								space =  space+" " ;
							}
							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 10);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}


					}
				}
			} else {
				total_Qty=0;
				posPtr.printAndroidFont(  null,true," السلعة                          " + "العدد" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
		             	posPtr.printBitmap(itemPrint(itemforPrint.get(i).getPrice()+"",convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))),itemforPrint.get(i).getQty()+"",itemforPrint.get(i).getItemName()),ESCPOSConst.LK_ALIGNMENT_CENTER,550);

					}
				}

			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "اجمالي الكمية  : " + total_Qty + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "المجموع  : " + voucherforPrint.getSubTotal() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الضريبة  : " + voucherforPrint.getTax() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الصافي   : " + voucherforPrint.getNetSales() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true,  "المستلم : ________________ التوقيع : __________" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

Bitmap itemPrint (String prices,String totals,String qtys,String items){


	final Dialog dialogs = new Dialog(context);
	dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialogs.setCancelable(true);
	dialogs.setContentView(R.layout.tabres);

	TextView price,total,qty,item;

	price=(TextView)dialogs.findViewById(R.id.price);
	total=(TextView)dialogs.findViewById(R.id.total);
	qty=(TextView)dialogs.findViewById(R.id.qty);
	item=(TextView)dialogs.findViewById(R.id.ittem);
	LinearLayout linearView=(LinearLayout)dialogs.findViewById(R.id.tab);
	price.setText(prices);
	total.setText(totals);
	qty.setText(qtys);
	item.setText(items);

	linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
			View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

	Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());
	Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(bitmap);
	Drawable bgDrawable = linearView.getBackground();
	if (bgDrawable != null) {
		bgDrawable.draw(canvas);
	} else {
		canvas.drawColor(Color.WHITE);
	}
	linearView.draw(canvas);
	return bitmap;

}

	public void printMultilingualFontPayCash(int count) throws UnsupportedEncodingException {

		if(count==0)
		{
			payList=paymentsforPrint;
			payforBank=ReceiptVoucher.payment;

		}else{
			payList=paymentPrinter;
			payforBank=PrintPayment.pay1;
			Log.e("Pay 0000 ==>",""+pay1.getPayMethod());
			Log.e("payforBank 0000 ==>",""+payforBank.getPayMethod());
		}

		int nLineWidth = 550;
		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);
			if (payforBank.getPayMethod() == 1) {
				posPtr.printAndroidFont(  null,true, companyInfo.getCompanyName() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"    "+ "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, " سند قبض "+ "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "رقم السند: " + payforBank.getVoucherNumber()+"        " + "التاريخ: " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "وصلني من السيد/السادة: " +payforBank.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "ملاحظة: " + payforBank.getRemark() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "المبلغ المقبوض: " + payforBank.getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "طريقة الدفع: " + (payforBank.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			} else {
				posPtr.printAndroidFont(  null,true, companyInfo.getCompanyName() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"    "+ "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, " سند قبض "+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "رقم السند: " + payforBank.getVoucherNumber()+"        " + "التاريخ: " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "وصلني من السيد/السادة: " +payforBank.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "ملاحظة: " + payforBank.getRemark() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "المبلغ المقبوض: " + payforBank.getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "طريقة الدفع: " + (payforBank.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true, "        القيمة     " + "      التاريخ      " + "   رقم الشيك         " + "  البنك    " + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < payList.size(); i++) {

					if (payList.get(i).getBank().length() <= 12) {
						String space = payList.get(i).getBank();
						for (int g = 0; g < 12 - payList.get(i).getBank().length(); g++) {
							space += "\t";
						}//"\t\t\t\t" +
						posPtr.printAndroidFont(  null,true, "\t\t"+space+ payList.get(i).getCheckNumber()+"\t\t\t\t"+ payList.get(i).getDueDate()+"\t\t\t" + payList.get(i).getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
					} else {
						String space = payList.get(i).getBank().substring(0, 10);
//                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
//                        space+= "\t" ;
//                    }
						String fullString = payList.get(i).getBank().substring(10, payList.get(i).getBank().length() - 1);
						posPtr.printAndroidFont(  null,true, "\t\t"+space +"\t\t\t"+ payList.get(i).getCheckNumber() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t" + payList.get(i).getAmount() + "\n" + fullString + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
					}
				}


			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void printMultilingualFontCashReport() throws UnsupportedEncodingException {

		int nLineWidth = 550;
		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

			String companney_name="";
			decimalFormat = new DecimalFormat("##.00");
			String dataArabic_Report="";
			try {
				total_cash=net+cash-returnCash;

				if (companyInfo.getCompanyName().equals("")) {
					companney_name = "Companey";
					//Please fill  the companey name
//					Toast.makeText(context, R.string.fill_name, Toast.LENGTH_SHORT).show();
				} else {
					companney_name = companyInfo.getCompanyName();
				}

				posPtr.printAndroidFont(  null,true,  companney_name + "\n"  , nLineWidth, 25, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,  "  الرقم الضريبي :  " + companyInfo.getTaxNo() + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"        -----------------------------------------------------------------------  " + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"التاريخ  : " + date.getText() + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"المبيعات نقدا :     " + convertToEnglish(decimalFormat.format((cash - returnCash))) + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"المبيعات ذمم :     " + convertToEnglish(decimalFormat.format((credit - returnCridet))) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"إجمالي المبيعات :     " + convertToEnglish(decimalFormat.format(total)) + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"        -----------------------------------------------------------------------  " + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"الدفع نقدا :     " + convertToEnglish(decimalFormat.format(cashPayment)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"الدفع شيك :     " + convertToEnglish(decimalFormat.format(creditPayment)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"الاجمالي :     " + convertToEnglish(decimalFormat.format(net)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"        -----------------------------------------------------------------------  " + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"اجمالي المقبوضات :     " + convertToEnglish(decimalFormat.format(total_cash)) + " \n\n \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			}catch (Exception e){
//				Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	public String convertToEnglish(String value) {
		String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
		return newValue;
	}


	public boolean ifEnglish(char value) {

for(int i=0;i<28;i++){
	if(((97+i)==((int)value))||((65+i)==((int)value)) ){
		return true;
	}
}
		return false;
	}


	public Bitmap convertLayoutToImageW(Context context) {
		LinearLayout linearView = null;

		final Dialog dialogs = new Dialog(context);
		dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogs.setCancelable(false);
		dialogs.setContentView(R.layout.printdialog);
//            fill_theVocher( voucherPrint);

		TextView	doneinsewooprint;

		CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
		doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

		TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;
		ImageView img = (ImageView) dialogs.findViewById(R.id.img);

		compname = (TextView) dialogs.findViewById(R.id.compname);
		tel = (TextView) dialogs.findViewById(R.id.tel);
		taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
		vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
		date = (TextView) dialogs.findViewById(R.id.date);
		custname = (TextView) dialogs.findViewById(R.id.custname);
		note = (TextView) dialogs.findViewById(R.id.note);
		vhType = (TextView) dialogs.findViewById(R.id.vhType);
		paytype = (TextView) dialogs.findViewById(R.id.paytype);
		total = (TextView) dialogs.findViewById(R.id.total);
		discount = (TextView) dialogs.findViewById(R.id.discount);
		tax = (TextView) dialogs.findViewById(R.id.tax);
		ammont = (TextView) dialogs.findViewById(R.id.ammont);
		textW = (TextView) dialogs.findViewById(R.id.wa1);
		TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//



		String voucherTyp = "";
		switch (voucherforPrint.getVoucherType()) {
			case 504:
				voucherTyp = "فاتورة بيع";
				break;
			case 506:
				voucherTyp = "فاتورة مرتجعات";
				break;
			case 508:
				voucherTyp = "طلب جديد";
				break;
		}
		img.setImageBitmap(companyInfo.getLogo());
		compname.setText(companyInfo.getCompanyName());
		tel.setText("" + companyInfo.getcompanyTel());
		taxNo.setText("" + companyInfo.getTaxNo());
		vhNo.setText("" + voucherforPrint.getVoucherNumber());
		date.setText(voucherforPrint.getVoucherDate());
		custname.setText(voucherforPrint.getCustName());
		note.setText(voucherforPrint.getRemark());
		vhType.setText(voucherTyp);

		paytype.setText((voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا"));
		total.setText("" + voucherforPrint.getSubTotal());
		discount.setText("" + voucherforPrint.getVoucherDiscount());///
		tax.setText("" + voucherforPrint.getTax());
		ammont.setText("" + voucherforPrint.getNetSales());


		if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
			textW.setVisibility(View.GONE);
		} else {
			textW.setVisibility(View.VISIBLE);
		}


		TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
		TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
		lp2.setMargins(0, 7, 0, 0);
		lp3.setMargins(0, 7, 0, 0);

		for (int j = 0; j < itemforPrint.size(); j++) {

			if (voucherforPrint.getVoucherNumber() == itemforPrint.get(j).getVoucherNumber()) {
				final TableRow row = new TableRow(context);


				for (int i = 0; i <= 7; i++) {
					TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 10, 0, 0);
					row.setLayoutParams(lp);

					TextView textView = new TextView(context);
					textView.setGravity(Gravity.CENTER);
					textView.setTextSize(18);

					switch (i) {
						case 0:
							textView.setText(itemforPrint.get(j).getItemName());
							textView.setLayoutParams(lp3);
							break;


						case 1:
							if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
								textView.setText("" + itemforPrint.get(j).getUnit());
								textView.setLayoutParams(lp2);
							} else {
								textView.setText("" + itemforPrint.get(j).getQty());
								textView.setLayoutParams(lp2);
							}
							break;

						case 2:
							if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
								textView.setText("" + itemforPrint.get(j).getQty());
								textView.setLayoutParams(lp2);
								textView.setVisibility(View.VISIBLE);
							} else {
								textView.setVisibility(View.GONE);
							}
							break;

						case 3:
							textView.setText("" + itemforPrint.get(j).getPrice());
							textView.setLayoutParams(lp2);
							break;


						case 4:
							String amount = "" + (itemforPrint.get(j).getQty() * itemforPrint.get(j).getPrice() - itemforPrint.get(j).getDisc());
							amount = convertToEnglish(amount);
							textView.setText(amount);
							textView.setLayoutParams(lp2);
							break;
					}
					row.addView(textView);
				}


				tabLayout.addView(row);
			}
		}


        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
		linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

		linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

		Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

		Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Drawable bgDrawable = linearView.getBackground();
		if (bgDrawable != null) {
			bgDrawable.draw(canvas);
		} else {
			canvas.drawColor(Color.WHITE);
		}
		linearView.draw(canvas);

		return bitmap;// creates bitmap and returns the same
	}

	public void printMultilingualFont_AccountReport() {
		int nLineWidth = 550;
		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

			String companney_name="";
			decimalFormat = new DecimalFormat("##.00");
			String dataArabic_Report="";
			try {
				total_cash=net+cash-returnCash;

				if (companyInfo.getCompanyName().equals("")) {
					companney_name = "Companey";
					//Please fill  the companey name
//					Toast.makeText(context, R.string.fill_name, Toast.LENGTH_SHORT).show();
				} else {
					companney_name = companyInfo.getCompanyName();
				}

				posPtr.printAndroidFont(  null,true,  companney_name + "\n"  , nLineWidth, 25, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,  "  الرقم الضريبي :  " + companyInfo.getTaxNo() + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"        -----------------------------------------------------------------------  " + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"التاريخ  : " + date.getText() + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"المبيعات نقدا :     " + convertToEnglish(decimalFormat.format((cash - returnCash))) + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"المبيعات ذمم :     " + convertToEnglish(decimalFormat.format((credit - returnCridet))) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"إجمالي المبيعات :     " + convertToEnglish(decimalFormat.format(total)) + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"        -----------------------------------------------------------------------  " + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"الدفع نقدا :     " + convertToEnglish(decimalFormat.format(cashPayment)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"الدفع شيك :     " + convertToEnglish(decimalFormat.format(creditPayment)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"الاجمالي :     " + convertToEnglish(decimalFormat.format(net)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"        -----------------------------------------------------------------------  " + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"اجمالي المقبوضات :     " + convertToEnglish(decimalFormat.format(total_cash)) + " \n\n \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			}catch (Exception e){
//				Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
