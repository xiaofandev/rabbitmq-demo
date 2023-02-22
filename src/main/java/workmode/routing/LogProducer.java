package workmode.routing;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class LogProducer {
	
	private final static String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.声明一个direct交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		// 2.发送三个不同的routing_key的消息
		String[] levels = new String[] {"warn", "info", "error"};
		for (String routingKey : levels) {
			String message = routingKey + ":this is a log message";
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		}
		
		
	}

}
