package com.jiurong.search.plugin;

import java.util.List;

import com.chenlb.mmseg4j.Dictionary;

//TODO 研究https://github.com/medcl/elasticsearch-analysis-mmseg项目，确认何种关键词存储形式合适。
//提示：简要理解lucene全文检索的关键概念：分词，倒排序索引。
//研究mmseg原本如何config/mmseg目录下的分词配置，从而确定如果将相同的分词概念映射到数据库。
public interface KeywordService {

	public void addChar(String character, Integer freq);

	public void addKeyword(String keyword);

	public void addKeywords(List<Keyword> keywords);

	public List<Keyword> getAllKeywords();

	public List<Keyword> getAllChars();

	public void syncNewWords();

	public Dictionary getDic();

	public void setDic(Dictionary dic);

}
