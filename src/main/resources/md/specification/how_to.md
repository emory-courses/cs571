# How To ?

## Install Java 8 and Maven on Ubuntu

* Install Java 8 by running the following commands:

   ```
   sudo add-apt-repository ppa:webupd8team/java
   sudo apt-get update
   sudo apt-get install oracle-java8-installer
   sudo apt-get install oracle-java8-set-default
   ```

* Check the Java version.

   ```
   java -version
   ```

* Install Maven by runnig the following command:

   ```
   sudo apt-get install maven
   ```

* Check the Maven version.

   ```
   mvn -version
   ```

## Run a Java class using Maven

* Specify the [JVM options](http://www.oracle.com/technetwork/articles/java/vmoptions-jsp-140102.html) in Maven.  If you are using the bash shell, export `MAVEN_OPTS`:

   ```
   export MAVEN_OPTS='-Xmx8g -XX:+UseConcMarkSweepGC -XX:MaxPermSize=128m'
   ```

* Compile the Java project using Maven (if not done).

   ```
   mvn compile
   ```

* Run an executable Java class using `mvn exec:java` from the top directory, where the [`pom.xml`](../../../../../pom.xml) is located.  For instance, the following command executes [`POSTrain`](../../src/main/java/edu/emory/mathcs/nlp/bin/POSTrain.java) (see [part-of-speech tagging](../component/part_of_speech_tagging.md#training) for more details about the command). Note that only the base filenames are specified here, but use the absolute pathnames if the filenames are not getting recognized.

   ```
   mvn exec:java -Dexec.mainClass="edu.emory.mathcs.nlp.bin.POSTrain" -Dexec.args="-c config_train_pos.xml -t wsj_0001.dep -d wsj_0001.dep"
   ```