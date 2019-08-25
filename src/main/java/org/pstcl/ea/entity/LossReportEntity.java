package org.pstcl.ea.entity;

import java.math.BigDecimal;
import java.util.List;

import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;

public class LossReportEntity implements Comparable<LossReportEntity> {

	private List<MeterLocationMap> meterLocationMapList;
	private List<LocationMFMap> locationMFMapList;

	private String meterSrNo;
	private String  meterCategory;
	private String  externalMF;
	private String netWHSign;

	private Long daysInMonthCount;

	private Long exportWHFCount;

	private BigDecimal exportWHFSum;

	private Long importWHFCount;

	private BigDecimal importWHFSum;

	private LocationMaster location;

	private Integer lossReportOrder;

	private Integer monthOfYear;

	private BigDecimal boundaryPtImportExportDifferenceMWH;



	private BigDecimal exportBoundaryPtMWH;




	private BigDecimal importBoundaryPtMWH;






	private BigDecimal netMWH;


	private Integer txnId;


	private Integer year;


	@Override
	public int compareTo(LossReportEntity o) {
		int result = 0;

		if (null != location && null != o.location) {
			result = location.getLossReportOrder() - o.location.getLossReportOrder();
		} else {
			result = 1;
		}
		return result;
	}



	public BigDecimal getBoundaryPtImportExportDifferenceMWH() {
		return boundaryPtImportExportDifferenceMWH;
	}




	public Long getDaysInMonthCount() {
		return daysInMonthCount;
	}


	public BigDecimal getExportBoundaryPtMWH() {
		return exportBoundaryPtMWH;
	}


	public Long getExportWHFCount() {
		return exportWHFCount;
	}



	public BigDecimal getExportWHFSum() {
		return exportWHFSum;
	}




	public String getExternalMF() {
		return externalMF;
	}

	public BigDecimal getImportBoundaryPtMWH() {
		return importBoundaryPtMWH;
	}

	public Long getImportWHFCount() {
		return importWHFCount;
	}

	public BigDecimal getImportWHFSum() {
		return importWHFSum;
	}

	public LocationMaster getLocation() {
		return location;
	}

	public List<LocationMFMap> getLocationMFMapList() {
		return locationMFMapList;
	}

	public Integer getLossReportOrder() {
		return lossReportOrder;
	}

	public String getMeterCategory() {
		return meterCategory;
	}

	public List<MeterLocationMap> getMeterLocationMapList() {
		return meterLocationMapList;
	}

	public String getMeterSrNo() {
		return meterSrNo;
	}

	public Integer getMonthOfYear() {
		return monthOfYear;
	}

	public BigDecimal getNetMWH() {
		return netMWH;
	}

	public String getNetWHSign() {
		return netWHSign;
	}



	public int getTxnId() {
		return txnId;
	}

	public Integer getYear() {
		return year;
	}

	public void setBoundaryPtImportExportDifferenceMWH(BigDecimal boundaryPtImportExportDifferenceMWH) {
		this.boundaryPtImportExportDifferenceMWH = boundaryPtImportExportDifferenceMWH;
	}

	public void setDaysInMonthCount(Long daysInMonthCount) {
		this.daysInMonthCount = daysInMonthCount;
	}

	public void setExportBoundaryPtMWH(BigDecimal exportBoundaryPtMWH) {
		this.exportBoundaryPtMWH = exportBoundaryPtMWH;
	}

	public void setExportWHFCount(Long exportWHFCount) {
		this.exportWHFCount = exportWHFCount;
	}

	public void setExportWHFSum(BigDecimal exportWHFSum) {
		this.exportWHFSum = exportWHFSum;
	}

	public void setImportBoundaryPtMWH(BigDecimal importBoundaryPtMWH) {
		this.importBoundaryPtMWH = importBoundaryPtMWH;
	}

	public void setImportWHFCount(Long importWHFCount) {
		this.importWHFCount = importWHFCount;
	}

	public void setImportWHFSum(BigDecimal importWHFSum) {
		this.importWHFSum = importWHFSum;
	}

	public void setLocation(LocationMaster location) {
		this.location = location;
	}

	public void setLocationMFMapList(List<LocationMFMap> locationMFMapList) {
		this.locationMFMapList = locationMFMapList;
		for (LocationMFMap locationMFMap : locationMFMapList) {
			if(null==externalMF)
			{
				externalMF=	locationMFMap.getExternalMF().toString();

			}
			else
			{
				externalMF+=	","+locationMFMap.getExternalMF();
			}
			if(null==netWHSign)
			{
				netWHSign=locationMFMap.getNetWHSign().toString();

			}
			else {
				netWHSign+=","+locationMFMap.getNetWHSign();
			}
		}
	}

	public void setLossReportOrder(Integer lossReportOrder) {
		this.lossReportOrder = lossReportOrder;
	}

	public void setMeterLocationMapList(List<MeterLocationMap> meterLocationMapList) {
		this.meterLocationMapList = meterLocationMapList;
		for (MeterLocationMap meterLocationMap : meterLocationMapList) {

			if(null==meterSrNo)
			{
				meterSrNo=meterLocationMap.getMeterMaster().getMeterSrNo();
			}
			else
			{
				meterSrNo+=","+meterLocationMap.getMeterMaster().getMeterSrNo();

			}
			if(null==meterCategory)
			{
				meterCategory=meterLocationMap.getMeterMaster().getMeterCategory();
			}
			else
			{
				meterCategory+=","+meterLocationMap.getMeterMaster().getMeterCategory();
			}
		}
	}



	public void setMonthOfYear(Integer monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public void setNetMWH(BigDecimal netMWH) {
		this.netMWH = netMWH;
	}

	public void setTxnId(int txnId) {
		this.txnId = txnId;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
