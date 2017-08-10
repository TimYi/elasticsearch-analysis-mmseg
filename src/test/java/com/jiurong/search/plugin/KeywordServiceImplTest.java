package com.jiurong.search.plugin;

import org.junit.Test;

public class KeywordServiceImplTest {

	private KeywordService keywordService = Container.getBean(KeywordService.class);

	@Test
	public void test() {
		keywordService.addKeyword("共享汽车");
	}

}
