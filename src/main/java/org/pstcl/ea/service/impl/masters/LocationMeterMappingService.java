package org.pstcl.ea.service.impl.masters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.pstcl.ea.dao.IDailyTransactionDao;
import org.pstcl.ea.dao.IDailyTransactionRemovedDao;
import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.dao.IMeterMasterDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.SubstationMaster;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransactionForRemovedMeters;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransactionMappedSuper;
import org.pstcl.ea.model.EAFilter;
import org.pstcl.ea.model.EAModel;
import org.pstcl.ea.model.LocationSurveyDataModel;
import org.pstcl.ea.model.SubstationMeter;
import org.pstcl.ea.model.mapping.LocationMeterMappingModel;
import org.pstcl.ea.service.impl.SubstationDataServiceImpl;
import org.pstcl.ea.service.impl.parallel.CalculationMappingUtil;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.pstcl.model.FilterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("locationMeterMappingService")
@org.springframework.transaction.annotation.Transactional(value="sldcTxnManager")
public class LocationMeterMappingService extends CalculationMappingUtil{

	@Autowired
	SubstationDataServiceImpl substationDataService;

	@Autowired
	MeterLocationMapDao mtrLocMapDao;

	@Autowired
	protected MeterLocationMapDao mtrLocMappingDao;

	@Autowired
	protected ILocationEMFDao locEmfDao;

	@Autowired
	IDailyTransactionDao dailyTransactionDao;

	@Autowired
	IDailyTransactionRemovedDao dailyTransactionRemovedDao;


	public MeterLocationMap getMeterDetails(int id) {
		return mtrLocMapDao.findById(id);

	}

	public List<SubstationMeter> findSubstationEnergyMeters(FilterModel filterModel) {
		EAFilter eaModel = new EAFilter();
		eaModel.setCircle(filterModel.getSelectedCircle());
		eaModel.setDivision(filterModel.getSelectedDivision());
		eaModel.setSubstation(filterModel.getSelectedSubstation());
		List<SubstationMaster> submasters = substationDataService.getSubstationList(eaModel);
		List<SubstationMeter> substationMeterList = new ArrayList<SubstationMeter>();
		for (SubstationMaster submaster : submasters) {

			SubstationMeter substationMeter = new SubstationMeter();
			eaModel.setSubstation(submaster);
			List<MeterLocationMap> substationMeterLocMapList = mtrLocMapDao.findMeterLocationMapBySubstation(eaModel);
			substationMeter.setMtrLocMap(substationMeterLocMapList);
			substationMeter.setSubstationMaster(submaster);

			try {
				substationMeterList.add(substationMeter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return substationMeterList;
	}
	@org.springframework.transaction.annotation.Transactional(value="sldcTxnManager")
	public List<MeterLocationMap> getMeterLocMapByLocationID(String locationid)

	{
		List<MeterLocationMap> meterLocationMappingList = mtrLocMapDao.findMeterLocationMapByLoc(locationid);
		return meterLocationMappingList;
	}

	/**
	 * Check Save details of changed location meter mapping
	 * 
	 * @param locationMeterMappingModel
	 * @return
	 */
	@org.springframework.transaction.annotation.Transactional(value="sldcTxnManager")
	public LocationMeterMappingModel saveLocationMeterMapping(LocationMeterMappingModel locationMeterMappingModel) {
		LocationMeterMappingModel resultModel=new LocationMeterMappingModel();

		try {
			endLocationMeterMapping(locationMeterMappingModel);

			if (locationMeterMappingModel.getLocation() != null && locationMeterMappingModel.getStartDate() != null) {
				MeterLocationMap newMtrLocMap = new MeterLocationMap();
				newMtrLocMap.setLocationMaster(locationMeterMappingModel.getLocation());

				newMtrLocMap.setMeterMaster(locationMeterMappingModel.getMeterMaster());
				newMtrLocMap.setStartDate(locationMeterMappingModel.getStartDate());
				if (mtrLocMapDao.find(newMtrLocMap) == false) {
					mtrLocMapDao.save(newMtrLocMap, null);
					modifyTxnsDueToMeterMapping(newMtrLocMap,resultModel);
				}
			}

			resultModel.setMappingSuccesful(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		resultModel.setMappingSuccesful(false);
		return resultModel;

	}


	private void modifyTxnsDueToMeterMapping(MeterLocationMap meterLocationMap,LocationMeterMappingModel locationMeterMappingModel)
	{
		if(null==meterLocationMap.getEndDate())
		{
			//When new mapping is added, transactions if any by the newly added meter are assigned to the new location
			getDailyTransactionsForRemovedMeter(meterLocationMap);
		}
		else if(null!=meterLocationMap.getEndDate())
		{
			//When a mapping is ended, i.e. when a meter is removed, transactions if any by the meter are removed from the DailyTransaction Table and kept in DailyTransactionRemovedMeters
			removeDailyTxnByOldMeter(meterLocationMap);
		}
	}

	private List<DailyTransactionMappedSuper> removeDailyTxnByOldMeter(MeterLocationMap newMtrLocMap) {
		//Date startDateOfMonth=	DateUtil.startDateTimeForDailyFromFileDate(newMtrLocMap.getEndDate());
		Date startDateOfMonth=DateUtil.startOfTheDay(newMtrLocMap.getEndDate());
		List<DailyTransactionMappedSuper> list=new ArrayList<>();
		List<DailyTransaction> dailyTransactions=dailyTransactionDao.getDailyTransactionsByMeterGreaterThanDate(newMtrLocMap.getMeterMaster(),startDateOfMonth);
		for (DailyTransaction dailyTransaction : dailyTransactions) {
			dailyTransaction.setLocation(null);
			dailyTransaction.setMeterLocationMap(null);
			dailyTransaction=calculateImportExport(dailyTransaction);
			dailyTransaction.setRemarks(dailyTransaction.getRemarks()+" removed due to Meter Mapping ending");

			DailyTransactionForRemovedMeters dailyTransactionForRemovedMeters=new DailyTransactionForRemovedMeters(dailyTransaction);
			dailyTransactionRemovedDao.saveOrUpdate(dailyTransactionForRemovedMeters, null);
			list.add(dailyTransactionForRemovedMeters);
			dailyTransactionDao.deleteById(dailyTransaction.getTxnId());
		}
		return list;
	}

	private List<DailyTransaction> getDailyTransactionsForRemovedMeter(MeterLocationMap newMtrLocMap) {
		Date startDateOfMonth =	DateUtil.previousDay(newMtrLocMap.getStartDate());
		List<DailyTransaction> list=new ArrayList<>();

		List<MeterLocationMap> mtrLocMapList =  mtrLocMappingDao.getLocationByMeterAndDate(newMtrLocMap.getMeterMaster(),startDateOfMonth);
		List<LocationMFMap> locationEMFList=locEmfDao.findLocationEmfByLocAndDate(mtrLocMapList, startDateOfMonth);
		List<DailyTransactionForRemovedMeters> dailyTransactionsRemovedMeter=dailyTransactionRemovedDao.getDailyTransactionForRemovedMeterssByMeterGreaterThanDate(newMtrLocMap.getMeterMaster(),startDateOfMonth);
		for (DailyTransactionForRemovedMeters dailyTransactionForRemovedMeters : dailyTransactionsRemovedMeter) {
			DailyTransaction dailyTransaction=new DailyTransaction(dailyTransactionForRemovedMeters);
			dailyTransaction.setLocation(null);
			dailyTransaction.setMeterLocationMap(null);
			setDailyTxnLocation(mtrLocMapList,dailyTransaction);
			setDailyTxnLocationMF(locationEMFList, dailyTransaction);
			dailyTransaction=calculateImportExport(dailyTransaction);
			dailyTransaction.setRemarks(dailyTransaction.getRemarks()+" modified due to Meter Mapping");
			dailyTransaction.setTransactionStatus(EAUtil.DAILY_TRANSACTION_MODFIED_DUE_TO_METER_MAPPING);
			dailyTransactionDao.save(dailyTransaction, null);
			list.add(dailyTransaction);
		}
		return list;
	}






	public LocationMeterMappingModel endLocationMeterMapping(LocationMeterMappingModel locationMeterMappingModel) {
		LocationMeterMappingModel resultModel=new LocationMeterMappingModel();
		MeterLocationMap oldMtrLocMap = locationMeterMappingModel.getOldMeterLocationMap();
		if (oldMtrLocMap != null) {
			if (locationMeterMappingModel.getEndDate() != null) {
				oldMtrLocMap.setEndDate(locationMeterMappingModel.getEndDate());
			}
			mtrLocMapDao.update(oldMtrLocMap, null);
			modifyTxnsDueToMeterMapping(oldMtrLocMap,resultModel);
		}
		resultModel.setMeterMaster(locationMeterMappingModel.getMeterMaster());
		resultModel.setMappingSuccesful(true);
		return locationMeterMappingModel;

	}

	public String validateDates(LocationMeterMappingModel locationMeterMap) {
		Date startDate = locationMeterMap.getStartDate();
		String error = null;
		List<MeterLocationMap> list= mtrLocMapDao.findMappingHistory(locationMeterMap.getMeterMaster(),locationMeterMap.getLocation());

		if(startDate==null)
		{
			error="Start Date can not be null";

		}
		else if(locationMeterMap.getLocation()==null)
		{
			error="Location can not be null";
		}
		else
		{
			for (MeterLocationMap meterLocationMap : list) {
				if(startDate.before(meterLocationMap.getEndDate())||startDate.before(meterLocationMap.getStartDate()))
				{
					error="Start Date at new location can not be lesser than end date of a previous location";
				}
			}

		}

		return error;
	}

	public String validateEndDate(LocationMeterMappingModel locationMeterMap) {

		Date endDate = locationMeterMap.getEndDate();
		Date current = new Date();
		String error = null;
		if (endDate == null) {
			error = "End date is Null";
		}
		else if (endDate.after(current)) {
			error = "Set End Date is greater than current Date";
		} 
		else if (endDate.before(locationMeterMap.getOldMeterLocationMap().getStartDate())) 
		{
			error = "Start Date of Meter At Old Location is After End Date at Old Location";
		}  
		return error;

	}

}
