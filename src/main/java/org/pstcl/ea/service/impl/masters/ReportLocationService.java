package org.pstcl.ea.service.impl.masters;

import java.util.List;

import org.pstcl.ea.dao.MontlyReportLocationsDao;
import org.pstcl.ea.dao.ILocationMasterDao;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.mapping.ReportLocationsMonthMap;
import org.pstcl.ea.model.MapLossReportLocationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportLocationService {
	
	@Autowired 
	MontlyReportLocationsDao addReportLocationsDao; 
	
	@Autowired
	ILocationMasterDao locationMasterDao;
	/**
	 * Service to generate options of added and can be added locations for selected month
	 * @param addReportLocations
	 * @return
	 */
	public List<LocationMaster> selectReportLocations(MapLossReportLocationModel addReportLocations) {
		 
		int month = addReportLocations.getMonth();
		int year = addReportLocations.getYear();
		List <LocationMaster> pendingLocation = locationMasterDao.findAllLocationMasters();
		List<LocationMaster> list =addReportLocationsDao.findByMonthAndYear(month,year);
		if(list!=null)
		{
			addReportLocations.setLocations(list);
			pendingLocation.removeAll(list);
		}
		
		//addReportLocations.setAddingLocations(pendingLocation);
		return pendingLocation;
	}
/**
 * Save details of changes made for report month - location form
 * @param addReportLocations
 * @return
 */
	public MapLossReportLocationModel saveReportLocations(MapLossReportLocationModel addReportLocations) {
		int month = addReportLocations.getMonth();
		int year = addReportLocations.getYear();
		List<LocationMaster> removelist =addReportLocations.getLocations();
		List<LocationMaster> addList = addReportLocations.getAddingLocations();
		
		if(removelist!=null)
			addReportLocationsDao.delete(month, year, removelist);
		if(addList!=null)
			for(LocationMaster loc:addList) {
				addReportLocationsDao.save(new ReportLocationsMonthMap(month,year,loc), null);
		
			}
		return addReportLocations;
	}

}
