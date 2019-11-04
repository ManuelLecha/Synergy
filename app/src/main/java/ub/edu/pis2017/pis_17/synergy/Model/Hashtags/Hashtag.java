package ub.edu.pis2017.pis_17.synergy.Model.Hashtags;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by manuellechasanchez on 10/04/2018.
 */

public class Hashtag {

    private final String hashtagName;

    public Hashtag(String hashtagName){
        this.hashtagName = hashtagName.replace("#","").split(" ")[0].toLowerCase();
    }

    public String getHashtagName(){
        return this.hashtagName;
    }

    public int getLength(){
        return hashtagName.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hashtag hashtag = (Hashtag) o;

        return hashtagName.equals(hashtag.hashtagName);
    }

    @Override
    public String toString() {
        return "#" + hashtagName;
    }

    @Override
    public int hashCode() {
        return hashtagName.hashCode();
    }

    /**
     * Checks if the hashtag starts with a prefix
     * @param prefix
     * @return
     */
    public boolean startsWith(String prefix) {
        return this.hashtagName.startsWith(prefix);
    }

    public static List<Hashtag> parseString(String s) {
        List<Hashtag> lst = new LinkedList<>();
        String pattern = "#(\\w+)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(s);
        while(m.find()){
            lst.add(new Hashtag(m.group().substring(1)));
        }
        return lst;
    }

    public static String printHashtags(List<Hashtag> list) {
        if(list.size() == 0) {return "";}
        String str = "";
        for(Hashtag h : list) {
            str = str + h.toString() + " ";
        }
        return str.substring(0, str.length() - 1);
    }
}
