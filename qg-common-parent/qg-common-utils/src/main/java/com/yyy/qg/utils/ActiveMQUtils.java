package com.yyy.qg.utils;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component
public class ActiveMQUtils {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;
    //发送队列消息
    public void sendQueueMessage(String queue, final Object message){
        Destination destination=new ActiveMQQueue(queue);
        jmsTemplate.convertAndSend(destination,message);
    }

}
