# API для заказа товаров
## Краткое описание
Данный проект представляет собой бекэнд-приложение, предоставляющее возможность создавать и просматривать заказы, а также отслеживать информацию о существующих товарах.
## Основной функционал
- Информация о товарах: просмотр существующих и добавление новых товаров в базу данных
- Учет товаров: отслеживание количества каждого из существующих товаров, проверка наличия определенного товара
- Оформление заказов на товары: получение списка всех заказов и создание нового заказа на определенный товар
- Отправка уведомлений о созданном заказе на почту
## Архитектура сервисов
Проект состоит из нескольких микросервисов, каждый из который отвечает за свой функционал. Все микросервисы связаны между собой.
- ProductService - информация о товарах
- InventoryService - учет товаров
- OrderService - оформление заказов
- NotificationService - сервис для отправки уведомлений на почту
- ApiGateway - шлюз для всех сервисов, является входной точкой. Порт ``:9000``
## Деплой
1. Установите Docker Desktop на свой ПК (для daemon-а) https://docs.docker.com/desktop/setup/install/windows-install/
2. Клонируйте репозиторий в желаемую папку
   
    ```
    cd <папка, где будет находится проект>
    git clone https://github.com/duahifnv/MicroserviceApp.git
    cd MicroserviceApp
    ```
3. Проверьте, что docker daemon запущен
   
   ```
    docker --version
    ```
4. Запустите docker контейнеры, необходимые для работы приложения<br>Это займет некоторое время, т.к. будут подгружаться необходимые образы
   ```
    docker-compose up
    ```
   
5. Откройте папку с проектом с помощью любой IDE, и запустите каждый из модулей
6. Входной точкой будет адрес ``localhost:9000/..``
   
