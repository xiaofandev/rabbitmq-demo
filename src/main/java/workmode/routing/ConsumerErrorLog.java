package workmode.routing;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitUtils;

public class ConsumerErrorLog {
	
	private final static String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.声明一个direct交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		// 2.获取一个随机名称的队列
		String queueName = channel.queueDeclare().getQueue();
		
		// 3.绑定队列到交换器，指定binding key为error
		channel.queueBind(queueName, EXCHANGE_NAME, "error");
		
		// 4.消费消息
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			System.out.println("错误日志处理：");
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	    };
	    
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
		
	}

}
