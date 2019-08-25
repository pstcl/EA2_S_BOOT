package org.pstcl.ea.service.impl.parallel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.entity.FileMaster;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.entity.meterTxnEntity.InstantRegisters;
import org.pstcl.ea.entity.meterTxnEntity.TamperLogTransaction;
import org.pstcl.ea.model.CMRIFileDataModel;
import org.pstcl.ea.model.FileFilter;
import org.pstcl.ea.model.FileModel;
import org.pstcl.ea.model.LocationFileModel;
import org.pstcl.ea.model.LocationSurveyDataModel;
import org.pstcl.ea.model.reporting.FileParameterModel;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.service.impl.EnergyAccountsService;
import org.pstcl.ea.service.impl.lossreport.IlossReportService;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;


@Service("dataService")
@EnableAsync
public class DataService extends EnergyAccountsService{

	@Autowired
	private ApplicationContext context;

	@Autowired
	private TaskExecutor taskExecutor; 

	@Autowired
	MeterLocationMapDao mtrLocMapDao;



	@Autowired
	IlossReportService lossReportService;



	public FileModel getCorruptRepoFiles(Integer month, Integer year) {

		FileFilter filter=new FileFilter();
		if(null!=month&&null!=year)
		{
			filter.setTransactionDateFrom(DateUtil.startDateTimeForDailySurveyRecs(month, year));
			filter.setTransactionDateTo(DateUtil.endDateTimeForDailySurveyRecs(month, year));
		}
		List<FileMaster> fileMasters=fileMasterDao.findCorruptFiles(filter);
		FileModel fileModel=new FileModel();
		fileModel.setFilesUploadedDetail(fileMasters);
		return fileModel;
	}


	public LocationFileModel getFilesForLocation(FileParameterModel parametersModel) {

		LocationFileModel locationFileModel=new LocationFileModel();
		locationFileModel.setLocationMaster(parametersModel.getLocation());
		FileFilter filter=new FileFilter();
		filter.setTransactionDateFrom(DateUtil.startDateTimeForDailySurveyRecs(parametersModel.getReportMonth()+1,parametersModel.getReportYear()));
		filter.setTransactionDateTo(DateUtil.endDateTimeForDailySurveyRecs(parametersModel.getReportMonth()+1,parametersModel.getReportYear()));
		filter.setLocation(parametersModel.getLocation());
		filter.setFileActionStatus(EAUtil.FILE_ACTION__APPROVED_AE);
		List<FileMaster> fileMasters=fileMasterDao.filterFiles(filter);
		locationFileModel.setFileMasters(fileMasters);
		return locationFileModel;

	}

	//	public CMRIFileDataModel processRepoFileForTamper(Integer id) {
	//
	//
	//		FileMaster fileMaster=fileMasterDao.findById(id);
	//		CMRIFileDataModel cmriDataModel=new CMRIFileDataModel();
	//
	//
	//		processTamperAsync(fileMaster);
	//
	//
	//		Integer year = 2018;
	//		Integer monthOfYear = 9;
	//
	//		if(null!=fileMaster.getTransactionDate())
	//		{
	//			Calendar cal = Calendar.getInstance();
	//			cal.setTime(fileMaster.getTransactionDate());
	//			year = cal.get(Calendar.YEAR);
	//			monthOfYear = cal.get(Calendar.MONTH)-1;
	//		}
	//
	//		List<TamperLogTransaction> tamperLogs=tamperLogTransactionDao.findTamperLogByDayAndMeter(fileMaster.getMeterMaster(),null , monthOfYear,year);
	//
	//		cmriDataModel.setTamperLogs(tamperLogs);
	//
	//		return cmriDataModel;
	//	}



	public FileModel getPendingRepoFiles(Integer month, Integer year) {

		FileFilter filter=new FileFilter();
		filter.setTransactionDateFrom(DateUtil.startDateTimeForDailySurveyRecs(month, year));
		filter.setTransactionDateTo(DateUtil.endDateTimeForDailySurveyRecs(month, year));
		filter.setProcessingStatus(EAUtil.FILE_ZIP_EXTRACTED);
		filter.setFileActionStatus(EAUtil.FILE_ACTION__APPROVED_AE);
		List<FileMaster> fileMasters=fileMasterDao.filterFiles(filter);
		FileModel fileModel=new FileModel();
		fileModel.setFilesUploadedDetail(fileMasters);
		return fileModel;
	}



	

	//	private void processTamperAsync(FileMaster fileDetails) {
	//		taskExecutor.execute(new Runnable() {
	//
	//			@Override
	//			public void run() {
	//
	//				DataReaderThread dataReaderThread= context.getBean(DataReaderThread.class);
	//
	//				CMRIFileDataModel cmriFileDataModel=new CMRIFileDataModel();
	//				cmriFileDataModel.setFileMaster(fileDetails);
	//
	//				dataReaderThread.extractTransactionDataFromFile(cmriFileDataModel);
	//				//dataReaderThread.saveTamperData(cmriFileDataModel);
	//
	//			}
	//		} );
	//	}


	private void processFilesAsync(FileMaster fileDetails) {
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {

				DataReaderThread dataReaderThread= context.getBean(DataReaderThread.class);

				CMRIFileDataModel cmriFileDataModel=new CMRIFileDataModel();
				cmriFileDataModel.setFileMaster(fileDetails);

				dataReaderThread.extractTransactionDataFromFile(cmriFileDataModel);
				dataReaderThread.saveLoadDailyFileDataToDB(cmriFileDataModel);

			}
		} );
	}

	//	public FileModel processZipFilesForInstantRegisters(Integer month, Integer year2)
	//	{
	//		FileFilter filter=new FileFilter();
	//		filter.setTransactionDateFrom(DateUtil.startDateTimeForDailySurveyRecs(month, year2));
	//		filter.setTransactionDateTo(DateUtil.endDateTimeForDailySurveyRecs(month, year2));
	//		filter.setProcessingStatus(EAUtil.FILE_TXT_PROCESSED);
	//
	//		List<FileMaster> fileMasters=fileMasterDao.filterFiles(filter);
	//		for (FileMaster fileMaster : fileMasters) {
	//			try {
	//				processRepoFileForInstantRegisters(fileMaster.getTxnId());
	//			}catch(Exception e)
	//			{
	//				e.printStackTrace();
	//			}
	//		}
	//
	//		FileModel fileModel=new FileModel();
	//		fileModel.setFilesUploadedDetail(fileMasters);
	//		return fileModel;
	//	}




	private void processFileSerial(FileMaster textFile) {
		DataReaderThread dataReaderThread= context.getBean(DataReaderThread.class);

		//dataReaderThread.setTextFile(textFile);
		CMRIFileDataModel cmriFileDataModel=new CMRIFileDataModel();
		cmriFileDataModel.setFileMaster(textFile);

		dataReaderThread.extractTransactionDataFromFile(cmriFileDataModel);

		dataReaderThread.saveLoadDailyFileDataToDB(cmriFileDataModel);
	}


//	public CMRIFileDataModel processRepoFile(Integer id) {
//
//
//		FileMaster fileMaster=fileMasterDao.findById(id);
//
//
//
//		processFilesAsync(fileMaster);
//
//
//		Integer year = 2018;
//		Integer monthOfYear = 9;
//
//		if(null!=fileMaster.getTransactionDate())
//		{
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(fileMaster.getTransactionDate());
//			year = cal.get(Calendar.YEAR);
//			monthOfYear = cal.get(Calendar.MONTH)-1;
//		}
//
//		List<DailyTransaction> dailyTransactions=dailyTransactionDao.getDailyTransactionsByMonth(fileMaster.getMeterMaster(),  monthOfYear,year);
//		CMRIFileDataModel cmriDataModel=new CMRIFileDataModel();
//		cmriDataModel.setDailyTransactions(dailyTransactions);
//		return cmriDataModel;
//	}


	public CMRIFileDataModel processRepoFileSerial(Integer id) {


		FileMaster fileMaster=fileMasterDao.findById(id);
		CMRIFileDataModel cmriDataModel=new CMRIFileDataModel();
		processFileSerial(fileMaster);
		Integer year = 2018;
		Integer monthOfYear = 9;

		if(null!=fileMaster.getTransactionDate())
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(fileMaster.getTransactionDate());
			year = cal.get(Calendar.YEAR);
			monthOfYear = cal.get(Calendar.MONTH)-1;
		}

		List<DailyTransaction> dailyTransactions=dailyTransactionDao.getDailyTransactionsByMonth(fileMaster.getMeterMaster(),  monthOfYear,year);

		cmriDataModel.setDailyTransactions(dailyTransactions);

		return cmriDataModel;
	}



	//	public DailyProjectionModel getReport(String criteria,int month, int year) {
	//
	//		DailyProjectionModel dailyProjectionModel=new DailyProjectionModel();
	//		criteria = EAUtil.LOSS_REPORT_CRITERIA_G_T ;
	//		dailyProjectionModel.setCriteria("G-T");
	//		dailyProjectionModel.setDailySurveyTableProjections(dailyTransactionDao.getDailyTransactionsProjection(criteria, month, year));
	//		return dailyProjectionModel ;
	//	}






	public FileModel processZipFiles(Integer month, Integer year2)
	{
		FileFilter filter=new FileFilter();
		filter.setTransactionDateFrom(DateUtil.startDateTimeForDailySurveyRecs(month, year2));
		filter.setTransactionDateTo(DateUtil.endDateTimeForDailySurveyRecs(month, year2));
		filter.setProcessingStatus(EAUtil.FILE_ZIP_EXTRACTED);


		filter.setFileActionStatus(EAUtil.FILE_ACTION__APPROVED_AE);
		List<FileMaster> fileMasters=fileMasterDao.filterFiles(filter);
		//set file status to processing to stop user from processing same file again and again

		for (FileMaster fileMaster : fileMasters) {
			fileMaster.setProcessingStatus(EAUtil.FILE_UNDER_PROCESSING);
			fileMasterDao.save(fileMaster, getLoggedInUser());
			DataReaderThread dataReaderThread= context.getBean(DataReaderThread.class);
			dataReaderThread.saveFileDetails(fileMaster);
		}
		
		//process files in asynchronous mode
		for (FileMaster fileMaster : fileMasters) {
			try {
				processFilesAsync(fileMaster);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		FileModel fileModel=new FileModel();
		fileModel.setFilesUploadedDetail(fileMasters);
		return fileModel;
	}




	public CMRIFileDataModel viewRepoFileData(Integer id) {

		FileMaster fileMaster=fileMasterDao.findById(id);
		DataReaderThread dataReaderThread= context.getBean(DataReaderThread.class);

		CMRIFileDataModel cmriFileDataModel=new CMRIFileDataModel();
		cmriFileDataModel.setFileMaster(fileMaster);
		dataReaderThread.extractTransactionDataFromFile(cmriFileDataModel);

		return	cmriFileDataModel;
	}




	//	//added by Leevansha
	//	public CMRIFileDataModel processRepoFileForInstantRegisters(Integer id) {
	//		// TODO Auto-generated method stub
	//
	//		FileMaster fileMaster=fileMasterDao.findById(id);
	//		CMRIFileDataModel cmriDataModel=new CMRIFileDataModel();
	//
	//
	//		processInstantRegisterAsync(fileMaster);
	//
	//
	//		Integer year = 2018;
	//		Integer monthOfYear = 9;
	//
	//		if(null!=fileMaster.getTransactionDate())
	//		{
	//			Calendar cal = Calendar.getInstance();
	//			cal.setTime(fileMaster.getTransactionDate());
	//			year = cal.get(Calendar.YEAR);
	//			monthOfYear = cal.get(Calendar.MONTH)-1;
	//		}
	//
	//		InstantRegisters instantRegistersDetails =instantRegistersDao.findInstantRegistersByMeterAndMonth(fileMaster.getMeterMaster(),monthOfYear,year);
	//
	//		cmriDataModel.setInstantRegistersDetails(instantRegistersDetails);
	//
	//		return cmriDataModel;
	//
	//	}

	//	private void processInstantRegisterAsync(FileMaster fileDetails) {
	//		taskExecutor.execute(new Runnable() {
	//
	//			@Override
	//			public void run() {
	//
	//				DataReaderThread dataReaderThread= context.getBean(DataReaderThread.class);
	//
	//				CMRIFileDataModel cmriFileDataModel=new CMRIFileDataModel();
	//				cmriFileDataModel.setFileMaster(fileDetails);
	//
	//				dataReaderThread.extractTransactionDataFromFile(cmriFileDataModel);
	//				dataReaderThread.saveInstantRegisterData(cmriFileDataModel);
	//
	//			}
	//		} );
	//	}










}
