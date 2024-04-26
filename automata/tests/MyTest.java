package automata.tests;

import org.junit.Test;
import org.jetbrains.jetCheck.*;

import java.util.regex.*;

import static automata.MyApplication.*;


public class MyTest {

    @Test
    public void test1() {
        String regex = "abab";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abc"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test2() {
        String regex = "ab|cde";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abcdef"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test3() {
        String regex = "a*bc*(ab)+";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abcd"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test4() {
        String regex = "a+.+b?a+.+b+";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abcde"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test5() {
        String regex = "abcd*|cba(abc)+|(abc)+(bdb)(abc)?|a.b.c.d|(((abc)+)+)+";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 80), Generator.charsFrom("abcde"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test6() {
        String regex = "(a?bc+)|(a?c+bc*)";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 80), Generator.charsFrom("abcde"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test7() {
        String regex = "a|b";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 80), Generator.charsFrom("ab"));

        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

}
