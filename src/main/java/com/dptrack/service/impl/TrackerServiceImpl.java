package com.dptrack.service.impl;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dptrack.model.Tracker;
import com.dptrack.repository.TrackerRepository;
import com.dptrack.service.TrackerService;
import com.dptrack.utils.Constants;
import com.dptrack.utils.DPTrackResponse;

@Service
@Transactional
public class TrackerServiceImpl implements TrackerService {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TrackerRepository trackerRepo;

	@Override
	public DPTrackResponse<Object> create(Tracker tracker) {
		DPTrackResponse<Object> response = null;
		try {
			Tracker tracker2 = trackerRepo.findByImei(tracker.getImei());
			if (tracker2 == null)
				return new DPTrackResponse<>(200, false, Constants.EXIST, null);
			tracker.setTrackerId(UUID.randomUUID().toString());
			response = new DPTrackResponse<>(201, true, Constants.OK, trackerRepo.save(tracker));
		} catch (Exception e) {
			log.error("Exception :create", e);
		}
		return response;
	}

	@Override
	public DPTrackResponse<Object> getTackers(String id, int pageNo) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DPTrackResponse<Object> update(Tracker tracker) {
		Tracker tracker2 = trackerRepo.findByImei(tracker.getImei());
		if (tracker2 == null)
			return new DPTrackResponse<>(200, false, Constants.EXIST, null);
		
		return null;
	}

	@Override
	public DPTrackResponse<Object> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DPTrackResponse<Object> getTackersByUser(String id, int pageNo) {
		try {
			List<Tracker> list = trackerRepo.findByUserId(id,PageRequest.of(pageNo, 10));
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return null;
	}

}
