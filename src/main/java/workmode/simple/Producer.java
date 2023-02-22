package workmode.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	public final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		// 1.创建连接工厂
		ConnectionFactory connFactory = new ConnectionFactory();
		connFactory.setHost("localhost");
		connFactory.setUsername("user");
		connFactory.setPassword("password");
		
		// 2.创建连接
		try (Connection conn = connFactory.newConnection()) {
			// 3.创建socket通道
			Channel channel = conn.createChannel();
			
			// 4.声明队列（支持幂等）
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "Hello World!";
			// 5.发送消息（如果硬盘空间不足则会发送失败，可以通过配置文件的disk_free_limit参数进行设置）
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println("sent msg:"+message);
			
		}
		
	}
	
}
