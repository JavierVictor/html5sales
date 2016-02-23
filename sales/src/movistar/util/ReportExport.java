package movistar.util;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportExport {
    private static final String PDF_EXTENSION = ".pdf";
    private static final String EXCEL_EXTENSION = ".xls";

    private static String exportPDF(OutputStream outputStream, String[] columnNames,
	    List<Map> contentData) throws Exception {
	Document document = new Document(PageSize.A3);
	PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	document.setMargins(10, 10, 10, 10);
	document.open();
	
        document.setMarginMirroring(true);
	BaseColor tdpBlue = new BaseColor(0, 50, 69);
	BaseColor tdpSkyBlue = new BaseColor(213, 238, 243);
	PdfPTable table = new PdfPTable(columnNames.length);
	
	table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
	table.setWidthPercentage(100f);
	
	PdfPCell cell = null;

	Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD,
		tdpBlue);
	
	for (String columnName : columnNames) {
	    cell = new PdfPCell(new Phrase(columnName, font));
	    cell.setBackgroundColor(tdpSkyBlue);
	    cell.setBorder(Rectangle.BOX);

	    cell.setBorderColor(tdpBlue);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

	    table.addCell(cell);
	}
	font = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL,
		tdpBlue);
	table.getDefaultCell().setBorderColor(tdpBlue);
	
	table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	for(Map map:contentData){
	    for(String columnName:columnNames){
		table.addCell(new Phrase(String.valueOf(map.get(columnName)), font));
	    }
	}

	document.add(table);
	document.close();
	return PDF_EXTENSION;
    }
    
    private static String exportExcel(OutputStream outputStream, String[] columnNames,
	    List<Map> contentData) throws Exception {
	HSSFWorkbook workbook = new HSSFWorkbook();
	HSSFSheet sheet = workbook.createSheet("export");
	Row row = sheet.createRow(1);
	CellStyle columnStyle = workbook.createCellStyle();
	HSSFPalette palette = workbook.getCustomPalette();
	palette.setColorAtIndex((short)8, (byte)213,(byte)238,(byte)243);
	palette.setColorAtIndex((short)9, (byte)0,(byte)50,(byte)69);
	columnStyle.setFillForegroundColor(palette.getColor(8).getIndex());
	columnStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	columnStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	columnStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	columnStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	columnStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	columnStyle.setTopBorderColor(palette.getColor(9).getIndex());
	columnStyle.setBottomBorderColor(palette.getColor(9).getIndex());
	columnStyle.setLeftBorderColor(palette.getColor(9).getIndex());
	columnStyle.setRightBorderColor(palette.getColor(9).getIndex());
	
	org.apache.poi.ss.usermodel.Font columnFont = workbook.createFont();
	columnFont.setBold(true);
	columnFont.setFontHeight((short)240);
	columnFont.setColor(palette.getColor(9).getIndex());
	columnStyle.setFont(columnFont);
	Cell cell = null;
	for(int i=1;i<=columnNames.length;i++){
	    cell = row.createCell(i);
	    cell.setCellValue(columnNames[i-1]);
	    cell.setCellStyle(columnStyle);
	}
	
	CellStyle rowStyle = workbook.createCellStyle();
	rowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	rowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	rowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	rowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	rowStyle.setTopBorderColor(palette.getColor(9).getIndex());
	rowStyle.setBottomBorderColor(palette.getColor(9).getIndex());
	rowStyle.setLeftBorderColor(palette.getColor(9).getIndex());
	rowStyle.setRightBorderColor(palette.getColor(9).getIndex());
	
	org.apache.poi.ss.usermodel.Font rowFont = workbook.createFont();
	rowFont.setFontHeight((short)200);
	rowFont.setColor(palette.getColor(9).getIndex());
	rowStyle.setFont(rowFont);
	
	for(int i=0;i<contentData.size();i++){
	    Row rowData = sheet.createRow(i+2);
	    Map map = contentData.get(i);
	    for(int j=0;j<columnNames.length;j++){
		cell = rowData.createCell(j+1);
		cell.setCellValue(String.valueOf(map.get(columnNames[j])));
		cell.setCellStyle(rowStyle);
	    }
	}
	for(int i = 1;i<=columnNames.length;i++){
	    sheet.autoSizeColumn(i);
	}
	
	workbook.write(outputStream);
	outputStream.close();
	workbook.close();
	return EXCEL_EXTENSION;
    }
    
    public static String export(ExportType exportType,OutputStream outputStream, String[] columnNames,
	    List<Map> contentData)throws Exception{
	if(exportType==null)
	    throw new IllegalArgumentException("Se debe indicar tipo de archivo a exportar");

	
	if(exportType==ExportType.PDF){
	    return exportPDF(outputStream, columnNames, contentData);
	}else
	    return exportExcel(outputStream, columnNames, contentData);
    }
    
    public static enum ExportType{
	PDF,
	EXCEL;
    }
}