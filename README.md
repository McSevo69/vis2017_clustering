Starting our app is fairly easy.
Just browse in your terminal (or cmd) to the directory, where clustering.jar is located.
Now type in: java -jar clustering.jar

Note: Java 1.8 (at least) is required

We have used a logging framework called log4j2 which allows to adjust logging levels even when the app is running.
It is strongly recommended to place the log4j2.xml file into the same directory as the jar file lies in.
Then you have the chance for adjusting log levels, log outputs and more.
Otherwise a default log4j2.xml is loaded and you have to live with the default settings.

P.S.: Our app was tested in Windows, Linux and even MacOS. So everything should work as expected.

