package com.dptrack.service;

import com.dptrack.model.Tracker;
import com.dptrack.utils.DPTrackResponse;

public interface TrackerService {

	DPTrackResponse<Object> create(Tracker tracker);

	DPTrackResponse<Object> getTackers(String id, int pageNo);

	DPTrackResponse<Object> update(Tracker tracker);

	DPTrackResponse<Object> delete(String id);

	DPTrackResponse<Object> getTackersByUser(String id, int pageNo);

}
