package workmode.topic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import utils.RabbitUtils;

public class NewsProducer {

	private final static String EXCHANGE_NAME = "topic_sport_news";
	
	public static void main(String[] args) throws IOException {
		Connection conn = RabbitUtils.getConnection();
		Channel channel = conn.createChannel();
		// 1.声明一个主题交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		
		// 2.发送三条新闻
		Map<String, String> news = new HashMap<>();
		news.put("basketball.la.james", "Lebron has made the history on score.");// 关于詹姆斯的新闻
		news.put("soccer.", "something happened.");// 关于足球的新闻
		news.put("non-conforming", "...");// 不符合routing_rule规则的新闻
		for (String routingKey : news.keySet()) {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, news.get(routingKey).getBytes());
		}
		
	}
	
}
