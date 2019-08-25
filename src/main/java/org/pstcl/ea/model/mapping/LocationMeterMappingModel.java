package org.pstcl.ea.model.mapping;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransactionForRemovedMeters;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransactionMappedSuper;
import org.pstcl.ea.entity.meterTxnEntity.TamperLogTransaction;
import org.springframework.format.annotation.DateTimeFormat;


public class LocationMeterMappingModel {

	private MeterLocationMap oldMeterLocationMap ;


	private MeterMaster meterMaster;

	private LocationMaster location;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	private String error;

	private Boolean mappingSuccesful;
	private List<DailyTransactionMappedSuper> dailyTransactions;
	private List<TamperLogTransaction> tamperLogTransactions;


	public LocationMeterMappingModel() {
		super();
		// TODO Auto-generated constructor stub
	}




















	public Boolean getMappingSuccesful() {
		return mappingSuccesful;
	}




















	public void setMappingSuccesful(Boolean mappingSuccesful) {
		this.mappingSuccesful = mappingSuccesful;
	}




















	public List<DailyTransactionMappedSuper> getDailyTransactions() {
		return dailyTransactions;
	}




















	public void setDailyTransactions(List<DailyTransactionMappedSuper> dailyTransactions) {
		this.dailyTransactions = dailyTransactions;
	}




















	public List<TamperLogTransaction> getTamperLogTransactions() {
		return tamperLogTransactions;
	}




















	public void setTamperLogTransactions(List<TamperLogTransaction> tamperLogTransactions) {
		this.tamperLogTransactions = tamperLogTransactions;
	}




















	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public MeterMaster getMeterMaster() {
		return meterMaster;
	}
	public void setMeterMaster(MeterMaster meterMaster) {
		this.meterMaster = meterMaster;
	}
	public MeterLocationMap getOldMeterLocationMap() {
		return oldMeterLocationMap;
	}

	public void setOldMeterLocationMap(MeterLocationMap oldMeterLocationMapId) {
		this.oldMeterLocationMap = oldMeterLocationMapId;
	}
	public void setOldValues(MeterLocationMap meterDetails) {
		// TODO Auto-generated method stub
		this.oldMeterLocationMap=meterDetails;
		this.meterMaster=meterDetails.getMeterMaster();
	}
	public LocationMaster getLocation() {
		return location;
	}

	public void setLocation(LocationMaster location) {
		this.location = location;
	}







	public String getError() {
		return error;
	}







	public void setError(String error) {
		this.error = error;
	}









}
