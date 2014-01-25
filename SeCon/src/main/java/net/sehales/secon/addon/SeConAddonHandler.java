
package net.sehales.secon.addon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sehales.secon.utils.SimplePriorityList.Priority;

@Documented()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SeConAddonHandler {
    
    Priority priority() default Priority.NORMAL;
}
