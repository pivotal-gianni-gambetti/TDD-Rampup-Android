package com.pivotallabs.testing;

import static roboguice.RoboGuice.getInjector;
import static roboguice.RoboGuice.newDefaultRoboModule;
import static roboguice.RoboGuice.setBaseApplicationInjector;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import roboguice.RoboGuice;

public class RobolectricTestRunnerWithInjection extends RobolectricTestRunner {
    public RobolectricTestRunnerWithInjection(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifeCycleWithInjection.class;
    }

    public static class TestLifeCycleWithInjection extends DefaultTestLifecycle {

        private final TestApplicationModule module = new TestApplicationModule();

        @Override
        public void beforeTest(Method method) {
            super.beforeTest(method);
            setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, newDefaultRoboModule(Robolectric.application), module);
        }

        @Override
        public void prepareTest(Object test) {
            getInjector(Robolectric.application).injectMembers(test);
        }
    }
}