package org.pstcl.ea.controller.mapping;

import org.pstcl.ea.model.mapping.LocationEMFModel;
import org.pstcl.ea.service.impl.masters.EMFMappingService;
import org.pstcl.ea.service.impl.masters.LocationMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EMFController {

	@Autowired
	EMFMappingService emfMappingService;
	@Autowired	
	LocationMasterService locationMasterService;

	/**
	 * To change location emf mapping of mapped locations in view energy details
	 * @param locationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/changeLocationEmf", method = RequestMethod.GET)
	public ModelAndView changeLocationEmf(@RequestParam(value = "locationId") String locationId, ModelMap model) {
		LocationEMFModel locationEMFModel =emfMappingService.prepareEMFModelForLocation(locationId);
		
		model.addAttribute("locationEMFModel", locationEMFModel);
		model.addAttribute("list",emfMappingService.getLocationEmfListByLocid(locationEMFModel.getLocationMaster().getLocationId()));

		return new ModelAndView("mapping/locationEmfForm", model);
	}

	/**
	 * Saves changed location emf details
	 * @param locationEMFModel
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/changeLocationEmf", method = RequestMethod.POST)
	public ModelAndView saveChangeLocationEmfDetails(LocationEMFModel locationEMFModel, BindingResult bindingResult,
			ModelMap model) {

		String returnView = "mapping/successEmf";
		
		
		String error= emfMappingService.validate(locationEMFModel);
		if (error==null)
		{
			LocationEMFModel locationEMFModel2=emfMappingService.saveDetailsOfLocationEmf(locationEMFModel);
			if (!locationEMFModel2.getMappingSuccesful()) {
				
				error = "Problem while Saving Details.";
				model.addAttribute("error", error);
				model.addAttribute("list",emfMappingService.getLocationEmfListByLocid(locationEMFModel.getLocationMaster().getLocationId()));
				
				returnView = "mapping/locationEmfForm";
			} 
			else
			{
				model.addAttribute("list",
						emfMappingService.getLocationEmfListByLocid(locationEMFModel.getLocationMaster().getLocationId()));
				model.addAttribute("locationSurveyDataModel",
						locationEMFModel2.getLocationSurveyDataModel());
				
				
				

			}

		}
		if (error != null) {
			model.addAttribute("locationEMFModel", locationEMFModel);
			model.addAttribute("error", error);
			model.addAttribute("list",emfMappingService.getLocationEmfListByLocid(locationEMFModel.getLocationMaster().getLocationId()));

			returnView = "mapping/locationEmfForm";
		}

		return new ModelAndView(returnView, model);
	}

}

