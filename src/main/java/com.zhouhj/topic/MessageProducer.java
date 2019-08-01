package com.zhouhj.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageProducer {
    public  final  static  String EXCHANGE_NAME="topics_change";

    public  static  void  main(String[] args){
        try {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"topic");

            String[] routingKeys=new String[]{
                "usa.news", "usa.weather", "europe.news", "europe.weather"
            };
            String[]  messages= new String[]{
                    "美国新闻", "美国天气",
                    "欧洲新闻", "欧洲天气"
            };

            for(int i=0;i<routingKeys.length;i++){

                String routingKey=routingKeys[i];
                String message = messages[i];
                channel.basicPublish(EXCHANGE_NAME,routingKey,null,
                        message.getBytes("UTF-8"));
                System.out.printf("发送消息到路由：%s, 内容是: %s%n ", routingKey,message);
            }
            channel.close();
            connection.close();


        }catch (Exception e){


        }



    }









}
