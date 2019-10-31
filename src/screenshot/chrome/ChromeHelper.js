var Config = require('../ChromeConfig')
var spawn = require('child_process').exec;

const CDP = require('chrome-remote-interface');
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
                }).then(resolve)
        })
    };


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
            waitTimeout: 2 * 60 * 1000
        })

        return chromeless.goto(config.url)
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
                chrome.kill();
                return this;
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
            chromeFlags: ['--disable-gpu', '--incognito', '--disable-dev-shm-usage', '--no-sandbox', '--disable-gpu', '--headless']//'--headless'
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
