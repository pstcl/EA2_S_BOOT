package org.pstcl.ea.service.impl.lossreport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.LossReportEntity;
import org.pstcl.ea.model.reporting.ConsolidatedLossReportModel;
import org.pstcl.ea.model.reporting.LossReportModel;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.util.EAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelGeneratorService2 {

	@Autowired
	IlossReportService ilossReportService;
	
	public HSSFColor setColor(HSSFWorkbook workbook, byte r,byte g, byte b){
	    HSSFPalette palette = workbook.getCustomPalette();
	    HSSFColor hssfColor = null;
	    try {
	        hssfColor= palette.findColor(r, g, b); 
	        if (hssfColor == null ){
	            palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g,b);
	            hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();;
	    }

	    return hssfColor;
	}

	private Font getDefaultFont(Workbook workbook)
	{		Font font = workbook.createFont();
	font.setFontHeightInPoints((short) 12);
	font.setFontName("Times New Roman");
	return font;	}

	private CellStyle getDefaultStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setFont(getDefaultFont(workbook));
		return style;
	}

	private CellStyle getDangerStyle(Workbook workbook) {
		return getDefaultStyle(workbook);
	}

	private CellStyle getNormalStyle(Workbook workbook) {
		return getDefaultStyle(workbook);
	}

	private CellStyle getNoWrapTextStyle(Sheet sheet) {
		CellStyle style = getNormalStyle(sheet);
		style.setWrapText(false);
		return style;
		
	}
	private CellStyle getNormalStyle(Sheet sheet) {
		return getDefaultStyle(sheet.getWorkbook());
	}
	private CellStyle getBoldCellStyle(Sheet workbook) {
		return getBoldCellStyle(workbook.getWorkbook());
	}
	private CellStyle getBoldCellStyle(Workbook workbook) {
		Font font=getDefaultFont(workbook);
		font.setBold(true);
		font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
		CellStyle style = getDefaultStyle(workbook);
		style.setWrapText(true);

		style.setFont(font);
		
		return style;
	}


	private CellStyle getHeaderCellStyle(Sheet workbook) {
		return getHeaderCellStyle(workbook.getWorkbook());
	}
	private CellStyle getHeaderCellStyle(Workbook workbook) {
		Font font=getDefaultFont(workbook);
		font.setBold(true);
		font.setFontHeightInPoints((short) 14);
		font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		CellStyle style = getDefaultStyle(workbook);
		style.setWrapText(true);

		style.setFont(font);
		
		 style.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());  
         style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  

       //  style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		return style;
	}

	public ByteArrayInputStream getReportExcel(ReportParametersModel parametersModel) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Workbook workbook = createLossReportWorkbook(parametersModel);
		workbook.write(out);
		return new ByteArrayInputStream(out.toByteArray());
	}

	private void createLabelsRow(Sheet sheet, Integer rowNum) {
		try {
			Row headerRow;
			headerRow = sheet.createRow(rowNum);
			Cell cell = headerRow.createCell(1);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue("Energy exchanged at Transmission-Distribution Boundary");
			cell = headerRow.createCell(2);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(3);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(4);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue("T-D Interface points");
			cell = headerRow.createCell(5);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(6);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(7);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Workbook createLossReportWorkbook(ReportParametersModel parametersModel) {
		if (null == parametersModel) {
			parametersModel = new ReportParametersModel();
		}

		if (parametersModel.getReportYear() == null) {
			parametersModel.setReportYear(2019);
		}
		if (parametersModel.getReportMonth() == null) {
			parametersModel.setReportMonth(05);
		} else {
			parametersModel.setReportMonth(parametersModel.getReportMonth() - 1);
		}

		ConsolidatedLossReportModel consolidatedLossReportModel = ilossReportService
				.getConsolidatedMonthlyLossReport(parametersModel.getReportMonth(), parametersModel.getReportYear(),true);

		Workbook workbook = new XSSFWorkbook();
		String sheetName = "PSTCL_Losses_" + parametersModel.getReportMonth() + "_" + parametersModel.getReportYear();
		createConsolidatedReportSheet(sheetName,parametersModel, consolidatedLossReportModel, workbook);

		return workbook;
	}

	private void createConsolidatedReportSheet(String sheetName,ReportParametersModel parametersModel,
			ConsolidatedLossReportModel consolidatedLossReportModel, Workbook workbook) {
		CreationHelper createHelper = workbook.getCreationHelper();

		Sheet sheet = workbook.createSheet(sheetName);
		Integer rowNumber =2;

		String[] headerColumnLabels = { "", "Description", "Interface", "Energy imported at Boundary points (MWh)",
				"Energy Exported at Boundary points (MWh)", "Total Interface Points", "CMRI data taken",
				"Data taken from other sources (Manual Entry)", };
		Integer[] headerColumnWidth = { 10, 400, 100, 200,
				200, 100, 100,200 };
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,parametersModel.getReportMonth());
		cal.set(Calendar.YEAR,parametersModel.getReportYear());
		
		String[] logoRowLabels = { "PSTCL-Loss Report for the month of "+new SimpleDateFormat("MMMM,YYYY").format(cal.getTime()) };
		Integer[] logoRowCellMergeSize = { headerColumnLabels.length};
		rowNumber = createTopLogoRow1(sheet, getHeaderCellStyle(workbook), logoRowLabels,logoRowCellMergeSize ,rowNumber);

		
		
		// Row for Header
		
		rowNumber = createTopLabels(sheet, getHeaderCellStyle(workbook), headerColumnLabels,headerColumnWidth ,rowNumber);

		// SHEET AND ROW FOR I_T
		LossReportModel lossReportModel = consolidatedLossReportModel.getLossReportModelMap()
				.get(EAUtil.LOSS_REPORT_CRITERIA_I_T_);
		createRow(sheet, rowNumber++, "Energy Interchange Between Interstate & PSTCL Substations", "I-T",
				lossReportModel);
		createLossReportCriteriaSheet(workbook, lossReportModel);

		// SHEET AND ROW FOR G_T
		lossReportModel = consolidatedLossReportModel.getLossReportModelMap().get(EAUtil.LOSS_REPORT_CRITERIA_G_T);

		createRow(sheet, rowNumber++, "Energy Interchange Between Punjab Generating Plants &	PSTCL Substations",
				"G-T", lossReportModel);

		createLossReportCriteriaSheet(workbook, lossReportModel);

		createLabelsRow(sheet, rowNumber++);

		// SHEET AND ROW FOR TD22066
		lossReportModel = consolidatedLossReportModel.getLossReportModelMap()
				.get(EAUtil.LOSS_REPORT_CRITERIA_T_D_220_66_);

		createRow(sheet, rowNumber++, "220/66kV Transformers", "T-D", lossReportModel);
		createLossReportCriteriaSheet(workbook, lossReportModel);

		// SHEET AND ROW FOR TD 132 66
		lossReportModel = consolidatedLossReportModel.getLossReportModelMap()
				.get(EAUtil.LOSS_REPORT_CRITERIA_T_D_132_66);

		createRow(sheet, rowNumber++, "132/66kV Transformers", "T-D", lossReportModel);
		createLossReportCriteriaSheet(workbook, lossReportModel);

		// SHEET AND ROW FOR TD 132 33
		lossReportModel = consolidatedLossReportModel.getLossReportModelMap()
				.get(EAUtil.LOSS_REPORT_CRITERIA_T_D_132_33_);

		createRow(sheet, rowNumber++, "132/33kV Transformers", "T-D", lossReportModel);
		createLossReportCriteriaSheet(workbook, lossReportModel);

		// SHEET AND ROW FOR TD 132 11
		lossReportModel = consolidatedLossReportModel.getLossReportModelMap()
				.get(EAUtil.LOSS_REPORT_CRITERIA_T_D_132_11_);

		createRow(sheet, rowNumber++, "132/11kV Transformers", "T-D", lossReportModel);
		createLossReportCriteriaSheet(workbook, lossReportModel);

		// SHEET AND ROW FOR Independent
		lossReportModel = consolidatedLossReportModel.getLossReportModelMap()
				.get(EAUtil.LOSS_REPORT_CRITERIA_INDEPENDENT_);

		createRow(sheet, rowNumber++, "Independent Lines at 220kV & 132KV connected from PSTCL substations", "T-D",
				lossReportModel);

		createLossReportCriteriaSheet(workbook, lossReportModel);

		createRowSubTotal(sheet, rowNumber++,
				"Total Energy Interchange Between PSPCL Substations and PSTCL at Specified Transformers", "",
				consolidatedLossReportModel.getSumAllTD(), consolidatedLossReportModel.getCountAllTD(),
				consolidatedLossReportModel.getCountAllTDDataAvailable(),
				consolidatedLossReportModel.getCountAllTDDataManualEntry());

		createRowSubTotal(sheet, rowNumber++,
				"Total Energy Interchange between PSTCL and Interstate Tie Lines & Punjab Generating Plants (I-T)+(G-T)",
				"", consolidatedLossReportModel.getSumITGT(), consolidatedLossReportModel.getCountITGT(),
				consolidatedLossReportModel.getCountITGTDataAvailable(),
				consolidatedLossReportModel.getCountITGTDataManualEntry());

		createRowSubTotal(sheet, rowNumber++,
				"Total Energy Interchange between PSTCL and PSPCL Distribution system (T-D)", "",
				consolidatedLossReportModel.getSumAllTD(), consolidatedLossReportModel.getCountAllTD(),
				consolidatedLossReportModel.getCountAllTDDataAvailable(),
				consolidatedLossReportModel.getCountAllTDDataManualEntry());

		createRowSubTotal(sheet, rowNumber++, "Total energy exchanged", "", consolidatedLossReportModel.getSumAll(),
				consolidatedLossReportModel.getCountAll(), consolidatedLossReportModel.getCountAllDataAvailable(),
				consolidatedLossReportModel.getCountAllDataManualEntry());

		createRowBottom(sheet, rowNumber++, "Difference between above", consolidatedLossReportModel.getDifference());

		createRowBottom(sheet, rowNumber++, "PSTCL Loss In percentage", consolidatedLossReportModel.getPercentage());
	}

	private Integer createTopLogoRow1(Sheet sheet, CellStyle headerCellStyle, String[] logoRowLabels,Integer[] logoRowCellMergeSize, Integer rowNumber) {
		Integer logoRownum=rowNumber;
		Integer colStart=0;
		Row headerRow = sheet.createRow(rowNumber++);
		

		// Header
		for (int col = 0; col < logoRowLabels.length; col++) {
			Cell cell = headerRow.createCell(colStart);
			cell.setCellValue(logoRowLabels[col]);
			cell.setCellStyle(headerCellStyle);
			if(logoRowCellMergeSize!=null)
			{
				if(logoRowCellMergeSize[col]!=null)
				{
					
					sheet.addMergedRegion(new CellRangeAddress(logoRownum, logoRownum,colStart ,colStart+logoRowCellMergeSize[col]-1 ));
					colStart=colStart+logoRowCellMergeSize[col];
				}
			}
			
		}
		return rowNumber;
	}
	private Integer createTopLabels(Sheet sheet, CellStyle headerCellStyle, String[] headerColumnLabel,Integer[] headerColumnWidth, Integer rowNumber) {
		Row headerRow = sheet.createRow(rowNumber++);

		// Header
		for (int col = 0; col < headerColumnLabel.length; col++) {
			if(headerColumnWidth!=null)
			{
				if(headerColumnWidth[col]!=null)
				{
					sheet.setColumnWidth(col,(headerColumnWidth[col])*32);
				}
			}
			Cell cell = headerRow.createCell(col);
			cell.setCellValue(headerColumnLabel[col]);
			cell.setCellStyle(headerCellStyle);
		}
		return rowNumber;
	}

	private void createLossReportCriteriaSheet(Workbook workbook, LossReportModel lossReportModel) {
		// TODO Auto-generated method stub
		Sheet sheet = workbook.createSheet(lossReportModel.getCriteria().replace('/', '_'));

		Integer rowNumber = 5;

		String[] headerColumnLabels = { "S.No", "Location Id", "Utility Name", "Boundary Type", "Station Name",
				"Line/Transformer Name", "Voltage Level", "Meter Serial No.", "Meter Category", "External MF", "Sign",
				"Import Wh", "Export Wh", "Energy Imported at Boundary point (MWh)",
				"Energy Exported at Boundary point (MWh)", "Difference bw Import and Export (MWh)",
				"Net MWh=(Export-Import)*MF/1000000", "Remarks" };
		Integer[] headerColumnWidth = { 50, 300, 100, 100, 100,
				100, 100, 150, 100, 100, 100,
				100, 100, 200,
				200, 200,
				200, null };
		createTopLabels(sheet, getHeaderCellStyle(workbook), headerColumnLabels,headerColumnWidth, rowNumber++);

		Cell cell;
		for (LossReportEntity lossReportEntity : lossReportModel.getLossReportEntities()) {

			LocationMaster locationMaster = lossReportEntity.getLocation();
			Row row = sheet.createRow(rowNumber++);

			Integer cellnumber = 0;
			if (locationMaster != null) {
				cell = row.createCell(cellnumber++);

				if (lossReportEntity.getExportWHFCount() < lossReportEntity.getDaysInMonthCount()) {
					row.setRowStyle(getNormalStyle(sheet));
					cell.setCellStyle(getNormalStyle(sheet));
				}
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getLossReportOrder());

				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getLocationId());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getUtiltiyName());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getBoundaryTypeMaster().getBoundaryType());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getSubstationMaster().getStationName());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getFeederMaster().getFeederName());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(locationMaster.getVoltageLevel());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				
				cell.setCellValue(lossReportEntity.getMeterSrNo());
				
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
			
				cell.setCellValue(lossReportEntity.getMeterCategory());
				
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(lossReportEntity.getExternalMF());
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue(lossReportEntity.getNetWHSign());

			} else {

				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");

				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");

				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");
				cell = row.createCell(cellnumber++);
				cell.setCellStyle(getNormalStyle(sheet));
				cell.setCellValue("");

			}

			
			cell = row.createCell(cellnumber++);
			if (null != lossReportEntity.getImportWHFSum())
			{	
				cell.setCellStyle(getBoldCellStyle(sheet));

				cell.setCellValue(lossReportEntity.getImportWHFSum().doubleValue());
			}
			cell = row.createCell(cellnumber++);
			if (null != lossReportEntity.getExportWHFSum())
			{	cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportEntity.getExportWHFSum().doubleValue());
			}cell = row.createCell(cellnumber++);
			if (null != lossReportEntity.getImportBoundaryPtMWH())
			{	cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportEntity.getImportBoundaryPtMWH().doubleValue());
			}cell = row.createCell(cellnumber++);
			if (null != lossReportEntity.getExportBoundaryPtMWH())
			{	cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportEntity.getExportBoundaryPtMWH().doubleValue());
			}cell = row.createCell(cellnumber++);
			if (null != lossReportEntity.getBoundaryPtImportExportDifferenceMWH())
			{	cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportEntity.getBoundaryPtImportExportDifferenceMWH().doubleValue());
			}cell = row.createCell(cellnumber++);
			if (null != lossReportEntity.getNetMWH())
			{cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportEntity.getNetMWH().doubleValue());
			}
			cell = row.createCell(cellnumber++);
			cell.setCellStyle(getNoWrapTextStyle(sheet));
			
			cell.setCellValue("No of Days of Export	Data:"+lossReportEntity.getExportWHFCount() +"No of Days of Import Data"
					+ lossReportEntity.getImportWHFCount()+"No of Days in the Month Data:"+lossReportEntity.getDaysInMonthCount());

		}

	}

	

	private void createRowBottom(Sheet sheet, Integer rowNumber, String rowDesc, java.math.BigDecimal value) {
		try {
			Row headerRow;
			headerRow = sheet.createRow(rowNumber);
			Cell cell = headerRow.createCell(1);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(rowDesc);
			cell = headerRow.createCell(2);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(3);
			if (value == null) {
				value = java.math.BigDecimal.ZERO;
			}
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(value.doubleValue());
			cell = headerRow.createCell(4);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(5);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(6);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
			cell = headerRow.createCell(7);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createRowSubTotal(Sheet sheet, Integer rowNumber, String rowDesc, String boundaryType,
			LossReportEntity lossReportModel, Integer pointsCount, Integer dataAvailableCount,
			Integer manualDataCount) {
		try {
			Row headerRow;
			headerRow = sheet.createRow(rowNumber);
			Cell cell = headerRow.createCell(1);
			cell.setCellStyle(getBoldCellStyle(sheet));

			cell.setCellValue(rowDesc);
			cell = headerRow.createCell(2);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(boundaryType);
			cell = headerRow.createCell(3);
			cell.setCellType(CellType.NUMERIC);
			if (null == lossReportModel) {
				lossReportModel = new LossReportEntity();
			}
			if (lossReportModel.getImportBoundaryPtMWH() == null) {
				lossReportModel.setImportBoundaryPtMWH(java.math.BigDecimal.ZERO);
				// value=java.math.BigDecimal.ZERO;
			}
			if (lossReportModel.getExportBoundaryPtMWH() == null) {
				lossReportModel.setExportBoundaryPtMWH(java.math.BigDecimal.ZERO);
				// value=java.math.BigDecimal.ZERO;
			}
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportModel.getImportBoundaryPtMWH().doubleValue());
			cell = headerRow.createCell(4);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportModel.getExportBoundaryPtMWH().doubleValue());
			cell = headerRow.createCell(5);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(pointsCount);
			cell = headerRow.createCell(6);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(dataAvailableCount);
			cell = headerRow.createCell(7);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(manualDataCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createRow(Sheet sheet, Integer rowNumber, String rowDesc, String boundaryType,
			LossReportModel lossReportModel) {

		try {
			if (null == lossReportModel) {
				lossReportModel = new LossReportModel();
			}
			if (null == lossReportModel.getSumEntity()) {
				lossReportModel.setSumEntity(new LossReportEntity());

			}
			if (lossReportModel.getSumEntity().getImportBoundaryPtMWH() == null) {
				lossReportModel.getSumEntity().setImportBoundaryPtMWH(java.math.BigDecimal.ZERO);
				// value=java.math.BigDecimal.ZERO;
			}
			if (lossReportModel.getSumEntity().getExportBoundaryPtMWH() == null) {
				lossReportModel.getSumEntity().setExportBoundaryPtMWH(java.math.BigDecimal.ZERO);
				// value=java.math.BigDecimal.ZERO;
			}

			Row headerRow;
			headerRow = sheet.createRow(rowNumber);
			Cell cell = headerRow.createCell(1);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(rowDesc);
			cell = headerRow.createCell(2);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(boundaryType);
			cell = headerRow.createCell(3);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportModel.getSumEntity().getImportBoundaryPtMWH().doubleValue());
			cell = headerRow.createCell(4);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellStyle(getBoldCellStyle(sheet));
			cell.setCellValue(lossReportModel.getSumEntity().getExportBoundaryPtMWH().doubleValue());
			cell = headerRow.createCell(5);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(lossReportModel.getTotalLocations().size());
			cell = headerRow.createCell(6);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(lossReportModel.getPointsCountDataAvailable());
			cell = headerRow.createCell(7);
			cell.setCellStyle(getNormalStyle(sheet));
			cell.setCellValue(lossReportModel.getManualEntryLocations().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
