/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.samples;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Scanner;
import org.eclipse.jetty.webapp.WebAppContext;

// new JettyServer(path, 8080, "").start();
public class JettyServer {
    private int port;
    private String context;
    private String webappPath;
    private int scanIntervalSeconds;
    private boolean jmxEnabled;
    private Server server;
    private WebAppContext webapp;

    public JettyServer(String webappPath, int port, String context) {
        this(webappPath, port, context, 0, false);
    }

    public JettyServer(String webappPath, int port, String context, int scanIntervalSeconds, boolean jmxEnabled) {
        this.webappPath = webappPath;
        this.port = port;
        this.context = context;
        this.scanIntervalSeconds = scanIntervalSeconds;
        this.jmxEnabled = jmxEnabled;
        validateConfig();
    }

    private void validateConfig() {
        if (port < 0 || port > 65536) {
            throw new IllegalArgumentException("Invalid port of web server: " + port);
        }
        if (context == null) {
            throw new IllegalStateException("Invalid context of web server: " + context);
        }
        if (webappPath == null) {
            throw new IllegalStateException("Invalid context of web server: " + webappPath);
        }
    }

    public void start() {
        if (server == null || server.isStopped()) {
            try {
                doStart();
            } catch (Throwable e) {
                e.printStackTrace();
                System.err.println("System.exit() ......");
                System.exit(1);
            }
        } else {
            throw new RuntimeException("Jetty Server already started.");
        }
    }

    private void doStart() throws Throwable {
        if (!portAvailable(port)) {
            throw new IllegalStateException("port: " + port + " already in use!");
        }

        System.setProperty("org.eclipse.jetty.util.URI.charset", "UTF-8");
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.Slf4jLog");
        
        webapp = new WebAppContext(webappPath, context);
        server = new Server(port);
        server.setHandler(webapp);

        if (jmxEnabled) {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
            server.addBean(mBeanContainer);
        }

        if (scanIntervalSeconds > 0) {
            startFileWatchScanner();
        }

        long ts = System.currentTimeMillis();
        server.start();

        ts = System.currentTimeMillis() - ts;
        System.err.println("Jetty Server started: " + String.format("%.2f sec", ts / 1000d));

        server.join();
    }

    private void startFileWatchScanner() throws Exception {
        List<File> scanList = new ArrayList<File>();
        scanList.add(new File(webappPath, "WEB-INF"));

        Scanner scanner = new Scanner();
        scanner.setReportExistingFilesOnStartup(false);
        scanner.setScanInterval(scanIntervalSeconds);
        scanner.setScanDirs(scanList);
        scanner.addListener(new Scanner.BulkListener() {
            @SuppressWarnings("rawtypes")
            @Override
            public void filesChanged(List changes) {
                try {
                    System.err.println("Loading changes ......");
                    webapp.stop();
                    webapp.start();
                    System.err.println("Loading complete.\n");
                } catch (Exception e) {
                    System.err.println("Error reconfiguring/restarting webapp after change in watched files");
                    e.printStackTrace();
                }
            }
        });
        System.err.println("Starting scanner at interval of " + scanIntervalSeconds + " seconds.");
        scanner.start();
    }

    private static boolean portAvailable(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) try {
                ss.close();
            } catch (IOException e) {
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        new JettyServer("webapps", 80, "/").start();
    }
}
