package workmode.workqueues.fairdispatch;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;
import workmode.simple.Producer;

public class Worker {

	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.设置可接受最大任务数
		int prefetch = 1;
		channel.basicQos(prefetch);
		
		channel.basicConsume(Producer.QUEUE_NAME, true, (consumerTag, delivery) -> {
			String msg = new String(delivery.getBody());
			
			try {
				System.out.println("收到消息：" + msg);
				System.out.println("收到任务，处理中...");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println("任务处理完毕，返回ack");
				
			}
		}, consumerTag -> {});
		
	}
	
}
