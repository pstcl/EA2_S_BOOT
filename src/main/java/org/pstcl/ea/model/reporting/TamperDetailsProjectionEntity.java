package org.pstcl.ea.model.reporting;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.pstcl.ea.entity.LocationMaster;

public class TamperDetailsProjectionEntity {
	
	
	
	private Long count;
	
	@ManyToOne
	@JoinColumn(name = "LOC_ID")
	private LocationMaster location;
	
	
	public  TamperDetailsProjectionEntity() {
		
	}
	
//	public  TamperDetailsProjectionEntity(int count,LocationMaster location) {
//		this.location=location;
//		this.count=count;
//	}

	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public LocationMaster getLocationMaster() {
		return location;
	}
	public void setLocationMaster(LocationMaster locationMaster) {
		this.location = locationMaster;
	}

}
