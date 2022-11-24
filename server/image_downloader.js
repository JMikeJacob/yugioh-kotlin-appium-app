const fs = require('fs');
const axios = require('axios');

const images = require('./images_small.json');

const sleep = async function(timeout) {
    await new Promise((resolve) => {
        setTimeout(resolve, timeout)
    });
};

const downloadImage = async function(imageUrl, name) {
    console.log(imageUrl);
    await axios(imageUrl, {
        responseType: 'stream'
    }).then((response) => {
        new Promise((resolve, reject) => {
            response.data.pipe(fs.createWriteStream(`images/cards_small/${name}`))
            .on('finish', () => resolve())
            .on('error', e => reject(e));
        });
    });
};

const process = async function() {
    for (const imageUrl of images) {
        const urlParts = imageUrl.split('/');
        const name = urlParts[urlParts.length - 1];

        console.log(`downloading ${imageUrl}`);
        await downloadImage(imageUrl, name);
        await sleep(1000);
    }
};

(async () => {
    await process();
})();