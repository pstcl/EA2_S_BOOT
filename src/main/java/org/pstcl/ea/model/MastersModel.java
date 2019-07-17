package org.pstcl.ea.model;

import java.util.List;

import org.pstcl.ea.model.entity.CircleMaster;
import org.pstcl.ea.model.entity.DivisionMaster;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.entity.SubstationMaster;

public class MastersModel {

	private List <CircleMaster> circleMaster;
	private List <DivisionMaster> divisionMaster;
	private List <SubstationMaster> substationMaster;
	private List <LocationMaster> locationMaster;
	public List<CircleMaster> getCircleMaster() {
		return circleMaster;
	}
	public void setCircleMaster(List<CircleMaster> circleMaster) {
		this.circleMaster = circleMaster;
	}
	public List<DivisionMaster> getDivisionMaster() {
		return divisionMaster;
	}
	public void setDivisionMaster(List<DivisionMaster> divisionMaster) {
		this.divisionMaster = divisionMaster;
	}
	public List<SubstationMaster> getSubstationMaster() {
		return substationMaster;
	}
	public void setSubstationMaster(List<SubstationMaster> substationMaster) {
		this.substationMaster = substationMaster;
	}
	public List<LocationMaster> getLocationMaster() {
		return locationMaster;
	}
	public void setLocationMaster(List<LocationMaster> locationMaster) {
		this.locationMaster = locationMaster;
	}
}
