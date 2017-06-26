package com.ehelp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ehelp.entity.Contact;
import com.ehelp.entity.User;
import com.ehelp.service.UserServiceImpl;
import com.ehelp.util.EncodingUtil;
import com.ehelp.util.SendMessageUtil;
import com.taobao.api.ApiException;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private UserServiceImpl userService;
	
	/*
	 * test
	 */
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> test() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", "123456");
		return map;
	}
	
	/*
	 * 发送验证码
	 */
	@RequestMapping(value="/sendCode", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getCode(@RequestParam(value="phone")String phone, HttpSession session)  {
		//随机生成4位验证码
        String code = "";
        Random r = new Random(new Date().getTime());
        for (int i = 0; i < 4; i++) {
        	code = code+r.nextInt(10);
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
			if (userService.phoneExisted(phone, code)) {
				map.put("status", 500);
				map.put("data", data);
				map.put("errmsg", "该手机已被注册");
				System.out.println("该手机已被注册");
				return map;
			}

			//发送验证码
	        String result = SendMessageUtil.send(phone, code);
	        if (!result.equals("success")) {
	        	map.put("status", 500);
	        	map.put("data", data);
				map.put("errmsg", "短信发送失败");
				System.out.println("短信发送失败");
				return map;
	        }
	        if (userService.addCode(phone, code)) {
	        	System.out.println("验证码添加成功");
	        	map.put("status", 200);
	        	data.put("code", code);
	        	map.put("data", data);
	        }
	        return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("errmsg", "请求失败，请重试");
		}
        return map;
	}
	
	/*
	 * 注册
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register(@RequestParam(value="code")String code, @RequestParam(value="phone")String phone,
			@RequestParam(value="username")String username, @RequestParam(value="password")String password,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (username.equals("") || password.equals("")) {
			map.put("status", 500);
        	map.put("data", data);
        	map.put("errmsg", "用户名或密码不能为空");
        	System.out.println("------注册失败------");
        	return map;
		}
		
		try {
			int status = userService.addUser(phone, username, password, code);
			if (status == 0) {
	        	map.put("status", 500);
	        	map.put("data", data);
	        	map.put("errmsg", "用户名已存在");
	        	System.out.println("------注册失败------");
	        	return map;
	        }
	        if (status == 1) {
	        	map.put("status", 200);
	        	map.put("data", data);
	        	System.out.println("------注册成功------");
	        	return map;
	        }
	        else if (status == 3) {
	        	map.put("status", 500);
	        	map.put("data", data);
	        	map.put("errmsg", "验证码错误");
	        	System.out.println("------注册失败------");
	        	return map;
	        }
	        else {
	        	map.put("status", 500);
	        	map.put("data", data);
	        	map.put("errmsg", "手机号已被注册");
	        	System.out.println("------注册失败------");
	        	return map;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("errmsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 登录
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@RequestParam(value="username")String username, @RequestParam(value="password")String password,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			System.out.println(username + " " + password);
			User user = new User(username, password);
			int id = userService.checkUser(user);
			if (id != -1) {
				map.put("status", 200);
				data.put("id", id);
				map.put("data", data);
				session.setAttribute("user", id);
				
				//添加cookie
				Cookie cookie = new Cookie("user", username+"+"+id);
				cookie.setMaxAge(60*60*24);
				response.addCookie(cookie);
				System.out.println("------登录成功------");
			}
			else {
				map.put("status", 500);
				map.put("data", data);
				map.put("errmsg", "用户名或密码错误");
				System.out.println("------登录失败------");
			}
		} catch (Exception e) {
			e.printStackTrace();
			data = new HashMap<String, Object>();
			map.put("status", 500);
			map.put("data", data);
			map.put("errmsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 自动登录
	 */
	@RequestMapping(value="/autologin", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> autologin(HttpServletRequest request,  HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					String username = cookie.getValue().substring(0, cookie.getValue().indexOf('+'));
					String value = cookie.getValue().substring(cookie.getValue().indexOf('+')+1);
					int id =Integer.parseInt(value);
					
					session.setAttribute("user", id);
					//更新cookie
					Cookie cookie1 = new Cookie("user", username+"+"+id);
					cookie1.setMaxAge(60*60*24);
					response.addCookie(cookie1);
					data.put("id", id);
					map.put("data", data);
					map.put("status", 200);
					break;
				}
			}
			if (session.getAttribute("user") == null) {
				System.out.println("----没有cookie或cookie过期----");
				map.put("status", 500);
				map.put("data", data);
				map.put("errmsg", "自动登录失败，请重新登录");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("errmsg", "请求失败");
		}
		return map;
	}
	
	/*
	 * 退出登录
	 */
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			if (session.getAttribute("user") != null) session.removeAttribute("user");
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					//删除cookie
					Cookie cookie1 = new Cookie("user", "");
					cookie1.setMaxAge(0);
					response.addCookie(cookie1);
				}
			}
			map.put("status", 200);
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("errmsg", "请求失败，请重试");
		}
		return map;
	}

	/*
	 * 查看紧急联系人
	 */
	@RequestMapping(value="/contacts", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getContacts(HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("errmsg", "请先登录");
			map.put("data", data);
			return map;
		}
		try {
			int id = (Integer) session.getAttribute("user");
			List<Contact> contacts = userService.getContacts(id);
			for (Contact c : contacts) {
				Map<String, String> m = new HashMap<String, String>();
				String u = c.getContact_user();
				String p = c.getContact_phone();
				m.put("username", u);
				m.put("phone", p);
				data.add(m);
			}
			map.put("status", "200");
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("errmsg", "请求失败，请重试");
			map.put("data", data);
		}
		return map;
	}
	
	/*
	 * 添加紧急联系人
	 */
	@RequestMapping(value="/contacts", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addContact(@RequestParam(value="username")String username, @RequestParam(value="phone")String phone,
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
			username = EncodingUtil.encodeStr(username);
			int id = (Integer) session.getAttribute("user");
			Contact contact = new Contact(id, username, phone);
			int status = userService.addContact(contact);
			if (status == 0) map.put("status", 200);
			else if (status == 1) {
				map.put("status", 500);
				map.put("errmsg", "联系人数量太多了，请先删除部分联系人");
			}
			else if (status == 2) {
				map.put("status", 500);
				map.put("errmsg", "联系人已存在");
			}
			else {
				map.put("status", 500);
				map.put("errmsg", "添加失败");
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
	 * 删除紧急联系人
	 */
	@RequestMapping(value="/contacts", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> deleteContact(@RequestParam(value="username")String username, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			username = EncodingUtil.encodeStr(username);
			int id = (Integer) session.getAttribute("user");
			if (userService.deleteContact(id, username)) map.put("status", 200);
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
	 * 我的模块
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addContact(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			//我发起的
			List<Map<String, Object>> launch = new ArrayList<Map<String,Object>>();
			List<Object[]> results = userService.getLaunch(id);
			for (Object[] o : results) {
				//提问
				if ((Integer)o[0] == 0) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("type", o[0]);
					m.put("id", o[1]);
					m.put("title", o[2]);
					m.put("date", sdf.format(o[3]));
					m.put("num", o[4]);
					m.put("finished", 2);
					launch.add(m);
				}
				//求助
				else if ((Integer)o[0] == 1) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("type", o[0]);
					m.put("id", o[1]);
					m.put("title", o[2]);
					m.put("date", sdf.format(o[3]));
					m.put("finished", o[4]);
					m.put("num", o[5]);
					launch.add(m);
				}
				//求救
				else {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("type", o[0]);
					m.put("id", o[1]);
					m.put("date", sdf.format(o[3]));
					m.put("title", "");
					m.put("finished", 2);
					m.put("num", 0);
					launch.add(m);
				}
			}
			
			//我响应的
			List<Map<String, Object>> response = new ArrayList<Map<String,Object>>();
			List<Object[]> results2 = userService.getResponse(id);
			for (Object[] o : results2) {
				//提问
				if ((Integer)o[0] == 0) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("type", o[0]);
					m.put("id", o[1]);
					m.put("title", o[2]);
					m.put("launcher_username", o[3]);
					m.put("num", o[4]);
					m.put("finished", 2);
					response.add(m);
				}
				else if ((Integer)o[0] == 1) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("type", o[0]);
					m.put("id", o[1]);
					m.put("title", o[2]);
					m.put("launcher_username", o[3]);
					m.put("finished", o[4]);
					m.put("num", 0);
					response.add(m);
				}
			}
			
			data.put("launch", launch);
			data.put("response", response);
			map.put("data", data);
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
	/*
	 * 我的信息
	 */
	@RequestMapping(value="/{id}/information", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInformation(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			User u = userService.getUser(id);
			if (u == null) {
				map.put("status", 500);
				map.put("data", data);
				map.put("ermsg", "用户不存在");
				return map;
			}
			data.put("username", u.getUsername());
			data.put("phone", u.getPhone());
			map.put("data", data);
			map.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 500);
			map.put("data", data);
			map.put("ermsg", "请求失败，请重试");
		}
		return map;
	}
	
}





