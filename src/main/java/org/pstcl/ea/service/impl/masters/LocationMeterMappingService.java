package org.pstcl.ea.service.impl.masters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.pstcl.ea.dao.IDailyTransactionDao;
import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.dao.IMeterMasterDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.model.EAFilter;
import org.pstcl.ea.model.EAModel;
import org.pstcl.ea.model.LocationSurveyDataModel;
import org.pstcl.ea.model.SubstationMeter;
import org.pstcl.ea.model.entity.DailyTransaction;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.entity.MeterMaster;
import org.pstcl.ea.model.entity.SubstationMaster;
import org.pstcl.ea.model.mapping.LocationMFMap;
import org.pstcl.ea.model.mapping.LocationMeterMappingModel;
import org.pstcl.ea.model.mapping.MeterLocationMap;
import org.pstcl.ea.service.impl.SubstationDataServiceImpl;
import org.pstcl.ea.service.impl.parallel.CalculationMappingUtil;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.model.FilterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("locationMeterMappingService")
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
			List<MeterLocationMap> mtrLocMap = new ArrayList<MeterLocationMap>();
			Set<LocationMaster> locMtrs = submaster.getLocationMasters();
			for (LocationMaster locMtr : locMtrs) {
				List<MeterLocationMap> mlml = mtrLocMapDao.findMeterLocationMapByLoc(locMtr.getLocationId());
				if (mlml != null && mlml.size() > 0) {
					for (MeterLocationMap mlm : mlml)
						if (mlm != null)
							mtrLocMap.add(mlm);

				}

			}
			substationMeter.setMtrLocMap(mtrLocMap);
			substationMeter.setSubstationMaster(submaster);

			try {
				substationMeterList.add(substationMeter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return substationMeterList;
	}

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
	public boolean saveLocationMeterMapping(LocationMeterMappingModel locationMeterMappingModel) {
		try {
			endLocationMeterMapping(locationMeterMappingModel);

			if (locationMeterMappingModel.getLocation() != null && locationMeterMappingModel.getStartDate() != null) {
				MeterLocationMap newMtrLocMap = new MeterLocationMap();
				newMtrLocMap.setLocationMaster(locationMeterMappingModel.getLocation());

				newMtrLocMap.setMeterMaster(locationMeterMappingModel.getMeterMaster());
				newMtrLocMap.setStartDate(locationMeterMappingModel.getStartDate());
				if (mtrLocMapDao.find(newMtrLocMap) == false) {
					mtrLocMapDao.save(newMtrLocMap, null);
					calcDailyTxnByNewMeterMapping(newMtrLocMap);
				}



			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



	private void calcDailyTxnByNewMeterMapping(MeterLocationMap newMtrLocMap) {
		Date startDateOfMonth=		DateUtil.startDateTimeForDailyFromFileDate(newMtrLocMap.getStartDate());
		List<MeterLocationMap> mtrLocMapList =  mtrLocMappingDao.getLocationByMeterAndDate(newMtrLocMap.getMeterMaster(),startDateOfMonth);
		List<DailyTransaction> dailyTransactions=dailyTransactionDao.getDailyTransactionsByMeterGreaterThanDate(newMtrLocMap.getMeterMaster(),startDateOfMonth);
		for (DailyTransaction dailyTransaction : dailyTransactions) {
			setDailyTxnLocation(mtrLocMapList,dailyTransaction);
			dailyTransaction=calculateImportExport(dailyTransaction);
			dailyTransaction.setRemarks(dailyTransaction.getRemarks()+" modified due to Meter Mapping");
			dailyTransactionDao.update(dailyTransaction, null);
		}

	}



	public LocationSurveyDataModel getDailyTransactionsByMeterGreaterThanDate(LocationMeterMappingModel locationMeterMappingModel) 
	{
		LocationSurveyDataModel locationSurveyDataModel=new LocationSurveyDataModel();
		MeterMaster  meterMaster=locationMeterMappingModel.getMeterMaster();
		locationSurveyDataModel.setMeterMaster(meterMaster);
		locationSurveyDataModel.setDailyTransactions(dailyTransactionDao.getDailyTransactionsByMeterGreaterThanDate(locationMeterMappingModel.getMeterMaster(),locationMeterMappingModel.getStartDate()));
		return locationSurveyDataModel;
	}



	public LocationMeterMappingModel endLocationMeterMapping(LocationMeterMappingModel locationMeterMappingModel) {
		MeterLocationMap oldMtrLocMap = locationMeterMappingModel.getOldMeterLocationMap();
		if (oldMtrLocMap != null) {
			if (locationMeterMappingModel.getEndDate() != null) {
				oldMtrLocMap.setEndDate(locationMeterMappingModel.getEndDate());
			}
			mtrLocMapDao.update(oldMtrLocMap, null);
		}
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
