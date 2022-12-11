package main.presentation;

import java.awt.*;

public class Main {
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater (new Runnable() {
            public void run()
            {
                CtrlPresentation cp = CtrlPresentation.getInstance();
                cp.initiateApp();
                cp.showDocuments(new Point(600, 300), new Dimension(500, 500));
            }
        });
    }
}
