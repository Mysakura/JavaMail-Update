package com.demo;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * 关于授权码：https://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256
 * 个人邮箱
 */
public class SendEmailPersonalUtils {

    private static String account;	//登录用户名
    private static String pass;		//登录密码
    private static String host;		//服务器地址（邮件服务器）
    private static String port;		//端口
    private static String protocol; //协议

    static{
        Properties prop = new Properties();
//		InputStream instream = ClassLoader.getSystemResourceAsStream("PersonalEmail.properties");//测试环境
        try {
//			prop.load(instream);//测试环境
            prop = PropertiesLoaderUtils.loadAllProperties("PersonalEmail.properties");//生产环境
        } catch (IOException e) {
            System.out.println("加载属性文件失败");
        }
        account = prop.getProperty("e.account");
        // 个人邮箱需要授权码，而不是密码。在密码的位置上填写授权码
        pass = prop.getProperty("e.authorizationCode");
        host = prop.getProperty("e.host");
        port = prop.getProperty("e.port");
        protocol = prop.getProperty("e.protocol");
    }

    static class MyAuthenticator extends Authenticator {
        String u = null;
        String p = null;

        private MyAuthenticator(String u,String p){
            this.u=u;
            this.p=p;
        }
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u,p);
        }
    }

    private String to;	//收件人
    private String subject;	//主题
    private String content;	//内容
    private String fileStr;	//附件路径

    public SendEmailPersonalUtils(String to, String subject, String content, String fileStr) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.fileStr = fileStr;
    }

    public void send(){
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", protocol);
        //服务器
        prop.setProperty("mail.smtp.host", host);
        //端口
        prop.setProperty("mail.smtp.port", port);
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(prop, new SendEmailPersonalUtils.MyAuthenticator(account, pass));
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            //发件人
            mimeMessage.setFrom(new InternetAddress(account,"XXX"));		//可以设置发件人的别名
            //mimeMessage.setFrom(new InternetAddress(account));	//如果不需要就省略
            //收件人
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //主题
            mimeMessage.setSubject(subject);
            //时间
            mimeMessage.setSentDate(new Date());
            //容器类，可以包含多个MimeBodyPart对象
            Multipart mp = new MimeMultipart();

            //MimeBodyPart可以包装文本，图片，附件
            MimeBodyPart body = new MimeBodyPart();
            //HTML正文
            body.setContent(content, "text/html; charset=UTF-8");
            mp.addBodyPart(body);

            //添加图片&附件
            if(!StringUtils.isEmpty(fileStr)){
                body = new MimeBodyPart();
                body.attachFile(fileStr);
                mp.addBodyPart(body);
            }

            //设置邮件内容
            mimeMessage.setContent(mp);
            //仅仅发送文本
            //mimeMessage.setText(content);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
