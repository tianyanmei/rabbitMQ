package com.zhouhj.topic;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class MessageConsumer2 {

    public  final  static  String EXCHANGE_NAME="topics_change";

    public  static  void  main(String[] args)throws  Exception{
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"*.news");

        String consumerName = "counsumer02"+ RandomUtil.randomString(5);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println(consumerName+"接收到的消息:"+message);
            }
        };
        channel.basicConsume(queueName,true,consumer);

    }

}
