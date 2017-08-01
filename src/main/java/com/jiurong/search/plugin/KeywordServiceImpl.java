package com.jiurong.search.plugin;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.Dictionary;

@Service
public class KeywordServiceImpl implements KeywordService {
	Dictionary dic;
	public Dictionary getDic() {
		return dic;
	}

	public void setDic(Dictionary dic) {
		this.dic = dic;
	}

	java.util.Date maxCreateDate = null;
	@Autowired
	private KeywordMapper keywordMapper;

	public void addChar(String character, Integer freq) {
		Keyword entity = Keyword.createChar(character, freq);
		saveKeyword(entity);
	}

	public void addKeyword(String keyword) {
		Keyword entity = Keyword.createWord(keyword);
		saveKeyword(entity);
	}

	private boolean exists(String keyword) {
		return keywordMapper.exists(keyword) > 0;
	}

	private void saveKeyword(Keyword keyword) {
		if (exists(keyword.getText())) {
			return;
		}
		keywordMapper.add(keyword);
	}

	public void addKeywords(List<Keyword> keywords) {
		keywordMapper.addAll(keywords);
	}

	public List<Keyword> getAllKeywords() {
		return keywordMapper.findAllWords();
	}

	public List<Keyword> getAllChars() {
		return keywordMapper.findAllChars();
	}
	
	private void updateIsNewWord() {
		keywordMapper.updateIsNewWord();
	}
	
	private List<Keyword> getAllNewWords() {
		return keywordMapper.findAllNewWords();
	}
	
	private Date getLatestUpdate() {
		return keywordMapper.latestUpdate();
	}
	
    @Scheduled(fixedDelay = 1000)   
    public void work() {
    	java.util.Date utilDate = getLatestUpdate();
    	if(utilDate!=maxCreateDate) {
    		List<Keyword> newWords = getAllNewWords();
    		if(newWords!=null) {
    			maxCreateDate = utilDate;
        		dic.addKeywordsToDic(newWords);
        		updateIsNewWord();
    		}
    		
    	}
    }   
    
    public static void main(String[] args) {
    	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	KeywordServiceImpl keywordService = context.getBean(KeywordServiceImpl.class);
    	System.out.println(keywordService.exists("共享单车"));
    	
    	keywordService.work();
    }

}
