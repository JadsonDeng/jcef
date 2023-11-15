package org.example;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefBuildInfo;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import sun.lwawt.macosx.CEmbeddedFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        CefAppBuilder builder = new CefAppBuilder();
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {
            @Override
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });

        builder.addJcefArgs(args);
        builder.setSkipInstallation(false);

        CefApp app = builder.build();
        CefBuildInfo buildInfo = CefBuildInfo.fromClasspath();
        System.out.println(buildInfo);
        CefApp.CefVersion version = app.getVersion();
        System.out.println(version);
        CefSettings settings = new CefSettings();
        settings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_FATAL;
        app.setSettings(settings);


        CefClient client = app.createClient();
        boolean isTransparent = true;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = graphicsEnvironment.getScreenDevices();

        // 主屏幕
        final CefBrowser browser = client.createBrowser("http://127.0.0.1:3301", false, isTransparent);
        Component uiComponent = browser.getUIComponent();

        GraphicsDevice screenDeviceMain = graphicsEnvironment.getDefaultScreenDevice();
        final JFrame main = new JFrame("test");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.getContentPane().add(uiComponent, BorderLayout.CENTER);
        main.setVisible(true);
        Rectangle bounds = screenDeviceMain.getDefaultConfiguration().getBounds();
        main.setSize(bounds.width, bounds.height);
        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                main.dispose();
                System.exit(0);
            }
        });
//        screenDeviceMain.setFullScreenWindow(main);
/*
        // 副屏幕
        GraphicsDevice screenDeviceSecond = screenDevices[1];
        final CefBrowser secondBrowser = client.createBrowser("https://www.baidu.com", false, isTransparent);
        Component secondComponent = secondBrowser.getUIComponent();

        final JFrame second = new JFrame("test");
        second.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        second.getContentPane().add(secondComponent, BorderLayout.CENTER);
        second.setSize(800, 600);
        second.setLocation(0, 0);
        second.setVisible(true);
        Rectangle bounds2 = screenDeviceSecond.getDefaultConfiguration().getBounds();
        second.setSize(bounds2.width, bounds2.height);
        second.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                second.dispose();
                System.exit(0);
            }
        });
        screenDeviceSecond.setFullScreenWindow(second);*/
    }
}
