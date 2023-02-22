package workmode.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitUtils;

public class Consumer {
	
	private static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = RabbitUtils.getConnection();
		
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 回调方法
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("recieved msg:" + message);
		};
		
		channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
		
	}
	
}
