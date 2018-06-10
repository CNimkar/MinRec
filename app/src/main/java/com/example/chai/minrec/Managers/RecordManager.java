package com.example.chai.minrec.Managers;

public interface RecordManager {
    public String fileName(String fileName);

    public void record(String fileName);

    public void stop();

    public void recorderCleanup();
}
