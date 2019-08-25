package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MonthlyTransaction;
import org.pstcl.ea.entity.SubstationMaster;
import org.pstcl.ea.model.ImportExportModel;


public interface IMonthlyTransactionDao {
	MonthlyTransaction findById(int id);
	MonthlyTransaction findMonthlyByLocationMonth(LocationMaster location, int monthOfYear, int year);

	void deleteById(String id);
	void save(MonthlyTransaction meter, EAUser user);
	void update(MonthlyTransaction txn, EAUser user);
	List<MonthlyTransaction> findDailyTxnByLocationList(ImportExportModel importExportModel);
	
	
	
	List<MonthlyTransaction> findMonthlyTxnsBySubstation(SubstationMaster location, int monthOfYear, int year);
}
