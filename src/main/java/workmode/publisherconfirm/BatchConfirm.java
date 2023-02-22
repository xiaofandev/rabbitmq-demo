package workmode.publisherconfirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class BatchConfirm {

	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		try (Connection conn = RabbitUtils.getConnection()) {
			Channel channel = conn.createChannel();
			
			// 1.打开publisher确认
			channel.confirmSelect();
			
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			// 2.发布10条消息
			for (int i=0; i<1000; i++) {
				String message = "Hello[" + i + "]";
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			}
			// 3.批量同步确认消息是否发送成功，获取broker的ack响应
			if (channel.waitForConfirms()) {
				System.out.println("发送消息成功");
			} else {
				System.out.println("发送消息失败");
			}
			
		}
		
	}
}
