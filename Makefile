all:
	make build
	make run

build:
	javac -cp lib/core.jar:. src/gui/Main.java 

run:
	java -cp ./src gui.Main

