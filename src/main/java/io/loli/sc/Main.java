package io.loli.sc;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Main {
    public static void main(String[] args) {
        GraphicsDevice[] devices =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (int i = 0; i < devices.length; i++) {
        GraphicsDevice device = devices[i];
        System.out.println("Device " + i + ": ID string=" +
        device.getIDstring());
        System.out.println(" Available accelerated memory: " +
        device.getAvailableAcceleratedMemory());
        System.out.println(" Fullscreen supported: " +
        device.isFullScreenSupported());
        System.out.println(" Display change supported: " +
        device.isDisplayChangeSupported());
        System.out.println();
        }
        }
}