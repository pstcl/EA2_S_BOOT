package org.pstcl.ea.service.impl.lossreport;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.LossReportEntity;
import org.pstcl.ea.entity.meterTxnEntity.InstantRegisters;
import org.pstcl.ea.model.reporting.ConsolidatedLossReportModel;
import org.pstcl.ea.model.reporting.LossReportModel;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.model.reporting.TamperDetailsProjectionEntity;

public interface IlossReportService {

	
	LossReportEntity saveLossReportEntity(LossReportEntity lossReportEntity);


	LossReportModel getReport(String reportType, int month, int year);

	
	LossReportModel getDateRangeReport(String reportType, Date startDate, Date endDate, Boolean initializeLazy);

	ConsolidatedLossReportModel getConsolidatedDateRangeLossReport(Date startDate, Date endDate,
			Boolean initialiseLazy);

	ConsolidatedLossReportModel getConsolidatedMonthlyLossReport(int month, int year, Boolean initialiseLazy);


}