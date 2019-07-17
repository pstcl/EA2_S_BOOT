package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.model.entity.EAUser;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.mapping.ReportLocationsMonthMap;

public interface IAddReportLocationsDao {

	ReportLocationsMonthMap findById(int id);

	void deleteById(int id);

	void save(ReportLocationsMonthMap txn, EAUser user);

	void update(ReportLocationsMonthMap txn, EAUser user);

	List<LocationMaster> findByMonthAndYear(int month, int year);

	void delete(int month, int year, List<LocationMaster> locationMaster);

	

	

	

	

}
