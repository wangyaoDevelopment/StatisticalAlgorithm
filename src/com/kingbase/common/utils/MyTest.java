package com.kingbase.common.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MyTest {
	
	@Test
	public void test(){
		Set<String> set = new HashSet<String>();
		set.add("1");
		set.add("1");
		set.add("1");
		set.add("1");
		System.out.println(set.size());
	}

}
