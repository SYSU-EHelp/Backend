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

import com.ehelp.entity.Answer;
import com.ehelp.entity.Question;
import com.ehelp.entity.QuestionResult;
import com.ehelp.service.QuestionServiceImpl;
import com.ehelp.util.EncodingUtil;


@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private QuestionServiceImpl questionService;
	
	/*
	 * 获取问题列表
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getQuestions(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		
		map.put("status", "200");
		List<QuestionResult> results = questionService.getAllQuestions();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (QuestionResult q : results) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", q.getId());
			m.put("title", q.getTitle());
			m.put("description", q.getAsk_description());
			m.put("date", sdf.format(q.getAsk_date()));
			m.put("asker_username", q.getAsker_username());
			m.put("asker_avatar", q.getAsker_avatar());
			data.add(m);
		}
		map.put("data", data);
		return map;
	}
	
	/*
	 * 查看问题详情
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> question(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		
		map.put("status", "200");
		List<QuestionResult> results = questionService.getQuestion(id);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (QuestionResult q : results) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("answerer_username", q.getAnswerer_username());
			m.put("answerer_avatar", q.getAnswerer_avatar());
			m.put("description", q.getAnswer_description());
			m.put("date", sdf.format(q.getAnswer_date()));
			data.add(m);
		}
		map.put("data", data);
		return map;
	}
	
	/*
	 * 提问
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ask(@RequestParam(value="title")String title, @RequestParam(value="description")String description,
			HttpSession session) throws ParseException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("errmsg", "请先登录");
			return map;
		}
		int asker_id = (Integer) session.getAttribute("user");
		Date date = sdf.parse(sdf.format(new Date()));
		Question q = new Question(title, description, asker_id, date);
		if (questionService.ask(q)) map.put("status", 200);
		else {
			map.put("status", 500);
			map.put("errmsg", "提问失败");
		}
		return map;
	}
	
	/*
	 * 回答
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> answer(@PathVariable("id") int id, @RequestParam(value="answer")String answer, HttpSession session) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("errmsg", "请先登录");
			return map;
		}
		int answerer_id = (Integer) session.getAttribute("user");
		Date date = sdf.parse(sdf.format(new Date()));
		Answer a = new Answer(id, answerer_id, answer, date);
		if (questionService.answer(a)) map.put("status", 200);
		else {
			map.put("status", 500);
			map.put("errmsg", "回答失败");
		}
		return map;
	}
	
}




