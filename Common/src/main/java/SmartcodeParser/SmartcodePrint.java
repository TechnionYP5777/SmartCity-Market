package SmartcodeParser;

import java.awt.*;
import java.awt.image.BufferedImage;

import BasicCommonClasses.SmartCode;

import java.awt.print.*;
import java.time.format.DateTimeFormatter;

/**
 * This class sends smartcodes for printing
 * 
 * @author noam yefet
 *
 */
public class SmartcodePrint implements Printable{

	private static final int MIN_FONT_SIZE = 6;
	private static final int MAX_FONT_SIZE = 36;
	
	private static final int INNER_BARCODE_TOP_BOTTOM_PADDING = 8;
	private static final int INNER_BARCODE_LEFT_RIGHT_PADDING = 8;
	
	SmartCode smartcodeToPrint;
	int barcodeTotalHeight;
	int barcodeTotalWidth;
	int lineWidth;
	int leftSpace;
	int rightSpace;
	int topSpace;
	int bottomSpace;
	int horizontalSpace;
	int verticalSpace;
	int count;
	
	public SmartcodePrint(SmartCode smartcodeToPrint, int barcodeTotalHeight, int lineWidth, int leftSpace,
			int rightSpace, int topSpace, int bottomSpace, int horizontalSpace, int verticalSpace, int count) {
		this.smartcodeToPrint = smartcodeToPrint;
		this.barcodeTotalHeight = barcodeTotalHeight;
		this.lineWidth = lineWidth;
		this.leftSpace = leftSpace;
		this.rightSpace = rightSpace;
		this.topSpace = topSpace;
		this.bottomSpace = bottomSpace;
		this.horizontalSpace = horizontalSpace;
		this.verticalSpace = verticalSpace;
		this.count = count;
	}

	public SmartcodePrint(SmartCode smartcodeToPrint, int count) {
		this.smartcodeToPrint = smartcodeToPrint;
		this.count = count;
		this.barcodeTotalHeight = 100;
		this.lineWidth = 1;
		this.leftSpace = 25;
		this.rightSpace = 0;
		this.topSpace = 25;
		this.verticalSpace = this.horizontalSpace = this.bottomSpace = 0;
	}
	
	private BufferedImage createSmarcodeImage(String SmartcodeGS1Code){
		int textHeight = 0;
		String textToDisplay;
		
		//this are the sizes if the printing area (they will be surrounded with white padding) 
		int barcodeWidth = lineWidth * SmartcodeGS1Code.length();
		int barcodeHeight = barcodeTotalHeight - 2 * INNER_BARCODE_TOP_BOTTOM_PADDING;
		
		barcodeTotalWidth = barcodeWidth + 2 * INNER_BARCODE_LEFT_RIGHT_PADDING;
		
		//create image to draw on
		BufferedImage $ = new BufferedImage(barcodeTotalWidth, barcodeTotalHeight, BufferedImage.TYPE_INT_ARGB);
   
        Graphics2D gd = $.createGraphics();
        
        //generate text to display beneath the barcode
        textToDisplay = "(" + SmartcodeParser.EXPIRATION_DATE_IDENTIFIER + ")" +
        		smartcodeToPrint.getExpirationDate().format(DateTimeFormatter.ofPattern("yyMMdd")) +
        		"(" + SmartcodeParser.BARCODE_IDENTIFIER + ")" + Long.toString(smartcodeToPrint.getBarcode());
        
        
        //find optimal width for the text
        for (int i=MAX_FONT_SIZE; i>=MIN_FONT_SIZE; --i){
	        Font font = new Font("Serif", Font.PLAIN, i);
	        FontMetrics metrics = gd.getFontMetrics(font);
	        if (metrics.stringWidth(textToDisplay) < barcodeWidth){
	        	textHeight = metrics.getHeight();
	        	gd.setFont(font);
	        	break;
	        }
        }
        
        //this commands will draw frame to the barcode
//        gd.setColor(Color.BLACK);
//        gd.drawRect(1, 1, barcodeTotalWidth + 2 * INNER_BARCODE_LEFT_RIGHT_PADDING-2, barcodeTotalHeight + 2 * INNER_BARCODE_TOP_BOTTOM_PADDING -2);
        
        for (int xpos = INNER_BARCODE_LEFT_RIGHT_PADDING, ¢ = 0; ¢ < SmartcodeGS1Code
				.length(); xpos += lineWidth, ++¢) {
			gd.setColor(SmartcodeGS1Code.charAt(¢) == '1' ? Color.BLACK : Color.WHITE);
			gd.fillRect(xpos, INNER_BARCODE_TOP_BOTTOM_PADDING, lineWidth, barcodeHeight - textHeight);
		}
        
        //draw the barcode label
        gd.setColor(Color.BLACK);
        gd.drawString(textToDisplay, (float) INNER_BARCODE_LEFT_RIGHT_PADDING, 
        		(float) (barcodeHeight + INNER_BARCODE_TOP_BOTTOM_PADDING - textHeight/2 + 2));
        
        return $;
	}
	
    @Override
	public int print(Graphics g, PageFormat f, int page) throws
                                                        PrinterException {
   	
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
    	Graphics2D g2d = (Graphics2D)g;
        g2d.translate(f.getImageableX(), f.getImageableY());
        
        
    	// calculate how much smarcodes fits in row and column
        String SmartcodeGS1Code = SmartcodeParser.smartcodeToCode(smartcodeToPrint);
		barcodeTotalWidth = 2 * INNER_BARCODE_LEFT_RIGHT_PADDING + lineWidth * SmartcodeGS1Code.length();
		
		int printAreaWidth = (int) (f.getImageableWidth() - leftSpace - rightSpace);
		int printAreaHeight = (int) (f.getImageableHeight() - topSpace - bottomSpace);
    	int smartcodesPerRow = printAreaWidth / (horizontalSpace + barcodeTotalWidth);
    	int smartcodesPerCol = printAreaHeight / (verticalSpace + barcodeTotalHeight);
    	
    	int totalSmartcodesInPage = smartcodesPerCol * smartcodesPerRow;
    	int totalPagesToPrint = (int) Math.ceil((float) count / totalSmartcodesInPage);
    	
        if (page > totalPagesToPrint - 1) /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
 
        int smartcodesForThisPage = totalSmartcodesInPage * (page + 1) <= count ?
        		totalSmartcodesInPage : count % totalSmartcodesInPage;
        
        
        //prints the smarcodes for this page
        BufferedImage smartcodeImg = createSmarcodeImage(SmartcodeGS1Code);
        for (int ¢ = 0 ; ¢ < smartcodesForThisPage; ++¢)
        	g2d.drawImage(smartcodeImg, leftSpace + ¢ % smartcodesPerRow * (horizontalSpace + barcodeTotalWidth) ,
        			topSpace + ¢ / smartcodesPerRow * (verticalSpace + barcodeTotalHeight), null);
        
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
 
    public void print() {
    	
         PrinterJob job = PrinterJob.getPrinterJob();
         job.setJobName("Barcode_" + smartcodeToPrint.getBarcode());
        
         job.setPrintable(this);
         if (job.printDialog())
			try {
				job.print();
			} catch (PrinterException ex) {
			}
    }

	public SmartCode getSmartcodeToPrint() {
		return smartcodeToPrint;
	}

	public void setSmartcodeToPrint(SmartCode smartcodeToPrint) {
		this.smartcodeToPrint = smartcodeToPrint;
	}

	public int getBarcodeTotalHeight() {
		return barcodeTotalHeight;
	}

	public void setBarcodeTotalHeight(int barcodeTotalHeight) {
		this.barcodeTotalHeight = barcodeTotalHeight;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getLeftSpace() {
		return leftSpace;
	}

	public void setLeftSpace(int leftSpace) {
		this.leftSpace = leftSpace;
	}

	public int getRightSpace() {
		return rightSpace;
	}

	public void setRightSpace(int rightSpace) {
		this.rightSpace = rightSpace;
	}

	public int getTopSpace() {
		return topSpace;
	}

	public void setTopSpace(int topSpace) {
		this.topSpace = topSpace;
	}

	public int getBottomSpace() {
		return bottomSpace;
	}

	public void setBottomSpace(int bottomSpace) {
		this.bottomSpace = bottomSpace;
	}

	public int getHorizontalSpace() {
		return horizontalSpace;
	}

	public void setHorizontalSpace(int horizontalSpace) {
		this.horizontalSpace = horizontalSpace;
	}

	public int getVerticalSpace() {
		return verticalSpace;
	}

	public void setVerticalSpace(int verticalSpace) {
		this.verticalSpace = verticalSpace;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}