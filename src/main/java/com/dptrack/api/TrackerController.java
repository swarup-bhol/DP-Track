package com.dptrack.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dptrack.model.Tracker;
import com.dptrack.service.TrackerService;
import com.dptrack.utils.DPTrackResponse;

@RestController
@RequestMapping("/api/v1/tracker")
public class TrackerController {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	TrackerService trackerService;
	
	/**
	 * @author swarup-bhol
	 * 
	 * @param tracker
	 * @return
	 */
	@PostMapping("/create")
	public DPTrackResponse<Object> create(@RequestBody Tracker tracker)  {
		return trackerService.create(tracker);
	}

	/**
	 * @author swarup-bhol
	 * 
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@GetMapping("/tacker/{id}/{pageNo}")
	public DPTrackResponse<Object> getTracker(@PathVariable String id , int pageNo){
		return trackerService.getTackers(id,pageNo);
	}
	/**
	 * @author swarup-bhol
	 * 
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@GetMapping("/tacker/{userId}/{pageNo}")
	public DPTrackResponse<Object> getTrackerByUser(@PathVariable String id , int pageNo){
		return trackerService.getTackersByUser(id,pageNo);
	}
	
	/**
	 * @author swarup-bhol
	 * 
	 * @param tracker
	 * @return
	 */
	@PutMapping("/{id}")
	public DPTrackResponse<Object> update(@RequestBody Tracker tracker)  {
		return trackerService.update(tracker);
	}
	
	/**
	 * @author swarup-bhol
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public DPTrackResponse<Object> delete(@PathVariable String id)  {
		return trackerService.delete(id);
	}
}
