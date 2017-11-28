all:
	make build
	make run

build:
	javac -cp lib/*:src/ src/vis2017/gui/Main.java 

run:
	java -cp lib/core.jar:src/. vis2017.gui.Main

