package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransactionForRemovedMeters;


public interface IDailyTransactionRemovedDao  {
	DailyTransactionForRemovedMeters findById(int id);
	DailyTransactionForRemovedMeters findByLocationDateCombo(LocationMaster location, Date txnDate);
	List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByMonth(LocationMaster locationMaster, Integer month, Integer year
			);
	DailyTransactionForRemovedMeters findByLocationDateFilter(DailyTransactionForRemovedMeters dailyTransaction);
	List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByLocGreaterThanDate(LocationMaster locationMaster, Date startDate);
	List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByMeterGreaterThanDate(MeterMaster meterMaster, Date startDate);
	List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByMonth(MeterMaster meterMaster, Integer month, Integer year);
	void deleteById(Integer id);
	void saveOrUpdate(DailyTransactionForRemovedMeters dailyTransaction, EAUser loggedInUser);
}
