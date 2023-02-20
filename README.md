
## Настраиваем Report Portal для данного проекта

#### Прежде всего надо добавить [данные зависимости](https://github.com/Evgenii083/Report-Portal-/blob/c04995cc8a8f20cc0de05a9b950e69dac3959175/build.gradle#L30-L42)  в build.gradle файл. 

Далее в папке [resources](https://github.com/Evgenii083/Report-Portal-/tree/main/src/test/resources) создаем папку META-INF.services внутрь которой кладем файл Extention и файл logback.xml


Для запуска Report Portal нам понадобится Docker. Docker-compose файл для запуска можно найти [тут](https://github.com/reportportal/reportportal/blob/master/docker-compose.yml). В файле важно верно прописать volumes для вашей операционной системы, в файле такие места [предусмотрены](https://github.com/Evgenii083/Report-Portal-/blob/c04995cc8a8f20cc0de05a9b950e69dac3959175/docker-compose.yml#L140-L144) разработчиком. Также важно проверить порты, например в моем случае Postgres физически установлен на компьютер , а значит порт 5432 занят и надо его [переназначить](https://github.com/Evgenii083/Report-Portal-/blob/c04995cc8a8f20cc0de05a9b950e69dac3959175/docker-compose.yml#L146-L147). 

Запускаем [JAR](https://github.com/Evgenii083/Report-Portal-/blob/main/artifacts/app-card-delivery.jar) файл тестируемого приложения 
Запускаем docker-compose файл в "тихом" режиме с помощью команды   docker-compose -p reportportal up -d --force-recreate (она указана в docker-compose файле) . Важно не забыть в настройках докера увеличить объем оперативной памяти до 5 гб если по умолчанию у вас меньше. Docker запустит 15 контейнеров, однако контейнер "db-scripts-1" остановится после запуска - это [нормально](https://github.com/reportportal/reportportal/issues/1659). 

Теперь нам надо связать наш проект с сервером Report Portal. Для этого открываем браузер и идем на http://localhost:8080/ для входа в систему. Данные для входа указаны [тут](https://reportportal.io/installation). Далее идем в профиль, копируем [строчки](https://github.com/Evgenii083/Report-Portal-/blob/5e095a0d4b9ce6791f68bc793fe6e8ea620ade32/src/test/resources/reportportal.properties#L2-L9) и вставляем их в файл reportportal.properties. 

Далее добавим два утилитных класса в наш проект. Один будет отвечать за [логирование](https://github.com/Evgenii083/Report-Portal-/blob/main/src/test/java/utils/LoggingUtils.java),другой за [скриншоты](https://github.com/Evgenii083/Report-Portal-/blob/main/src/test/java/utils/ScreenShooterReportPortalExtension.java). 

После выполнения всех вышеперечисленных действий необходимо расширить тестовый класс проекта [анотацией](https://github.com/Evgenii083/Report-Portal-/blob/c04995cc8a8f20cc0de05a9b950e69dac3959175/src/test/java/%D0%A1ardDeliveryFormTest.java#L17) из утилитного класса скришнотера, а так же указать какие данные необходимо писать в отчет Report Portal. Указываются эти данные прямо в вашем тесте , вот [таким образом](https://github.com/Evgenii083/Report-Portal-/blob/c04995cc8a8f20cc0de05a9b950e69dac3959175/src/test/java/%D0%A1ardDeliveryFormTest.java#L39), по средствам вызова метода из нашего утилитного класса. 
