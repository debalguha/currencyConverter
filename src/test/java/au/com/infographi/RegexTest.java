package au.com.infographi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String atgs[]) throws Exception {
        String sourceStr = "${fn:upper (_[var1]) == '62118664298' || fn:lower (_[var2]) == '62118664298'}";
        //String sourceStr = "${fn:upper (_[EmployerABN]) == '62118664298' || fn:lower (_[EmployerABN]) == '62118664298'}";
        //String sourceStr = "${(_[EmployerABN]) + (_[EmployerABN2])}";
        //String sourceStr = "${_[Member.FamilyName].concat(',').concat(_[Member.GivenName]).concat(_[Member.OtherGivenName]) * _[Member.BenefitCategoryText][0]}";
        Pattern pattern = Pattern.compile("[^_]*(_\\[)([a-zA-Z\\.\\s+0-9]*)(\\])[^_]*");
        //Pattern pattern = Pattern.compile("[^_]*_\\[([a-zA-Z\\.]*)\\][^_]*");
        Matcher matcher = pattern.matcher(sourceStr);
        System.out.println(sourceStr);
        //Matcher matcher = pattern.matcher("${!empty _[Benefit Category][0]}");
        StringBuilder builder = new StringBuilder();
        while(matcher.find()){
            System.out.println(String.format("Match details 0 -> %s, 1-> %s, 2-> %s, 3-> %s", matcher.group(0), matcher.group(1), matcher.group(2), matcher.group(3)));
            builder.append(matcher.group(0));
        }
        System.out.println(builder.toString());
    }
}