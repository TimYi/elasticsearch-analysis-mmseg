package com.jiurong.search.plugin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.Dictionary;

@Service
public class KeywordServiceImpl implements KeywordService {
	Dictionary dic;

	@Override
	public Dictionary getDic() {
		return dic;
	}

	@Override
	public void setDic(Dictionary dic) {
		this.dic = dic;
	}

	java.util.Date maxCreateDate = null;
	@Autowired
	private KeywordMapper keywordMapper;

	@Override
	public void addChar(String character, Integer freq) {
		Keyword entity = Keyword.createChar(character, freq);
		saveKeyword(entity);
	}

	@Override
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

	@Override
	public void addKeywords(List<Keyword> keywords) {
		keywordMapper.addAll(keywords);
	}

	@Override
	public List<Keyword> getAllKeywords() {
		return keywordMapper.findAllWords();
	}

	@Override
	public List<Keyword> getAllChars() {
		return keywordMapper.findAllChars();
	}

	private void updateIsNewWord(Date maxCreateDate) {
		keywordMapper.updateIsNewWord(maxCreateDate);
	}

	private List<Keyword> getAllNewWords() {
		return keywordMapper.findAllNewWords();
	}

	private Date getLatestUpdate() {
		return keywordMapper.latestUpdate();
	}

	@Override
	@Scheduled(fixedDelay = 1000)
	public void syncNewWords() {
		java.util.Date utilDate = getLatestUpdate();
		if (utilDate != maxCreateDate) {
			List<Keyword> newWords = getAllNewWords();
			if (newWords != null) {
				maxCreateDate = utilDate;
				dic.addKeywordsToDic(newWords);
				updateIsNewWord(maxCreateDate);
			}
		}
	}

}
