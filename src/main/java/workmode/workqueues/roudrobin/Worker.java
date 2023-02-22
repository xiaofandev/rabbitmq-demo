package workmode.workqueues.roudrobin;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;
import workmode.workqueues.Publisher;

public class Worker {

	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		
		Channel channel = conn.createChannel();
		channel.queueDeclare(Publisher.QUEUE_NAME, false, false, false, null);
		channel.basicConsume(Publisher.QUEUE_NAME, true, (consumerTag, message) -> {
			String msg = new String(message.getBody(), "utf-8");
			System.out.println("recieved task:" + msg);
		}, consummerTag ->{});
	}
}
