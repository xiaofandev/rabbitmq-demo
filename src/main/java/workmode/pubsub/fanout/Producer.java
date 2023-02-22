package workmode.pubsub.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class Producer {

	private final static String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.声明fanout类型的交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		String logs = "this is a log message";
		
		// 2.把消息发送到fanout类型的交换器
		channel.basicPublish(EXCHANGE_NAME, "", null, logs.getBytes());
		
		channel.close();
		conn.close();
		
	}
	
}
