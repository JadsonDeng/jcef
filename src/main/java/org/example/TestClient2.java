package org.example;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TestClient2 extends JFrame {

    private CefApp cefApp;
    private CefClient client;
    private CefBrowser browser;
    private Component browserUI;


    private TestClient2(final String startUrl, boolean useOSR, boolean isTransparent, String[] args) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
        CefAppBuilder builder = new CefAppBuilder();
        builder.getCefSettings().windowless_rendering_enabled = useOSR;
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {
            @Override
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });
        if (args.length > 0) {
            builder.addJcefArgs(args);
        }
        builder.setSkipInstallation(true);
        this.cefApp = builder.build();
        this.client = cefApp.createClient();
        this.client.addMessageRouter(CefMessageRouter.create());
        this.browser = client.createBrowser(startUrl, useOSR, isTransparent);
        this.browserUI = browser.getUIComponent();

        this.getContentPane().add(browserUI, BorderLayout.CENTER);
        this.setUndecorated(true); // 不显示顶部按钮，需要放在pack()上面
        this.pack();
        this.setSize(800, 600);
        this.setVisible(true);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });


//        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDeviceMain = graphicsEnvironment.getDefaultScreenDevice();
        screenDeviceMain.setFullScreenWindow(this);
    }

    public static void main(String[] args) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
        new TestClient2("https://www.baidu.com", true, false, args);
    }
}
