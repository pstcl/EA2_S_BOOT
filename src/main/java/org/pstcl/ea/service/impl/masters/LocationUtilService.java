
package org.pstcl.ea.service.impl.masters;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.pstcl.ea.dao.IAddReportLocationsDao;
import org.pstcl.ea.dao.IBoundaryTypeMasterDao;
import org.pstcl.ea.dao.IDeviceTypeMasterDao;
import org.pstcl.ea.dao.IFeederMasterDao;
import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.dao.ILocationMasterDao;
import org.pstcl.ea.dao.IMeterMasterDao;
import org.pstcl.ea.dao.MeterLocationMapDao;
import org.pstcl.ea.dao.SubstationUtilityDao;
import org.pstcl.ea.model.AddReportLocationModel;
import org.pstcl.ea.model.MastersForLocationEntry;
import org.pstcl.ea.model.entity.CircleMaster;
import org.pstcl.ea.model.entity.DivisionMaster;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.entity.MeterMaster;
import org.pstcl.ea.model.entity.SubstationMaster;
import org.pstcl.ea.model.mapping.LocationEMFModel;
import org.pstcl.ea.model.mapping.LocationMFMap;
import org.pstcl.ea.model.mapping.LocationMeterMappingModel;
import org.pstcl.ea.model.mapping.MeterLocationMap;
import org.pstcl.ea.model.mapping.ReportLocationsMonthMap;
import org.pstcl.model.FilterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("restService")
public class LocationUtilService {

	@Autowired
	IDeviceTypeMasterDao deviceTypeMasterDao;
	

	@Autowired
	MeterLocationMapDao mtrLocMapDao;

	@Autowired
	ILocationMasterDao locationMasterDao;
	
	@Autowired
	IFeederMasterDao feederMasterDao;
	
	@Autowired
	IBoundaryTypeMasterDao boundaryTypeMasterDao;
	


	

	@Autowired
	protected IMeterMasterDao meterDao;

	@Autowired
	SubstationUtilityDao ssUtilDao;



	

	public CircleMaster findCircleById(Integer code) {
		return this.ssUtilDao.findCircleByID(code);
	}

	public DivisionMaster findDivisionById(Integer code) {
		return this.ssUtilDao.findDivisionByID(code);
	}

	public SubstationMaster findSubstationById(Integer code) {
		return this.ssUtilDao.findSubstationByID(code);
	}

	public List<CircleMaster> getCircleList(FilterModel locationModel) {

		return this.ssUtilDao.listCircles(locationModel);
	}

	public List<DivisionMaster> getDivisionList(FilterModel locationModel) {
		return this.ssUtilDao.listDivisions(locationModel);
	}

	public List<SubstationMaster> getSubstationList(FilterModel locationModel) {
		return this.ssUtilDao.listSubstations(locationModel);
	}

	public List<LocationMaster> getLocationList(FilterModel locationModel) {
		return this.ssUtilDao.listLocations(locationModel);
	}

	
	/**
	 * Generate interlinked select options on selecting circle,substation ,division
	 * @param circle
	 * @param divCode
	 * @param substationCode
	 * @param Locationid
	 * @return
	 */
	public FilterModel getLocationModel(Integer circle, Integer divCode, Integer substationCode, String Locationid) {
		FilterModel locationModel = new FilterModel();
		if (circle != null) {
			locationModel.setSelectedCircle(findCircleById(circle));
		}
		if (divCode != null) {
			locationModel.setSelectedDivision(findDivisionById(divCode));
		}
		if (substationCode != null) {
			locationModel.setSelectedSubstation(findSubstationById(substationCode));
		}
		if (Locationid != null) {
			locationModel.setSelectedLocation(findLocationBYId(Locationid));
		}
		locationModel.setCircleList(getCircleList(locationModel));
		locationModel.setDivisionList(getDivisionList(locationModel));
		locationModel.setSubstationList(getSubstationList(locationModel));
		locationModel.setLocationList(getLocationList(locationModel));
		return locationModel;
	}

	public LocationMaster findLocationBYId(String locationid) {
		return this.ssUtilDao.findLocationByID(locationid);
	}

	public List<MeterLocationMap> findLocations(MeterMaster meterMaster) {
		return mtrLocMapDao.findLocations(meterMaster);
	}
	
	

	
	public List<MeterLocationMap> findLocations(MeterMaster meterMaster,LocationMaster locationMaster ) {
		return mtrLocMapDao.findMappingHistory(meterMaster, locationMaster);
	}


	public LocationMaster getMeterDeatils(String locationid) {
		return locationMasterDao.findById(locationid);
	}
	

	/**
	 * Service to generate options in adding - location form 
	 * @return
	 */
	public MastersForLocationEntry getLocationMasterListModel() {
		// TODO Auto-generated method stub
		MastersForLocationEntry list = new MastersForLocationEntry();
		list.setUtiltiyName(locationMasterDao.findDistinctUtiltiyName());
		list.setVoltageLevel(locationMasterDao.findDistinctVoltageLevel());
		list.setBoundaryTypeMaster(boundaryTypeMasterDao.findAllUsers());
		list.setFeederMaster(feederMasterDao.findAllFeeders());
		list.setDeviceTypeMaster(deviceTypeMasterDao.findAllDeviceTypes());
		return list;
	}
	





}
