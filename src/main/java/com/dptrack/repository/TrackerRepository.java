package com.dptrack.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dptrack.model.Tracker;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long>{

	Tracker findByImei(String imei);

	List<Tracker> findByUserId(String id,Pageable page);

}
