package com.example.network;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail 
{
	private String Host = "smtp.163.com"; // smtp服务器
	private String UserName = "neituiMePost";//用户名
	private String Password = "123456a?";//发送邮箱的密码
	private String From = "neituiMePost@163.com"; // 发件人地址
	private String To = "neituiMeGet@163.com"; // 收件人地址 
	private String Subject = "UserInfo"; // 邮件标题
	/**
	 * 默认邮箱发送邮件
	 * @param Text 邮件内容
	 */
	public void sendMailController(String Text)
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", Host);// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
		props.put("mail.smtp.auth", "true");// 需要经过授权，也就是有户名和密码的校验
		Session session = Session.getDefaultInstance(props);
		
		session.setDebug(true);
		// 用session为参数定义消息对象
		MimeMessage message = new MimeMessage(session);
		try
		{
			message.setFrom(new InternetAddress(From));// 加载发件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(To));// 加载收件人地址
			message.setSubject(Subject);// 加载标题
			Multipart multipart = new MimeMultipart();// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(Text);
			multipart.addBodyPart(contentPart);
			
			
			message.setContent(multipart);// 将multipart对象放到message中
			  // 保存邮件
			   message.saveChanges();
			   // 发送邮件
			   Transport transport = session.getTransport("smtp");
			   // 连接服务器的邮箱
			   transport.connect(Host, UserName, Password);
			   // 把邮件发送出去
			   transport.sendMessage(message, message.getAllRecipients());
			   transport.close();
		}
		catch(Exception e)
		{
			 e.printStackTrace();
		}
	}
	public void sendMailController(String userName, String password)
	{
		
	}
}
