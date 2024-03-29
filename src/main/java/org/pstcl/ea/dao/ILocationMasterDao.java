package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.model.EAFilter;


public interface ILocationMasterDao {
	void deleteById(String id);
	List<LocationMaster> findAllLocationMasters();
	List<LocationMaster> findAllLocationMasters(EAFilter filterModel);
	void save(LocationMaster meter, EAUser user);
	LocationMaster findById(String id);
	
	List<String> findDistinctUtiltiyName();
	List<String> findDistinctVoltageLevel();
	List<LocationMaster> findLocationsWithNoMapping();
	void update(LocationMaster meter, EAUser user);
}
