package com.github.open.casslog.accesslog;

import com.google.common.collect.Lists;
import org.springframework.util.AntPathMatcher;

import java.util.List;

public class PathMatcherUtil {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final List<String> IGNORED_PATTERN = Lists.newArrayList(
            "/actuator/**", "/hellgate/**");

    private PathMatcherUtil() {

    }

    public static boolean ignoredPath(String path) {
        for (String pattern : IGNORED_PATTERN) {
            if (PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

}
