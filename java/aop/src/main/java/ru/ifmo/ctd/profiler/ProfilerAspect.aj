package ru.ifmo.ctd.profiler;

import org.aspectj.lang.Signature;

import java.lang.reflect.Modifier;
import java.util.*;

public class ProfilerAspect {

    private ProfilerAspect() {
    }


    private static String pkg;

    public static String getPkg() {
        return pkg;
    }

    public static void setPkg(String pkg) {
        ProfilerAspect.pkg = pkg;
    }

    private static Map<String, MethodInfo> calls = new HashMap<>();

    public static void reset(){
        calls.clear();
    }

    public static Collection<MethodInfo> getUsages(){
        return calls.values();
    }

    public static void printResult(){
        for (MethodInfo methodInfo : getUsages()) {
            System.out.println(String.format(
                    "%s : %dns overall, %fns average, %dns min, %dns max, %dns times",
                    methodInfo.name, methodInfo.all(), methodInfo.avg(),
                    methodInfo.min(), methodInfo.max(), methodInfo.count()));
        }
    }

    private static aspect AspectJWorker {
        Object around(): execution(* *(..)) && !execution(* ru.ifmo.ctd.profiler.*.*(..)){
            long start = System.nanoTime();
            Object result = proceed();
            long end = System.nanoTime();

            Signature sign = thisJoinPointStaticPart.getSignature();
            if (!Modifier.isAbstract(sign.getModifiers())){
                Class type = sign.getDeclaringType();
                String pkg = type.getPackage().getName();

                if (pkg.startsWith(ProfilerAspect.pkg)){
                    String name = sign.toString();
                    if (!calls.containsKey(name)){
                        calls.put(name, new MethodInfo(name));
                    }
                    calls.get(name).usages.add(new MethodInfo.Usage(start, end - start));
                }
            }
            return result;
        }
    }
}


