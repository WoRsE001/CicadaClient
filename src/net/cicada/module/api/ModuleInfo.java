package net.cicada.module.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo
{
    String name() default "Test";
    Category category() default Category.Combat;
    int key() default 0;
    boolean state() default false;
}
