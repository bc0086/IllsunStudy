package com.oneline.psi.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oneline.psi.dao.BoardDao;

@Repository("dao")
public class BoardDaoImpl implements BoardDao {
	
	@Autowired
	private  SqlSessionTemplate sqlSession;

	@Override
	public List<Map<String, Object>> list(Map<String, Object> map) {
		return sqlSession.selectList("mapper.list", map);
	}

	@Override
	public int writeAction(Map<String, Object> map) {
		return sqlSession.insert("mapper.writeAction", map);
	}

	@Override
	public Map<String, Object> detail(int seq) {
		return sqlSession.selectOne("mapper.detail", seq);
	}

	@Override
	public int editAction(Map<String, Object> map) {
		return sqlSession.update("mapper.editAction", map);
	}

	@Override
	public int viewCnt(int seq) {
		return sqlSession.update("mapper.viewCnt", seq);
	}

	@Override
	public int delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.delete("mapper.delete", map);
	}

	@Override
	public int delete2(List<Integer> list) {
		// TODO Auto-generated method stub
		return sqlSession.delete("mapper.delete2", list);
	}

	@Override
	public int totalCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("mapper.total", map);
	}

	


}
