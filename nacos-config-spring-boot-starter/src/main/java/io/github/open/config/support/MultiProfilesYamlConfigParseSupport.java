/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.open.config.support;

import com.alibaba.nacos.spring.util.parse.DefaultYamlConfigParse;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * yaml multi profiles.
 * <p>
 * read nacos propertysource:
 * <p>
 * 1. AbstractNacosPropertySourceBuilder#doBuild(String, BeanDefinition, Map)<br/>
 * 2. NacosPropertySource#NacosPropertySource(String, String, String, String, String)<br/>
 * 3. com.alibaba.nacos.spring.util.NacosUtils#toProperties(String, String, String, String)<br/>
 * 4. com.alibaba.nacos.spring.util.ConfigParseUtils#toProperties(String, String, String, String)<br/>
 *
 * @author <a href="https://github.com/studeyang">studeyang</a>
 */
public class MultiProfilesYamlConfigParseSupport extends DefaultYamlConfigParse {

    private static final String SPRING_PROFILES = "spring.profiles";
    private static Set<String> profiles;

    public static void initSpringProfiles(String[] activeProfiles, String[] defaultProfiles) {
        if (null == profiles) {
            if (activeProfiles.length > 0) {
                profiles = Arrays.stream(activeProfiles).collect(Collectors.toSet());
            } else {
                profiles = Arrays.stream(defaultProfiles).collect(Collectors.toSet());
            }
        }
    }

    @Override
    public Map<String, Object> parse(String configText) {
        final AtomicReference<Map<String, Object>> result = new AtomicReference<>();
        process(map -> {
            // first block
            if (result.get() == null) {
                result.set(map);
            } else {
                setFromOtherBlock(result, map);
            }
        }, createYaml(), configText);
        return result.get();
    }

    private void setFromOtherBlock(AtomicReference<Map<String, Object>> result, Map<String, Object> map) {
        if (map.get(SPRING_PROFILES) == null) {
            result.get().putAll(map);
            return;
        }

        for (String profile : profiles) {
            if (profile.equals(map.get(SPRING_PROFILES))) {
                result.get().putAll(map);
            }
        }
    }

}
