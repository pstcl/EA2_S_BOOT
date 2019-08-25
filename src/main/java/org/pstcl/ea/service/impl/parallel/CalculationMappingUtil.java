package org.pstcl.ea.service.impl.parallel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.pstcl.ea.entity.FileMaster;
import org.pstcl.ea.entity.Transaction;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CalculationMappingUtil {

	public CalculationMappingUtil() {
		super();
	}

	public void setDailyTxnLocationAndMF(List<MeterLocationMap> mtrLocMapList, List<LocationMFMap> locationEMFList,  DailyTransaction dailyTransaction) {
		setDailyTxnLocation(mtrLocMapList, dailyTransaction);
		setDailyTxnLocationMF( locationEMFList, dailyTransaction);
	}
	public void setDailyTxnLocation(List<MeterLocationMap> mtrLocMapList,   DailyTransaction dailyTransaction) {
		dailyTransaction.setLocation(null);
		dailyTransaction.setMeterLocationMap(null);

		if (null!= mtrLocMapList)
		{
			if( mtrLocMapList.size()==1)
			{
				dailyTransaction.setLocation(mtrLocMapList.get(0).getLocationMaster());
				dailyTransaction.setMeterLocationMap(mtrLocMapList.get(0));
			}
			else if(mtrLocMapList.size()>1)
			{
				for (MeterLocationMap locationMap : mtrLocMapList) {
					if(null!=locationMap.getEndDate()&&(locationMap.getEndDate().compareTo(dailyTransaction.getTransactionDate())>=0)&&(locationMap.getStartDate().compareTo(dailyTransaction.getTransactionDate())<=0))
					{
						dailyTransaction.setLocation(locationMap.getLocationMaster());
						dailyTransaction.setMeterLocationMap(locationMap);

					}
					else if(null==locationMap.getEndDate()&&(locationMap.getStartDate().compareTo(dailyTransaction.getTransactionDate())<=0))
					{
						dailyTransaction.setLocation(locationMap.getLocationMaster());
						dailyTransaction.setMeterLocationMap(locationMap);

					}

				}

			}

		}
	}


	public void setDailyTxnLocationMF( List<LocationMFMap> locationEMFList,  DailyTransaction dailyTransaction) {
	
		dailyTransaction.setExternalMFMap(null);
		dailyTransaction.setExternalMF(null);
		dailyTransaction.setNetWHSign(null);
	
		if(null!=dailyTransaction.getLocation())
		{

			if (null!= locationEMFList)
			{
				if( locationEMFList.size()==1)
				{
					LocationMFMap locationMap =locationEMFList.get(0);
					dailyTransaction.setExternalMFMap(locationMap);
					dailyTransaction.setExternalMF(locationMap.getExternalMF());
					dailyTransaction.setNetWHSign(locationMap.getNetWHSign());
				}
				else if(locationEMFList.size()>=1)
				{
					for (LocationMFMap locationMap : locationEMFList) {
						if (dailyTransaction.getLocation().getLocationId().equalsIgnoreCase(locationMap.getLocationMaster().getLocationId()))
						{
							if(null!=locationMap.getEndDate()&&(locationMap.getEndDate().compareTo(dailyTransaction.getTransactionDate())>=0)&&(locationMap.getStartDate().compareTo(dailyTransaction.getTransactionDate())<=0))
							{
								dailyTransaction.setExternalMFMap(locationMap);
								dailyTransaction.setExternalMF(locationMap.getExternalMF());
								dailyTransaction.setNetWHSign(locationMap.getNetWHSign());

							}
							else if(null==locationMap.getEndDate()&&(locationMap.getStartDate().compareTo(dailyTransaction.getTransactionDate())<=0))
							{
								dailyTransaction.setExternalMFMap(locationMap);
								dailyTransaction.setExternalMF(locationMap.getExternalMF());
								dailyTransaction.setNetWHSign(locationMap.getNetWHSign());

							}
						}

					}

				}

			}

		}
		


	}

	public DailyTransaction calculateImportExport(DailyTransaction dailyTransaction) {
		
		dailyTransaction.setExportBoundaryPtMWH(null);
		dailyTransaction.setImportBoundaryPtMWH(null);
		dailyTransaction.setBoundaryPtImportExportDifferenceMWH(null);
		dailyTransaction.setNetMWH(null);
		
		BigDecimal emf=dailyTransaction.getExternalMF();

		if (null != dailyTransaction.getExportWHF() && null != dailyTransaction.getImportWHF()
				&& null != dailyTransaction.getLocation()) {
			if (null != dailyTransaction.getNetWHSign()
					&& null != emf) {

				BigDecimal exportMeterReading = dailyTransaction.getExportWHF()
						.multiply(emf).divide(new BigDecimal(1000 * 1000)).setScale(EAUtil.DECIMAL_SCALE_BOUNDARY_PT_IMPORT_EXPORT, EAUtil.DECIMAL_ROUNDING_MODE);
				BigDecimal importMeterReading = dailyTransaction.getImportWHF()
						.multiply(emf).divide(new BigDecimal(1000 * 1000)).setScale(EAUtil.DECIMAL_SCALE_BOUNDARY_PT_IMPORT_EXPORT, EAUtil.DECIMAL_ROUNDING_MODE);
				// Export and Import are interchanged for G-T and I-T points in normal case i.e.
				// when Net Wh Sign is -1
				// Export and Import are interchanged for all other in normal case i.e. when Net
				// Wh Sign is 1
				// For G-T & I-T points the getInvertExportImportOnNegativeSign() returns TRUE
				// else it returns false
				// Else import goes to import and export goes to export
				if (((dailyTransaction.getNetWHSign().equals(-1)) && (dailyTransaction.getLocation()
						.getBoundaryTypeMaster().getInvertExportImportOnNegativeSign()))
						|| ((dailyTransaction.getNetWHSign().equals(1)) && (!dailyTransaction
								.getLocation().getBoundaryTypeMaster().getInvertExportImportOnNegativeSign()))) {

					dailyTransaction.setExportBoundaryPtMWH(importMeterReading);
					dailyTransaction.setImportBoundaryPtMWH(exportMeterReading);
					dailyTransaction
					.setBoundaryPtImportExportDifferenceMWH(exportMeterReading.subtract(importMeterReading)
							.multiply(new BigDecimal(dailyTransaction.getNetWHSign())));
					dailyTransaction
					.setNetMWH((dailyTransaction.getExportWHF().subtract(dailyTransaction.getImportWHF()))
							.multiply(emf)
							.multiply(new BigDecimal(dailyTransaction.getNetWHSign()))
							.divide(new BigDecimal(1000 * 1000)).setScale(EAUtil.DECIMAL_SCALE_BOUNDARY_PT_IMPORT_EXPORT, EAUtil.DECIMAL_ROUNDING_MODE));
				} else {

					dailyTransaction.setExportBoundaryPtMWH(exportMeterReading);
					dailyTransaction.setImportBoundaryPtMWH(importMeterReading);
					dailyTransaction
					.setBoundaryPtImportExportDifferenceMWH(exportMeterReading.subtract(importMeterReading)
							.multiply(new BigDecimal(dailyTransaction.getNetWHSign())));
					dailyTransaction
					.setNetMWH((dailyTransaction.getExportWHF().subtract(dailyTransaction.getImportWHF()))
							.multiply(emf)
							.multiply(new BigDecimal(dailyTransaction.getNetWHSign()))
							.divide(new BigDecimal(1000 * 1000)).setScale(EAUtil.DECIMAL_SCALE_BOUNDARY_PT_IMPORT_EXPORT, EAUtil.DECIMAL_ROUNDING_MODE));
				}

			}

		}
		return dailyTransaction;
	}



	public void setTransactionLocationFromMeter(List<MeterLocationMap> mtrLocMapList,   Transaction eaTransaction) {

		eaTransaction.setLocation(null);
		eaTransaction.setMeterLocationMap(null);

		
		if (null!= mtrLocMapList)
		{
			if( mtrLocMapList.size()==1)
			{
				eaTransaction.setLocation(mtrLocMapList.get(0).getLocationMaster());
				eaTransaction.setMeterLocationMap(mtrLocMapList.get(0));
			}
			else if(mtrLocMapList.size()>1)
			{
				for (MeterLocationMap locationMap : mtrLocMapList) {
					if(null!=locationMap.getEndDate()&&(locationMap.getEndDate().compareTo(eaTransaction.getTransactionDate())>=0)&&(locationMap.getStartDate().compareTo(eaTransaction.getTransactionDate())<=0))
					{
						eaTransaction.setLocation(locationMap.getLocationMaster());
						eaTransaction.setMeterLocationMap(locationMap);

					}
					else if(null==locationMap.getEndDate()&&(locationMap.getStartDate().compareTo(eaTransaction.getTransactionDate())<=0))
					{
						eaTransaction.setLocation(locationMap.getLocationMaster());
						eaTransaction.setMeterLocationMap(locationMap);

					}

				}

			}

		}
	}
	public void setFileLocationFromMeter(List<MeterLocationMap> mtrLocMapList,   FileMaster fileMaster) {

		fileMaster.setLocation(null);
		
		if (null!= mtrLocMapList)
		{
			if( mtrLocMapList.size()==1)
			{
				fileMaster.setLocation(mtrLocMapList.get(0).getLocationMaster());
			}
			else if(mtrLocMapList.size()>1)
			{
				for (MeterLocationMap locationMap : mtrLocMapList) {
					if(null!=locationMap.getEndDate()&&(locationMap.getEndDate().compareTo(fileMaster.getTransactionDate())>=0)&&(locationMap.getStartDate().compareTo(fileMaster.getTransactionDate())<=0))
					{
						fileMaster.setLocation(locationMap.getLocationMaster());
					}
					else if(null==locationMap.getEndDate()&&(locationMap.getStartDate().compareTo(fileMaster.getTransactionDate())<=0))
					{
						fileMaster.setLocation(locationMap.getLocationMaster());
					}

				}

			}

		}
	}

}