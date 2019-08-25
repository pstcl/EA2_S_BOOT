package org.pstcl.ea.converters;

import org.pstcl.ea.dao.MontlyReportLocationsDao;
import org.pstcl.ea.entity.mapping.ReportLocationsMonthMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MapReportLocationsConverter implements Converter<String,ReportLocationsMonthMap>{

	
	static final Logger logger;
	@Autowired 
	MontlyReportLocationsDao service; 
	
	static {
		logger = LoggerFactory.getLogger((Class) MapReportLocationsConverter.class);
	}
	
	public ReportLocationsMonthMap convert(final String code) {
		BoundaryTypeConverter.logger.info("Profile : {}", (Object) code);
		if(code.equalsIgnoreCase(""))
		{
			return null;
		}
		return this.service.findById(Integer.parseInt(code));
	}
}
