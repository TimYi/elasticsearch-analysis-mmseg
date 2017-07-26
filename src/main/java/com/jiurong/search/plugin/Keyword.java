package com.jiurong.search.plugin;

import java.util.Date;

public class Keyword {

	private String text;
	private boolean isChar;
	private Integer freq;
	private Date createTime;

	private Keyword() {

	}

	public static Keyword createChar(String character, int freq) {
		Keyword keyword = new Keyword();
		keyword.setText(character);
		keyword.setFreq(freq);
		keyword.setChar(true);
		return keyword;
	}

	public static Keyword createWord(String word) {
		Keyword keyword = new Keyword();
		keyword.setText(word);
		keyword.setChar(false);
		return keyword;
	}

	public Integer getFreq() {
		return freq;
	}

	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	private String domain;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChar() {
		return isChar;
	}

	public void setChar(boolean isChar) {
		this.isChar = isChar;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
