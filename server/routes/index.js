var express = require('express');
var router = express.Router();
const controller = require('../controllers/controller');
/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/cards', controller.getCards);
router.get('/cards/:id', controller.getCard);
router.get('/images/cards/:filename', controller.getCardImage);
router.get('/images/cards_small/:filename', controller.getCardImageSmall);


module.exports = router;
