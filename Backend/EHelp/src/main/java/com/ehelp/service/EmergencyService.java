package com.ehelp.service;

import java.util.List;

import com.ehelp.entity.Emergency;

public interface EmergencyService {

	//发起求救
	public boolean launchEmergency(Emergency e);
	
	//获取紧急联系人电话 
	public List<String> getPhones(int id);
	
	//根据id获取用户名
	public String getName(int id);
		
}
