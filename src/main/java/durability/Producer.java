package durability;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import utils.RabbitUtils;

public class Producer {

	public final static String QUEUE_NAME = "durable_task";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		try (Connection conn = RabbitUtils.getConnection()) {
			Channel channel = conn.createChannel();
			
			// 1.声明队列为“持久化”
			boolean durable = true;
			channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
			
			String message = "Hello World!";
			// 2.设置消息持久化
			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println("sent msg:"+message);
			
		}
		
	}
	
}
