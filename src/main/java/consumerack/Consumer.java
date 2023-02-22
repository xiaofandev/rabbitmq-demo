package consumerack;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;
import workmode.simple.Producer;

public class Consumer {

	public static void main(String[] args) throws IOException {
		
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		boolean autoAck = false;
		channel.basicConsume(Producer.QUEUE_NAME, autoAck, (consumerTag, delivery) -> {
			String msg = new String(delivery.getBody());
			
			try {
				System.out.println("收到消息：" + msg);
				System.out.println("收到任务，处理中...");
				// 模拟处理任务
				//Thread.sleep(60000);
				// 返回ack（注意，此处一定要记得ack，不然会导致broker内存消耗越来越大，而且在消费者重启后，也会导致消息重复消费）
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			} finally {
				System.out.println("任务处理完毕，返回ack");
				
			}
		}, consumerTag -> {});
		
	}
	
}
