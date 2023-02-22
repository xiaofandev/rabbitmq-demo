package workmode.workqueues;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class Publisher {

	public final static String QUEUE_NAME = "log_save";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 模拟发送三条消息
		for (int i=0; i<3; i++) {
			channel.basicPublish("", QUEUE_NAME, null, ("new log["+i+"]").getBytes());
		}
		conn.close();
		
	}
	
}
