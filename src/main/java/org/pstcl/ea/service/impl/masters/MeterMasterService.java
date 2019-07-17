package org.pstcl.ea.service.impl.masters;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.pstcl.ea.dao.IMeterMasterDao;
import org.pstcl.ea.model.MastersForLocationEntry;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.entity.MeterMaster;
import org.pstcl.ea.model.mapping.LocationMeterMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Service
public class MeterMasterService {

	@Autowired
	IMeterMasterDao meterMasterDao;

	public List<MeterMaster> findNotMappedMeters() {
		// TODO Auto-generated method stub
		return meterMasterDao.findMeterWithNoMapping();
	}

	public MeterMaster findMeterbyId(String meterId)
	{
		MeterMaster master=null;
		if(null!=meterId)
		{
			master=meterMasterDao.findByMeterSrNo(meterId);
		}
		return 	master;
	}

//	public List<MeterMaster> findMetersLike(String meterId) {
//		MeterMaster master=null;
//		if(null!=meterId)
//		{
//			master=meterMasterDao.findByMeterSrNo(meterId);
//		}
//		return 	master;
//	}

	/**
	 * Check and save details of added new meter
	 * @param meter
	 */
	public void saveMeterDetails(MeterMaster meter) {
		//ask conditions of same meter
		if(meterMasterDao.findByMeterSrNo(meter.getMeterSrNo())==null)
		{
			meterMasterDao.save(meter, null);
		}
		return;
	}
	
	/**
	 * Check and save details of added new meter
	 * @param meter
	 */
	public void updateMeterDetails(MeterMaster meter) {
		//ask conditions of same meter
		if(meterMasterDao.findByMeterSrNo(meter.getMeterSrNo())!=null)
		{
			meterMasterDao.update(meter,null);

		}
		return;
	}

	/**
	 * To generate options with fixed values in adding new meter form 
	 * @return
	 */
	public MastersForLocationEntry meterAttrMasterListModel() {
		// TODO Auto-generated method stub
		MastersForLocationEntry list = new MastersForLocationEntry();
		list.setMeterCategory(meterMasterDao.findDistinctMeterCategory());
		list.setMeterMake(meterMasterDao.findDistinctMeterMake());
		list.setMeterType(meterMasterDao.findDistinctMeterType());
		return list;
	}


	public String validate(MeterMaster master) {

		String error=null;
		if( master.getCTAccuracy()==null || master.getGridLossFactor()==null || master.getInstalledDate()==null || master.getInternalCTRatio()==null || master.getInternalMF()==null || master.getInternalPTRatio()==null || master.getPTAccuracy()==null || master.getMeterCategory()==null || master.getMeterMake()==null || master.getMeterSrNo()==null || master.getMeterType()==null)
		{
			error="One of Values is null";

		}
		else if(master.getInstalledDate().after(new Date()))
		{
			error= "Installed Date is set Wrong";

		}

		return error;
	}

	public String isAlreadyExisting(MeterMaster meter) {
		String error=null;
		if(meterMasterDao.findByMeterSrNo(meter.getMeterSrNo())!=null)
		{
			error="Meter with Serial no"+meter.getMeterSrNo()+"already exists. Do you want to update the same meter?";
		}
		// TODO Auto-generated method stub
		return error;
	}


}

