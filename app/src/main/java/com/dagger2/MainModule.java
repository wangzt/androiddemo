package com.dagger2;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    A provideA(B b) {
        return new A(b);
    }

    @Provides
    B provideB() {
        return new B();
    }
}
