package io.github.open.config.inject;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import io.github.open.config.support.MultiProfilesYamlConfigParseSupport;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class NacosConfigApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {

        ConfigurableApplicationContext context = event.getApplicationContext();

        if (context.isActive()) {
            return;
        }

        if (AnnotationConfigApplicationContext.class.equals(context.getClass())) {
            return;
        }

        ConfigurableEnvironment environment = context.getEnvironment();

        // YamlConfigParseSupport 需要知道当前环境的 Profiles
        MultiProfilesYamlConfigParseSupport.initSpringProfiles(environment.getActiveProfiles(),
                environment.getDefaultProfiles());

        //通过注解扫描生成 NacosPropertySource
        this.doScanNacosPropertySourceAnnotation(event, context);

        context.addBeanFactoryPostProcessor(new NacosConfigBeanDefinitionRegistryPostProcessor(context));

    }

    private int doScanNacosPropertySourceAnnotation(SpringApplicationEvent event, ApplicationContext context) {

        Set<String> basePackages = new LinkedHashSet<>();
        Class<?> mainClass = event.getSpringApplication().getMainApplicationClass();
        // 使用 new StandardAnnotationMetadata, 为了兼容 spring-core:4.3.4 及更早版本
        AnnotationMetadata mainClassMetadata = new StandardAnnotationMetadata(mainClass, true);
        Map<String, Object> attributesMap = mainClassMetadata.getAnnotationAttributes(ComponentScan.class.getName(), false);
        if (null != attributesMap) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(attributesMap);

            String[] basePackagesArray = attributes.getStringArray("basePackages");
            for (String pkg : basePackagesArray) {
                String[] tokenized = StringUtils.tokenizeToStringArray(context.getEnvironment().resolvePlaceholders(pkg),
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                basePackages.addAll(Arrays.asList(tokenized));
            }
            for (Class<?> clazz : attributes.getClassArray("basePackageClasses")) {
                basePackages.add(ClassUtils.getPackageName(clazz));
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(mainClass.getName()));
        }

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) context);
        scanner.setIncludeAnnotationConfig(false);
        scanner.resetFilters(false);
        scanner.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
            return !metadata.hasAnnotation(Configuration.class.getName());
        });
        scanner.addIncludeFilter(new AnnotationTypeFilter(NacosPropertySource.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(NacosPropertySources.class));

        int scanResult = 0;
        for (String basePackage : basePackages) {
            scanResult += scanner.scan(basePackage);
        }
        return scanResult;
    }

}
