package com.jiurong.search.plugin;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KeywordMapper {

	int add(Keyword keyword);

	int addAll(List<Keyword> keywords);

	int exists(String keyword);

	List<Keyword> findAllWords();

	List<Keyword> findAllChars();
	
	Date latestUpdate();
	
	void updateIsNewWord();
	
	List<Keyword> findAllNewWords();
}
