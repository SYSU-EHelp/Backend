package com.ehelp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ehelp.entity.User;
import com.ehelp.service.UserServiceImpl;
import com.ehelp.util.SendMessageUtil;
import com.taobao.api.ApiException;

@Controller
@RequestMapping("/users")
public class UserController {
	
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
	public Map<String, Object> getCode(@RequestParam(value="phone")String phone, HttpSession session) throws ApiException {
		//随机生成4位验证码
        String code = "";
        Random r = new Random(new Date().getTime());
        for (int i = 0; i < 4; i++) {
        	code = code+r.nextInt(10);
        }
        session.setAttribute("code", code);
        
        Map<String, Object> map = new HashMap<String, Object>();
		if (userService.phoneExisted(phone)) {
			map.put("status", 500);
			map.put("errmsg", "该手机已被注册");
			System.out.println("该手机已被注册");
			return map;
		}
		
		//发送验证码
        String result = SendMessageUtil.send(phone, code);
        if (!result.equals("success")) {
        	map.put("status", 500);
			map.put("errmsg", "短信发送失败");
			System.out.println("短信发送失败");
			return map;
        }
        map.put("status", 200);
        map.put("code", code);
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
		String CODE = null;
		if (session.getAttribute("code") != null) 
			CODE = session.getAttribute("code").toString();
		System.out.println("code : " + CODE);
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (!code.equals(CODE)) {
			map.put("status", 500);
			map.put("errmsg", "验证码错误");
			System.out.println("验证码错误");
			return map;
		}
        if (userService.addUser(phone, username, password)) {
        	map.put("status", 200);
        	System.out.println("------注册成功------");
        }
        else {
        	map.put("status", 500);
        	map.put("errmsg", "注册失败");
        	System.out.println("------注册失败------");
        }
        return map;
	}
	
	/*
	 * 登录
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@RequestParam(value="username")String username, @RequestParam(value="password")String password,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User(username, password);
		int id = userService.checkUser(user);
		if (id != -1) {
			map.put("status", 200);
			map.put("id", id);
			session.setAttribute("username", user.getUsername());
			
			//添加cookie
			Cookie cookie = new Cookie("user", username+"+"+id);
			cookie.setMaxAge(60*60*24);
			response.addCookie(cookie);
			System.out.println("------登录成功------");
		}
		else {
			map.put("status", 500);
			map.put("errmsg", "用户名或密码错误");
			System.out.println("------登录失败------");
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
		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("user")) {
				String username = cookie.getValue().substring(0, cookie.getValue().indexOf('+'));
				String value = cookie.getValue().substring(cookie.getValue().indexOf('+')+1);
				int id =Integer.parseInt(value);
				
				session.setAttribute("username", username);
				//更新cookie
				Cookie cookie1 = new Cookie("user", username+"+"+id);
				cookie1.setMaxAge(60*60*24);
				response.addCookie(cookie1);
				map.put("id", id);
				map.put("status", 200);
			}
		}
		if (session.getAttribute("username") == null) {
			System.out.println("----没有cookie或cookie过期----");
			map.put("status", 500);
			map.put("errmsg", "自动登录失败，请重新登录");
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
		if (session.getAttribute("username") != null) session.removeAttribute("username");

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
		return map;
	}

	/*
	 * 查看紧急联系人
	 */
	
	/*
	 * 添加
	 */
}

