package com.ehelp.dao;

import java.util.List;

import com.ehelp.entity.Answer;
import com.ehelp.entity.Question;
import com.ehelp.entity.QuestionResult;

public interface QuestionDao {

	// 获取问题列表
	public List<QuestionResult> getAllQuestions();
	
	// 根据问题id获取问题
	public List<QuestionResult> getQuestion(int id);
	
	//添加问题
	public boolean ask(Question q);
	
	//回答
	public boolean answer(Answer a);
	
}
