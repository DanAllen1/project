package until;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import common.ServerResponse;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

import pojo.Customer;
import pojo.Email;

/*发邮件
 * 
 * 
 * */
public class EmailUntil {
	
	//用于记录第几位顾客
	int customerNum = 0;
	
	//获取邮箱验证码
	public void emailPost(String receiveEmail, int checkNum) throws MessagingException {
		// 创建Properties 类用于记录邮箱的一些属性
	    Properties props = new Properties();
	    // 表示SMTP发送邮件，必须进行身份验证
	    props.put("mail.smtp.auth", "true");
	    //此处填写SMTP服务器
	    props.put("mail.smtp.host", "smtp.qq.com");
	    //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
	    props.put("mail.smtp.port", "587");
	    // 此处填写你的账号
	    props.put("mail.user", "2284420486@qq.com");
	    // 此处的密码就是前面说的16位STMP口令
	    props.put("mail.password", "jlipcinvkkxyebej");
	    // 构建授权信息，用于进行SMTP进行身份验证
	    Authenticator authenticator = new Authenticator() {
	
	        protected PasswordAuthentication getPasswordAuthentication() {
	            // 用户名、密码
	            String userName = props.getProperty("mail.user");
	            String password = props.getProperty("mail.password");
	            return new PasswordAuthentication(userName, password);
	        }
	    };
	    // 使用环境属性和授权信息，创建邮件会话
	    Session mailSession = Session.getInstance(props, authenticator);
	    // 创建邮件消息
	    MimeMessage message = new MimeMessage(mailSession);
	    // 设置发件人
	    InternetAddress form = new InternetAddress(
	            props.getProperty("mail.user"));
	    message.setFrom(form);
	
	    // 设置收件人的邮箱
	    InternetAddress to = new InternetAddress(receiveEmail);
	    message.setRecipient(RecipientType.TO, to);
	
	    // 设置邮件标题
	    message.setSubject("你的验证码");
	
	    // 设置邮件的内容体
	    message.setContent("你找回账号的验证码为: "+checkNum, "text/html;charset=UTF-8");
	
	    // 最后当然就是发送邮件啦
	    Transport.send(message);	
		}

	//群发邮件
	public void emailPost(List<String> receiveEmailList) throws MessagingException {
		// 创建Properties 类用于记录邮箱的一些属性
		Properties props = new Properties();
		// 表示SMTP发送邮件，必须进行身份验证
		props.put("mail.smtp.auth", "true");
		//此处填写SMTP服务器
		props.put("mail.smtp.host", "smtp.qq.com");
		//端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
		props.put("mail.smtp.port", "587");
		// 此处填写你的账号
		props.put("mail.user", "2284420486@qq.com");
		// 此处的密码就是前面说的16位STMP口令
		props.put("mail.password", "jlipcinvkkxyebej");
		// 构建授权信息，用于进行SMTP进行身份验证
		Authenticator authenticator = new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		// 设置发件人
		InternetAddress form = new InternetAddress(
				props.getProperty("mail.user"));
		message.setFrom(form);

		// 设置收件人的邮箱,群发
		Address[] addressList = new Address[receiveEmailList.size()];
		for (int i = 0;i<receiveEmailList.size();i++){
			addressList[i] = new InternetAddress(receiveEmailList.get(i));
		}
		message.setRecipients(RecipientType.TO,addressList);

		// 设置邮件标题
		message.setSubject("有新的产品或文章发布了");

		// 设置邮件的内容体
		message.setContent("demo", "text/html;charset=UTF-8");

		// 最后当然就是发送邮件啦
		Transport.send(message);
	}

    //发邮件(万能方法)
    public ServerResponse emailPost(Email email) throws MessagingException {
		//先判断标题和内容是否为空
		if (email.getSubject() == null || email.getContent() ==null){
			return ServerResponse.createByErrorMessage("标题和内容不能为空");
		}
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.qq.com");
        //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
        props.put("mail.smtp.port", "587");
        // 此处填写你的账号
        props.put("mail.user", "2284420486@qq.com");
        // 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", "jlipcinvkkxyebej");
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		// 设置邮件标题
		message.setSubject(email.getSubject());
		// 设置邮件的内容体
		message.setContent(email.getContent(), "text/html;charset=UTF-8");
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);
        //判断收件人是多个还是一个
		if (email.getRecipient() != null){
			//收件人地址
			Address addressReceived = new InternetAddress(email.getRecipient());
			//设置发件人地址
			message.setRecipient(RecipientType.TO,addressReceived);
			//发送邮件
			try {
				Transport.send(message);
				return ServerResponse.createBySuccessMessage("发送成功：一个接收者)");
			} catch (SendFailedException e) {
				e.printStackTrace();
			}
			return ServerResponse.createByErrorMessage("邮箱地址无效");
		}
		//收件人为多个时
		else if (email.getRecipients() != null){
			//获取收件人的邮箱地址
			List<String> recipientList = email.getRecipients();
			//创建一个收件人的数组，数组长度和recipientList的长度一样
			Address[] addressList = new Address[recipientList.size()];
			for (int i = 0;i<recipientList.size();i++){
				addressList[i] = new InternetAddress(recipientList.get(i));
			}
			//设置收件人地址
			message.setRecipients(RecipientType.TO,addressList);
			// 最后当然就是发送邮件啦
			try {
				Transport.send(message);
				return ServerResponse.createBySuccessMessage("发送成功:发送给多个人");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			ServerResponse.createByErrorMessage("邮箱地址无效");
		}else {
			return ServerResponse.createByErrorMessage("发送失败 没有接收者无法发送");
		}
		return ServerResponse.createByErrorMessage("发送失败 未知错误");
    }

	//把顾客信息通过邮件发送到管理员邮箱
	public void sendCustomerInfo(Customer customer) throws MessagingException {
		// 创建Properties 类用于记录邮箱的一些属性
	    Properties props = new Properties();
	    // 表示SMTP发送邮件，必须进行身份验证
	    props.put("mail.smtp.auth", "true");
	    //此处填写SMTP服务器
	    props.put("mail.smtp.host", "smtp.qq.com");
	    //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
	    props.put("mail.smtp.port", "587");
	    // 此处填写你的账号
	    props.put("mail.user", "2284420486@qq.com");
	    // 此处的密码就是前面说的16位STMP口令
	    props.put("mail.password", "jlipcinvkkxyebej");
	    // 构建授权信息，用于进行SMTP进行身份验证
	    Authenticator authenticator = new Authenticator() {
	
	        protected PasswordAuthentication getPasswordAuthentication() {
	            // 用户名、密码
	            String userName = props.getProperty("mail.user");
	            String password = props.getProperty("mail.password");
	            return new PasswordAuthentication(userName, password);
	        }
	    };
	    // 使用环境属性和授权信息，创建邮件会话
	    Session mailSession = Session.getInstance(props, authenticator);
	    // 创建邮件消息
	    MimeMessage message = new MimeMessage(mailSession);
	    // 设置发件人
	    InternetAddress form = new InternetAddress(
	            props.getProperty("mail.user"));
	    message.setFrom(form);
	
	    // 设置收件人的邮箱
	    InternetAddress to = new InternetAddress("lang@forleaves.com");
	    message.setRecipient(RecipientType.TO, to);
	
	    // 设置邮件标题
	    customerNum++;
	    message.setSubject("You have a new inquiry from "+customer.getName());
	    
	    // 设置邮件的内容体
	    String emailContent = "To: For Leaves Ltd,<br>" + 
	    					  "Dear Mr.Lang,<br>"+
	    					  "Message Content:"+customer.getMessage()+"<br><br><br><br><br>"+
	    					  "Name:"+customer.getName()+"<br>"+
	    					  "Company:"+customer.getEmail()+"<br>"+
	    					  "Email:"+customer.getCompany()+"<br>";
	    					  
	    message.setContent(emailContent, "text/html;charset=UTF-8");
	
	    // 最后当然就是发送邮件啦
	    Transport.send(message);	
		}
}

