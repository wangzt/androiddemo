package com.dagger2;

import dagger.Module;
import dagger.Provides;

@Module
public class BModule {

    @Provides
    B provideB() {
        return new B();
    }
}
