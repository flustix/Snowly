package flustix.fluxifyed.database.api.types;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface APIRoute {
    String path();
    String method() default "GET";
}
