var express = require('express');
var chromeHelper = require('../chrome/ChromeHelper');
var fs = require('fs');
var path = require('path');
// var spawn = require('child_process').execSync;
var router = express.Router();


/* GET users listing. */
router.get('/', function (req, res, next) {
    tips(res);
});


/**
 * post请求
 */
router.post('/', function (req, res, next) {

    let url = req.body.url;
    let w = req.body.w;
    let h = req.body.h;

    //必要参数
    if (url == null || w == null || h == null) {
        tips(res);
        return;
    }


    //创建临时目录
    if (!fs.existsSync('tmp')) {
        fs.mkdirSync('tmp');
    }


    //创建命令行
    let tmpFile = path.join(process.cwd(), 'tmp', 'screenshot-' + new Date().getTime() + '.png');


    //截图
    chromeHelper
        .screenshot({
            'url': url,
            'w': w,
            'h': h,
            'path': tmpFile
        })
        .then(() => {

            //文件不存在
            if (!fs.existsSync(tmpFile)) {
                res.writeHead(404);
                res.end();
                return;
            }

            //返回给调用者
            res.writeHead(200, {'Content-Type': 'image/png'});
            let stream = fs.createReadStream(tmpFile);
            let responseData = [];//存储文件流
            if (stream) {//判断状态
                stream.on('data', function (chunk) {
                    responseData.push(chunk);
                });
                stream.on('end', function () {
                    let finalData = Buffer.concat(responseData);
                    res.write(finalData);
                    res.end();
                });
            }


            //删除临时文件
            setTimeout(function () {
                console.log('remove : ' + tmpFile);
                if (fs.existsSync(tmpFile)) {
                    fs.unlinkSync(tmpFile);
                }
            }, 60000);




        })



});


/**
 * 创建cmd
 * @param map
 * @returns {string}
 */
let createCmd = function (map) {
    let cmd = require('../ChromeConfig').cmd;
    for (let key in map) {
        cmd = cmd.split('${' + key + '}').join(map[key]);
    }
    return cmd;
}


/**
 * 提示语法错误
 * @param res
 */
let tips = function (res) {
    res.send('请用 post  , url : 网页地址 , w : 宽度  ， h : 高度');
}


module.exports = router;
