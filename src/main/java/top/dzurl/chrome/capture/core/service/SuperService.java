package top.dzurl.chrome.capture.core.service;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import top.dzurl.chrome.capture.core.conf.ChromeCommandConf;
import top.dzurl.chrome.capture.core.model.TaskModel;
import top.dzurl.chrome.capture.core.util.SpringELUtil;
import top.dzurl.chrome.capture.core.util.response.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class SuperService<T extends TaskModel> {

    @Autowired
    private ChromeCommandConf chromeCommandConf;

    /**
     * 执行任务
     *
     * @param driver
     */
    public abstract void task(ChromeDriver driver, T model, File outFile);


    /**
     * 实验性的参数
     *
     * @return
     */
    public Map<String, Map<String, Object>> experimentalOption() {
        return null;
    }


    public String[] options(T model, File outFile) {
        return new String[]{
                "--incognito",
                "--allow-running-insecure-content",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--headless",
                "--disable-gpu",
        };
    }


    /**
     * 文件类型
     *
     * @return
     */
    public abstract ResponseUtil.MimeType mimeType();


    @SneakyThrows
    public void task(HttpServletRequest request, HttpServletResponse response, final T model) {
        Assert.hasText(model.getUrl(), "URL地址不能为空");

        //构建临时文件
        @Cleanup("delete") File outFile = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".png");

        ChromeOptions options = new ChromeOptions();
        Arrays.stream(options(model, outFile)).forEach((it) -> {
            options.addArguments(it);
        });

        //增加实验性的配置
        Optional.ofNullable(experimentalOption()).ifPresent((experimentalOption) -> {
            experimentalOption.entrySet().forEach((entry) -> {
                options.setExperimentalOption(entry.getKey(), entry.getValue());
            });
        });


        ChromeDriver driver = new ChromeDriver(options);
        //timout
        WebDriver.Timeouts timeouts = driver.manage().timeouts();
        timeouts.setScriptTimeout(Duration.ofMillis(chromeCommandConf.getTimeout()));
        timeouts.pageLoadTimeout(Duration.ofMillis(chromeCommandConf.getTimeout()));

        //设置宽度与高度
        driver.manage().window().setSize(new Dimension(model.getWidth(), model.getHeight()));


        //打开页面
        driver.get(model.getUrl());

        //滑动到底部,保证全图加载
        driver.executeScript("window.scrollTo(0,document.body.scrollHeight);");
        Optional.ofNullable(model.getWait()).ifPresent((waitTime) -> {
            if (waitTime > 0) {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));
                try {
                    Thread.sleep(waitTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        driver.executeScript("window.scrollTo(0,0);");


        //自定义处理
        try {
            task(driver, model, outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //退出
        driver.quit();


        //写出流
        if (outFile.exists()) {
            @Cleanup FileInputStream imageInputFileStream = new FileInputStream(outFile);
            writeStream(request, response, imageInputFileStream, outFile.length());
        } else {
            response.sendError(404);
        }
    }

    /**
     * 执行任务
     *
     * @param request
     * @param response
     */
//    @SneakyThrows
//    public void task2(HttpServletRequest request, HttpServletResponse response, T model) {
//
//        //构建临时文件
//        @Cleanup("delete") File file = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".png");
////        model.setOutput(file.getAbsolutePath());
//
//        //执行命令行
//        String cmd = buildCmd(model);
//
//        //生产命令文件
//        @Cleanup("delete") File cmdFile = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".cmd");
//        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(cmdFile);
//        fileOutputStream.write(cmd.getBytes());
//
//        //执行命令文件
//        runCmdFile(cmdFile);
//
//        //写出流
//        if (file.exists()) {
//            @Cleanup FileInputStream imageInputFileStream = new FileInputStream(file);
//            writeStream(request, response, imageInputFileStream, file.length());
//        } else {
//            response.sendError(404);
//        }
//    }

    /**
     * 写出流
     *
     * @param response
     * @param inputStream
     */
    @SneakyThrows
    protected void writeStream(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, long sourceSize) {
        ResponseUtil.writeStream(request, response, inputStream, sourceSize, mimeType());
    }

    /**
     * 启动命令行
     */
    @SneakyThrows
    protected static void runCmdFile(File cmdFile) {
        Runtime runtime = Runtime.getRuntime();
        @Cleanup("destroy") Process p = runtime.exec(createStartCmd(cmdFile));
        p.waitFor();
    }


    /**
     * 构建命令行
     *
     * @return
     */
    protected String buildCmd(Object model, String cmd) {
        return SpringELUtil.parseExpression(model, cmd).toString();
    }

    /**
     * 是否win系统
     *
     * @return
     */
    private static boolean isWin() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }


    /**
     * 构建启动的命令行
     *
     * @param cmdFile
     * @return
     */
    private static String[] createStartCmd(File cmdFile) {
        String filePath = cmdFile.getAbsolutePath();
        return isWin() ? new String[]{"cmd", "/c", filePath} : new String[]{"sh", filePath};
    }

}
