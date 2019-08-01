package com.zhouhj.topic;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class MessageConsumer {

    public  final  static  String EXCHANGE_NAME="topics_change";


    public static  void  main(String[] args) throws  Exception{
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection =factory.newConnection();
        Channel channel = connection.createChannel();
        //设置路由类型
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        //设置一个临时队列
        String queueName =channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,"usa.*");

        String consumerName = "consumer"+ RandomUtil.randomString(5);
        System.out.println(consumerName+"等待接收消息");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               // super.handleDelivery(consumerTag, envelope, properties, body);
                String message = new String(body,"UTF-8");
                System.out.println(consumerName+"接收到的消息："+message);
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }


}
