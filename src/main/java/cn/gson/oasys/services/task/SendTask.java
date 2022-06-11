package cn.gson.oasys.services.task;

import cn.gson.oasys.services.mail.MailServices;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @author 86156
 * mission6 发送邮件 定时任务
 */
//@Component
public class SendTask {

    private static final String  APOLLO_DEFAULT_VALUE_JSON_ARRAY ="[]";

    @Autowired
    MailServices mailServices;


    @Value("${task.account}")
    String account;
    @Value("${task.password}")
    String password;
    @Value("441102036@qq.com")
    String receiver;
    @Value("${task.name}")
    String name;
    @Value("${task.content}")
    String content;
    @Value("${task.title}")
    String title;

    @Scheduled(cron = "0 0/30 9-18 ? * MON-FRI")
    public void sendMail(){
        System.out.println("定时任务启动..........");
        mailServices.pushmail(account,password,receiver,name,title,content,null,null);

    }
}
