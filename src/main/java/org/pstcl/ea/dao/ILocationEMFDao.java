package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;


public interface ILocationEMFDao {
	
	//ss

	LocationMFMap findById(int id);



	void deleteById(Integer i);

	void save(LocationMFMap txn, EAUser user);

	void update(LocationMFMap txn, EAUser user);

	void save(List<LocationMFMap> locationEMFs, EAUser loggedInUser);




	List<LocationMFMap> findLocationEmfByDate(String locationId, Date current);



	List<LocationMFMap> findLocationEmfByLocAndDate(List<MeterLocationMap> mtrLocMapList, Date startDateOfMonth);



	LocationMFMap findLocationRecentEmf(String locationId);



	//List<LocationMFMap> find(LocationMFMap newEmf);



	LocationMFMap findSimiliarEMF(LocationMFMap newEmf);



	




}
