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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehelp.entity.Emergency;
import com.ehelp.service.EmergencyServiceImpl;
import com.ehelp.service.UserServiceImpl;
import com.ehelp.util.SendMessageUtil;
import com.taobao.api.ApiException;

@Controller
@RequestMapping("/emergencies")
public class EmergencyController {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private EmergencyServiceImpl emergencyService;
	
	@Autowired
	private UserServiceImpl userService;
	
	/*
	 * 发起求救
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> launchEmergency(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			int id = (Integer) session.getAttribute("user");
			Date date = sdf.parse(sdf.format(new Date()));
			Emergency e = new Emergency(id, date, 0);
			int event_id = emergencyService.launchEmergency(e);
			if (event_id != -1) {
				map.put("status", 200);
				data.put("id", event_id);
				//发送紧急短信
				List<String> list = emergencyService.getPhones(id);
				String name = userService.getName(id);
				SendMessageUtil.send2(name, list);
			}
			else {
				map.put("status", 500);
				map.put("errmsg", "求救失败");
			}
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
			return map;
		}
		return map;
	}
	
	/*
	 * 结束求救
	 */
	@RequestMapping(value="", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> stopEmergency(@RequestParam(value="id")int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			if (emergencyService.stopEmergency(id)) map.put("status", 200);
			else {
				map.put("status", 500);
				map.put("ermsg", "请求失败，请重试");
			}
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
			return map;
		}
		return map;
	}
	
}
