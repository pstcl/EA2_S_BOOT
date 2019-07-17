package org.pstcl.ea.model.mapping;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.pstcl.ea.model.entity.LocationMaster;

@Entity
@Table(name="month_location_map")
@NamedQuery(name="ReportLocationsMonthMap.findAll", query="SELECT c FROM ReportLocationsMonthMap c")
public class ReportLocationsMonthMap {

	@Id
	@GeneratedValue
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column
	private int month;

	@Column
	private int year;

	@ManyToOne
	@JoinColumn(name = "LOC_ID")
	private LocationMaster locationMaster1;






	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public LocationMaster getLocationMaster1() {
		return locationMaster1;
	}

	public void setLocationMaster1(LocationMaster locationMaster1) {
		this.locationMaster1 = locationMaster1;
	}

	public ReportLocationsMonthMap() {
		super();
	}

	public ReportLocationsMonthMap(int month, int year, LocationMaster locations) {
		super();
		System.out.println("Inside Constructor "+locations.getLocationId());
		this.month = month;
		this.year = year;
		this.locationMaster1 = locations;
	}




}
