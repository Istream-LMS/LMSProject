package org.mifosplatform.portfolio.loanaccount.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
	private static final String LEASE = "LeaseCalculator";
	private static final String PDF_FILE_EXTENSION = ".pdf";
	private static final String UNDERSCORE = "_";
	private static final String LEASE_SCREEN_REPORT = "Loan Calculator Report";
	private static String residualDeprecisation = "residualDeprecisation", 
			residualCost = "residualCost", payTerms = "payTerms";
	 
	private static MathContext mc;
	private static BigDecimal HUNDERED;
	
	private static SimpleDateFormat dateFormat;
	private static Random randomGenerator;
	
	private final FromJsonHelper fromApiJsonHelper;
	
	static {
		dateFormat = new SimpleDateFormat(DATEFORMAT);
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

		String path = null;
		int y = 600;
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
			
			JsonArray jArray = jsonObject.getAsJsonArray("keyTerms");
			
			generateLayout(doc, cb, jArray);
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

	private void generateLayout(Document doc, PdfContentByte cb, JsonArray jArray) {

		try {

			cb.setLineWidth(1f);

			// Invoice Header box For Address
			cb.rectangle(20, 730, 550, 40);

			cb.moveTo(20, 750);
			cb.lineTo(570, 750);
			
			cb.moveTo(150, 770);
			cb.lineTo(150, 730);			
			cb.moveTo(300, 770);
			cb.lineTo(300, 730);
			cb.stroke();
			
			createHeadings(cb, 40, 755, "Customer Name");
			createHeadings(cb, 180, 755, "Mobile Number");
			createHeadings(cb, 350, 755, "Address");
			
			// Invoice Header box For Product
			cb.rectangle(20, 670, 550, 40);

			cb.moveTo(20, 690);
			cb.lineTo(570, 690);
			
			cb.moveTo(120, 710);
			cb.lineTo(120, 670);
			cb.moveTo(210, 710);
			cb.lineTo(210, 670);
			cb.moveTo(300, 710);
			cb.lineTo(300, 670);
			cb.moveTo(400, 710);
			cb.lineTo(400, 670);
			cb.moveTo(500, 710);
			cb.lineTo(500, 670);
			cb.stroke();

			// Invoice Header box Text Headings
			createHeadings(cb, 40, 695, "Lease Product");
			createHeadings(cb, 122, 695, "Vehicle Cost price");
			createHeadings(cb, 230, 695, "Cof");
			createHeadings(cb, 310, 695, "Maintenance");
			createHeadings(cb, 410, 695, "Intrest");
			createHeadings(cb, 510, 695, "Deposit");

			// Invoice Detail box layout
			cb.rectangle(20, 50, 550, 600);
			cb.moveTo(20, 630);
			cb.lineTo(570, 630);
			cb.moveTo(190, 50);
			cb.lineTo(190, 650);
			cb.moveTo(260, 50);
			cb.lineTo(260, 650);
			cb.moveTo(330, 50);
			cb.lineTo(330, 650);
			cb.moveTo(400, 50);
			cb.lineTo(400, 650);
			cb.moveTo(470, 50);
			cb.lineTo(470, 650);
			cb.stroke();

			// Invoice Detail box Text Headings
			createHeadings(cb, 22, 633, "Terms");
			
			int xAxisVal = 140;
			
			for (int i=0; i< jArray.size() && i<5; i++) {
				xAxisVal =  xAxisVal + 70;
				createHeadings(cb, xAxisVal, 633, jArray.get(i).getAsString());
			}
			
			
			/*createHeadings(cb, 280, 633, "24");
			createHeadings(cb, 350, 633, "36");
			createHeadings(cb, 420, 633, "48");
			createHeadings(cb, 490, 633, "60");*/

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
			createContent(cb, 520, y, element.getValueFor60().toString(), PdfContentByte.ALIGN_RIGHT);
			
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
			
			String customerName = this.fromApiJsonHelper.extractStringNamed("customerName", jsonElement);
			String address = this.fromApiJsonHelper.extractStringNamed("address", jsonElement);
			String phoneNo = this.fromApiJsonHelper.extractStringNamed("phone", jsonElement);
			
			createHeadings(cb, 30, 675, productName);
			createHeadings(cb, 140, 675, principal.toString());
			createHeadings(cb, 230, 675, cof.toString());
			createHeadings(cb, 330, 675, maintenance.toString());
			createHeadings(cb, 425, 675, interest.toString());
			createHeadings(cb, 510, 675, deposit.toString());
			
			createHeadings(cb, 40, 735, customerName == null ? "" : customerName);
			createHeadings(cb, 180, 735, phoneNo == null ? "" : phoneNo);   
			createHeadings(cb, 330, 735, address == null ? "" : address);
			   
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

