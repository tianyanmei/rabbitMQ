package com.zhouhj.fanout;



import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//消费者
public class MessageCustomer {
    public   final static   String EXCHANGE_NAME="fanout_exchange";

    public static  void  main(String[] args) throws IOException, TimeoutException {
        final String consumerName="consumer:"+ RandomUtil.randomString(5);

        //判断端口是否占用
        if(NetUtil.isUsableLocalPort(5672)){
            System.out.printf("该端口%d不可用！",5672);
            return;
        }

        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        //创建连接
        Connection connection = factory.newConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //交换机声明（参数为：交换机名称；交换机类型）
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //获取一个临时队列
        String queue = channel.queueDeclare().getQueue();
        //队列与交换机绑定（参数为：队列名称；交换机名称；routingKey忽略）
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println(consumerName+"等待接收消息");
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，
        // 就会执行回调函数handleDelivery

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               // super.handleDelivery(consumerTag, envelope, properties, body);
                String message =new String(body,"UTF-8");
                System.out.println(consumerName+ "接收到的消息："+message);

            }
        };

      //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queue,true,consumer);
    }


}
