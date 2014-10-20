package com.example.network;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

/**
 * 筛选需要的控件信息
 * @author Wen
 *
 */

public class DocumentsSelect {
	private String Html;
	private Document myDoc;//从MainActivity传来的Doc
	private HashMap<String, String> map;
	/**
	 * 此方法一定要在子线程中使用
	 * @param Url
	 */
	public DocumentsSelect(String Url)
	{
		GetHtml GH = new GetHtml();
		Html = GH.GetHtmlByUrl(Url);
	}
	
	public DocumentsSelect(Document Doc)
	{
		myDoc = Doc;
		map = new HashMap<String, String>();
	}
	public HashMap<String, String> GetList()
	{
		Elements elements = new Elements();
		if(myDoc != null)
		{
			elements = myDoc.getElementsByClass("detail-article-content");
			Element el = elements.first();
//			elements.clear();
			elements = myDoc.getElementsByClass("detail-infos-item");
			String JobTitle = "";
			for(Element ee : elements)
			{
				JobTitle += ee.text()+"<br>";
			}
			map.put("JobInfo", JobTitle.substring(0, JobTitle.length()-4));
			elements = myDoc.getElementsByClass("company-tag");
			int ComanyTag = 1;
			for(Element ee : elements)
			{
				map.put("CompanyTag" + ComanyTag, ee.text());
				ComanyTag++;
			}
			elements = myDoc.getElementsByClass("cont");
			Element h3 = elements.first().getElementsByTag("h3").first();
			Element pp = elements.first().getElementsByTag("p").first();
			map.put("CompanyCont", h3.text());
			map.put("CompanyDetail", pp.text());
			elements = myDoc.getElementsByClass("detail-company");
			elements = elements.first().getElementsByTag("a");
			map.put("CompanyHref", "http://www.neitui.me" + elements.first().attr("href"));
			elements = elements.first().getElementsByClass("img");
			elements = elements.first().getElementsByTag("img");
			map.put("CompanyImageSrc", elements.attr("src"));
			elements = myDoc.getElementsByClass("detail-hoster");
			elements = elements.first().getElementsByTag("span");
			String inputStr = elements.text();
			StringBuilder sb = new StringBuilder(inputStr);
			int index = inputStr.indexOf(")");
			sb.insert(index + 2, "\n");
			inputStr = sb.toString();
			map.put("RealName", inputStr);
		}
		return map;
	}
	public HashMap<String, String> GetText()
	{
		HashMap<String, String> myMap = new HashMap<String, String>();
		if(myDoc != null)
		{
			Elements elements = new Elements();
			elements = myDoc.getElementsByClass("detail-article-content");
			Element el = elements.first();
			myMap = CutString(el.getElementsByTag("p").toString());
		}
		return myMap;
	}
	public HashMap<String, String> GetImage()
	{
		HashMap<String, String> myMap = new HashMap<String, String>();
		if(myDoc != null)
		{
			Elements elements = new Elements();
			elements = myDoc.getElementsByClass("detail-article-content").first().getElementsByTag("p").first().getElementsByTag("a");
			if(elements != null)
			{
				for(Element ele : elements)
				{
					myMap.put("ImageInfo"+ImageNum, ele.attr("href").toString());
					ImageNum++;
				}
				
			}
			else
				map.put("ImageInfo", ""); 
		}
		return myMap;
	}
	private int TextNum = 1;
	private int ImageNum = 1;
	
	public HashMap<String, String> CutString(String str)
	{
		HashMap<String, String> myMap = new HashMap<String, String>();
		String StrLeft = str;
		String StrRight = "";
		if(str.indexOf("<a") > 0)
		{
			StrLeft = str.substring(0, str.indexOf("<a")) + "</p>";
			StrRight = "<p>" + str.substring(str.indexOf("</a>") + 4, str.length());
			myMap.put("Text"+TextNum, StrLeft);
			Log.e("Text", TextNum+"");
			TextNum++;
			//map.put("Image"+ImageNum, "");
			myMap.putAll(CutString(StrRight));
			
		}
		else
		{
			myMap.put("Text"+TextNum, StrLeft);
			TextNum++;
		}
		return myMap;
	}
	
	public HashMap<String, String> GetCompanyDetailActivityInfo()
	{
		Elements elements = myDoc.getElementsByTag("h2");
		map.put("CompanyName", elements.first().text());
		elements = myDoc.getElementsByClass("company-item");
		map.put("Area", elements.get(0).text());
		map.put("Scale", elements.get(1).text());
		map.put("Financing", elements.get(2).text());
		elements = myDoc.getElementsByClass("company-tag");
		int i=0;
		for(Element ele : elements)
		{
			map.put("CompanyTag"+i, ele.text());
			i++;
		}
		return map;
	}
	
}
