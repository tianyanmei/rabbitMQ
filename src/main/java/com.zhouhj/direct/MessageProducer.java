package com.zhouhj.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageProducer {

    public  final  static  String EXCHANGE_NAME="direct_queue";


    public  static  void main(String[] args){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel  =connection.createChannel();
            //channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            for (int i=0;i<100;i++){
                String message = "direct发送的消息:"+i;
                channel.basicPublish("",EXCHANGE_NAME,null,message.getBytes("UTF-8"));
                System.out.println("发送的消息："+message);
            }
            channel.close();
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }










    }

}
