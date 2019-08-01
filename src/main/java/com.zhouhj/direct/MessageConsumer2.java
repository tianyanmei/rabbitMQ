package com.zhouhj.direct;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;

public class MessageConsumer2 {

    public  final  static  String EXCHANGE_NAME="direct_queue";


    public  static  void  main(String[] args){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(EXCHANGE_NAME,false,false,true,null);
            String consumerName="consumer2"+ RandomUtil.randomString(5);
            System.out.println(consumerName+"等待接收消息");

            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new  String(body,"UTF-8");


                    System.out.println(consumerName+"接收消息:"+message);
                }
            };

            channel.basicConsume(EXCHANGE_NAME,true,consumer);


        }catch (Exception e){

            System.out.println(e.getMessage());
        }








    }


}
