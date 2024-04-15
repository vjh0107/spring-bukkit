package kr.junhyung.testplugin;

import kr.junhyung.springbukkit.SpringPlugin;
import org.springframework.lang.NonNull;

public class TestPlugin extends SpringPlugin {
    @Override
    protected @NonNull Class<?> getApplicationClass() {
        return TestApplication.class;
    }
}

