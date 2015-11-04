package org.mifosplatform.portfolio.loanaccount.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.mifosplatform.infrastructure.documentmanagement.contentrepository.FileSystemContentRepository;
import org.mifosplatform.portfolio.loanaccount.loanschedule.data.LoanScheduleData;
import org.mifosplatform.portfolio.loanaccount.loanschedule.data.LoanSchedulePeriodData;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class LoanRePaymentScreenPdf {

	private BaseFont bfBold;
	private BaseFont bf;
	private int pageNumber = 0;
	
	private static final String DATEFORMAT = "ddMMyyhhmmss";
	private static final String SIMPLEDATEFORMAT = "dd MMM yyyy";
	private static final String ZERO = "0";
	private static final String LEASE = "Lease";
	private static final String PDF_FILE_EXTENSION = ".pdf";
	private static final String UNDERSCORE = "_";
	private static final String LEASE_SCREEN_REPORT = "Lease Screen Report";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLEDATEFORMAT);
	
	DecimalFormat decimalFormat = new DecimalFormat(ZERO);

	private String getFileLocation(Long loanId) {

		String fileLocation = FileSystemContentRepository.MIFOSX_BASE_DIR + File.separator + LEASE + File.separator + loanId;

		/** Recursively create the directory if it does not exist **/
		if (!new File(fileLocation).isDirectory()) {
			new File(fileLocation).mkdirs();
		}

		return fileLocation + File.separator + LEASE + UNDERSCORE + loanId 
				+ UNDERSCORE + dateFormat.format(new Date()) + PDF_FILE_EXTENSION;
	}

	public String createPDF(Long loanId, LoanScheduleData repaymentSchedule, 
			String name, String emailId, String phoneNumber) {

		boolean beginPage = true;
		String path = null;
		int y = 0, height = 570, finalHeight = 0, count = 0;
		PdfWriter docWriter = null;
		
		Document doc = new Document();	
		initializeFonts();

		try {			

			path = getFileLocation(loanId);
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
			doc.addCreationDate();
			doc.addProducer();
			doc.addTitle(LEASE_SCREEN_REPORT);
			doc.setPageSize(PageSize.LETTER);

			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();

			Collection<LoanSchedulePeriodData> periods = repaymentSchedule.getPeriods();

			for (LoanSchedulePeriodData period : periods) {
                
				if (beginPage) {					
					beginPage = false;
					generateHeader(doc, cb, loanId, name, emailId, phoneNumber);
					y = 570;
					finalHeight = 80;
				}

				generateDetail(doc, cb, period, y);
				y -= 15;
				finalHeight += 15;
				count++;

				if (y < 80) {
					generateLayout(doc, cb, 80, height, false);
					printPageNumber(cb);
					doc.newPage();
					beginPage = true;
				}
			}

			if (count%33 == 0) {				
				generateHeader(doc, cb, loanId, name, emailId, phoneNumber);
				generateLayout(doc, cb, 580, 60, true);
				generateFinalValueLayout(doc, cb, 565, repaymentSchedule);
			
			} else {
				generateLayout(doc, cb, y, finalHeight, true);
				generateFinalValueLayout(doc, cb, y - 13, repaymentSchedule);
			}

			/*
			 * generateLayout(doc, cb, y, finalHeight, true);
			 * generateFinalValueLayout(doc, cb, y - 13, repaymentSchedule);
			 */
			printPageNumber(cb);

		} catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (docWriter != null) {
				docWriter.close();
			}
		}
		
		return path;
	}

	private void generateLayout(Document doc, PdfContentByte cb, int y, int h, boolean val) {

		try {
			
			cb.setLineWidth(1f);

			// Invoice Header box layout
			cb.rectangle(50, 680, 250, 80);
			cb.moveTo(50, 700);
			cb.lineTo(300, 700);
			cb.moveTo(50, 720);
			cb.lineTo(300, 720);
			cb.moveTo(50, 740);
			cb.lineTo(300, 740);
			cb.moveTo(130, 680);
			cb.lineTo(130, 760);
			cb.stroke();

			// Invoice Header box Text Headings
			createHeadings(cb, 72, 743, "  Loan Number");
			createHeadings(cb, 62, 723, "Customer Name");
			createHeadings(cb, 68, 703, "Email Address");
			createHeadings(cb, 68, 683, "Phone Number");

			// Invoice Detail box layout
			cb.rectangle(10, y, 575, h);
			cb.moveTo(10, 620);
			cb.lineTo(585, 620);
			cb.moveTo(10, 590);
			cb.lineTo(585, 590);

			cb.moveTo(40, y);
			cb.lineTo(40, 620);
			cb.moveTo(90, y);
			cb.lineTo(90, 620);
			cb.moveTo(150, y);
			cb.lineTo(150, 620);
			cb.moveTo(200, y);
			cb.lineTo(200, 620);
			cb.moveTo(250, y);
			cb.lineTo(250, 620);
			cb.moveTo(300, y);
			cb.lineTo(300, 620);
			cb.moveTo(350, y);
			cb.lineTo(350, 620);
			cb.moveTo(390, y);
			cb.lineTo(390, 620);
			cb.moveTo(430, y);
			cb.lineTo(430, 620);
			cb.moveTo(480, y);
			cb.lineTo(480, 620);
			cb.moveTo(530, y);
			cb.lineTo(530, 620);
			/*cb.moveTo(600, y);
			cb.lineTo(600, 620);*/
			if (val) {
				cb.rectangle(10, y - 20, 575, 20);
				cb.moveTo(150, y);
				cb.lineTo(150, y - 20);
				cb.moveTo(200, y);
				cb.lineTo(200, y - 20);
				cb.moveTo(250, y);
				cb.lineTo(250, y - 20);
				cb.moveTo(300, y);
				cb.lineTo(300, y - 20);
				cb.moveTo(350, y);
				cb.lineTo(350, y - 20);
				cb.moveTo(390, y);
				cb.lineTo(390, y - 20);
				cb.moveTo(430, y);
				cb.lineTo(430, y - 20);
				cb.moveTo(480, y);
				cb.lineTo(480, y - 20);
				cb.moveTo(530, y);
				cb.lineTo(530, y - 20);
				/*cb.moveTo(600, y);
				cb.lineTo(600, y - 20);*/
			}
			cb.stroke();

			// Invoice Detail box Text Headings
			createHeadings(cb, 160, 630, "Lease Amount and");
			createHeadings(cb, 185, 622, "Balance");

			createHeadings(cb, 255, 622, "Total Cost of Lease");
			createHeadings(cb, 412, 622, "Installment Totals");

			createHeadings(cb, 12, 592, "Period");
			createHeadings(cb, 55, 592, "Date");
			createHeadings(cb, 102, 592, "Paid Date");

			createHeadings(cb, 160, 602, "Principal");
			createHeadings(cb, 160, 592, "Due");

			createHeadings(cb, 207, 602, "Residual");
			createHeadings(cb, 207, 592, "Amount");

			createHeadings(cb, 255, 602, "Balance of");
			createHeadings(cb, 255, 592, "Lease");

			createHeadings(cb, 312, 592, "Interest");
			createHeadings(cb, 362, 592, "Fees");
			createHeadings(cb, 392, 592, "Penalties");
			createHeadings(cb, 450, 592, "Due");
			createHeadings(cb, 500, 592, "Paid");
			createHeadings(cb, 535, 592, "Total Due");

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void generateHeader(Document doc, PdfContentByte cb, 
			Long loanId, String name, String emailId, String phoneNumber) {

		try {

			createHeadings(cb, 133, 743, loanId.toString());
			createHeadings(cb, 133, 723, name);
			createHeadings(cb, 133, 703, emailId);
			createHeadings(cb, 133, 683, phoneNumber);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void generateFinalValueLayout(Document doc, PdfContentByte cb,
			int y, LoanScheduleData loanScheduleData) {

		createHeadings(cb, 100, y, "Total");
		createContent(cb, 170, y, decimalFormat.format(loanScheduleData.totalPrincipalExpected()), PdfContentByte.ALIGN_CENTER);

		createContent(cb, 320, y, decimalFormat.format(loanScheduleData.totalInterestCharged()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 370, y, decimalFormat.format(loanScheduleData.totalFeeChargesCharged()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 410, y, decimalFormat.format(loanScheduleData.totalPenaltyChargesCharged()), PdfContentByte.ALIGN_CENTER);

		createContent(cb, 450, y, decimalFormat.format(loanScheduleData.totalRepaymentExpected()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 500, y, decimalFormat.format(loanScheduleData.totalRepayment()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 550, y, decimalFormat.format(loanScheduleData.totalOutstanding()), PdfContentByte.ALIGN_CENTER);
	}

	private void generateDetail(Document doc, PdfContentByte cb, LoanSchedulePeriodData period, int y) {

		String paidDate = "", residualAmount = "", intrestOriginalDue = "", totalOutstandingForPeriod = "";
		String dueDate = simpleDateFormat.format(period.periodDueDate().toDate());

		if (null != period.periodObligationsMetOnDate()) {
			paidDate = simpleDateFormat.format(period.periodObligationsMetOnDate().toDate());
		}

		if (period.periodResidualAmount().compareTo(BigDecimal.ZERO) > 0) {
			residualAmount = decimalFormat.format(period.periodResidualAmount());
		}

		if (null != period.periodInterestOriginalDue()) {
			intrestOriginalDue = decimalFormat.format(period.periodInterestOriginalDue());
		}

		if (null != period.totalOutstandingForPeriod()) {
			totalOutstandingForPeriod = decimalFormat.format(period.totalOutstandingForPeriod());
		}

		createContent(cb, 25, y, period.periodNumber().toString(), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 65, y, dueDate, PdfContentByte.ALIGN_CENTER);
		createContent(cb, 118, y, paidDate, PdfContentByte.ALIGN_CENTER);

		createContent(cb, 170, y, decimalFormat.format(period.principalDue()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 220, y, residualAmount, PdfContentByte.ALIGN_CENTER);
		createContent(cb, 270, y, decimalFormat.format(period.periodPrincipalLoanBalanceOutstanding()), PdfContentByte.ALIGN_CENTER);

		createContent(cb, 320, y, intrestOriginalDue, PdfContentByte.ALIGN_CENTER);
		createContent(cb, 370, y, decimalFormat.format(period.feeChargesDue()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 410, y, decimalFormat.format(period.penaltyChargesDue()), PdfContentByte.ALIGN_CENTER);

		createContent(cb, 450, y, decimalFormat.format(period.totalDueForPeriod()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 500, y, decimalFormat.format(period.totalPaidForPeriod()), PdfContentByte.ALIGN_CENTER);
		createContent(cb, 550, y, totalOutstandingForPeriod, PdfContentByte.ALIGN_CENTER);
	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text) {
		
		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();
	}

	private void printPageNumber(PdfContentByte cb) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber + 1), 570, 25, 0);
		cb.endText();
		pageNumber++;
	}

	private void createContent(PdfContentByte cb, float x, float y,
			String text, int align) {

		cb.beginText();
		cb.setFontAndSize(bf, 8);
		cb.showTextAligned(align, text.trim(), x, y, 0);
		cb.endText();
	}

	private void initializeFonts() {

		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
