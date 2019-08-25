package org.pstcl.ea.dao;

import java.util.Date;
import java.util.List;

import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.FileMaster;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.model.FileFilter;


public interface IFileMasterDao  {
	FileMaster findById(int id);
	//FileMaster findByLocationDateCombo(LocationMaster location, Date txnDate);
	void deleteById(String id);
	void save(FileMaster meter, EAUser user);
	void update(FileMaster txn, EAUser user);
	
	//List<FileMaster> findFileDetailsByLocation(LocationMaster location);
	List<FileMaster> findByMonth(Integer monthOfYear, Integer year);
	List<FileMaster> filterFiles(FileFilter filter);
	List<FileMaster> findCorruptFiles(FileFilter filter);
	FileMaster findByMeterFileDateCombo(MeterMaster meter, Date txnDate);
	
	
}
