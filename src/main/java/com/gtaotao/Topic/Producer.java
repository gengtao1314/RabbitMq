package com.gtaotao.Topic;

/**
 * Created by gengtao on 2019/5/6.
 订阅模式：
 1、1个生产者，多个消费者
 2、每一个消费者都有自己的一个队列
 3、生产者没有将消息直接发送到队列，而是发送到了交换机
 4、每个队列都要绑定到交换机
 5、生产者发送的消息，经过交换机，到达队列，实现，一个消息被多个消费者获取的目的
 注意：一个消费者队列可以有多个消费者实例，只有其中一个消费者实例会消费
 *
 *
 * 交换机有四种模式，分为direct直连交换机、topic主题交换机、fanout扇型交换机、headers头交换机，四种模式都是先将queue（队列）绑定到exchange（交换机）上
 * direct模式下，消费者queue的key要与生产者的key完全匹配
 * topic模式下，消费者queue的key可以模糊匹配生产者的key，使用'#'匹配任意多个单词，使用'*'仅能匹配一个单词，例如update.*可以匹配update.ok,不可以匹配update.ok.to,而update.#两者都可以匹配到
 * fanout模式下，queue的key将不起作用，交换机会将消息发给所有绑定的queue
 * headers模式下，参考https://blog.csdn.net/qq1052441272/article/details/53940754
 *
 * 测试topic（主题）模式
 * 1、设置交换机的模式为topic
 * 2、设置队列绑定的key ,如下key为update.1
 */
import com.gtaotao.Util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer {
    private final static String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 消息内容
        for(int i = 1; i < 10000 ; i++){
            String message = String.valueOf(i);
            channel.basicPublish(EXCHANGE_NAME, "update.1", null, message.getBytes());
            System.out.println("Consumer_1 [x] Sent '" + message + "'");
            Thread.sleep(1000);
        }


        channel.close();
        connection.close();
    }
}
