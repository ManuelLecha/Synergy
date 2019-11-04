package ub.edu.pis2017.pis_17.synergy;

import org.junit.Test;

import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String s = "#hashtag s";
        System.out.println(s.replace("#","").split(" ")[0]);
        Hashtag h = new Hashtag("#hashtag s");
        System.out.println(h.getHashtagName());
    }
}