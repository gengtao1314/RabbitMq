package com.gtaotao.WorkQueue;

/**
 * Created by gengtao on 2019/5/5.
 * work模式有两种模式，一种是轮询模式，一种是公平分发模式，即能者多得
 */
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
public class Consumer_1 {
    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 同一时刻服务器只会发一条消息给消费者，使用能者多得模式时，要设置该代码，轮询模式时不需要设置
        channel.basicQos(1);

        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听队列，false表示手动返回完成状态，true表示自动
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [y] Received '" + message + "'");
            //休眠
            Thread.sleep(10);
            // 返回确认状态，默认使用自动确认模式，false表示使用手动确认模式，使用能者多得模式时要手动确认
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
