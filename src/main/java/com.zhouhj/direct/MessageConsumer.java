package com.zhouhj.direct;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class MessageConsumer {
    public  final  static  String EXCHANGE_NAME="direct_queue";

    public  static  void  main(String[] args){
        String consumerName="consumer-"+ RandomUtil.randomString(5);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //声明要关注的队列名称(队列名称，持久化，独自占有，服务器将在不再使用时删除它，队列的其他属性(构造参数))
            channel.queueDeclare(EXCHANGE_NAME,false,false,true,null);
            System.out.println(consumerName+"等待接收消息：");

            Consumer consumer= new DefaultConsumer(channel){

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body,"UTF-8");
                    System.out.println(consumerName+"接收消息："+message);

                }
            };
            //自动回复队列应答 -- RabbitMQ中的消息确认机制
          channel.basicConsume(EXCHANGE_NAME,true,consumer);

        }catch (Exception e){
             System.out.println(e.getMessage());
        }
    }



}
