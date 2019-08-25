package org.pstcl.ea.controller.mapping;

import java.util.List;

import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.model.mapping.LocationMeterMappingModel;
import org.pstcl.ea.service.impl.SubstationDataServiceImpl;
import org.pstcl.ea.service.impl.masters.LocationMasterService;
import org.pstcl.ea.service.impl.masters.LocationMeterMappingService;
import org.pstcl.ea.service.impl.masters.LocationUtilService;
import org.pstcl.ea.service.impl.masters.MeterMasterService;
import org.pstcl.model.FilterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MeterLocationMappingController {
	@Autowired
	LocationUtilService restService;
	@Autowired
	SubstationDataServiceImpl substationDataService;
	@Autowired
	LocationMeterMappingService locationMeterMappingService;
	@Autowired
	MeterMasterService meterMasterService;
	@Autowired
	LocationMasterService locationMasterService;

	/**
	 * Generates options to select circle ,division,substation and location dynamically
	 * @param circleCode
	 * @param divCode
	 * @param substationCode
	 * @param locationid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/substationMaster" }, method = RequestMethod.POST)
	public String substationMaster(@RequestParam(value = "circleSelected") Integer circleCode,
			@RequestParam(value = "divisionSelected") Integer divCode,
			@RequestParam(value = "substationSelected") Integer substationCode,
			ModelMap model) {

		FilterModel locationModel = restService.getLocationModel(circleCode, divCode, substationCode,null);
		model.addAttribute("currentUser", substationDataService.getLoggedInUser());
		model.addAttribute("substationList", locationMeterMappingService.findSubstationEnergyMeters(locationModel));
		//	model.addAttribute("notMappedMeters",meterMasterService.findNotMappedMeters());
		return "masterViews/substationListInsert";
	}

	@RequestMapping(value = { "/substationMaster" }, method = { RequestMethod.GET })
	public String substationMaster(final ModelMap model) {
		FilterModel locationModel = substationDataService.getUserLocationModel();
		model.addAttribute("currentUser", substationDataService.getLoggedInUser());
		model.addAttribute("substationList", locationMeterMappingService.findSubstationEnergyMeters(locationModel));
		model.addAttribute("notMappedMeters",meterMasterService.findNotMappedMeters());
		return "masterViews/substationList";
	}



	/**
	 * Saves the changed meter mapping in textAjax.jsp
	 * @param locationMeterMap
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/mapMeter" }, method = RequestMethod.POST)
	public ModelAndView saveMeterLocationMapping(LocationMeterMappingModel locationMeterMap, BindingResult bindingResult,
			ModelMap model) {

		ModelAndView modelAndView=null;
		String error = locationMeterMappingService.validateDates(locationMeterMap);

		LocationMeterMappingModel resultModel=locationMeterMappingService.saveLocationMeterMapping(locationMeterMap);
		if (error == null && resultModel.getMappingSuccesful()) {
			List<MeterLocationMap> mappings = restService.findLocations(locationMeterMap.getMeterMaster());
			model.addAttribute("locationMeterMappingList", mappings);
			model.addAttribute("changeMeterSnippet", locationMeterMap);
			model.addAttribute("locationSurveyDataModel", resultModel);
			modelAndView= new ModelAndView("mapping/success", model);
		}
		else 
		{

			model.addAttribute("error", error);
			model.addAttribute("changeMeterSnippet", locationMeterMap);
			modelAndView= new ModelAndView("mapping/mapMeterLoc", model);
		}
		return modelAndView;
	}








	/**
	 * To change location details of an existing mapped meter when clicked in view energy details
	 * @param meterlocationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/changeMeterLocation" }, method = RequestMethod.GET)
	public ModelAndView changeMeterLocation(@RequestParam(value = "meterlocationId") int meterlocationId,
			ModelMap model) {
		LocationMeterMappingModel chgMtr = new LocationMeterMappingModel();
		chgMtr.setOldValues(locationMeterMappingService.getMeterDetails(meterlocationId));
		model.addAttribute("error", "");
		model.addAttribute("changeMeterSnippet", chgMtr);
		List<MeterLocationMap> mappings = restService.findLocations(chgMtr.getOldMeterLocationMap().getMeterMaster(),chgMtr.getOldMeterLocationMap().getLocationMaster());
		model.addAttribute("locationMeterMappingList", mappings);
		return new ModelAndView("mapping/mapMeterLoc", model);

	}

	/**
	 * To change location details of an existing mapped meter when clicked in view energy details
	 * @param meterlocationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/removeLocationMeter" }, method = RequestMethod.GET)
	public ModelAndView removeLocationMeter(@RequestParam(value = "meterlocationId") int meterlocationId,
			ModelMap model) {
		LocationMeterMappingModel chgMtr = new LocationMeterMappingModel();
		chgMtr.setOldValues(locationMeterMappingService.getMeterDetails(meterlocationId));
		model.addAttribute("error", "");
		model.addAttribute("changeMeterSnippet", chgMtr);
		List<MeterLocationMap> mappings = restService.findLocations(chgMtr.getOldMeterLocationMap().getMeterMaster(),chgMtr.getOldMeterLocationMap().getLocationMaster());
		model.addAttribute("locationMeterMappingList", mappings);
		return new ModelAndView("mapping/removeLocationMeter", model);

	}

	/**
	 * Saves the changed meter mapping in textAjax.jsp
	 * @param locationMeterMap
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/removeLocationMeter" }, method = RequestMethod.POST)
	public ModelAndView saveRemovedMeterLocationMapping(LocationMeterMappingModel locationMeterMap, BindingResult bindingResult,
			ModelMap model) {

		ModelAndView modelAndView=null;
		String error = locationMeterMappingService.validateEndDate(locationMeterMap);

		LocationMeterMappingModel resultModel=locationMeterMappingService.endLocationMeterMapping(locationMeterMap);

		if (error == null &&resultModel.getMappingSuccesful()) {
			List<MeterLocationMap> mappings = restService.findLocations(locationMeterMap.getMeterMaster());
			model.addAttribute("locationMeterMappingList", mappings);
			model.addAttribute("locationSurveyDataModel", resultModel);
			modelAndView= new ModelAndView("mapping/success", model);
		}
		else 
		{

			model.addAttribute("error", error);
			model.addAttribute("changeMeterSnippet", locationMeterMap);

			modelAndView= new ModelAndView("mapping/mapMeterLoc", model);
		}
		return modelAndView;
	}


	/**
	 * To change location details of unmapped meter in view energy details
	 * @param meterId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/map-location" }, method = RequestMethod.GET)
	public ModelAndView mapLocation(@RequestParam(value = "locationid") String locationid, ModelMap model) {
		LocationMeterMappingModel locationMeterMappingModel = new LocationMeterMappingModel();
		locationMeterMappingModel.setLocation(locationMasterService.findLocationById(locationid));
		model.addAttribute("changeMeterSnippet", locationMeterMappingModel);
		List<MeterLocationMap> mappings = restService.findLocations(locationMeterMappingModel.getMeterMaster());
		model.addAttribute("locationMeterMappingList", mappings);
		return new ModelAndView("mapping/mapLoc", model);
	}

	/**
	 * To change location details of unmapped meter in view energy details
	 * @param meterId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/map-meter" }, method = RequestMethod.GET)
	public ModelAndView mapMeter(@RequestParam(value = "meterId") String meterId, ModelMap model) {
		LocationMeterMappingModel locationMeterMappingModel = new LocationMeterMappingModel();
		locationMeterMappingModel.setMeterMaster(meterMasterService.findMeterbyId(meterId));
		model.addAttribute("changeMeterSnippet", locationMeterMappingModel);
		List<MeterLocationMap> mappings = restService.findLocations(locationMeterMappingModel.getMeterMaster());
		model.addAttribute("locationMeterMappingList", mappings);
		return new ModelAndView("mapping/mapMeterLoc", model);
	}


	@RequestMapping(value = { "/mappingHome" }, method = { RequestMethod.GET })
	public String notMappedMeters(final ModelMap model) {
		model.addAttribute("currentUser", substationDataService.getLoggedInUser());
		//added for editing meter details leevansha	
		//	model.addAttribute("substationList", locationMeterMappingService.findSubstationEnergyMeters());
		model.addAttribute("notMappedMeters",meterMasterService.findNotMappedMeters());
		model.addAttribute("notMappedLocations",locationMasterService.findNotMappedLocations());
		return "mapping/mappingHome";
	}


}
