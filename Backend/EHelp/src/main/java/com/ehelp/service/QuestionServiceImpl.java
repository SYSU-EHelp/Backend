package com.ehelp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehelp.dao.QuestionDaoImpl;
import com.ehelp.entity.Answer;
import com.ehelp.entity.Question;
import com.ehelp.entity.QuestionResult;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionDaoImpl questionDao;
	
	// 获取问题列表
	public List<QuestionResult> getAllQuestions() {
		return questionDao.getAllQuestions();
	}

	// 根据问题id获取问题
	public List<QuestionResult> getQuestion(int id) {
		return questionDao.getQuestion(id);
	}

	//添加问题
	public boolean ask(Question q) {
		return questionDao.ask(q);
	}

	//回答
	public boolean answer(Answer a) {
		return questionDao.answer(a);
	}

}
