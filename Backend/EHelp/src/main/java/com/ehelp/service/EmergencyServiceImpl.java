package com.ehelp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehelp.dao.EmergencyDaoImpl;
import com.ehelp.entity.Emergency;

@Service
public class EmergencyServiceImpl implements EmergencyService {

	@Autowired
	private EmergencyDaoImpl emergencyDao;

	public boolean launchEmergency(Emergency e) {
		return emergencyDao.launchEmergency(e);
	}

	public List<String> getPhones(int id) {
		return emergencyDao.getPhones(id);
	}
	
}
