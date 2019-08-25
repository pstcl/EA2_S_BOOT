package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;


public interface IDailyTransactionDao  {
	DailyTransaction findById(int id);
	DailyTransaction findByLocationDateCombo(LocationMaster location, Date txnDate);
	void save(DailyTransaction meter, EAUser user);
	void update(DailyTransaction txn, EAUser user);
	void save(List<DailyTransaction> dailyTransactions, EAUser loggedInUser);
	List<DailyTransaction> getDailyTransactionsByMonth(LocationMaster locationMaster, Integer month, Integer year
			);
	DailyTransaction findByLocationDateFilter(DailyTransaction dailyTransaction);
	List<DailyTransaction> getDailyTransactionsByLocGreaterThanDate(LocationMaster locationMaster, Date startDate);
	List<DailyTransaction> getDailyTransactionsByMeterGreaterThanDate(MeterMaster meterMaster, Date startDate);
	List<DailyTransaction> getDailyTransactionsByMonth(MeterMaster meterMaster, Integer month, Integer year);
	void deleteById(Integer id);
}
