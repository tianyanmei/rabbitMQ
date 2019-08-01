package com.zhouhj.fanout;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageCustomer2 {

    public   final static   String EXCHANGE_NAME="fanout_exchange";
    public  static  void  main(String[] args)throws  IOException, TimeoutException {
        //建立工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //创建连接
        Connection connection = factory.newConnection();
        //创建通道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //创建队列
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        final String consumerName="consumer2:"+ RandomUtil.randomString(5);
        System.out.println(consumerName+"等待接收消息");
        Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               // super.handleDelivery(consumerTag, envelope, properties, body);
                String message = new  String(body,"UTF-8");
                System.out.println(consumerName+"接收到消息："+message);
            }
        };

        channel.basicConsume(queueName,true,consumer);

    }




}
