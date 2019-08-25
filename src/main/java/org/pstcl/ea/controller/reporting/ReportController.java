package org.pstcl.ea.controller.reporting;

import org.pstcl.ea.service.impl.lossreport.IlossReportService;
import org.pstcl.ea.service.impl.lossreport.ReportService;
import org.pstcl.ea.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ReportController {
	

	@Autowired
	private ReportService lossReportService;
	
	

	
		
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getReportTransactions-{locationId}", method = RequestMethod.GET)
	public String getReportTransactions(@PathVariable String locationId,@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year,ModelMap modelMap) {
		modelMap.addAttribute("locationSurveyDataModel", lossReportService.getReportTransactions(locationId,month,year));
		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(month, year) );

		modelMap.addAttribute("month",month);
		modelMap.addAttribute("year",year);


		return "locationImportExport";
	}


	


	



}
