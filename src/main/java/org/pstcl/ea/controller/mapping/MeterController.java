package org.pstcl.ea.controller.mapping;

import java.util.List;

import org.pstcl.ea.model.MastersForLocationEntry;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.entity.MeterMaster;
import org.pstcl.ea.service.impl.SubstationDataServiceImpl;
import org.pstcl.ea.service.impl.masters.EMFMappingService;
import org.pstcl.ea.service.impl.masters.LocationMasterService;
import org.pstcl.ea.service.impl.masters.LocationMeterMappingService;
import org.pstcl.ea.service.impl.masters.MeterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MeterController {

	@Autowired
	LocationMasterService locationMasterService;

	@Autowired
	SubstationDataServiceImpl substationDataService;

	@Autowired
	MeterMasterService meterMasterService;

	@Autowired
	LocationMeterMappingService locationMeterMappingService;

	@Autowired
	private EMFMappingService emfMappingService;

	/**
	 * To generate options like meter type,meter make and meter category in add meter form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/getMeterListModel" }, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody MastersForLocationEntry getMeterListModel( ModelMap model) {

		MastersForLocationEntry listModel = meterMasterService.meterAttrMasterListModel();
		return listModel;
	}



	/**
	 * To add a new Meter 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addMeter", method = RequestMethod.GET)
	public ModelAndView addMeter(ModelMap model) {
		model.addAttribute("meter", new MeterMaster());
		return new ModelAndView("mapping/addMeter", model);
	}
	/**
	 * To save added meter
	 * @param meter
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addMeter", method = RequestMethod.POST)
	public Object saveMeterMaster(MeterMaster meter, BindingResult bindingResult, ModelMap model) {

		String error=meterMasterService.validate(meter);
		if (error != null) {
			model.addAttribute("msg", error);
			model.addAttribute("meter", meter);
			return new ModelAndView("mapping/addMeter", model);
		}
		error=meterMasterService.isAlreadyExisting(meter);
		if (error != null)	{
			model.addAttribute("edit", true);		
			model.addAttribute("msg", error);
			model.addAttribute("meter", meter);
			return new ModelAndView("mapping/addMeter", model);

		}
		meterMasterService.saveMeterDetails(meter);
		return (String) "redirect:mappingHome";
	}

	/**
	 * To save added meter
	 * @param meter
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateMeter", method = RequestMethod.POST)
	public Object updateMeter(MeterMaster meter, BindingResult bindingResult, ModelMap model) {

		String error=meterMasterService.validate(meter);
		if (error != null) {
			model.addAttribute("edit", true);		
			model.addAttribute("msg", error);
			model.addAttribute("meter", meter);
			return new ModelAndView("mapping/addMeter", model);
		}
		meterMasterService.updateMeterDetails(meter);
		return (String) "redirect:mappingHome";
	}



	/**
	 * get meter details when option is clicked in view energy meter details
	 */
	@RequestMapping(value = { "/getMeterDetails" }, method = RequestMethod.GET)
	public ModelAndView getMeterDetails(@RequestParam(value = "locationId") int locationId,
			@RequestParam(value = "ssCode") int ssCode, ModelMap model) {

		model.addAttribute("location", locationMeterMappingService.getMeterDetails(locationId));
		model.addAttribute("substation", substationDataService.findSubstationById(ssCode));

		return new ModelAndView("masterViews/meterDetailsSnippet", model);

	}

	@RequestMapping(value = {"/getLocationMeterDetails" }, method = RequestMethod.GET)
	public ModelAndView getMeterDetails(
			@RequestParam(value="locationid") String locationid,
			ModelMap model) 
	{
		model.addAttribute("list",emfMappingService.getLocationEmfListByLocid(locationid));
		model.addAttribute("meterLocationMappingList",locationMeterMappingService.getMeterLocMapByLocationID(locationid));
		return new ModelAndView("masterViews/locationMeterDetailsSnippet", model);
	}








	/*
	 * To generate options like boundary type,feeder , utility ,device and model in add Location Master form
	 */
	@RequestMapping(value = { "/getUnmappedLocations" }, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<LocationMaster> getUnmappedLocations( ModelMap model) {

		return locationMasterService.findNotMappedLocations();
	}



	//	@GetMapping("/searchMeters")
	//	public ResponseEntity<String> doAutoComplete(@RequestParam("q") final String input) {
	//		List<String> strings = meterMasterService.findMetersLike(input);
	//		ObjectMapper mapper = new ObjectMapper();
	//		String resp = "";
	//		try {
	//			resp = mapper.writeValueAsString(strings);
	//		} catch (JsonProcessingException e) {
	//		}
	//		return new ResponseEntity<String>(resp, HttpStatus.OK);
	//	}




}
