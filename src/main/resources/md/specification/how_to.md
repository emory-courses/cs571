# How To ?

## Install Java 8 and Maven 3 on Ubuntu

* Install Java 8 by running the following commands:

   ```
   sudo apt-add-repository ppa:webupd8team/java
   sudo apt-get update
   sudo apt-get install oracle-java8-installer
   sudo apt-get install oracle-java8-set-default
   ```

* Check the Java version.

   ```
   java -version
   ```

* Install Maven 3 by running the following command:

   ```
   sudo apt-add-repository ppa:andrei-pozolotin/maven3
   sudo apt-get update
   sudo apt-get install maven3
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

* Compile the Java project using Maven by running the following command from the top directory, where the [`pom.xml`](../../../../../pom.xml) is located. The `target/classes` directory should be created after running this command if it does not already exist.

   ```
   mvn compile
   ```

* Copy [`log4j.properties`](../../src/main/resources/configuration/log4j.properties) to `target/classes` if it is not already specified in your path.

* Run an executable Java class using `mvn exec:java`.  For instance, the following command executes [`POSTrain`](../../src/main/java/edu/emory/mathcs/nlp/bin/POSTrain.java) (see [part-of-speech tagging](../component/part_of_speech_tagging.md#training) for more details about the command). Note that the base filenames are used in this example, but use the filenames with their absolute paths if they are not getting recognized.

   ```
   mvn exec:java -Dexec.mainClass="edu.emory.mathcs.nlp.bin.POSTrain" -Dexec.args="-c config_train_pos.xml -t wsj_0001.dep -d wsj_0001.dep"
   ```