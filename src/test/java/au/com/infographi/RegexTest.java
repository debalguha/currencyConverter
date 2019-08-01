package au.com.infographi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String atgs[]) throws Exception {
        Pattern pattern = Pattern.compile("[^_]*_\\[([a-zA-Z\\.]*)\\][^_]*");
        Matcher matcher = pattern.matcher("${_[Member.FamilyName].concat(',').concat(_[Member.GivenName]).concat(_[Member.OtherGivenName]) * _[Member.BenefitCategoryText][0]}");
        while(matcher.find()){
            System.out.println(String.format("Match details 0 -> %s, 1-> %s", matcher.group(0), matcher.group(1)));
        }
    }
}
