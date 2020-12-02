package top.dzurl.chrome.capture.core.util;


import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * EL 表达式
 */
public class SpringELUtil {


    /**
     * 执行el表达式
     *
     * @param beanObj
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> T parseExpression(Object beanObj, String expression) {
        Map<String, Object> ret = (beanObj instanceof Map) ? (Map<String, Object>) beanObj : bean2Map(beanObj);
        return parseExpression(ret, expression);
    }


    public static Map<String, Object> bean2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        try {
            // 获取javaBean的BeanInfo对象
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
            // 获取属性描述器
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                // 获取属性名
                String key = propertyDescriptor.getName();
                // 获取该属性的值
                Method readMethod = propertyDescriptor.getReadMethod();
                // 通过反射来调用javaBean定义的getName()方法
                Object value = readMethod.invoke(obj);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 执行el表达式
     *
     * @param val
     * @param expression
     * @param <T>
     * @return
     */
    private static <T> T parseExpression(Map<String, Object> val, String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        EvaluationContext ctx = null;
        if (val != null) {
            ctx = new StandardEvaluationContext();
            //在上下文中设置变量，变量名为user，内容为user对象
            for (Map.Entry<String, Object> entry : val.entrySet()) {
                ctx.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return (T) (ctx == null ? exp.getValue() : exp.getValue(ctx));
    }


    /**
     * 执行el表达式
     *
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> T parseExpression(String expression) {
        return parseExpression(null, expression);
    }

}
