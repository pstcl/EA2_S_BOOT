package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.SubstationMaster;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.entity.meterTxnEntity.jpa.LoadSurveyTransaction;
import org.pstcl.ea.model.ImportExportModel;


public interface ILoadSurveyTransactionDao  {
	LoadSurveyTransaction findById(int id);
	LoadSurveyTransaction findByLocationDateCombo(LocationMaster location, Date txnDate);
	void deleteById(String id);
	void save(LoadSurveyTransaction meter, EAUser user);
	void update(LoadSurveyTransaction txn, EAUser user);
	List<LoadSurveyTransaction> findLoadSurveyTxnByLocationList(ImportExportModel importExportModel);
	
	List<LoadSurveyTransaction> findLoadSurveyByDayAndLocation(LocationMaster location, int dayOfMonth, int monthOfYear, int year);
	
	List<LoadSurveyTransaction> findLoadSurveyTxnsBySubstation(SubstationMaster location, int dayOfMonth, int monthOfYear,
			int year);
	void save(List<LoadSurveyTransaction> loadSurveyList, EAUser loggedInUser);
	List<LoadSurveyTransaction> findLoadSurveyByDayAndLocation(LocationMaster location, Date transactionDate);
	DailyTransaction sumLoadSurveyByDayAndLocation(LocationMaster location, Date transactionDate);
	//DailyTransaction sumLoadSurveyByDayAndMeter(MeterMaster meterMaster, Date transactionDate);
	DailyTransaction sumLoadSurveyByDayAndMeter(MeterMaster meterMaster, Date startDate, Date endDate);
	//List<LoadSurveyTransaction> findLoadSurveyByDayAndLocation(LocationMaster location, Date startDate, Date endDate);
	
}
