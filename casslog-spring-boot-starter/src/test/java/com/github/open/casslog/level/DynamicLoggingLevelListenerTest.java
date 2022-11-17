package com.github.open.casslog.level;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2022/1/21
 */
public class DynamicLoggingLevelListenerTest {

    private DynamicLoggingLevelListener dynamicLoggingLevelListener;

    @Before
    public void before() {
        dynamicLoggingLevelListener = new DynamicLoggingLevelListener();
    }

    @Test
    public void test_casslogLevelChange() {

        dynamicLoggingLevelListener.casslogLevelChange("casslog.level.root: error");
    }

}