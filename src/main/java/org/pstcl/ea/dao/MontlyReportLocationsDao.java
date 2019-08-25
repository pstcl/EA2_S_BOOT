package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.mapping.ReportLocationsMonthMap;

public interface MontlyReportLocationsDao {

	ReportLocationsMonthMap findById(int id);

	void deleteById(int id);

	void save(ReportLocationsMonthMap txn, EAUser user);

	void update(ReportLocationsMonthMap txn, EAUser user);

	List<LocationMaster> findByMonthAndYear(int month, int year);

	void delete(int month, int year, List<LocationMaster> locationMaster);

	

	

	

	

}
