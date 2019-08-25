package org.pstcl.ea.controller;

import javax.validation.Valid;

import org.pstcl.ea.model.FileModel;
import org.pstcl.ea.model.reporting.FileParameterModel;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.service.impl.SubstationDataServiceImpl;
import org.pstcl.ea.service.impl.UploadingService;
import org.pstcl.ea.service.impl.lossreport.IlossReportService;
import org.pstcl.ea.service.impl.parallel.DataService;
import org.pstcl.ea.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class SLDCUserController {

	@Autowired
	private UploadingService restService;


	@Autowired
	private DataService dateService;

	@Autowired
	private SubstationDataServiceImpl substationDataServiceImpl;

	@Autowired
	private IlossReportService lossReportService;


	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getLocationFileDetails", method = RequestMethod.POST)
	public String getLocationFileDetails(@Valid @RequestBody FileParameterModel parametersModel , Errors errors,
			ModelMap modelMap,BindingResult bindingResult)  {
		modelMap.addAttribute("locDetails", dateService.getFilesForLocation(parametersModel));
		return "snippets/filesSnippet";
	}
















	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/processRepoFiles", method = RequestMethod.GET)
	public String processRepoFiles(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year,ModelMap modelMap) {
		modelMap.addAttribute("fileModel", dateService.processZipFiles(month,year));
		modelMap.addAttribute("reportMonthYearDate",DateUtil.convertMonthYearToDate(month, year) );

		return "processingFilesStatus";
	}





	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN') or hasRole('ROLE_SE')")
	@RequestMapping(value = "/pendingRepoFiles", method = RequestMethod.GET)
	public String pendingRepoFiles(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year,ModelMap modelMap) {
		modelMap.addAttribute("fileModel", dateService.getPendingRepoFiles(month,year));
		modelMap.addAttribute("reportMonthYearDate",DateUtil.convertMonthYearToDate(month, year) );

		return "uploadedFilesDetail";
	}



	//	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	//	@RequestMapping(value = "/filesInRepo", method = RequestMethod.GET)
	//	public String getFilesInRepo( @RequestParam(value = "month") Integer month,
	//			@RequestParam(value = "year") Integer year, ModelMap modelMap) {
	//		modelMap.addAttribute("fileModel", restService.getFileInRepo(11,2018));
	//		modelMap.addAttribute("reportMonthYearDate",DateUtil.convertMonthYearToDate(month, year) );
	//		
	//		return "uploadedFilesDetail";
	//	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN') or hasRole('ROLE_SE')")
	@RequestMapping(value = "/filesInRepoS", method = RequestMethod.GET)
	public String getFilesInRepo(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year, ModelMap modelMap) {
		modelMap.addAttribute("fileModel", substationDataServiceImpl.getFileInRepo(month,year));
		modelMap.addAttribute("reportMonthYearDate",DateUtil.convertMonthYearToDate(month, year) );

		return "uploadedFilesDetail";
	}



//
//	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
//	@RequestMapping(value = "/processFileTamper-{id}", method = RequestMethod.GET)
//	public String processFileTamper(@PathVariable Integer id,ModelMap modelMap) {
//		modelMap.addAttribute("cmriModel", dateService.processRepoFileForTamper(id));
//		return "processingFilesStatus";
//	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/processRepoFile-{id}", method = RequestMethod.GET)
	public String uploadFile(@PathVariable Integer id,ModelMap modelMap) {
		modelMap.addAttribute("cmriModel", dateService.processRepoFileSerial(id));
		return "processingFilesStatus";
	}

	//	@RequestMapping(value = "/uploadZipFile", method = RequestMethod.POST)
	//	public String uploadZipFile(FileModel fileModel, BindingResult result, ModelMap modelMap) {
	//		modelMap.addAttribute("file", fileModel.getFile());
	//		modelMap.addAttribute("cmriModel", restService.processZipUploadedFile(fileModel));
	//		return "fileImportExport";
	//	}
	//
	//	@RequestMapping(value = "/uploadZipFile", method = RequestMethod.GET)
	//	public String uploadZipFile(ModelMap modelMap) {
	//		FileModel fileModel=new FileModel();
	//		modelMap.addAttribute("fileModel", fileModel);
	//		return "uploadedFilesDetail";
	//	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/uploadMultiZip", method = RequestMethod.GET)
	public String uploadMultiZip(ModelMap modelMap) {
		FileModel fileModel=new FileModel();
		modelMap.addAttribute("fileModel", fileModel);

		return "addCMRIMultiZip";
	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/uploadMultiZip", method = RequestMethod.POST)
	public String uploadMultiZip(FileModel fileModel, BindingResult result, ModelMap modelMap) {
		modelMap.addAttribute("fileModel", restService.processMultiZipUploadedFile(fileModel));

		return "uploadedFilesDetail";
	}


	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/uploadTxtFile", method = RequestMethod.POST)
	public String submit(FileModel fileModel, BindingResult result, ModelMap modelMap) {
		modelMap.addAttribute("file", fileModel.getFile());

		modelMap.addAttribute("fileModel", restService.processUploadedFile(fileModel));
		return "uploadedFilesDetail";
	}

	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/uploadTxtFile", method = RequestMethod.GET)
	public String uploadTxnFile( ModelMap modelMap) {
		FileModel fileModel=new FileModel();
		modelMap.addAttribute("fileModel", fileModel);
		return "addMeterDataText";
	}






	//uploadFile
}
