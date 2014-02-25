package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import model.models.Item;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Generates pdf's of barcode labels when new Item batches are added to the HIT
 * program.
 * 
 * @author Group 1
 * 
 */
public class BarcodeLabelGenerator extends
		ReportGenerator {
	/** The resulting PDF. */

	private static final String RESULT;
	static {
		RESULT = new File("").getAbsolutePath()
				+ System.getProperty("file.separator")
				+ "data"
				+ System.getProperty("file.separator")
				+ "barcodes";
	}
	private static String fileName;

	/**
	 * Constructor
	 */
	public BarcodeLabelGenerator() {
	}

	/**
	 * Main method to generate the Barcode Label pdf. Called statically.
	 * 
	 * @pre itemsToPrint not null
	 * @post The pdf is generated
	 * 
	 * @param itemsToPrint
	 *            ArrayList of Item objects whose barcodes will be printed
	 * @throws FileNotFoundException
	 *             , DocumentException
	 */
	public static String generatePDF(
			ArrayList<Item> itemsToPrint)
			throws Exception {
		Date date = new Date();
		fileName = RESULT + date.getTime()
				+ ".pdf";
		try {
			Document document = new Document(
					new Rectangle(340, 842));
			PdfWriter writer = PdfWriter
					.getInstance(document,
							new FileOutputStream(
									fileName));
			document.open();
			cb = writer.getDirectContent();
			addMetaData(document);
			addContent(document, itemsToPrint);
			document.close();
			writer.close();
			try {
				java.awt.Desktop.getDesktop()
						.open(new File(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out
					.println("Exception in generatePDF: ");
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * Add the meta data to the pdf (Title, Subject, Keywords, Author, Creator)
	 * 
	 * @pre doc is not null
	 * @post metadata is added to the pdf
	 * 
	 * @param doc
	 *            The Document object to add the metadata to
	 */
	private static void addMetaData(Document doc) {
		doc.addTitle("Newly Added Barcodes");
		doc.addSubject("Barcodes of Items Just Added");
		doc.addKeywords("Item, Barcode");
		doc.addAuthor("Home Inventory Tracker");
		doc.addCreator("Home Inventory Tracker");
	}

	/**
	 * Add the content to the pdf.
	 * 
	 * @pre doc is not null
	 * @post all barcodes in itemsToPrint are printed to pdf.
	 * @param doc
	 *            Document object to add content to.
	 * @param itemsToPrint
	 *            ArrayList of Items whose barcodes will be printed to pdf.
	 */
	private static void addContent(Document doc,
			ArrayList<Item> itemsToPrint)
			throws DocumentException {
		try {
			Iterator<Item> itr = itemsToPrint
					.iterator();
			while (itr.hasNext()) {
				addUPCBarcode(itr.next(), doc);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a barcode to the pdf.
	 * 
	 * @pre doc is not null
	 * @post barcode of item is printed to pdf.
	 * @param item
	 *            Item whose barcode will be printed to the pdf.
	 * @param doc
	 *            Document object to add barcode to.
	 * @throws DocumentException
	 */
	private static void addUPCBarcode(Item item,
			Document doc)
			throws DocumentException {
		BarcodeEAN codeEAN = new BarcodeEAN();
		try {
			doc.add(new Paragraph(item
					.getProduct()
					.getDescription()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		codeEAN.setCodeType(Barcode.UPCA);
		codeEAN.setCode(item.getItemBarcode()
				.toString());
		try {
			doc.add(codeEAN
					.createImageWithBarcode(cb,
							null, null));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
