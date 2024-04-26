# How to use this code template

Compile everything:

    $ javac automata/*.java

Run your application:

    $ java automata.MyApplication

Compile and run the tests:

    $ javac -cp ".:junit-4.13.jar:jetCheck-0.1-SNAPSHOT.jar" automata/*.java automata/tests/*.java
    $ java -cp ".:junit-4.13.jar:hamcrest-2.2.jar:jetCheck-0.1-SNAPSHOT.jar" org.junit.runner.JUnitCore automata.tests.MyTest
