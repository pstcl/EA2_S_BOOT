package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.SubstationMaster;
import org.pstcl.ea.entity.meterTxnEntity.TamperLogTransaction;
import org.pstcl.ea.model.ImportExportModel;
import org.pstcl.ea.model.reporting.TamperDetailsProjectionEntity;


public interface ITamperLogDao  {
	TamperLogTransaction findById(int id);
	TamperLogTransaction findByLocationDateCombo(LocationMaster location, Date txnDate);
	void deleteById(String id);
	void save(TamperLogTransaction meter, EAUser user);
	void update(TamperLogTransaction txn, EAUser user);
	List<TamperLogTransaction> findTamperLogByLocationList(ImportExportModel importExportModel);
	
	List<TamperLogTransaction> findTamperLogByDayAndLocation(LocationMaster location, Integer dayOfMonth, int monthOfYear, int year);
	
	List<TamperLogTransaction> findTamperLogBySubstation(SubstationMaster location, int dayOfMonth, int monthOfYear,
			int year);
	List<TamperLogTransaction> getTamperLogTransactionsByMonth(LocationMaster locationMaster, Integer month, Integer year
			);
	void save(List<TamperLogTransaction> loadSurveyList, EAUser loggedInUser);
	List<TamperDetailsProjectionEntity> getTamperLogTransactionsCountByDateRange(Date startDate, Date endDate);
	List<TamperLogTransaction> findTamperLogByDayAndMeter(MeterMaster meterMaster, Object object, Integer monthOfYear,
			Integer year);
	
}
