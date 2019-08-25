package org.pstcl.ea.dao;

import java.util.List;

import org.pstcl.ea.entity.FeederMaster;

public interface IFeederMasterDao {

	FeederMaster findById(int id);

	List<FeederMaster> findAllFeeders();

}
