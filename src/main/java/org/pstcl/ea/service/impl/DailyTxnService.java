package org.pstcl.ea.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.dao.ILocationMasterDao;
import org.pstcl.ea.dao.IMeterMasterDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.model.LocationSurveyDataModel;
import org.pstcl.ea.service.impl.parallel.CalculationMappingUtil;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dailyTxnService")
public class DailyTxnService extends EnergyAccountsService{


	@Autowired
	ILocationMasterDao locationMasterDao;

	@Autowired
	IMeterMasterDao meterDao;


	@Autowired
	protected MeterLocationMapDao mtrLocMappingDao;

	@Autowired
	protected ILocationEMFDao locEmfDao;




	public LocationSurveyDataModel createDailyFromLoadSurveyModel(String locationId, int month, int year) {

		LocationSurveyDataModel locationSurveyDataModel=new LocationSurveyDataModel();
		LocationMaster locationMaster=locationMasterDao.findById(locationId);

		MeterMaster  meterMaster=null;//meterDao.findMeterForMonth(locationId, month, year);
		locationSurveyDataModel.setLocationMaster(locationMaster);
		//locationSurveyDataModel.setMeterMaster(meterMaster);

		Date startDate=DateUtil.additionDailySurveyRecsStartDate(month, year);
		Date endDate=DateUtil.additionDailySurveyRecsEndDate(month, year);
		locationSurveyDataModel.setStartDate(startDate);
		locationSurveyDataModel.setEndDate(endDate);
		List<DailyTransaction> dailyTransactions=new ArrayList<DailyTransaction>();
		for(Date current = startDate;!current.after(endDate);current =DateUtil.nextDay(current))
		{
			DailyTransaction dailyTransaction=dailyTransactionDao.findByLocationDateCombo(locationMaster, current);
			DailyTransaction dailyTransactionFromLoadSurvey=null;
			if(null==dailyTransaction)
			{	
				MeterLocationMap  meterLocationMap=mtrLocMappingDao.findMeterLocationMapByDate(locationId, current);
				if(meterLocationMap!=null)
				{
					meterMaster=meterLocationMap.getMeterMaster();
				}
				if(meterMaster!=null)
				{
					Date startDateLoadSurvey=DateUtil.startDateTimeForLoadSurveyFromDailyDate(current);
					Date endDateLoadSurvey=DateUtil.endDateTimeForLoadSurveyFromDailyDate(current);
					dailyTransactionFromLoadSurvey=loadSurveyTransactionDao.sumLoadSurveyByDayAndMeter(meterMaster, startDateLoadSurvey,endDateLoadSurvey);
				}
				dailyTransaction=new DailyTransaction();
				dailyTransaction.setMeter(meterMaster);
				dailyTransaction.setTransactionDate(current);
				dailyTransaction.setLocation(locationMaster);
				if(dailyTransactionFromLoadSurvey!=null)
				{
					dailyTransaction.setExportWHF(dailyTransactionFromLoadSurvey.getExportWHF());
					dailyTransaction.setImportWHF(dailyTransactionFromLoadSurvey.getImportWHF());
					dailyTransaction.setRemarks(EAUtil.DAILY_TRANSACTION_FROM_LOAD_SURVEY);
					dailyTransaction.setTransactionStatus(EAUtil.DAILY_TRANSACTION_CALC_FROM_LOAD_SURVEY);
					
				}
				else
				{
					dailyTransaction.setExportWHF(new BigDecimal(0));
					dailyTransaction.setImportWHF(new BigDecimal(0));
					
					dailyTransaction.setRemarks(EAUtil.DAILY_TRANSACTION_ENTERED_MANUALLY);
					dailyTransaction.setTransactionStatus(EAUtil.DAILY_TRANSACTION_ADDED_MANUALLY);
					
				}
			}
			dailyTransactions.add(dailyTransaction);
		}

		locationSurveyDataModel.setDailyTransactions(dailyTransactions);
		return locationSurveyDataModel;
	}


	public LocationSurveyDataModel createReportTransactions(String locationId, int month, int year) {

		LocationSurveyDataModel locationSurveyDataModel=new LocationSurveyDataModel();
		LocationMaster locationMaster=locationMasterDao.findById(locationId);
		MeterMaster  meterMaster=null;
		locationSurveyDataModel.setLocationMaster(locationMaster);
		locationSurveyDataModel.setMeterMaster(meterMaster);

		Date startDate=DateUtil.additionDailySurveyRecsStartDate(month, year);
		Date endDate=DateUtil.additionDailySurveyRecsEndDate(month, year);
		locationSurveyDataModel.setStartDate(startDate);
		locationSurveyDataModel.setEndDate(endDate);
		List<DailyTransaction> dailyTransactions=new ArrayList<DailyTransaction>();
		for(Date current = startDate;!current.after(endDate);current =DateUtil.nextDay(current))
		{
			DailyTransaction dailyTransaction=dailyTransactionDao.findByLocationDateCombo(locationMaster, current);
			MeterLocationMap  meterLocationMap=mtrLocMappingDao.findMeterLocationMapByDate(locationId, current);
			if(meterLocationMap!=null)
			{
				meterMaster=meterLocationMap.getMeterMaster();
			}
			if(null==dailyTransaction)
			{	
				dailyTransaction=new DailyTransaction();
				dailyTransaction.setTransactionDate(current);
				dailyTransaction.setLocation(locationMaster);
				dailyTransaction.setExportWHF(new BigDecimal(0));
				dailyTransaction.setImportWHF(new BigDecimal(0));
				dailyTransaction.setMeter(meterMaster);
			}
			dailyTransaction.setRemarks(EAUtil.DAILY_TRANSACTION_ENTERED_MANUALLY);
			dailyTransactions.add(dailyTransaction);
		}

		locationSurveyDataModel.setDailyTransactions(dailyTransactions);
		return locationSurveyDataModel;
	}


	public LocationSurveyDataModel saveDailyTransactions(LocationSurveyDataModel dailyTransactionModel) {
		//CalculationMappingUtil is not a typical service class because it is never instantiated other than this.
		//USed as a part of DataReaderThread
		CalculationMappingUtil calculationMappingUtil=new CalculationMappingUtil();
		List<DailyTransaction> dailyTransactions=dailyTransactionModel.getDailyTransactions();


		Date startDateOfMonth=		dailyTransactionModel.getStartDate();
		List<MeterLocationMap> mtrLocMapList =  mtrLocMappingDao.getMapByLocationAndDate(dailyTransactionModel.getLocationMaster(), startDateOfMonth);

		List<LocationMFMap> locationEMFList=locEmfDao.findLocationEmfByLocAndDate(mtrLocMapList, startDateOfMonth); 

		for (DailyTransaction dailyTransaction : dailyTransactions) {

			//dailyTransaction.setTransactionStatus(EAUtil.DAILY_TRANSACTION_ADDED_MANUALLY);
			calculationMappingUtil.setDailyTxnLocationAndMF(mtrLocMapList, locationEMFList, dailyTransaction);
			calculationMappingUtil.calculateImportExport(dailyTransaction);
		}
		dailyTransactionDao.save(dailyTransactions, getLoggedInUser());
		return dailyTransactionModel;

	}

}
