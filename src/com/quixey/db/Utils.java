package com.quixey.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Collection of utility methods.
 * <p/>
 */
public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static final String PASS = Utils.class + "_PASS";

    public static final String FAIL = Utils.class + "_FAIL";

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd' 'HH:mm:ss.SSS";

    private static final String TIME_FORMAT = "HH:mm:ss.SSS";

    private static final String DATE_TIME_STRING = "yyyy_MM_dd_HH_mm_ss_SSS";

    /**
     * system specific file path separator, "/" for Linux, and "\" for Windows, etc
     */
    public static final String FS = System.getProperty("file.separator");

    private Utils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Please use Utils.method");
    }

    public static String formatDateTime(long milliSinceEpoch) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(milliSinceEpoch));
    }

    public static String formatTime(long milliSinceEpoch) {
        return new SimpleDateFormat(TIME_FORMAT).format(new Date(milliSinceEpoch));
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat(DATE_TIME_STRING).format(System.currentTimeMillis());
    }

    public static String addLog4jFileAppender(String file) throws IOException {
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();

        String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %C.%M:%L - %m%n";
        final String threadName = Thread.currentThread().getName();

        class ThreadFilter extends Filter {
            @Override
            public int decide(LoggingEvent event) {
                if (event.getThreadName().startsWith(threadName)) {
                    return Filter.ACCEPT;
                }
                return Filter.DENY;
            }
        }

        FileAppender fa = new FileAppender(new PatternLayout(pattern), file);
        fa.addFilter(new ThreadFilter());
        fa.setThreshold(Level.DEBUG);

        fa.setImmediateFlush(true);
        fa.setAppend(true);
        fa.setName(file);

        fa.activateOptions();
        rootLogger.addAppender(fa);

        return file;
    }

    public static void removeLog4jAppender(String appenderName) {
        if (appenderName == null) {
            LOG.warn("Appender name is null");
            return;
        }
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        Appender appender = rootLogger.getAppender(appenderName);
        if (appender != null) {
            appender.close();
            rootLogger.removeAppender(appender);
        }
    }

    /**
     * Executes command, and waits for the expected phrase in console printout.
     *
     * @param command
     *
     * @return console output as a list of strings
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> cmd(String command) throws IOException, InterruptedException {
        return cmd(command.split(" "));
    }

    /**
     * Executes command, and waits for the expected phrase in console printout.
     *
     * @param commands
     *
     * @return console output as a list of strings
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> cmd(String[] commands) throws IOException, InterruptedException {
        return cmd(commands, null, null, 300000L, null);
    }

    /**
     * Executes command, and waits for the expected phrase in console printout.
     *
     * @param commands
     *
     * @return console output as a list of strings
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> cmdWithWorkingDir(String[] commands, String workindDir, final long timeout) throws IOException,
            InterruptedException {
        return cmd(commands, null, null, timeout, workindDir);
    }

    /**
     * Executes command, and waits for the expected phrase in console printout.
     * <p/>
     * @param commands
     * @param expected
     *
     * @return console output as a list of strings
     *
     * @throws IOException
     * @throws CorpException
     */
    public static List<String> cmd(String[] commands, String expected) throws IOException, InterruptedException {
        return cmd(commands, expected, null, 300000L, null);
    }

    /**
     * Executes command, and waits for the expected pass/fail phrase in console printout within timeout,
     *
     * @param commands
     * @param pass     skip checking if null
     * @param fail     skip checking if null
     * @param timeout  set 0 for not to check the output message, otherwise, waiting for timeout
     *
     * @return console output as a list of strings
     *
     * @throws IOException
     * @throws CorpException
     */
    public static List<String> cmd(String[] commands, final String pass, final String fail, final long timeout,
            final String workingDir) throws
            IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        if (workingDir != null) {
            pb.directory(new File(workingDir));
        }
        pb.redirectErrorStream(true);
        LOG.debug("Running command: " + pb.command().toString().replace(",", ""));
        final Process p = pb.start();
        Thread thread;
        final List<String> output = new ArrayList<>();

        if (timeout == 0) {
            LOG.debug("This is a start-and-exit command");
            output.add(PASS);
            return output;
        } else {
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        LOG.debug("Command timeouts in {} ms", timeout);
                        Thread.sleep(timeout);
                        try {
                            p.exitValue();
                        } catch (IllegalThreadStateException ex) {
                            LOG.debug("killing subprocess {} - {}", p, ex.getMessage());
                            p.destroy();
                        }
                    } catch (InterruptedException ex) {
                        LOG.trace("{}", ex.getMessage());
                    } catch (Exception ex) {
                        LOG.warn(ex.getMessage());
                    }
                }
            };
            thread.setName(Thread.currentThread().getName() + "-" + p.hashCode());
            thread.setDaemon(true);
            thread.start();
        }
        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String c = p + " - ";
        for (String line = stdIn.readLine(); line != null;) {
            LOG.trace("{}{}", c, line);
            output.add(line);
            try {
                line = stdIn.readLine();
            } catch (IOException ex) {
                LOG.warn(ex.getMessage());
                break;
            }
        }
        LOG.debug("Command exit code {}", p.waitFor());
        thread.interrupt();
        try {
            stdIn.close();
        } catch (IOException ex) {
            LOG.warn("", ex);
        }

        for (String s : output) {
            if (pass != null && (s.contains(pass) || s.matches(pass))) {
                output.add(PASS);
                break;
            } else if (fail != null && s.contains(fail)) {
                output.add(FAIL);
                break;
            }
        }
        return output;
    }

    public static Process cmd(String[] commands, final File file) throws IOException {
        return cmd(commands, file, new String[0]);
    }

    public static Process cmd(String[] commands, final File file, final String... ignoreRegex) throws IOException {
        FileUtils.touch(file);

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        LOG.info("Running command: " + pb.command().toString().replaceAll(",", ""));
        final Process p = pb.start();

        Thread t = new Thread() {
            @Override
            public void run() {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(file);
                    for (String line = stdIn.readLine(); line != null;) {
                        if (null == ignoreRegex || ignoreRegex.length == 0) {
                            pw.println(line);
                        } else {
                            boolean ignore = false;
                            for (String regex : ignoreRegex) {
                                if (!regex.isEmpty() && (line.contains(regex) || line.matches(regex))) {
                                    ignore = true;
                                    break;
                                }
                            }
                            if (!ignore) {
                                pw.println(line);
                            }
                        }
                        line = stdIn.readLine();
                    }
                } catch (IOException ex) {
                    LOG.warn(ex.getMessage());
                } finally {
                    if (pw != null) {
                        pw.flush();
                        pw.close();
                    }
                }
                LOG.trace("command is done");
            }
        };
        t.setDaemon(true);
        t.start();
        return p;
    }

    public static void waitForProcess(final Process process, final long timeout) throws InterruptedException {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    LOG.warn(ex.getMessage());
                } finally {
                    if (process != null) {
                        process.destroy();
                    }
                }
            }
        };
        t.start();
        int exitValue = process.waitFor();
        LOG.trace("process {} exits with {}", process, exitValue);
    }

    public static void deleteFileAfterMinutes(final File file, final int minutes) {
        file.deleteOnExit();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(minutes * 60000);
                    //FileUtils.deleteQuietly(file);
                } catch (Exception ex) {
                    LOG.trace(ex.getMessage());
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }

    
    public static String getUniqueId() {
        return System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
    }

    public static void logVmStat(final long intervalMillis) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        cmd("vmstat 2 5 -S M".split(" "));
                        Thread.sleep(intervalMillis);
                    } catch (Exception ex) {
                        LOG.warn(ex.getMessage());
                        return;
                    }
                }
            }
        };
        thread.setName(Thread.currentThread().getName() + "-vmstat-" + thread.hashCode());
        thread.setDaemon(true);
        LOG.info("Starting vmstat monitoring thread");
        thread.start();
    }

    /**
     *
     * @param dir
     * @param keepAliveHour any file/directory having last modified time longer than keepAliveHour will be deleted
     */
    public static void cleanDirectory(final String path, final float keepAliveHour) {
        final long intervalMillis = 3600000;
        final File dir = new File(path);
        if (!dir.exists()) {
            return;
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    long lastModifiedMillis = (long) (System.currentTimeMillis() - keepAliveHour * 3600000);
                    File[] files = dir.listFiles();
                    for (File file : files) {
                        if (file.lastModified() < lastModifiedMillis) {
                            LOG.debug("deleting {}", file);
                            /*
                            if (!FileUtils.deleteQuietly(file)) {
                                LOG.debug("Cannot delete {}", file);
                            }
                            */
                        }
                    }
                    try {
                        Thread.sleep(intervalMillis);
                    } catch (Exception ex) {
                        LOG.warn(ex.getMessage());
                        return;
                    }
                }
            }
        };
        thread.setName(Thread.currentThread().getName() + "-cleaning-" + thread.hashCode());
        thread.setDaemon(true);
        LOG.info("Starting directory cleaning thread (scanning hourly), "
                + "all files/directories in {} and older than {} hour(s) will be deleted", dir, keepAliveHour);
        thread.start();
    }

    public static void main(String[] args) throws Exception {
    	List<String> ss = Utils.cmd(new String[]{"ls", "-la"});
        for (String s : ss) {
            LOG.info("{}", s);
        }
        System.exit(0);
    }
}

