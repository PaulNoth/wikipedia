package com.eriksuta;

import com.eriksuta.page.SearchPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 *  @author Erik Suta
 * */
public class TestHomePage{

	private WicketTester tester;

	@Before
	public void setUp(){
		tester = new WicketTester(new WicketApplication());
	}

	@Test
	public void homepageRendersSuccessfully(){
		//start and render the test page
		tester.startPage(SearchPage.class);

		//assert rendered page class
		tester.assertRenderedPage(SearchPage.class);
	}
}
