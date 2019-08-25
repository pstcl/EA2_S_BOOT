package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.entity.CircleMaster;
import org.pstcl.ea.entity.DivisionMaster;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.SubstationMaster;
import org.pstcl.ea.model.EAModel;
import org.pstcl.model.FilterModel;

public interface SubstationUtilityDao {
	
	List<CircleMaster> listCircles(EAModel eaFilter);
	List<DivisionMaster> listDivisions(EAModel eaFilter);
	List<SubstationMaster> listSubstations(EAModel eaFilter);
	
	
	List<CircleMaster> listCircles(FilterModel eaFilter);
	List<DivisionMaster> listDivisions(FilterModel eaFilter);
	List<SubstationMaster> listSubstations(FilterModel eaFilter);
	List<LocationMaster> listLocations(FilterModel eaFilter);
	
	CircleMaster findCircleByID(Integer code);
	DivisionMaster findDivisionByID(Integer code);
	SubstationMaster findSubstationByID(Integer code);
	LocationMaster findLocationByID(String code);
	void update(SubstationMaster txn);
	
	

}
