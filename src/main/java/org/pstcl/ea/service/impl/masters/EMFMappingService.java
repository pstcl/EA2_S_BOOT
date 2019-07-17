package org.pstcl.ea.service.impl.masters;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.dao.IDailyTransactionDao;
import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.model.entity.DailyTransaction;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.mapping.LocationEMFModel;
import org.pstcl.ea.model.mapping.LocationMFMap;
import org.pstcl.ea.model.mapping.MeterLocationMap;
import org.pstcl.ea.service.impl.parallel.CalculationMappingUtil;
import org.pstcl.ea.util.DateUtil;
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
		LocationMaster locationMaster=locationMasterService.findLocationById(locationId);
		LocationMFMap locationMFMap= getLocationRecentEmfByLocid(locationId);		

		LocationEMFModel locationEMFModel = new LocationEMFModel();
		locationEMFModel.setLocationMaster(locationMaster);

		locationEMFModel.setOldLocationEmf(locationMFMap);
		if(locationMFMap!=null)
		{
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
	public boolean saveDetailsOfLocationEmf(LocationEMFModel changeLocationEmf) {
		try {
			if (changeLocationEmf.getOldLocationEmf() != null) {
				LocationMFMap updateEmf = changeLocationEmf.getOldLocationEmf();
				updateEmf.setEndDate(changeLocationEmf.getEffectiveDate());
				locEmfDao.update(updateEmf, null);
			}
			if (changeLocationEmf.getEffectiveDate()!=null&&changeLocationEmf.getExternalMF()!=null) 
			{
				LocationMFMap newEmf = new LocationMFMap();
				newEmf.setLocationMaster(changeLocationEmf.getLocationMaster());
				newEmf.setStartDate(changeLocationEmf.getEffectiveDate());
				newEmf.setExternalMF(changeLocationEmf.getExternalMF());
				newEmf.setNetWHSign(changeLocationEmf.getNetWHSign());

				if (locEmfDao.find(newEmf) == false)
				{		locEmfDao.save(newEmf, null);
				calcDailyTxnByNewEMFMapping(newEmf);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private void calcDailyTxnByNewEMFMapping(LocationMFMap newMtrLocMap) {
		Date startDateOfMonth=		DateUtil.startDateTimeForDailyFromFileDate(newMtrLocMap.getStartDate());
		List<LocationMFMap> locationEMFList=locEmfDao.findLocationEmfByDate(newMtrLocMap.getLocationMaster().getLocationId(), startDateOfMonth); 
		List<DailyTransaction> dailyTransactions=dailyTransactionDao.getDailyTransactionsByLocGreaterThanDate(newMtrLocMap.getLocationMaster(),startDateOfMonth);
		for (DailyTransaction dailyTransaction : dailyTransactions) {
			setDailyTxnLocationMF(locationEMFList, dailyTransaction);
			dailyTransaction=calculateImportExport(dailyTransaction);
			String remarks= dailyTransaction.getRemarks();
			remarks= (remarks==null?"":remarks)+"Modified due to MF Mapping";
			dailyTransaction.setRemarks(remarks);
			dailyTransactionDao.update(dailyTransaction, null);
		}

	}

	public String validate(LocationEMFModel changeLocationEmf) {

		String error = null;

		if(changeLocationEmf.getExternalMF() == null
				|| changeLocationEmf.getEffectiveDate() == null 
				|| changeLocationEmf.getNetWHSign() == null) 
		{

			error = "One of value is not set";
		}



		return error;
	}

}
