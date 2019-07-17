package org.pstcl.ea.controller.mapping;

import org.pstcl.ea.model.MastersForLocationEntry;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.service.impl.masters.LocationMasterService;
import org.pstcl.ea.service.impl.masters.LocationUtilService;
import org.pstcl.model.FilterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
