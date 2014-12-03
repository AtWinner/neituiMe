package com.example.network;

import android.util.Log;

public class DoSend 
{
	public static void sendMail()
	{
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		// 邮箱用户名
		mailInfo.setUserName("hufanglin8866@163.com");
		// 邮箱密码
		mailInfo.setPassword("coder?1:0");
		// 发件人邮箱
		mailInfo.setFromAddress("hufanglin8866@163.com");
		// 收件人邮箱
		mailInfo.setToAddress("hufanglin@inspur.com");
		// 邮件标题
		mailInfo.setSubject("测试Java程序发送邮件");
		// 邮件内容
		mailInfo.setContent("邮件发送成功了咩？");
		// 发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		// 发送文体格式
		SimpleMailSender.sendTextMail(mailInfo);
		// 发送html格式
		SimpleMailSender.sendHtmlMail(mailInfo);
	}
}
