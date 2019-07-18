package org.pstcl.ea.controller.mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.pstcl.ea.model.AddReportLocationModel;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.service.impl.masters.ReportLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportLocMappingController {
	
	@Autowired
	ReportLocationService reportLocMapService;
	
	/**
	 * To add locations in a month for calculations first redirect to this method to select month
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectMonthForReportLocations", method = RequestMethod.GET)
	public ModelAndView selectReportMonths(ModelMap model) {
		model.addAttribute("error",null);
		model.addAttribute("addReportLocations", new AddReportLocationModel());
		return new ModelAndView("mapping/selectMonthForReportLocations", model);
	}

	/**
	 * check errors in entered month and year for report month and locations mapping and redirect on success case to adding/removing locations form
	 * @param addReportLocations
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectMonthForReportLocations", method = RequestMethod.POST)
	public Object selectReportLocations(AddReportLocationModel addReportLocations,ModelMap model) {
		//System.out.println(addReportLocations.getMonth());
		if( addReportLocations.getYear()<1000 || addReportLocations.getYear()>9999)
		{
			model.addAttribute("error","Year should be of 4 digits");
			model.addAttribute("addReportLocations", new AddReportLocationModel());
			return new ModelAndView("mapping/selectMonthForReportLocations", model);
		}


		Set <LocationMaster> pendingLocation= new HashSet<LocationMaster>(reportLocMapService.selectReportLocations(addReportLocations));
		try {
			Set<LocationMaster> addedLocations = new HashSet<LocationMaster>(addReportLocations.getLocations());
			addReportLocations.setLocations(new ArrayList<LocationMaster>());
			if(addedLocations.size()>0)
				model.addAttribute("addedLocations",addedLocations);
			else
				model.addAttribute("addedLocations",null);	
			//System.out.println(addedLocations.size());
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("addedLocations",null);
		}
		model.addAttribute("pendingLocation",pendingLocation);

		model.addAttribute("addReportLocations", addReportLocations);
		return new ModelAndView("mapping/addReportLocations", model);
	}

	/**
	 * Save the locations mapped or removed for report month matching
	 * @param addReportLocations
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addReportLocation",method=RequestMethod.POST)
	public Object saveReportLocations(AddReportLocationModel addReportLocations,ModelMap model) {
		addReportLocations = reportLocMapService.saveReportLocations(addReportLocations);
		model.addAttribute("addReportLocations", addReportLocations);
		return "redirect:selectMonthForReportLocations";
	}	


}
