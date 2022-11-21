package com.github.open.casslog.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CassLogStarterBanner {

    private static final String LOG4J_LOWEST_VERSION = "2.16.0";

    private static boolean bannerPrinted = false;

    public static void print() {

        if (!bannerPrinted) {
            Logger logger = LoggerFactory.getLogger(CassLogStarterBanner.class);

            if (logger instanceof Log4jLogger) {
                printBanner();
            } else {
                throw new CassLogInitializeException("初始化失败，请使用 Log4j Logger。");
            }

            String version = Log4jLogger.class.getPackage().getImplementationVersion();
            if (compareVersion(LOG4J_LOWEST_VERSION, version) >= 0) {
                System.err.println("请使用 Log4j " + LOG4J_LOWEST_VERSION + " 及以上版本。");
                System.exit(-1);
            }

            bannerPrinted = true;
        }
    }

    public static void printBanner() {

        String version = Optional.ofNullable(CassLogStarterBanner.class.getPackage())
                .map(Package::getImplementationVersion)
                .map(" v"::concat)
                .orElse("");

        String banner = "欢迎使用 Casslog" + version;

        System.out.println(banner);

    }

    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] v1Array = version1.split("\\.");
        String[] v2Array = version2.split("\\.");
        int v1Len = v1Array.length;
        int v2Len = v2Array.length;

        for (int i = 0; i < Math.min(v1Len, v2Len); i++) {
            if (!v1Array[i].equals(v2Array[i])) {
                return Integer.parseInt(v1Array[i]) > Integer.parseInt(v2Array[i]) ? 1 : -1;
            }
        }
        //基础版本相同，再比较子版本号
        if (v1Len == v2Len) {
            //基础版本相同，无子版本号
            return 0;
        } else {
            return v1Len > v2Len ? 1 : -1;
        }
    }

}
