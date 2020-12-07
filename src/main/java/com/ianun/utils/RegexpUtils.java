package com.ianun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ....: WangQk
 * @project....: ianun-crawlers
 * @description:
 * @date ......: 2020-12-03
 */
public class RegexpUtils {

    public static Matcher reg(String pattern, String document) {
        Pattern pat = Pattern.compile("(?<=<scrip>window.__playinfo__=).+?(?=</script>)");
        return pat.matcher(document);
    }
}
