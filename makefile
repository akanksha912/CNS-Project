JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	HMAC.java \
	AES.java \
	Signatures.java \
	RSAEncryption.java \
	MainClass.java

default: classes

classes: $(CLASSES:.java=.class)

clean: 
	rm -f *.class
