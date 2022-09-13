package com.example.drawau;

public interface DrawauIObservable
{
    void subscribe(DrawauArrow sub, double selfX, String mode);
    void notifySubscribers();
}
