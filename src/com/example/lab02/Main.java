package com.example.lab02;

import com.example.lab02.model.Sample;
import com.example.lab02.service.ArgumentFactory;
import com.example.lab02.service.MethodRunner;

public class Main
{
    private static final String LOG_FORMAT_KEY = "java.util.logging.SimpleFormatter.format";
    private static final String LOG_FORMAT = "%5$s%n";

    public static void main(String[] args)
    {
        System.setProperty(LOG_FORMAT_KEY, LOG_FORMAT);

        Sample sample = new Sample();
        MethodRunner runner = new MethodRunner(new ArgumentFactory());
        runner.runAll(sample);
    }
}
