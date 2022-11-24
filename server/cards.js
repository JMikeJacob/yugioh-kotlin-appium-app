const cards = require('./cards.json').data;
const fs = require('fs');

const images = [];
const images_small = [];

for (let card of cards) {
    images.push(card.card_images[0].image_url)
    images_small.push(card.card_images[0].image_url_small)
}

fs.writeFileSync('images.json', JSON.stringify(images));
fs.writeFileSync('images_small.json', JSON.stringify(images_small));