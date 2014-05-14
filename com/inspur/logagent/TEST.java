package com.inspur.logagent;

import java.util.ArrayList;
import java.util.List;

public class TEST {
public static void main(String[] args) {
	List<String> str1=new ArrayList<String>();
	str1.add("a");
	str1.add("3");
	
	List<String> str2=new ArrayList<String>();
	str2.add("a");
	str2.add("b");
	System.out.println(str2.equals(str1));
}
}
