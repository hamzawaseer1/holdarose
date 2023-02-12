package com.holdarose;

import com.holdarose.HoldaroseApp;
import com.holdarose.config.EmbeddedMongo;
import com.holdarose.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { HoldaroseApp.class, TestSecurityConfiguration.class })
@EmbeddedMongo
public @interface IntegrationTest {
}
