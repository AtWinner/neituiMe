package com.example.network;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class DoSend 
{
	public static void sendMail(String Content)
	{
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		// 邮箱用户名
		mailInfo.setUserName("hufanglin8866@163.com");
		// 邮箱密码
		mailInfo.setPassword("123456a?");
		// 发件人邮箱
		mailInfo.setFromAddress("hufanglin8866@163.com");
		// 收件人邮箱
		mailInfo.setToAddress("neituiMeGet@163.com");
		// 邮件标题
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		DateFormat d1 = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL); //默认语言（汉语）下的默认风格（MEDIUM风格，比如：2008-6-16 20:54:53）
		String strNow = d1.format(now);
		mailInfo.setSubject("型号："+android.os.Build.MODEL + " 时间：" + strNow);
		// 邮件内容
		mailInfo.setContent(Content);
		// 发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		// 发送文体格式
		SimpleMailSender.sendTextMail(mailInfo);
		// 发送html格式
		SimpleMailSender.sendHtmlMail(mailInfo);
	}
}
