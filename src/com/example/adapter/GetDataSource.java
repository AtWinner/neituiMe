package com.example.adapter;

public class GetDataSource {
	public static String GetCity(long SpinnerNum)
	{
		int num = (int)SpinnerNum;
		String city = "";
		switch (num) {
		case 0:
			city = "";
			break;
		case 1:
			city = "厦门";
			break;
		case 2:
			city = "成都";
			break;
		case 3:
			city = "南京";
			break;
		case 4:
			city = "武汉";
			break;
		case 5:
			city = "深圳";
			break;
		case 6:
			city = "杭州";
			break;
		case 7:
			city = "广州";
			break;
		case 8:
			city = "上海";
			break;
		case 9:
			city = "北京";
			break;
		
		}
		
		return city;
	}

}
