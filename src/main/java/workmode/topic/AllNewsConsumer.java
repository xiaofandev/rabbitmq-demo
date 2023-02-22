package workmode.topic;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitUtils;

public class AllNewsConsumer {

	private final static String EXCHANGE_NAME = "topic_sport_news";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		// 1.声明一个主题交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		
		// 2.声明一个随机名称队列
		String queueName = channel.queueDeclare().getQueue();
		
		// 3.接收所有新闻
		String binddingKey = "#";
		channel.queueBind(queueName, EXCHANGE_NAME, binddingKey);
		
		// 4.消费新闻
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody());
			System.out.println("recieved msg:" + message);
		};
		
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
		
	}
	
}
