package workmode.pubsub.fanout;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitUtils;

public class Consumer {

	private final static String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.声明交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		// 2.声明一个随机名称的队列
		String queueName = channel.queueDeclare().getQueue();
		
		// 3.绑定队列到fanout交换器
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		
		// 4.消费消息
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			System.out.println("日志处理任务1");
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	    };
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}
	
}
