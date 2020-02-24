package com.geekbrains;

import com.geekbrains.utils.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MarketApplication {
	// Домашнее задание:
	// 1. После оформления заказа в корзине, пользователю необходимо показать
	// отдельную страницу с ифнормацией о заказе
	// 2. На этой же странице (из п.1) пользовательн должен указать: адрес
	// достваки, контактный телефон (по-умалочанию показываем телефон
	// пользователя)
	// 3. Только после подтверждения на страницу из п.1 заказ должен быть
	// сохранен в базе данных

	// План на курс:
	// 1. Добавить платежную систему (PayPal)
	// 2. Авторизация через соцсети
	// 3. Комментарии к товарам, отзыв/рейтинг
	// 4. Отправка уведомлений пользователю, на сайте, или на почту
	// 5. Промокоды
	// 6. Логирование
	// 7. Профиль/редактирование профиля
	// 8. Отдельная админка
	// 9. Картинки для товаров
	// 10. HTTPS
	// 11. История просмотров товара (куки)
	// 12. Статистика для владельца
	// 13. История действий на сайте
	// 14. Смс сервис
	// 15. Восстановление пароля
	// 16. Формирование PDF для заказа
	public static final String TOPIC_EXCHANGER_NAME = "topicExchanger";

	@Bean
	Queue queueTopic1() {
		return new Queue("Client #2", false, false, true);
	}

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(TOPIC_EXCHANGER_NAME);
	}

	@Bean
	Binding bindingTopic1(@Qualifier("queueTopic1") Queue queue, TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with("Confirmed.#");
	}

	@Bean
	SimpleMessageListenerContainer containerForTopic(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("Client #2");
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}
}
