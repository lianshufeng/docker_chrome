var express = require('express');
var chromeHelper = require('../chrome/ChromeHelper');
var chromeConfigHelper = require('../chrome/ChromeConfigHelper')
var fs = require('fs');
var path = require('path');
var router = express.Router();


/* GET users listing. */
router.get('/', function (req, res, next) {
    tips(res);
});


/**
 * post请求
 */
router.post('/', function (req, res, next) {


    //必要参数
    if (req.body.url == null || req.body.w == null || req.body.h == null) {
        tips(res);
        return;
    }


    let config = chromeConfigHelper.createConfig(req.body);


    //创建临时目录
    if (!fs.existsSync('tmp')) {
        fs.mkdirSync('tmp');
    }


    //创建命令行
    config.path = path.join(process.cwd(), 'tmp', 'screenshot-' + new Date().getTime() + '.png');

    //截图
    chromeHelper
        .screenshot(config)
        .then(() => {

            //文件不存在
            if (!fs.existsSync(config.path)) {
                res.writeHead(404);
                res.end();
                return;
            }

            //返回给调用者
            res.writeHead(200, {'Content-Type': 'image/png'});
            let stream = fs.createReadStream(config.path);
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
                console.log('remove : ' + config.path);
                if (fs.existsSync(config.path)) {
                    fs.unlinkSync(config.path);
                }
            }, 60000);


        })


});


/**
 * 提示语法错误
 * @param res
 */
let tips = function (res) {
    res.send('请用 post 表单请求下列参数  :\n' + JSON.stringify(chromeConfigHelper.createConfig({})));
}


module.exports = router;
