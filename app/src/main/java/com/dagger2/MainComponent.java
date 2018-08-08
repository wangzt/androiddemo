package com.dagger2;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
//@Component(modules = {MainModule.class})
@Component(modules = {AModule.class, BModule.class})
public interface MainComponent {
    void inject(MyActivity activity);
}
