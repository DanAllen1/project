package pojo;

import java.util.List;
/*
* 用来封装邮件内容的实体类
*
* */
public class Email {
    private String sender;//发件人,此属性暂未使用
    private String subject;//标题
    private List<String> recipients;//接收者，多个接收者时用
    private String recipient;//接收者，只有一个时使用
    private String content;//邮件内容

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Email{" +
                "sender='" + sender + '\'' +
                ", subject='" + subject + '\'' +
                ", recipients=" + recipients +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
