package ub.edu.pis2017.pis_17.synergy;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;

/**
 * Created by icanalma7.alumnes on 15/05/18.
 */

public class HashtagTest {
    @Test
    public void test() {
        String hashtags = "#hasht #hasho #tag x";

        String pattern = "(#\\w+)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(hashtags);
        while(m.find()){
            System.out.println(m.group());

        }
    }
}
