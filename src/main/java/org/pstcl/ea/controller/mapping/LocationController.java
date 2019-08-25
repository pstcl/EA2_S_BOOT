package org.pstcl.ea.controller.mapping;

import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.model.MastersForLocationEntry;
import org.pstcl.ea.model.reporting.ReportParametersModel;
import org.pstcl.ea.service.impl.lossreport.IlossReportService;
import org.pstcl.ea.service.impl.lossreport.ReportService;
import org.pstcl.ea.service.impl.masters.EMFMappingService;
import org.pstcl.ea.service.impl.masters.LocationMasterService;
import org.pstcl.ea.service.impl.masters.LocationMeterMappingService;
import org.pstcl.ea.service.impl.masters.LocationUtilService;
import org.pstcl.ea.service.impl.parallel.DataService;
import org.pstcl.ea.util.DateUtil;
import org.pstcl.ea.util.EAUtil;
import org.pstcl.model.FilterModel;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LocationController {
	@Autowired
	LocationMasterService locationMasterService;
	@Autowired
	LocationUtilService locationUtilService;

	@Autowired
	LocationMeterMappingService locationMeterMappingService;


	@Autowired
	private ReportService lossReportService;

	@Autowired
	private EMFMappingService emfMappingService;
	
	
	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/getPendingLossReportLocation", method = RequestMethod.GET)
	public String getPendingLossReportLocation(@RequestParam(value = "month") Integer month,
			@RequestParam(value = "year") Integer year,ModelMap modelMap) {
		modelMap.addAttribute("pendingLocList", lossReportService.getPendingLossReportLocation(month,year));
		modelMap.addAttribute("monthOfReport",DateUtil.convertMonthYearToDate(month, year) );
		modelMap.addAttribute("month",month);
		modelMap.addAttribute("year",year);
		return "pendingLossMetersDetail";
	}
	/**
	 * Generates options to select circle ,division,substation and location dynamically
	 * @param circleCode
	 * @param divCode
	 * @param substationCode
	 * @param locationid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/getLocationsModel" }, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody FilterModel getLocationsModel(@RequestParam(value = "circleSelected") Integer circleCode,
			@RequestParam(value = "divisionSelected") Integer divCode,
			@RequestParam(value = "substationSelected") Integer substationCode,
			@RequestParam(value = "locationSelected") String locationid, ModelMap model) {

		FilterModel locationModel = locationUtilService.getLocationModel(circleCode, divCode, substationCode, locationid);
		return locationModel;
	}

	/**
	 * To add a new Location in Mapping
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addLocation", method = RequestMethod.GET)
	public ModelAndView addLocationMasterDetails(ModelMap model) {
		model.addAttribute("edit", false);
		model.addAttribute("locationMaster", new LocationMaster());
		return new ModelAndView("mapping/addLocation", model);
	}

	/**
	 * To save added Locations
	 * @param locationMaster
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addLocation", method = RequestMethod.POST)
	public Object saveLocationMaster(LocationMaster locationMaster, BindingResult bindingResult, ModelMap model) {

		String error=locationMasterService.validate(locationMaster);
		if (error != null) {

			model.addAttribute("msg", error);
			model.addAttribute("edit", false);
			model.addAttribute("locationMaster", locationMaster);
			return new ModelAndView("mapping/addLocation", model);
		}
		error=locationMasterService.isAlreadyExisting(locationMaster);
		if (error != null)	{
			model.addAttribute("edit", true);		
			model.addAttribute("msg", error);
			model.addAttribute("locationMaster", locationMaster);
			return new ModelAndView("mapping/addLocation", model);

		}

		locationMasterService.saveLocationMasterDetails(locationMaster);
		return (String) "redirect:mappingHome";
	}


	@PreAuthorize("hasRole('ROLE_SLDC_USER') or hasRole('ROLE_SLDC_ADMIN')")
	@RequestMapping(value = "/locationHome-{locationId}", method = RequestMethod.GET)
	public String viewLoadSurveyData(@PathVariable String locationId,ModelMap modelMap) {
		modelMap.addAttribute("location", locationMasterService.getLocation(locationId));
		modelMap.addAttribute("list",emfMappingService.getLocationEmfListByLocid(locationId));
		modelMap.addAttribute("meterLocationMappingList",locationMeterMappingService.getMeterLocMapByLocationID(locationId));

		return "masterViews/locationHome";
	}

	

	/**
	 * To save added Locations
	 * @param locationMaster
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateLocation", method = RequestMethod.POST)
	public Object updateLocation(LocationMaster locationMaster, BindingResult bindingResult, ModelMap model) {

		String error=locationMasterService.validate(locationMaster);
		if (error != null) {
			model.addAttribute("edit", true);		

			model.addAttribute("msg", error);
			model.addAttribute("locationMaster", locationMaster);
			return new ModelAndView("mapping/addLocation", model);
		}
		locationMasterService.saveLocationMasterDetails(locationMaster);
		return (String) "redirect:mappingHome";
	}

	/*
	 * To generate options like boundary type,feeder , utility ,device and model in add Location Master form
	 */
	@RequestMapping(value = { "/getLocationListModel" }, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody MastersForLocationEntry getLocationsMasterListModel( ModelMap model) {

		MastersForLocationEntry listModel = locationUtilService.getLocationMasterListModel();
		return listModel;
	}




}
