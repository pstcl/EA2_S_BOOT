package org.pstcl.ea.model.reporting;

import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;

public class FileParameterModel {

	private MeterMaster meter;
	private LocationMaster location;
	private Integer reportMonth;
	private Integer reportYear;
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
	public Integer getReportMonth() {
		return reportMonth;
	}
	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}
	public Integer getReportYear() {
		return reportYear;
	}
	public void setReportYear(Integer reportYear) {
		this.reportYear = reportYear;
	}
	
}
