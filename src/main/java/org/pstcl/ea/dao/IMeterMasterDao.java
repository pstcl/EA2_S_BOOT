package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.model.EAFilter;


public interface IMeterMasterDao {
	MeterMaster findByMeterSrNo(String meterNo);
	void deleteById(String id);
	List<MeterMaster> findAllMeterMasters();
	List<MeterMaster> findAllMeterMasters(EAFilter filterModel);
	void save(MeterMaster meter, EAUser user);
	//MeterMaster findMeterForMonth(String locationid, int month, int year);
	List<MeterMaster> findMeterWithNoMapping();
	List<String> findDistinctMeterMake();
	List<String> findDistinctMeterType();
	List<String> findDistinctMeterCategory();
	void update(MeterMaster meter, EAUser object);
}
