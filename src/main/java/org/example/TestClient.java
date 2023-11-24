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

public class TestClient {
    public void create(String[] args, String url) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
        CefAppBuilder builder = new CefAppBuilder();
        builder.getCefSettings().windowless_rendering_enabled = true;
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {
            @Override
            public void stateHasChanged(CefApp.CefAppState state) {
                // Shutdown the app if the native CEF part is terminated
                if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
            }
        });
        if (args.length > 0) {
            builder.addJcefArgs(args);
        }
        builder.setSkipInstallation(true);
        CefApp cefApp = builder.build();
        CefClient client = cefApp.createClient();

        CefMessageRouter messageRouter = CefMessageRouter.create();
        client.addMessageRouter(messageRouter);

        boolean useOSR = true;
        boolean isTransparent = false;
        CefBrowser browser = client.createBrowser(url, useOSR, isTransparent);
        Component uiComponent = browser.getUIComponent();


        final JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(uiComponent, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);

        jFrame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                jFrame.dispose();
            }
        });

    }

    public static void main(String[] args) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
        new TestClient().create(args, "https://www.baidu.com");

    }
}
