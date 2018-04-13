package ru.ifmo.ctd.profiler;

import org.junit.Assert;
import org.junit.Test;
import ru.ifmo.ctd.pk1.TestClass1;

import java.util.Collection;

public class ProfilerAspectTest {
    @Test
    public void anotherPackageTest() {
        ProfilerAspect.setPkg("some.package");
        ProfilerAspect.reset();
        new TestClass1().test();
        Assert.assertTrue(ProfilerAspect.getUsages().isEmpty());
    }

    @Test
    public void testClass1() {
        ProfilerAspect.setPkg("ru.ifmo.ctd.pk1");
        ProfilerAspect.reset();
        new TestClass1().test();
        Assert.assertEquals(5, ProfilerAspect.getUsages().size());
        Collection<MethodInfo> info = ProfilerAspect.getUsages();
        for (MethodInfo methodInfo : info) {
            if (methodInfo.name.contains("f()")) {
                Assert.assertEquals(1, methodInfo.count());
            } else if (methodInfo.name.contains("f(int)")) {
                Assert.assertEquals(2, methodInfo.count());
            } else if (methodInfo.name.contains("f(double, double)")) {
                Assert.assertEquals(3, methodInfo.count());
            } else if (methodInfo.name.contains("test()")) {
                Assert.assertEquals(1, methodInfo.count());
            } else if (methodInfo.name.contains("g()")) {
                Assert.assertEquals(1, methodInfo.count());
            } else {
                Assert.fail();
            }
        }
    }
}