package org.pstcl.ea.service.impl.masters;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.pstcl.ea.dao.IDailyTransactionDao;
import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.model.LocationSurveyDataModel;
import org.pstcl.ea.model.mapping.LocationEMFModel;
import org.pstcl.ea.service.impl.parallel.CalculationMappingUtil;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EMFMappingService extends CalculationMappingUtil {

	@Autowired
	ILocationEMFDao locEmfDao;

	@Autowired
	protected MeterLocationMapDao mtrLocMappingDao;

	@Autowired
	IDailyTransactionDao dailyTransactionDao;

	@Autowired
	LocationMasterService locationMasterService;

	public LocationMFMap getLocationRecentEmfByLocid(String locationId) {
		return locEmfDao.findLocationRecentEmf(locationId);
	}

	/*
	 * display mapped emf with location on successful changed details
	 */
	public List<LocationMFMap> getLocationEmfListByLocid(String locationId) {
		// TODO Auto-generated method stub
		return locEmfDao.findLocationEmfByDate(locationId, null);
	}

	public LocationEMFModel prepareEMFModelForLocation(String locationId) {
		LocationMaster locationMaster = locationMasterService.findLocationById(locationId);
		LocationMFMap locationMFMap = getLocationRecentEmfByLocid(locationId);

		LocationEMFModel locationEMFModel = new LocationEMFModel();
		locationEMFModel.setLocationMaster(locationMaster);

		locationEMFModel.setOldLocationEmf(locationMFMap);
		if (locationMFMap != null) {
			locationEMFModel.setExternalMF(locationMFMap.getExternalMF());
			locationEMFModel.setNetWHSign(locationMFMap.getNetWHSign());
		}
		return locationEMFModel;
	}

	/**
	 * Check and save details of location and emf mapping
	 * @param changeLocationEmf
	 * @return
	 */
	public LocationEMFModel saveDetailsOfLocationEmf(LocationEMFModel changeLocationEmf) {
		try {
			if (changeLocationEmf.getOldLocationEmf() != null) {
				//Set end date for existing EMF
				LocationMFMap updateEmf = changeLocationEmf.getOldLocationEmf();
				updateEmf.setEndDate(changeLocationEmf.getEffectiveDate());
				locEmfDao.update(updateEmf, null);
			}
			if (changeLocationEmf.getEffectiveDate()!=null&&changeLocationEmf.getExternalMF()!=null) 
			{
				//Save new EMF
				LocationMFMap newEmf = new LocationMFMap();
				newEmf.setLocationMaster(changeLocationEmf.getLocationMaster());
				newEmf.setStartDate(changeLocationEmf.getEffectiveDate());
				newEmf.setExternalMF(changeLocationEmf.getExternalMF());
				newEmf.setNetWHSign(changeLocationEmf.getNetWHSign());
				newEmf.setExternalCTRation(changeLocationEmf.getExternalCTRation());
				newEmf.setExternalPTRation(changeLocationEmf.getExternalPTRation());
				locEmfDao.save(newEmf, null);


				//Remove all those EMF where start date and end date is same i.e. when more than one EMF is added by the user with same start date
				//system saves the EMF and all previous are ended on the effective date thus all those having same start date are ineffective
				List<LocationMFMap> mfMap=locEmfDao.findLocationEmfByDate(changeLocationEmf.getLocationMaster().getLocationId(),null);
				if (CollectionUtils.isNotEmpty(mfMap))
				{					
					for (LocationMFMap locationMFMap : mfMap) {
						if(locationMFMap.getEndDate()!=null)
						{
							if(locationMFMap.getStartDate().compareTo(locationMFMap.getEndDate())==0 && locationMFMap.getLocationMaster().getLocationId().equalsIgnoreCase(changeLocationEmf.getLocationMaster().getLocationId()))
							{
								locEmfDao.deleteById(locationMFMap.getId());
							}
						}
					}
				}
				LocationSurveyDataModel mappingAffectedDAilyTxns= calcDailyTxnByNewEMFMapping(changeLocationEmf.getLocationMaster(),changeLocationEmf.getEffectiveDate());
				changeLocationEmf.setLocationSurveyDataModel(mappingAffectedDAilyTxns);

			}

		} catch (Exception e) {
			e.printStackTrace();
			changeLocationEmf.setMappingSuccesful(false);
			return changeLocationEmf;
		}
		changeLocationEmf.setMappingSuccesful(true);
		return changeLocationEmf;
	}

	private LocationSurveyDataModel calcDailyTxnByNewEMFMapping(LocationMaster locationMaster, Date effectiveDate) {
		Date startDateOfMonth = DateUtil.startDateTimeForDailyFromFileDate(effectiveDate);
		List<LocationMFMap> locationEMFList = locEmfDao
				.findLocationEmfByDate(locationMaster.getLocationId(), startDateOfMonth);
		List<DailyTransaction> dailyTransactions = dailyTransactionDao
				.getDailyTransactionsByLocGreaterThanDate(locationMaster, startDateOfMonth);
		for (DailyTransaction dailyTransaction : dailyTransactions) {
			setDailyTxnLocationMF(locationEMFList, dailyTransaction);
			dailyTransaction = calculateImportExport(dailyTransaction);
			String remarks = dailyTransaction.getRemarks();
			remarks = (remarks == null ? "" : remarks) + "Modified due to MF Mapping";
			dailyTransaction.setRemarks(remarks);
			dailyTransaction.setTransactionStatus(EAUtil.DAILY_TRANSACTION_MODFIED_DUE_TO_MF_MAPPING);
		}
		dailyTransactionDao.save(dailyTransactions, null);

		LocationSurveyDataModel locationSurveyDataModel = new LocationSurveyDataModel();
		locationSurveyDataModel.setDailyTransactions(dailyTransactions);
		return locationSurveyDataModel;

	}

	public String validate(LocationEMFModel changeLocationEmf) {

		String error = null;

		if (changeLocationEmf.getExternalMF() == null || changeLocationEmf.getEffectiveDate() == null
				|| changeLocationEmf.getNetWHSign() == null) {

			error = "One of value is not set";
		} else {

			List<LocationMFMap> locationEMFList = locEmfDao.findLocationEmfByDate(
					changeLocationEmf.getLocationMaster().getLocationId(), changeLocationEmf.getEffectiveDate());
			for (LocationMFMap locationMFMap : locationEMFList) {
				if (locationMFMap.getStartDate().after(changeLocationEmf.getEffectiveDate())) {
					error = "Mapping with later dates than you are entering already exists!";
				} else if (null != locationMFMap.getEndDate()) {
					if (locationMFMap.getEndDate().after(changeLocationEmf.getEffectiveDate())) {
						error = "Mapping with later dates than you are entering already exists!";

					}
				}
			}

		}

		return error;
	}

}
