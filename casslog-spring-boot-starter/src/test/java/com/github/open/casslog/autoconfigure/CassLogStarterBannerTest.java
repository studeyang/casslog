package com.github.open.casslog.autoconfigure;

import com.github.open.casslog.logging.CassLogStarterBanner;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2021/12/24
 */
public class CassLogStarterBannerTest {

    @Test
    public void testVersion() {
        Assert.assertEquals(1, CassLogStarterBanner.compareVersion("2.17.0", "2.7.0"));
        Assert.assertEquals(1, CassLogStarterBanner.compareVersion("2.17", "2.7"));
        Assert.assertEquals(1, CassLogStarterBanner.compareVersion("2.17.0", "2.7"));
        Assert.assertEquals(1, CassLogStarterBanner.compareVersion("2.17", "2.7.0"));

        Assert.assertEquals(1, CassLogStarterBanner.compareVersion("2.17.0", "2.17"));

        Assert.assertEquals(0, CassLogStarterBanner.compareVersion("2.16.0", "2.16.0"));
    }

    @Test
    public void uuid() {
        System.out.println(UUID.randomUUID().toString());
    }

}