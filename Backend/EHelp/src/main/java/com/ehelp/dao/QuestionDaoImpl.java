package com.ehelp.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ehelp.entity.Answer;
import com.ehelp.entity.Contact;
import com.ehelp.entity.Question;
import com.ehelp.entity.QuestionResult;
import com.ehelp.util.DBSessionUtil;

@Repository
public class QuestionDaoImpl implements QuestionDao {
	
	//根据id获取问题
	public Question getQues(int id) {
		Session session = DBSessionUtil.getSession();
		Question q = (Question) session.get(Question.class, id);
		DBSessionUtil.closeSession(session);
		return q;
	}

	// 获取问题列表
	public List<Object[]> getAllQuestions() {
		List<Object[]> results = new ArrayList<Object[]>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select q.id, q.title, q.description, q.date, u.username, u.avatar, q.asker_id "
				+ "from Question q, User u where q.asker_id=u.id"); 
		// 查询结果
		results = query.list();
		
		for (Object[] o : results) {
			int question_id = (int) o[0];
			query = session.createQuery("select count(*) from Answer a where a.question_id=:question_id");
			query.setParameter("question_id", question_id);
			long num = (long) query.uniqueResult();
			o[6] = num;
		}
		
		//排序
		Collections.sort(results, new Comparator<Object[]>() {

			public int compare(Object[] o1, Object[] o2) {
				Date d1 = (Date) o1[3];
				Date d2 = (Date) o2[3];
				if (d1.after(d2)) return -1;
				else return 1;
			}
			
		});	
		
		DBSessionUtil.closeSession(session);
		if (results.size() <= 30) return results;
		//返回前30个问题
		List<Object[]> results2 = new ArrayList<Object[]>();
		for (int i = 0; i < 30; i++) {
			results2.add(results.get(i));
		}
		return results2;
	}

	// 根据问题id获取问题
	public List<QuestionResult> getQuestion(int id) {
		List<QuestionResult> results = new ArrayList<QuestionResult>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select new com.ehelp.entity.QuestionResult(u.username, u.avatar, a.description, a.date) "
				+ "from Question q, Answer a, User u where q.id=a.question_id and a.answerer_id=u.id and q.id=:id");
		query.setParameter("id", id);
		// 查询结果
		results = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return results;
	}

	//添加问题
	public boolean ask(Question q) {
		Session session = DBSessionUtil.getSession();
		session.save(q);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//回答
	public boolean answer(Answer a) {
		Session session = DBSessionUtil.getSession();
		session.save(a);
		DBSessionUtil.closeSession(session);
		return true;
	}

	public static void main(String[] args) {
		QuestionDaoImpl questionDaoImpl = new QuestionDaoImpl();
//		List<QuestionResult> results = new ArrayList<QuestionResult>();
//		results = questionDaoImpl.getAllQuestions();
////		results = questionDaoImpl.getQuestion(1);
//		for (QuestionResult q : results) {
//			System.out.println(q.toString());
//		}
		System.out.println(questionDaoImpl.getQues(25));
	}
	
}



