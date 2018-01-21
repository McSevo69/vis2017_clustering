Starting our app is fairly easy.
Just browse in your terminal (or cmd) to the directory, where clustering.jar is located.
Now type in: java -jar ClusteringVisualizer.jar

Note: Java 1.8 (at least) is required

We have used a logging framework called log4j2 which allows to adjust logging levels even when the app is running.
It is strongly recommended to place the log4j2.xml file into the same directory as the jar file lies in.
Then you have the chance for adjusting log levels, log outputs and more.
Otherwise a default log4j2.xml is loaded, a error message is printed and you have to live with the default settings.
When starting you will get a warning, because the FXML in the jar file is loaded with a different JavaFX runtime version.
So don't get confused, this is okay and no problem at all.

P.S.: Our app was tested in Windows, Linux and even MacOS. So everything should work as expected.

