package workmode.publisherconfirm;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class AsyncConfirm {

	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.打开publisher确认
		channel.confirmSelect();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		// 2.添加确认监听器
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("nack,deliveryTag:"+deliveryTag+",multiple:"+multiple);
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("ack,deliveryTag:"+deliveryTag+",multiple:"+multiple);
			}
		});
		// 2.发布10条消息
		for (int i=0; i<100; i++) {
			String message = "Hello[" + i + "]";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		}
		
		
	}
}
