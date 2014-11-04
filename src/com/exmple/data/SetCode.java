package com.exmple.data;

import java.util.Random;

public class SetCode {
	public static String weChatCode()
	{
		String code = "00dcbefa64e88fe3ebc4d0df04779fb8";
		Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < code.length(); i++) {     
	        int number = random.nextInt(code.length());     
	        sb.append(code.charAt(number));     
	    }     
		
		return sb.toString();
	}
}
