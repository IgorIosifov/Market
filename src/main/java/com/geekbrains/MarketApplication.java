package com.geekbrains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}
}
