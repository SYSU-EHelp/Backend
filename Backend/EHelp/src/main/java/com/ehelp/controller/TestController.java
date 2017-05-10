package com.ehelp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehelp.entity.User;
import com.ehelp.service.UserService;
import com.ehelp.util.SendMessageUtil;
import com.taobao.api.ApiException;

@Controller
public class TestController {

	private String CODE = null;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> test() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", "123456");
		return map;
	}
	
	
	@RequestMapping(value="/sendCode", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getCode(@RequestParam(value="phone")String phone, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ApiException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!userService.phoneExisted(phone)) {
			map.put("status", 500);
			map.put("errmsg", "用户已存在");
			System.out.println("用户已存在");
			return map;
		}
		//随机生成4位验证码
        String code = "";
        Random r = new Random(new Date().getTime());
        for (int i = 0; i < 4; i++) {
        	code = code+r.nextInt(10);
        }
        System.out.println("code :" + code);
        String result = SendMessageUtil.send(phone, code);
        CODE = code;
        if (!result.equals("success")) {
        	map.put("status", 500);
			map.put("errmsg", "短信发送失败");
			System.out.println("短信发送失败");
			return map;
        }
//        if (session.getAttribute("code") != null) session.removeAttribute("code");
//        session.setAttribute("code", code);
//        System.out.println("session: " + session.getAttribute("code"));
        map.put("status", 200);
		return map;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register(@RequestParam(value="code")String code, @RequestParam(value="phone")String phone,
			@RequestParam(value="username")String username, @RequestParam(value="password")String password,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		System.out.println(session.getAttribute("code"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (!code.equals(CODE)) {
			map.put("status", 500);
			map.put("errmsg", "验证码错误");
			System.out.println("验证码错误");
			return map;
		}
        if (userService.addUser(phone, username, password)) {
        	map.put("status", 200);
        	CODE = null;
        	System.out.println("------注册成功------");
        }
        else {
        	map.put("status", 500);
        	map.put("errmsg", "用户名已存在");
        	System.out.println("------注册失败------");
        }
        return map;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@RequestParam(value="username")String username, @RequestParam(value="password")String password,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User(username, password);
		if (userService.checkUser(user)) {
			map.put("status", 200);
			System.out.println("------登录成功------");
		}
		else {
			map.put("status", 500);
			map.put("errmsg", "用户名或密码错误");
			System.out.println("------登录失败------");
		}
		return map;
	}
	
}









