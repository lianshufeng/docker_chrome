const chromeLauncher = require('chrome-launcher');
const {Chromeless} = require('chromeless')

module.exports = new function () {


    /**
     * 截图
     */
    this.screenshot = function (config) {
        //随机端口
        let port = randomNum(81, 65535)
        return new Promise((resolve, reject) => {
            createChromeProcess(port) //创建调试浏览器进程
                .then((chrome) => {
                    return renderChrome(chrome, config)
                })
                .then(killChromeProcess)
                .then(resolve)
        })
    };

    /**
     * 关闭浏览器
     */
    killChromeProcess = function (chrome) {
        console.log('kill ： ' + chrome.port);
        chrome.kill();
    }


    /**
     * 连接浏览器
     * @param port
     */
    renderChrome = function (chrome, config) {
        //连接
        let chromeless = new Chromeless({
            debug: true,
            cdp: {
                port: chrome.port
            },
            viewport: {
                width: parseInt(config.w),
                height: parseInt(config.h)
            },
            waitTimeout: config.timeout
        })


        return chromeless.goto(config.url)
            .wait(config.waitTime <= 0 ? 1 : config.waitTime)
            .evaluate(function () {
                return {'w': document.body.scrollWidth, 'h': document.body.scrollHeight};
            })
            .then(async function (scroll) {
                console.log(scroll);
                await chromeless.setViewport({
                    width: parseInt(scroll.w),
                    height: parseInt(scroll.h)
                })
                return this;
            })
            .then(async () => {
                await chromeless.screenshot({
                    filePath: config.path
                })
                return this;
            })
            .then(async () => {
                await chromeless.end();
                return this;
            })
            .then(() => {
                return new Promise((resolve, reject) => {
                    resolve(chrome)
                });
            })
            .catch((e) => {
                console.error(e);
                return new Promise((resolve, reject) => {
                    resolve(chrome)
                });
            })
    }


    /**
     * 创建浏览器进程
     * @param port
     * @returns {Promise<unknown>}
     */
    createChromeProcess = function (port) {
        return chromeLauncher.launch({
            port: port, // Uncomment to force a specific port of your choice.
            chromeFlags: ['--disable-gpu', '--incognito', '--disable-dev-shm-usage', '--no-sandbox', '--headless']
        });
    }


    /**
     * 模板替换
     * @param template
     * @param map
     * @returns {string}
     */
    template = function (template, map) {
        let cmd = template;
        for (let key in map) {
            cmd = cmd.split('${' + key + '}').join(map[key]);
        }
        return cmd;
    }


    /**
     * 获取随机数
     * @param minNum
     * @param maxNum
     * @returns {number}
     */
    randomNum = function (minNum, maxNum) {
        switch (arguments.length) {
            case 1:
                return parseInt(Math.random() * minNum + 1, 10);
                break;
            case 2:
                return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
                break;
            default:
                return 0;
                break;
        }
    }


}
