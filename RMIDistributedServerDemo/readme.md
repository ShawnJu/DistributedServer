RMI Server/Client Compile:
javac RMIServerImpl.java
javac RMIClientA.java
javac RMIClientB.java

RMI Server/Client Usage:
rmiregistry 5106&
java RMIServerImpl 5106
java RMIClientA <serverHostName:port>
java RMIClientB <serverHostName:port> <threadsCount> <iterationCount>