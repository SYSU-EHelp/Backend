package com.ehelp.dao;

import java.util.List;
import java.util.Map;

import com.ehelp.entity.Emergency;

public interface EmergencyDao {

	//发起求救
	public boolean launchEmergency(Emergency e);
	
	//获取紧急联系人电话
	public List<String> getPhones(int id);
	
}
