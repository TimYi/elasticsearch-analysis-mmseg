package com.jiurong.search.plugin;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.plugin.analysis.mmseg.AnalysisMMsegPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.Dictionary;

@Service
public class KeywordServiceImpl implements KeywordService {

	private static final Logger log = ESLoggerFactory.getLogger(AnalysisMMsegPlugin.class.getName());

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
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			@Override
			public Boolean run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				return keywordMapper.exists(keyword) > 0;
			}
		});
	}

	private void saveKeyword(Keyword keyword) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				if (exists(keyword.getText())) {
					return null;
				}
				keywordMapper.add(keyword);
				return null;
			}
		});
	}

	@Override
	public void addKeywords(List<Keyword> keywords) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				keywordMapper.addAll(keywords);
				return null;
			}
		});
	}

	@Override
	public List<Keyword> getAllKeywords() {
		return AccessController.doPrivileged(new PrivilegedAction<List<Keyword>>() {
			@Override
			public List<Keyword> run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				return keywordMapper.findAllWords();
			}
		});
	}

	@Override
	public List<Keyword> getAllChars() {
		return AccessController.doPrivileged(new PrivilegedAction<List<Keyword>>() {
			@Override
			public List<Keyword> run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				return keywordMapper.findAllChars();
			}
		});
	}

	private void updateIsNewWord(Date maxCreateDate) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				keywordMapper.updateIsNewWord(maxCreateDate);
				return null;
			}
		});
	}

	private List<Keyword> getAllNewWords() {
		return AccessController.doPrivileged(new PrivilegedAction<List<Keyword>>() {
			@Override
			public List<Keyword> run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				return keywordMapper.findAllNewWords();
			}
		});
	}

	private Date getLatestUpdate() {
		return AccessController.doPrivileged(new PrivilegedAction<Date>() {
			@Override
			public Date run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				return keywordMapper.latestUpdate();
			}
		});
	}

	@Override
	@Scheduled(fixedDelay = 1000)
	public void syncNewWords() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Thread.currentThread().setContextClassLoader(AnalysisMMsegPlugin.class.getClassLoader());
				if (dic == null) {
					return null;
				}
				try {
					java.util.Date utilDate = getLatestUpdate();
					if (utilDate != maxCreateDate) {
						List<Keyword> newWords = getAllNewWords();
						if (newWords != null) {
							maxCreateDate = utilDate;
							dic.addKeywordsToDic(newWords);
							updateIsNewWord(maxCreateDate);
						}
					}
				} catch (Exception e) { // fixedDelay如果有异常，会终止整个调度，因此异常必须捕获并记录日志
					log.error(e.getMessage(), e, new Object[0]);
				}
				return null;
			}
		});
	}

}
