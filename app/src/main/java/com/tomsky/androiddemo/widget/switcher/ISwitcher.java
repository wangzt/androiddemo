package com.tomsky.androiddemo.widget.switcher;

public interface ISwitcher {
    void next();
    void previous();
    int size();
    void setSwitcherClickListener(SwitcherClickListener switcherClickListener);

    public interface SwitcherClickListener {
        void onItemClick(int pos);
    }
}
