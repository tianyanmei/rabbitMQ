package com.zhouhj.fanout;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

//生产者
public class MessagePrducer {
    public   final static  String EXCHANGE_NAME="fanout_exchange";


    public static  void  main(String[] args) throws  Exception{
        //创建连接工厂
        ConnectionFactory factory  = new ConnectionFactory();

        //设置主机
        factory.setHost("localhost");

        //创建新的连接
        Connection connection = factory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();

        //设置通道模式
        //type：有direct、fanout、topic三种
        //durable：true、false true：服务器重启会保留下来Exchange。
        //autoDelete:true、false.true:当已经没有消费者时，服务器是否可以删除该Exchan
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        for(int i =0;i<100;i++){
            String message = "direct消息："+i;
            //发送到队列中
            channel.basicPublish(EXCHANGE_NAME,"",null, message.getBytes("UTF-8"));
            System.out.println("发送的消息："+message);
        }

        channel.close();
        connection.close();
    }



}
