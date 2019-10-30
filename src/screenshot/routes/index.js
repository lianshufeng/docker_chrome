var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.send('哟哟，没有主页');
});


module.exports = router;
