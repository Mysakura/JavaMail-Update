package com.demo;

/**
 * 发送线程
 */
public class CompanySending implements Runnable {

    private String to;	//收件人
    private String subject;	//主题
    private String content;	//内容
    private String fileStr;	//附件路径

    public CompanySending(String to, String subject, String content, String fileStr) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.fileStr = fileStr;
    }

    public void run() {
        SendEmailCompanyUtils sendEmail = new SendEmailCompanyUtils(to, subject, content, fileStr);
        sendEmail.send();
    }
}
