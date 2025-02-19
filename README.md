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
- ApiGateway - шлюз для всех сервисов, является входной точкой.
- localhost:9000/swagger-ui.html
## Технический стек
- **Spring Boot 3.4.2** - фреймворк, использовавшийся для разработки сервера ресурсов и контроллеров к ним 
- **MySQL** & **MongoDB** - SQL и NoSQL базы данных
- **Keycloak** - сервер авторизации
- **Resilience4j** - библиотека для отказоустойчивости приложения
- **Apache Kafka** - фреймворк для формирования очередей сообщений с OrderService в NotificationService о созданном заказе
- **Grafana** & **Prometheus** - мониторинг состояния системы
- **Docker** & **Kubernetes** - деплой приложения на локальной машине

<br>![screenshot](images/app-schema.png)
## Эндпоинты приложения
Для получения ресурсов от сервера предоставлены следующие эндпоинты:
- ``/api/product/`` - доступ к ProductService
- ``/api/order/`` - доступ к OrderService
- ``/api/inventory/`` - доступ к InventoryService
> [!IMPORTANT]
> Информация обо всех эндпоинтах представлена по эндпоинту ``/swagger-ui.html`` (OpenAPI Specification)
## Деплой (docker-compose.yml)
1. Установите Docker Desktop на свой ПК (для daemon-а) https://docs.docker.com/desktop/setup/install/windows-install/
2. Клонируйте репозиторий в желаемую папку
   
    ```powershell
    cd <папка, где будет находится проект>
    git clone https://github.com/duahifnv/MicroserviceApp.git
    cd MicroserviceApp
    ```
3. Проверьте, что docker daemon запущен
   
   ```docker
    docker --version
    ```
4. Запустите docker контейнеры, необходимые для работы приложения
   >Это займет некоторое время, т.к. будут подгружаться необходимые образы
   ```docker
    docker-compose up
    ```
   
5. Откройте папку с проектом с помощью любой IDE, и запустите **каждый** из модулей
6. Конфигурация по умолчанию:
- Хост: ``127.0.0.1 | localhost``
- Порт: ``9000``

## Деплой (kubernetes)
1. Выполнить пункты 1-3 из раздела **Деплой (docker-compose.yml)**
2. Установить **kind tool** на свой ПК https://kind.sigs.k8s.io/
3. Создайте кластер на основе конфигурации по пути ./k8s/kind/kind-config.yaml
   
   ```powershell
   kind create cluster --config k8s/kind/kind-config.yaml
   ```
4. Примените все манифесты к кластеру при помощи kubectl
   > Возможно, kubectl не будет предустановлен, и его будет необходимо ставить ручками
   
   ```powershell
   kubectl apply -f k8s/manifests
   ```
   Чтобы проверить деплой: ``kubectl get deployment``
5. Пробросьте необходимые порты

   Порт для API-шлюза (эндпоинты контроллеров)
   ```powershell
   kubectl port-forward svc/api-gateway 9000:9000
   ```

   Порт для Keycloak (Логин - 'admin', пароль - 'admin)
   ```powershell
   kubectl port-forward svc/keycloak 8080:8080
   ```

   Порт для Grafana
   ```powershell
   kubectl port-forward svc/grafana 3000:3000
   ```
