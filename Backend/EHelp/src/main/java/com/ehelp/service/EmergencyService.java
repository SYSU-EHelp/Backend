package com.ehelp.service;

import java.util.List;
import java.util.Map;

import com.ehelp.entity.Emergency;

public interface EmergencyService {

	//发起求救
	public boolean launchEmergency(Emergency e);
	
	//结束求救
	public boolean stopEmergency(int id);
	
	//获取紧急联系人电话
	public List<String> getPhones(int id);
		
}
