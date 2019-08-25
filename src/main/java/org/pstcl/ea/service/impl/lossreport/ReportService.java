package org.pstcl.ea.service.impl.lossreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pstcl.ea.dao.IDailyTransactionDao;
import org.pstcl.ea.dao.IInstantRegistersDao;
import org.pstcl.ea.dao.ILocationMasterDao;
import org.pstcl.ea.dao.ILossReportDao;
import org.pstcl.ea.dao.ITamperLogDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.InstantRegisters;
import org.pstcl.ea.model.FileFilter;
import org.pstcl.ea.model.LocationFileModel;
import org.pstcl.ea.model.LocationSurveyDataModel;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.model.reporting.TamperDetailsProjectionEntity;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportService")
public class ReportService {
	
	@Autowired
	protected IDailyTransactionDao dailyTransactionDao;
	
	

	@Autowired
	protected ILossReportDao lossReportDao;
	
	@Autowired
	protected ILocationMasterDao locationMasterDao;
	



	@Autowired
	MeterLocationMapDao mtrLocMapDao;

	public LocationSurveyDataModel getReportTransactions(String locationId, int month, int year) {

		LocationSurveyDataModel locationSurveyDataModel=new LocationSurveyDataModel();
		LocationMaster locationMaster=locationMasterDao.findById(locationId);
		//MeterMaster  meterMaster=meterDao.findMeterForMonth(locationId, month, year);
		locationSurveyDataModel.setLocationMaster(locationMaster);
		//locationSurveyDataModel.setMeterMaster(meterMaster);
		locationSurveyDataModel.setDailyTransactions(dailyTransactionDao.getDailyTransactionsByMonth(locationMaster, month, year));
		return locationSurveyDataModel;
	}

	public LocationSurveyDataModel getReportTransactions(ReportParametersModel parametersModel) {
		// TODO Auto-generated method stub
		return getReportTransactions(parametersModel.getLocationId(),parametersModel.getReportMonth(),parametersModel.getReportYear());
	}


	public List<LocationFileModel> getPendingLossReportLocation(Integer month, Integer year) {

		List<LocationFileModel> list =new ArrayList<LocationFileModel>();
		Date startDate=DateUtil.startDateTimeForDailySurveyRecs(month, year);
		Date endDate=DateUtil.endDateTimeForDailySurveyRecs(month, year);
		//locationMasterDao.findPendingLossReportLocations1(startDate,endDate);
		//List <LocationMaster> locations =lossReportDao.getIncompleteDailyEntryLocations(null,startDate,endDate);

		List <LocationMaster> locations =lossReportDao.findPendingLossReportLocations(startDate,endDate);
		FileFilter filter=new FileFilter();
		filter.setTransactionDateFrom(DateUtil.startDateTimeForDailySurveyRecs(month+1, year));
		filter.setTransactionDateTo(DateUtil.endDateTimeForDailySurveyRecs(month+1, year));

		for (LocationMaster locationMaster : locations) {
			LocationFileModel locationFileModel=new LocationFileModel();
			locationFileModel.setLocationMaster(locationMaster);
			filter.setLocation(locationMaster);
			filter.setFileActionStatus(EAUtil.FILE_ACTION__APPROVED_AE);
			List<MeterLocationMap> meterLocationMappingList = mtrLocMapDao.findMeterLocationMapByLoc(locationMaster.getLocationId());
			locationFileModel.setMeterLocationMaps(meterLocationMappingList);
			//List<FileMaster> fileMasters=fileMasterDao.filterFiles(filter);
			//locationFileModel.setFileMasters(fileMasters);
			list.add(locationFileModel);
		}

		return list;
	}


}

