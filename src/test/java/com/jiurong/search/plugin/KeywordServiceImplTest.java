package com.jiurong.search.plugin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class KeywordServiceImplTest {

	@Autowired
	private KeywordService keywordService;

	@Test
	public void test() {
		keywordService.addKeyword("共享汽车");
	}

}
