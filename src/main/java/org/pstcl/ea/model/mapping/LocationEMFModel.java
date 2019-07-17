package org.pstcl.ea.model.mapping;

import java.math.BigDecimal;
import java.util.Date;

import org.pstcl.ea.model.entity.LocationMaster;
import org.springframework.format.annotation.DateTimeFormat;

public class LocationEMFModel {

	private LocationMaster locationMaster;
	private LocationMFMap oldLocationEmf;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDate;
	public Date getEffectiveDate() {
		return effectiveDate;
	}


	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}


	private BigDecimal externalMF;
	private Integer netWHSign;
	
	public LocationMFMap getOldLocationEmf() {
		return oldLocationEmf;
	}


	public void setOldLocationEmf(LocationMFMap oldLocationEmf) {
		this.oldLocationEmf = oldLocationEmf;
	}



	public BigDecimal getExternalMF() {
		return externalMF;
	}


	public void setExternalMF(BigDecimal bigDecimal) {
		this.externalMF = bigDecimal;
	}


	public Integer getNetWHSign() {
		return netWHSign;
	}


	public void setNetWHSign(Integer netWHSign) {
		this.netWHSign = netWHSign;
	}


	public LocationMaster getLocationMaster() {
		return locationMaster;
	}


	public void setLocationMaster(LocationMaster locationMaster) {
		this.locationMaster = locationMaster;
	}


	

}
