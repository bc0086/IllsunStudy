package com.oneline.psi.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oneline.psi.dao.BoardDao;
import com.oneline.psi.service.BoardService;

@Service("service")
public class BoardServiceImpl implements BoardService {

	@Resource(name  ="dao")
	private BoardDao boardDao;
	
	
	@Override
	public List<Map<String, Object>> list(Map<String, Object> map) {
		return boardDao.list(map);
	}


	@Override
	public int writeAction(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return boardDao.writeAction(map);
	}


	@Override
	public Map<String, Object> detail(int seq) {
		// TODO Auto-generated method stub
		return boardDao.detail(seq);
	}


	@Override
	public int editAction(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return boardDao.editAction(map);
	}


	@Override
	public int viewCnt(int seq) {
		// TODO Auto-generated method stub
		return boardDao.viewCnt(seq);
	}


	@Override
	public int delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return boardDao.delete(map);
	}


	@Override
	public int delete2(List<Integer> list) {
		// TODO Auto-generated method stub
		return boardDao.delete2(list);
	}


	@Override
	public int totalCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return boardDao.totalCount(map);
	}


	@Override
	public List<Map<String, Object>> excelDown(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return boardDao.excelDown(map);
	}











}
