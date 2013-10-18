# makefile дл€ java
# работает с GNU make 3.80
# --------------------------
#

# им€ результирующего файла без расширени€
#JAR_NAME := 01
#JMAINCLASS := romeogolf.HelloWorld
JMAINCLASS := romeogolf.Main

# пути к программам JDK 
JCOMPILER := "C:\Program Files\Java\jdk1.7.0_40\bin\javac"
JPACKAGER := "C:\Program Files\Java\jdk1.7.0_40\bin\javafxpackager"
JVM := "C:\Program Files\Java\jdk1.7.0_40\bin\java"

JCLASSPATH := "C:\Program Files\Java\jre7\lib\jfxrt.jar;.\out"

# строка с перечнем файлов исходников, участвующих в компил€ции
SRC := romeogolf/Main
#SRC += romeogolf\

# то же с расширени€ми
SRC_JAVA := $(addsuffix .java,$(SRC))
SRC_JAVA := $(addprefix ./src/,$(SRC_JAVA))
SRC_CLASS := $(addsuffix .class,$(SRC))
SRC_CLASS := $(addprefix ./out/,$(SRC_CLASS))

.PHONY: all
.PHONY: clean
.PHONY: TAGS
.PHONY: JAR

# конечна€ цель
#all : $(SRC_CLASS)
all : run TAGS

run : $(SRC_CLASS)
	@$(JVM) -classpath $(JCLASSPATH) $(JMAINCLASS)

# компил€ци€ .class-файлов в .java-файлы
$(SRC_CLASS) : $(SRC_JAVA)
	@$(JCOMPILER) -d out -classpath $(JCLASSPATH) $(SRC_JAVA)

# очистка от всех файлов, кроме участвующих в компил€ции
clean :
	@RMDIR "./out/" /S /Q
	@MKDIR "./out"

#
TAGS :
	@c:/bin/ctags.exe --language-force=java -R ./src/*
#	не думал еще, как сделать в одну строчку из€щно и рекурсивно только дл€ *.java

.PHONY: foo
foo :
	@echo Yep!
#	@pause
#
#@rem package the app as a click to run jar
#@rem "C:\Program Files\Java\jdk1.7.0_25\bin\javafxpackager" -createjar -appclass HelloWorld -runtimeversion 2.0 -outfile 01.jar -v
#@pause
##	
