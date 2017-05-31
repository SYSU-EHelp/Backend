package com.ehelp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehelp.entity.Help;
import com.ehelp.entity.Response;
import com.ehelp.service.HelpServiceImpl;
import com.ehelp.util.EncodingUtil;

@Controller
@RequestMapping("/helps")
public class HelpController {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private HelpServiceImpl helpService;
	
	/*
	 * 获取求助列表
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHelps(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			map.put("status", "200");
		 	List<Object[]> results = helpService.getAllHelps();
			for (Object[] o : results) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", o[0]);
				m.put("title", o[1]);
				m.put("description", o[2]);
				m.put("address", o[3]);
				m.put("finished", o[4]);
				m.put("date", sdf.format((Date)o[5]));
				m.put("launcher_username", o[6]);
				m.put("launcher_avatar", o[7]);
				m.put("phone", o[8]);
				m.put("longitude", o[9]);
				m.put("latitude", o[10]);
				data.add(m);
			}
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 查看求助详情
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHelp(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
		 	List<Object[]> results = helpService.getHelp(id);
		 	if (results.size() == 0) {
		 		map.put("status", 500);
				map.put("data", data);
				map.put("ermsg", "事件不存在，请重试");
		 	}
		 	else {
		 		map.put("status", "200");
				for (Object[] o : results) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("id", o[0]);
					m.put("title", o[1]);
					m.put("description", o[2]);
					m.put("address", o[3]);
					m.put("finished", o[4]);
					m.put("date", sdf.format((Date)o[5]));
					m.put("launcher_username", o[6]);
					m.put("launcher_avatar", o[7]);
					m.put("phone", o[8]);
					m.put("longitude", o[9]);
					m.put("latitude", o[10]);
					data.add(m);
				}
				map.put("data", data);
		 	}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 响应求助事件
	 */
	@RequestMapping(value="/{id}/responses", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> responseHelp(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			int user_id = (Integer) session.getAttribute("user");
			Response r = new Response(1, id, user_id);
			int status = helpService.responseHelp(r);
			if (status == 0) map.put("status", 200);
			else if (status == 1) {
				map.put("status", 500);
				map.put("errmsg", "求助事件已结束");
			}
			else if (status == 2) {
				map.put("status", 500);
				map.put("errmsg", "您上次响应的求助未结束");
			}
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 结束求助事件
	 */
	@RequestMapping(value="/{id}/finish", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> finishHelp(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			if (helpService.endHelp(id)) map.put("status", 200);
			else {
				map.put("status", 500);
				map.put("errmsg", "操作失败");
			}
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 发起求助
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> launchHelp(@RequestParam(value="title")String title, @RequestParam(value="description")String description,
			@RequestParam(value="address")String address, @RequestParam(value="longitude")double longitude, @RequestParam(value="latitude")double latitude, 
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			title = EncodingUtil.encodeStr(title);
			description = EncodingUtil.encodeStr(description);
			address = EncodingUtil.encodeStr(address);
			int id = (Integer) session.getAttribute("user");
			Date date = sdf.parse(sdf.format(new Date()));
			Help h = new Help(id, title, description, date, address, longitude, latitude, 0);
			int help_id = helpService.launchHelp(h);
			if (help_id >= 0) {
				map.put("status", 200);
				data.put("id", help_id);
			}
			else {
				map.put("status", 500);
				map.put("errmsg", "求助失败");
			}
			map.put("data", data);
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
   	
	/*
	 * 求助者查看响应详情
	 */
	@RequestMapping(value="/{id}/responses", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> response(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> responser = new ArrayList<Map<String,Object>>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			map.put("status", "200");
			List<Object[]> results = helpService.getAllResponse(id);
			for (Object[] o : results) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("responser_username", o[0]);
				m.put("responser_avatar", o[1]);
				m.put("phone", o[2]);
				responser.add(m);
			}
			data.put("responser", responser);
			int num = responser.size();
			data.put("num", num);
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
}




