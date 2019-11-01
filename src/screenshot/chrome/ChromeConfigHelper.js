module.exports = new function () {

    //默认的配置
    var defaultConfig = function () {
        return {
            url: null,
            path: null,
            timeout: 1000 * 60 * 1,
            waitTime: 0,
            w: 1920,
            h: 1080
        }
    }


    var copyValue = function (source, target, key, isNumber) {
        if (source[key]) {
            target[key] = isNumber ? parseInt(source[key]) : source[key];
        }
    }

    /**
     * 创建配置文件
     * @param requestBody
     */
    this.createConfig = function (requestBody) {
        let config = new defaultConfig()

        copyValue(requestBody, config, 'url', false)
        copyValue(requestBody, config, 'timeout', true)
        copyValue(requestBody, config, 'waitTime', true)
        copyValue(requestBody, config, 'w', true)
        copyValue(requestBody, config, 'h', true)

        return config;
    }

    /**
     * 拷贝数值
     * @param source
     * @param target
     * @param key
     * @param isNumber
     */


}
