# Circuits
Search directed graph for circular dependencies.

<h2>build</h2>
mvn package

<h2>launch</h2>
java -jar circuits.jar < test1.txt

<h2>description</h2>
Разработать программу на языке Java для обработки файла, в котором задаются зависимости сущностей друг от друга. 
Каждая сущность представлена своим уникальным идентификатором в виде целого числа. 
Зависимости задаются строкой в текстовом файле, содержащем произвольное количество строк формата: 
&ltid сущности&gt &ltid сущности от которой она зависит&gt. Файл читать из STDIN. Для прочитанных сведений о связях между 
сущностями необходимо найти циклические зависимости и вывести их в STDOUT в виде id сущностей через пробел.  

Пример файла:  
1 2  
2 1  
3 4  
5 6  
6 5  
  
Ожидаемый вывод в STDOUT:  
1 2 1  
5 6 5  
