package org.pstcl.ea.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class Transaction {
	
	@Column
	protected Integer dayOfMonth;
	@Column
	protected Integer monthOfYear;
	
	@Column
	protected Integer year;
	

	public Transaction() {
		super();
	}
	
	@JsonIgnore
	@ManyToOne 
	@JoinColumn(name = "METER_ID")
	protected MeterMaster meter;
	


	@Column
	protected Date transactionDate;
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
		if (null != this.transactionDate) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(transactionDate);
			this.year = cal.get(Calendar.YEAR);
			this.monthOfYear = cal.get(Calendar.MONTH);
			this.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

		}
	}
	
	public Integer getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public Integer getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(Integer monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public MeterMaster getMeter() {
		return meter;
	}

	public void setMeter(MeterMaster meter) {
		this.meter = meter;
	}

	public LocationMaster getLocation() {
		return location;
	}

	public void setLocation(LocationMaster location) {
		this.location = location;
	}

	public MeterLocationMap getMeterLocationMap() {
		return meterLocationMap;
	}

	public void setMeterLocationMap(MeterLocationMap meterLocationMap) {
		this.meterLocationMap = meterLocationMap;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "LOC_ID")
	protected LocationMaster location;
	
	
	@JsonIgnore
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name = "METER_LOC_MAP_ID")
	protected MeterLocationMap meterLocationMap;



}