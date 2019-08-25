package org.pstcl.ea.controller.reporting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.service.impl.lossreport.ExcelGeneratorService2;
import org.pstcl.ea.service.impl.lossreport.IlossReportService;
import org.pstcl.ea.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class LossReportController {

	@Autowired
	private IlossReportService lossReportService;
	

	@Autowired
	private ExcelGeneratorService2 excelGeneratorService;

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = { "/lossReportDyn" }, method = { RequestMethod.GET })
	public String reportsHome(final ModelMap model) {
		return "reporting/lossReportDashboard";
	}
	
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = { "/reportDashboard" }, method = { RequestMethod.GET })
	public String reportDashboard(final ModelMap model) {
		return "reporting/reportDashboard";
	}





	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/lossReportExl", method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> lossReportExl(ReportParametersModel parametersModel , BindingResult bindingResult,
			ModelMap modelMap) throws IOException{
		ByteArrayInputStream in = excelGeneratorService.getReportExcel(parametersModel);
		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=LossReportPstcl.xlsx");

		headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		return ResponseEntity
				.ok()
				.headers(headers)
				.body(new InputStreamResource(in));
	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/lossReportDyn", method = RequestMethod.POST)
	public String lossReportDyn(@Valid @RequestBody ReportParametersModel parametersModel , Errors errors,
			ModelMap modelMap) {

		parametersModel.setReportMonth(parametersModel.getReportMonth()-1);
		if (parametersModel.getReportYear() == null) {
			parametersModel.setReportYear(new Date().getYear());
		}
		if (parametersModel.getReportMonth() == null) {
			parametersModel.setReportMonth(new Date().getMonth());
		}

		if  (StringUtils.isNotEmpty(parametersModel.getReportType())) {
			getReportDetails(parametersModel.getReportType(), parametersModel.getReportMonth(), parametersModel.getReportYear(), modelMap);
			return "reporting/lossReportInsert";
		} else {
			getLossReport(parametersModel.getReportMonth(), parametersModel.getReportYear(), modelMap);
			return "reporting/lossReportConsolidatedInsert";

		}
	}


	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getReport", method = RequestMethod.GET)
	public String getReportDetails(@RequestParam(value = "type") String reportType,
			@RequestParam(value = "month") Integer month, @RequestParam(value = "year") Integer year,
			ModelMap modelMap) {
		modelMap.addAttribute("lossReportModel", lossReportService.getReport(reportType, month, year));
		modelMap.addAttribute("reportMonthYearDate", DateUtil.convertMonthYearToDate(month, year));

		modelMap.addAttribute("monthOfReport", month);
		modelMap.addAttribute("yearOfReport", year);

		return "reporting/lossReport";
	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getLossReport", method = RequestMethod.GET)
	public String getLossReport(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year, ModelMap modelMap) {
		modelMap.addAttribute("consolidatedLossReportModel",
				lossReportService.getConsolidatedMonthlyLossReport(month, year,false));
		modelMap.addAttribute("reportMonthYearDate", DateUtil.convertMonthYearToDate(month, year));

		modelMap.addAttribute("monthOfReport", month);
		modelMap.addAttribute("yearOfReport", year);

		return "reporting/lossReportConsolidated";
	}



	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getLossReport", method = RequestMethod.POST)
	public String getLossReportPost(@RequestParam(value = "startDate") Integer month,
			@RequestParam(value = "endDate") Integer year, ModelMap modelMap) {
		modelMap.addAttribute("consolidatedLossReportModel",
				lossReportService.getConsolidatedMonthlyLossReport(month, year,false));
		modelMap.addAttribute("reportMonthYearDate", DateUtil.convertMonthYearToDate(month, year));

		modelMap.addAttribute("monthOfReport", month);
		modelMap.addAttribute("yearOfReport", year);

		return "reporting/lossReportConsolidated";
	}


}
