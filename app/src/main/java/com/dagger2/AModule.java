package com.dagger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (includes = {BModule.class})
public class AModule {

    @Singleton
    @Provides
    A provideA(B b) {
        return new A(b);
    }
}
