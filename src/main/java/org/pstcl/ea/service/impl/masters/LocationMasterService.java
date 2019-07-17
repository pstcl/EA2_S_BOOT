package org.pstcl.ea.service.impl.masters;

import java.util.List;
import java.util.Set;

import org.pstcl.ea.dao.ILocationMasterDao;
import org.pstcl.ea.dao.SubstationUtilityDao;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.entity.SubstationMaster;
import org.pstcl.ea.model.mapping.LocationEMFModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationMasterService {

	@Autowired
	ILocationMasterDao locationMasterDao;

	@Autowired
	SubstationUtilityDao substationUtilityDao;
	public List<LocationMaster> findNotMappedLocations() {
		// TODO Auto-generated method stub
		return locationMasterDao.findLocationsWithNoMapping();
	}

	public LocationMaster findLocationById(String locationid) {
		LocationMaster locationMaster=null;
		if(locationid!=null){
			locationMaster= locationMasterDao.findById(locationid);
		}
		return locationMaster;
	}

	/**
	 * check and save details of added location in adding new location form
	 * @param locationMaster
	 */
	public void saveLocationMasterDetails(LocationMaster locationMaster) {
		//ask conditions of same meter
		if(locationMasterDao.findById(locationMaster.getLocationId())==null) {
			locationMasterDao.save(locationMaster, null);
		}
		return;

	}

	/**
	 * check and save details of added location in adding new location form
	 * @param locationMaster
	 */
	public void updateLocationMasterDetails(LocationMaster locationMaster) {
		//ask conditions of same meter
		LocationMaster entity=locationMasterDao.findById(locationMaster.getLocationId());
		if(entity!=null) 
		{
			entity.setFeederMaster(locationMaster.getFeederMaster());
			entity.setBoundaryTypeMaster(locationMaster.getBoundaryTypeMaster());
			entity.setDeviceTypeMaster(locationMaster.getDeviceTypeMaster());
			entity.setUtiltiyName(locationMaster.getUtiltiyName());
			entity.setVoltageLevel(locationMaster.getVoltageLevel());
			entity.setLocation_status(locationMaster.getLocation_status());
			locationMasterDao.update(entity, null);

		}
		return;

	}

	public String validate(LocationMaster location) {
		String error = null;
		if(location.getLocationId()==null )
		{
			error="Locationid can't be empty";
		}
		if( location.getSubstationMaster()==null) {
			error="Please select a Substation";
		}
		else
		{
			initialiseLocationDivCircle(location);

		}

		return error;
	}	

	//With lazy initialization it will require modification
	private void initialiseLocationDivCircle(LocationMaster location)
	{
		if( location.getCircleMaster()==null || location.getDivisionMaster()==null ) {
			location.setDivisionMaster(location.getSubstationMaster().getDivisionMaster());
			location.setCircleMaster(location.getSubstationMaster().getDivisionMaster().getCircleMaster());
		}
	}

	public String isAlreadyExisting(LocationMaster locationMaster) {
		String error=null;
		if(locationMasterDao.findById(locationMaster.getLocationId())!=null)
		{
			error="Location with Id "+locationMaster.getLocationId()+" already exists. Do you want to update the same location?";
		}
		// TODO Auto-generated method stub
		return error;	}

	


}
