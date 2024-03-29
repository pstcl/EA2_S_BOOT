package org.pstcl.ea.entity.mapping;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="map_meter_location")
@NamedQuery(name="MeterLocationMap.findAll", query="SELECT m FROM MeterLocationMap m")
public class MeterLocationMap {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public LocationMaster getLocationMaster() {
		return locationMaster;
	}
	public void setLocationMaster(LocationMaster locationMaster) {
		this.locationMaster = locationMaster;
	}
	public MeterMaster getMeterMaster() {
		return meterMaster;
	}
	public void setMeterMaster(MeterMaster meterMaster) {
		this.meterMaster = meterMaster;
	}
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	
	@JsonIgnore
	@ManyToOne 
	@JoinColumn(name = "LOC_ID")
	private LocationMaster locationMaster;

	//private LocationMaster locationMaster;

	@JsonIgnore
	@ManyToOne 
	@JoinColumn(name = "METER_ID")
	private MeterMaster meterMaster;
}
