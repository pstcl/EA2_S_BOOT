package org.pstcl.ea.dao;
import java.util.List;

import org.pstcl.ea.entity.BoundaryTypeMaster;
import org.pstcl.ea.model.EAFilter;

public interface IBoundaryTypeMasterDao {

	BoundaryTypeMaster findById(String id);
	List<BoundaryTypeMaster> findAllUsers();
	List<BoundaryTypeMaster> listSubDivisions(BoundaryTypeMaster user);
	List<BoundaryTypeMaster> listDivisions(BoundaryTypeMaster user);
	List<BoundaryTypeMaster> findUserHistory(EAFilter model);
	void save(BoundaryTypeMaster eaUser);
	BoundaryTypeMaster findBoundaryById(Integer id);
	
}