package com.ehelp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehelp.entity.Emergency;
import com.ehelp.service.EmergencyServiceImpl;
import com.ehelp.service.UserServiceImpl;
import com.ehelp.util.SendMessageUtil;
import com.taobao.api.ApiException;

@Controller
@RequestMapping("/emergencies")
public class EmergencyController {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private EmergencyServiceImpl emergencyService;
	
	@Autowired
	private UserServiceImpl userService;
	
	/*
	 * 发起求救
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> launchEmergency(HttpSession session) throws ParseException, ApiException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		int id = (Integer) session.getAttribute("user");
		Date date = sdf.parse(sdf.format(new Date()));
		Emergency e = new Emergency(id, date, 0);
		if (emergencyService.launchEmergency(e)) {
			map.put("status", 200);
			//发送紧急短信
			List<String> phoneList = emergencyService.getPhones(id);
			String username = emergencyService.getName(id);
			for (String phone : phoneList) {
				SendMessageUtil.send(phone, username);
			}
		}
		else {
			map.put("status", 500);
			map.put("errmsg", "求救失败");
		}
		return map;
	}
	
}
