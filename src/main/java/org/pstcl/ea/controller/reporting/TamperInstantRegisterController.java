package org.pstcl.ea.controller.reporting;

import java.util.Date;

import javax.validation.Valid;

import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.service.impl.lossreport.IlossReportService;
import org.pstcl.ea.service.impl.lossreport.ReportService;
import org.pstcl.ea.service.impl.lossreport.TamperInstantRegisterService;
import org.pstcl.ea.service.impl.parallel.DataService;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class TamperInstantRegisterController {

	@Autowired
	private TamperInstantRegisterService tamperService;

	@Autowired
	private ReportService lossReportService1;


	// added
		@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
		@RequestMapping(value = "/getIRDetails", method = RequestMethod.GET)
		public String getIRDetails(@RequestParam(value = "month") Integer month, @RequestParam(value = "year") Integer year,
				ModelMap modelMap) {

			modelMap.addAttribute("iRDetails", tamperService.getIRDetailForMonth( month, year));
			modelMap.addAttribute("reportMonthYearDate", DateUtil.convertMonthYearToDate(month, year));

			modelMap.addAttribute("monthOfReport", month);
			modelMap.addAttribute("yearOfReport", year);

			return "iRDetails";
		}
		
		@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
		@RequestMapping(value = "/getLocationInstantRegisters-{locationId}", method = RequestMethod.GET)
		public String getLocationInstantRegisters(@PathVariable String locationId,@RequestParam(value = "month") Integer month,
				@RequestParam(value = "year") Integer year,ModelMap modelMap) {

			modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(month, year) );

			modelMap.addAttribute("month",month);
			modelMap.addAttribute("year",year);
			modelMap.addAttribute("irDetail", tamperService.getIRDetails(locationId,month,year));

			return "locationIRDetails";
		}

	
	// get Tamper Loss Report
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getTamperLossReport", method = RequestMethod.GET)
	public String getTamperLossReport(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year, ModelMap modelMap) {
		modelMap.addAttribute("tamperDetailsProjectionModel",
				tamperService.getTamperDetailsProjectionReport(month, year));
		modelMap.addAttribute("reportMonthYearDate", DateUtil.convertMonthYearToDate(month, year));

		modelMap.addAttribute("monthOfReport", month);
		modelMap.addAttribute("yearOfReport", year);
		return "tamperDetailsProjection";
	}
	
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getAllLocationTampers", method = RequestMethod.GET)
	public String getLocationTampers(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year,ModelMap modelMap) {

		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(month, year) );

		modelMap.addAttribute("month",month);
		modelMap.addAttribute("year",year);

		modelMap.addAttribute("locationSurveyDataModel", tamperService.getTamperReportTransactions(null,month,year));

		return "locationTampers";
	}
	//leevansha end
	
	//leevansha start//
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getLocationTampers-{locationId}", method = RequestMethod.GET)
	public String getLocationTampers(@PathVariable String locationId,@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year,ModelMap modelMap) {

		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(month, year) );

		modelMap.addAttribute("month",month);
		modelMap.addAttribute("year",year);

		modelMap.addAttribute("locationSurveyDataModel", tamperService.getTamperReportTransactions(locationId,month,year));

		return "locationTampers";
	}
	
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/locationReports", method = RequestMethod.POST)
	public ModelAndView locationReports(@Valid @RequestBody ReportParametersModel parametersModel , Errors errors,
			ModelMap modelMap) {

		parametersModel.setReportMonth(parametersModel.getReportMonth()-1);
		if (parametersModel.getReportYear() == null) {
			parametersModel.setReportYear(new Date().getYear());
		}
		if (parametersModel.getReportMonth() == null) {
			parametersModel.setReportMonth(new Date().getMonth());
		}

		ModelAndView mv=null;
		if(EAUtil.EA_LOCATION_REPORT_INSTANT_REGISTERS.equalsIgnoreCase(parametersModel.getReportType()))
		{
			 mv=getInstantModelView(modelMap, parametersModel);
		}
		else if(EAUtil.EA_LOCATION_REPORT_TAMPERS.equalsIgnoreCase(parametersModel.getReportType()))
		{
			 mv=getTamperModelView(modelMap, parametersModel);
		}
		else
		{
			mv=getEnergyModelView(modelMap, parametersModel);

		}


		return mv;


	}


	private ModelAndView getEnergyModelView(ModelMap modelMap, ReportParametersModel parametersModel) {

		modelMap.addAttribute("locationSurveyDataModel", lossReportService1.getReportTransactions(parametersModel));
		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(parametersModel.getReportMonth(), parametersModel.getReportYear()) );
		modelMap.addAttribute("month",parametersModel.getReportMonth());
		modelMap.addAttribute("year",parametersModel.getReportYear());

		return new ModelAndView("reporting/locationImportExportInsert", modelMap);

	}

	private ModelAndView getTamperModelView(ModelMap modelMap, ReportParametersModel parametersModel) {
		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(parametersModel.getReportMonth(), parametersModel.getReportYear()) );
		modelMap.addAttribute("month",parametersModel.getReportMonth());
		modelMap.addAttribute("year",parametersModel.getReportYear());

		modelMap.addAttribute("locationSurveyDataModel", tamperService.getTamperReportTransactions(parametersModel));

		return new ModelAndView("reporting/locationTampersInsert", modelMap);

	}

	private ModelAndView getInstantModelView(ModelMap modelMap, ReportParametersModel parametersModel) {
		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(parametersModel.getReportMonth(), parametersModel.getReportYear()) );
		modelMap.addAttribute("month",parametersModel.getReportMonth());
		modelMap.addAttribute("year",parametersModel.getReportYear());
		modelMap.addAttribute("irDetail", tamperService.getIRDetails(parametersModel));


		return new ModelAndView("reporting/locationIRDetailsInsert", modelMap);

	}

}
