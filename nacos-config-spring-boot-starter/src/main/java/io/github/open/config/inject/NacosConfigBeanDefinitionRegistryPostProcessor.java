package io.github.open.config.inject;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;

import static com.alibaba.nacos.spring.util.NacosBeanUtils.*;

@Slf4j
public class NacosConfigBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final ConfigurableApplicationContext context;

    public NacosConfigBeanDefinitionRegistryPostProcessor(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        Map<String, Object> globalPropertiesAttributes = getAnnotationAttributes();
        if (null == globalPropertiesAttributes || globalPropertiesAttributes.isEmpty()) {
            return;
        }

        registerGlobalNacosProperties(globalPropertiesAttributes, registry, context.getEnvironment(),
                CONFIG_GLOBAL_NACOS_PROPERTIES_BEAN_NAME);

        registerNacosCommonBeans(registry);

        registerNacosConfigBeans(registry, context.getEnvironment(), context.getBeanFactory());

        invokeNacosPropertySourcePostProcessor(context.getBeanFactory());

    }

    private Map<String, Object> getAnnotationAttributes() {
        try {
            Annotation annotation = AnnotationUtils.synthesizeAnnotation(EnableNacosConfig.class);
            Object globalPropertiesDefaultValue = AnnotationUtils.getDefaultValue(annotation, "globalProperties");
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(globalPropertiesDefaultValue);
            return (Map) getObjFieldVal(invocationHandler, "memberValues");
        } catch (Exception e) {
            log.error("无法读取 'EnableNacosConfig' 注解的属性 'globalProperties'", e);
            return Collections.emptyMap();
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private Object getObjFieldVal(Object instance, String fieldName) {
        Field field = getInnerField(instance.getClass(), fieldName);
        if (null == field) {
            return null;
        } else {
            try {
                Method readMethod = getReadMethod(instance.getClass(), field);
                if (null != readMethod) {
                    return readMethod.invoke(instance);
                } else {
                    field.setAccessible(true);
                    return field.get(instance);
                }
            } catch (Exception var4) {
                log.warn("读取属性值失败", var4);
                return null;
            }
        }
    }

    private Field getInnerField(Class<?> clazz, String fieldName) {
        if (null != clazz && StringUtils.hasText(fieldName)) {
            while (!"java.lang.object".equalsIgnoreCase(clazz.getName())) {
                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (Exception var3) {
                    log.warn("类型 '{}' 中没有属性 '{}'", clazz.getName(), fieldName);
                    clazz = clazz.getSuperclass();
                }
            }

            log.warn("没有找到属性 '{}'", fieldName);
            return null;
        } else {
            return null;
        }
    }

    private Method getReadMethod(Class<?> claz, Field field) {
        StringBuilder sb = new StringBuilder();
        sb.append("get").append(field.getName().substring(0, 1).toUpperCase()).append(field.getName().substring(1));

        try {
            Class<?>[] types = new Class[0];
            return claz.getMethod(sb.toString(), types);
        } catch (Exception var4) {
            return null;
        }
    }
}
