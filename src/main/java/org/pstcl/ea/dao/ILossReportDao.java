package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.LossReportEntity;
import org.pstcl.ea.model.EAFilter;

public interface ILossReportDao {

	
	
	//List<LossReportEntity> getDailyTransactionsProjection(String reportCriteria, Date startDate, Date endDate);

	//List<LossReportEntity> getLossReportEntries(String reportCriteria, Date startDate, Date endDate);

	List<LocationMaster> manualDailyEntryLocations(String reportCriteria, Date startDate, Date endDate);

	List<LocationMaster> lossReportLocations(String reportCriteria, Date startDate, Date endDate);

	List<LocationMaster> getIncompleteDailyEntryLocations(String reportCriteria, Date startDate, Date endDate);
	
	List<LocationMaster> findPendingLossReportLocations(Date startDate,Date endDate);
	List<LocationMaster> findPendingLocations(EAFilter entity, Integer month, Integer year);

	LossReportEntity getDailyTransactionsProjectionSumEntity(String reportType, Date startDate, Date endDate);

	
	List<LossReportEntity> getDailyTransactionsProjection(String reportCriteria, Date startDate, Date endDate);


}
