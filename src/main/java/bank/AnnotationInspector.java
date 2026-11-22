package bank;

import bank.application.Audited;
import bank.application.RequiresRole;

import java.lang.reflect.Method;

public class AnnotationInspector {

    public void inspect(Class<?> type) {
        System.out.println("Class: " + type.getName());

        System.out.println(
                "@Audited: " +
                        type.isAnnotationPresent(Audited.class)
        );

        System.out.println("Declared methods:");

        for (Method method : type.getDeclaredMethods()) {
            System.out.println("  " + method.getName());
        }

        System.out.println("Methods with @RequiresRole:");

        for (Method method :
                type.getDeclaredMethods()) {
            RequiresRole role =
                    method.getAnnotation(
                            RequiresRole.class
                    );
            if (role != null) {
                System.out.println(
                        method.getName() +
                                " -> " +
                                role.value()
                );
            }
        }
    }
}
