package workmode.publisherconfirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class Transaction {

	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		try (Connection conn = RabbitUtils.getConnection()) {
			Channel channel = conn.createChannel();
			
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "transaction message";
			
			try {
				// 1.打开事务
				channel.txSelect();
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				// 模拟异常
//				int i = 1/0;
				// 2.提交事务
				channel.txCommit();
			} 
			catch (Exception e) {
				// 3.回滚事务
				channel.txRollback();
			}
			System.out.println("sent msg:"+message);
			
		}
		
	}
}
