## Flowmodel

Программный комплекс для дисциплины
>Информационные технологии


Использованные ресурсы:
+ [Учебник по Javafx](http://code.makery.ch/library/javafx-8-tutorial/ru)
+ [Java for kids](http://yfain.github.io/Java4Kids/)
+ [Tutorial Apache POI](https://www.tutorialspoint.com/apache_poi_word/index.htm)
+ [Пример создания word документа apache poi](http://javadevblog.com/sozdanie-dokumenta-word-v-formate-docx-s-pomoshh-yu-apache-poi.html)
+ [Инициализация массива компоненотов JavaFx stackoverflow](http://stackoverflow.com/questions/28587297/create-array-of-label-using-fxml-in-javafx)

TO DO:

В первую очередь:
+ ~~найти ошибку в формуле, где неправильно рассчитывается вязкость~~
+ ~~переделать интерфейс согласно рекомендации из методы~~

Далее:
+ ~~сделать чтобы кнопочки работали, табличка заполнялась~~
+ ~~добавить графики(либо в основном окне, либо сделать новое)~~
+ реализовать "защиту от дурака" при вводе данных
+ ~~сделать чтобы поля ввода данных при запуске были с некоторыми начальными значениями~~
+ ~~добавить таймер, отображающий время расчёта~~

Теперь:
+ ~~кнопки сохранения графиков~~
+ ~~перевести проект на мавен~~
+ ~~масштабирование графиков(не нужно)~~
+ ~~графики строятся вместе с расчетом, перестраиваются после каждого расчета~~
+ добавить рисунки в отчет
+ карта меню доделать
+ ~~форма авторизации пофиксить~~
+ добавить второго юзвера
+ ~~возможность указать путь для сохранения отчета~~
+ генерацию отчета запускать в отдельном потоке
+ в имя отчета добавить время, когда он был сформирован
+ ~~проверка, был ли произведен уже расчет, перед нажатием отчета~~
+ поправить решение защиты от ввода в текстовые поля нежелательных вещей



Позже:
+ прикрутить возможность создания отчета (желательно MSWord);
+ прикрутить базу данных (SQLite скорее всего)
+ прикрутить разграничение прав пользователя
  +  продумать архитектуру решения: либо 
  логический флаг выключает элементы, либо скопировать формочки и изменить немного
  +  исследователь должен обладать возможностью только тыкаться, грубо говоря
  +  админ должен иметь право править базу и менять пароль
  
замечания:
+ ~~на интерфейсе не делать сокращения~~
+ ~~единицы измерения(указать)~~
+ ~~обеспечение надежности ПО, не ноль, не буква, не символ~~
+ ~~оси на графике подписаны~~
+ ~~легенды не нужны~~
+ ~~температура должна расти, вязкость должна падать~~
+ ~~время расчета еще вывести~~
+ ~~текущие параметры состояния~~
+ ~~скорость движения крышки~~
+ ~~перетащить ГридПейны~~
+ ~~глубина по дефолту 0,05~~

замечания ещё:

 

