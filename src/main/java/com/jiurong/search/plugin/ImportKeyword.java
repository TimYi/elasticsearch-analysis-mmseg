package com.jiurong.search.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ImportKeyword {

	private static void writeCharAndWordToMysql(String filePath, KeywordService keywordService) throws IOException {
		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = reader.readLine();
			List<Keyword> keywords = new ArrayList<>();
			while (tempString != null) {
				String[] w = tempString.split(" ");
				switch (w.length) {
				case 2: { // 代码快，防止变量污染其它case语句块
					Keyword keyword = Keyword.createChar(w[0], Integer.valueOf(w[1]));
					keywords.add(keyword);
					break;
				}

				case 1: {
					Keyword keyword = Keyword.createWord(w[0]);
					keywords.add(keyword);
					break;
				}
				}
				if(keywords.size()==10000) {
					keywordService.addKeywords(keywords);
					keywords.clear();
				}
				tempString = reader.readLine();
			}
			keywordService.addKeywords(keywords);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		KeywordServiceImpl keywordService = context.getBean(KeywordServiceImpl.class);
		String filePath = "";
		writeCharAndWordToMysql(filePath, keywordService);
		context.close();
	}
}
