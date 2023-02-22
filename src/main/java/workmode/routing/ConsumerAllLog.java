package workmode.routing;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitUtils;

public class ConsumerAllLog {
	
	private final static String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		
		// 1.声明一个direct交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		// 2.声明一个随机名称的队列
		String queueName = channel.queueDeclare().getQueue();
		
		// 3.相同一个队列，通过不同的binding_key，多次绑定到同一个交换器
		String[] levels = new String[] {"warn", "info", "error"};
		for (String bindingKey : levels) {
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		}
		
		// 4.消费消息
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			System.out.println("全部日志处理：");
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	    };
	    
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
		
	}

}
