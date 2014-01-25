package net.sehales.secon.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodCommandHandler {

	String[] additionalPerms() default {};

	String[] aliases() default {};

	String description() default "no description";

	String name();

	boolean overrideHelp() default false;

	String permission() default "op";

	String tabExecutorMethod() default "none";

	CommandType type() default CommandType.ALL;

	String usage() default "no usage information";

}
