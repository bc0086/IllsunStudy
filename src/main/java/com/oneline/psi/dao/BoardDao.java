package com.oneline.psi.dao;

import java.util.List;
import java.util.Map;

public interface BoardDao {

	List<Map<String, Object>> list(Map<String, Object> map);

	int writeAction(Map<String, Object> map);

	Map<String, Object> detail(int seq);

	int editAction(Map<String, Object> map);

	int viewCnt(int seq);

	int delete(Map<String, Object> map);

	int delete2(List<Integer> list);

	int totalCount(Map<String, Object> map);

	List<Map<String, Object>> excelDown(Map<String, Object> map);


	

}
