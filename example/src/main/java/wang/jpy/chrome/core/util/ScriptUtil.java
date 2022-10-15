package wang.jpy.chrome.core.util;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptUtil {


    /**
     * 转换脚本为对象
     *
     * @param scriptContent
     * @return
     */
    public static Script parse(String scriptContent) {
        return new GroovyShell().parse(scriptContent);
    }


}
