package org.mifosplatform.portfolio.loanaccount.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.documentmanagement.contentrepository.FileSystemContentRepository;
import org.mifosplatform.portfolio.loanaccount.data.LoanCalculatorPdfGenerate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class LoanCalculatorScreenPdf {

	private BaseFont bfBold;
	private BaseFont bf;
	private int pageNumber = 0;
	
	private static final String DATEFORMAT = "ddMMyyhhmmss";
	private static final String SIMPLEDATEFORMAT = "dd MMM yyyy";
	private static final String ZERO = "0";
	private static final String LEASE = "LeaseCalculator";
	private static final String PDF_FILE_EXTENSION = ".pdf";
	private static final String UNDERSCORE = "_";
	private static final String LEASE_SCREEN_REPORT = "Loan Calculator Report";
	private static String residualDeprecisation = "residualDeprecisation", 
			residualCost = "residualCost", payTerms = "payTerms";
	 
	private static MathContext mc;
	private static BigDecimal HUNDERED;
	
	private static SimpleDateFormat dateFormat;
	private static SimpleDateFormat simpleDateFormat;
	
	private static DecimalFormat decimalFormat;
	private static Random randomGenerator;
	
	private final FromJsonHelper fromApiJsonHelper;
	
	static {
		dateFormat = new SimpleDateFormat(DATEFORMAT);
		simpleDateFormat = new SimpleDateFormat(SIMPLEDATEFORMAT);
		decimalFormat = new DecimalFormat(ZERO);
		randomGenerator = new Random();
		mc = new MathContext(8, RoundingMode.HALF_EVEN);
		HUNDERED = new BigDecimal(100);
	}
	
	public LoanCalculatorScreenPdf(final FromJsonHelper fromApiJsonHelper) {
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	private String getFileLocation() {

		String fileLocation = FileSystemContentRepository.MIFOSX_BASE_DIR + File.separator + LEASE;

		/** Recursively create the directory if it does not exist **/
		if (!new File(fileLocation).isDirectory()) {
			new File(fileLocation).mkdirs();
		}

		int value = randomGenerator.nextInt(1000);
		 
		return fileLocation + File.separator + LEASE + UNDERSCORE + dateFormat.format(new Date()) + value + PDF_FILE_EXTENSION;
	}
	
	

	public String createPDF(String jsonObjectInString) {

		boolean beginPage = true;
		String path = null;
		int y = 600, height = 570;
		PdfWriter docWriter = null;
		
		Document doc = null;

		try {			
			doc = new Document();
			initializeFonts();
			path = getFileLocation();
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
			doc.addCreationDate();
			doc.addProducer();
			doc.addTitle(LEASE_SCREEN_REPORT);
			doc.setPageSize(PageSize.LETTER);

			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();
						
			JsonElement jsonElement = this.fromApiJsonHelper.parse(jsonObjectInString);
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			ArrayList<LoanCalculatorPdfGenerate> data = getPdfData(jsonObject);
			
			generateLayout(doc, cb);
			generateHeader(doc, cb, jsonElement);
				
			for (LoanCalculatorPdfGenerate element : data) {
				
				generateDetail(doc, cb, element, y);
				y -= 16;
			}

			
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

	private void generateLayout(Document doc, PdfContentByte cb) {

		try {

			cb.setLineWidth(1f);

			// Invoice Header box layout
			cb.rectangle(20, 700, 550, 40);

			cb.moveTo(20, 720);
			cb.lineTo(570, 720);
			
			cb.moveTo(120, 740);
			cb.lineTo(120, 700);
			cb.moveTo(210, 740);
			cb.lineTo(210, 700);
			cb.moveTo(300, 740);
			cb.lineTo(300, 700);
			cb.moveTo(400, 740);
			cb.lineTo(400, 700);
			cb.moveTo(500, 740);
			cb.lineTo(500, 700);
			cb.stroke();

			// Invoice Header box Text Headings
			createHeadings(cb, 40, 723, "Lease Product");
			createHeadings(cb, 122, 723, "Vehicle Cost price");
			createHeadings(cb, 222, 723, "Cof");
			createHeadings(cb, 302, 723, "Maintenance");
			createHeadings(cb, 402, 723, "Intrest");
			createHeadings(cb, 502, 723, "Deposit");

			// Invoice Detail box layout
			cb.rectangle(20, 50, 550, 600);
			cb.moveTo(20, 630);
			cb.lineTo(570, 630);
			cb.moveTo(180, 50);
			cb.lineTo(180, 650);
			cb.moveTo(260, 50);
			cb.lineTo(260, 650);
			cb.moveTo(330, 50);
			cb.lineTo(330, 650);
			cb.moveTo(400, 50);
			cb.lineTo(400, 650);
			cb.moveTo(460, 50);
			cb.lineTo(460, 650);
			cb.stroke();

			// Invoice Detail box Text Headings
			createHeadings(cb, 22, 633, "Terms");
			createHeadings(cb, 198, 633, "12");
			createHeadings(cb, 282, 633, "24");
			createHeadings(cb, 352, 633, "36");
			createHeadings(cb, 422, 633, "48");
			createHeadings(cb, 482, 633, "60");

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void generateDetail(Document doc, PdfContentByte cb,
			LoanCalculatorPdfGenerate element, int y) {

		try {

			createHeadings(cb, 25, y, element.getKeyName());
			createContent(cb, 252, y, element.getValueFor12().toString(), PdfContentByte.ALIGN_RIGHT);
			createContent(cb, 328, y, element.getValueFor24().toString(), PdfContentByte.ALIGN_RIGHT);
			createContent(cb, 392, y, element.getValueFor36().toString(), PdfContentByte.ALIGN_RIGHT);
			createContent(cb, 450, y, element.getValueFor48().toString(), PdfContentByte.ALIGN_RIGHT);
			createContent(cb, 510, y, element.getValueFor60().toString(), PdfContentByte.ALIGN_RIGHT);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	private void generateHeader(Document doc, PdfContentByte cb, JsonElement jsonElement) {

		try {

			String productName = this.fromApiJsonHelper.extractStringNamed("productName", jsonElement);
			BigDecimal principal = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("principal", jsonElement);
			BigDecimal cof = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("cof", jsonElement);
			BigDecimal maintenance = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("maintenance", jsonElement);
			BigDecimal interest = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("interest", jsonElement);
			BigDecimal deposit = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("deposit", jsonElement);
			
			createHeadings(cb, 22, 705, productName);
			createHeadings(cb, 130, 705, principal.toString());
			createHeadings(cb, 210, 705, cof.toString());
			createHeadings(cb, 310, 705, maintenance.toString());
			createHeadings(cb, 410, 705, interest.toString());
			createHeadings(cb, 510, 705, deposit.toString());
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text) {
		
		cb.beginText();
		cb.setFontAndSize(bfBold, 10);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();
	}

	private void printPageNumber(PdfContentByte cb) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 10);
		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber + 1), 570, 25, 0);
		cb.endText();
		pageNumber++;
	}

	private void createContent(PdfContentByte cb, float x, float y,
			String text, int align) {

		cb.beginText();
		cb.setFontAndSize(bf, 10);
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
	
	private ArrayList<LoanCalculatorPdfGenerate> getPdfData(JsonObject jsonObject) {
		
		ArrayList<String> keyList = new ArrayList<String>();		
		ArrayList<JsonObject> listForJsonObject = new ArrayList<JsonObject>();
		ArrayList<LoanCalculatorPdfGenerate> loanCalculatorPdfGenerateList = new ArrayList<LoanCalculatorPdfGenerate>();  
	
		JsonArray payTermArrays = jsonObject.getAsJsonArray(payTerms);
		
		for(JsonElement payTerm : payTermArrays) {
			listForJsonObject.add(payTerm.getAsJsonObject());		
		}
		
		Set<Entry<String, JsonElement>> keys = listForJsonObject.get(0).entrySet();
		
		for (Entry<String, JsonElement> key : keys) {
			String keyval = key.getKey();
			keyList.add(keyval);
		}
			
		for (int i = 1; i < keyList.size(); i++) {
			
			String keyName = keyList.get(i);
			
			LoanCalculatorPdfGenerate loanCalculatorPdfGenerate = new LoanCalculatorPdfGenerate(keyName);
					
			for (int j = 0; j < listForJsonObject.size() && j < 5 ; j++) {

				JsonObject jsonObj = listForJsonObject.get(j);
				
				BigDecimal finalValue = jsonObj.get(keyName).getAsBigDecimal();
				
				if(keyName.equalsIgnoreCase(residualDeprecisation) || keyName.equalsIgnoreCase(residualCost)) {
					finalValue = finalValue.multiply(HUNDERED, mc).setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					finalValue = finalValue.setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				
				switch (j) {
				
				case 0:
					loanCalculatorPdfGenerate.setValueFor12(finalValue);
					break;

				case 1:
					loanCalculatorPdfGenerate.setValueFor24(finalValue);
					break;

				case 2:
					loanCalculatorPdfGenerate.setValueFor36(finalValue);
					break;

				case 3:
					loanCalculatorPdfGenerate.setValueFor48(finalValue);
					break;
				
				case 4:
					loanCalculatorPdfGenerate.setValueFor60(finalValue);
					break;

				default:
					break;
				}
			}
			loanCalculatorPdfGenerateList.add(loanCalculatorPdfGenerate);
		}
			          
		 return loanCalculatorPdfGenerateList;
	}
}

