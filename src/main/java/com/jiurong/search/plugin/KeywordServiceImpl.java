package com.jiurong.search.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordServiceImpl implements KeywordService {

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

}
