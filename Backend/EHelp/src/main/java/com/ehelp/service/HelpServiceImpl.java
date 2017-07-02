package com.ehelp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ehelp.dao.HelpDaoImpl;
import com.ehelp.entity.Help;
import com.ehelp.entity.Response;

@Service
public class HelpServiceImpl implements HelpService {

	@Autowired
	private HelpDaoImpl helpDao;

	@Cacheable("allHelps")
	public List<Object[]> getAllHelps() {
		return helpDao.getAllHelps();
	}

	public List<Object[]> getHelp(int id) {
		return helpDao.getHelp(id);
	}

	public int responseHelp(Response r) {
		return helpDao.responseHelp(r);
	}

	public boolean endHelp(int id) {
		return helpDao.endHelp(id);
	}

	public int launchHelp(Help p) {
		return helpDao.launchHelp(p);
	}

	public List<Object[]> getAllResponse(int id) {
		return helpDao.getAllResponse(id);
	}

	public int getHelpStatus(int id) {
		return helpDao.getHelpStatus(id);
	}
	
	
}
