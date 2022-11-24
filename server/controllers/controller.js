const cards = require('../data/cards.json').data;
const path = require('path');

const controller = {
    getCards: (req, res, next) => {
        const itemsPerPage = req.query.itemsPerPage ? req.query.itemsPerPage : 15;
        const page = req.query.page ? req.query.page : 1
        const start = (page - 1) * itemsPerPage;
        const search = req.query.search || null
        let end = start + itemsPerPage - 1;

        let filteredCards = cards;
        if (search) {
            filteredCards = cards.filter((card) => card.name.toLowerCase().indexOf(search.toLowerCase()) !== -1);
        }

        if (end > filteredCards.length) {
            end = filteredCards.length;
        }

        const cardsList = filteredCards.slice(start, end).map((card) => {
            return {
                id: card.id,
                name: card.name,
                type: card.type,
                atk: card.atk || null,
                def: card.def || null,
                level: card.level,
                race: card.race,
                attribute: card.attribute,
                price: card.card_prices[0].cardmarket_price,
                image_uri: `http://192.168.55.105:3000/images/cards/${card.id}.jpg`,
                image_small_uri: `http://192.168.55.105:3000/images/cards_small/${card.id}.jpg`,
                description: card.desc
            }
        });

        res.send(JSON.stringify({
            items: cardsList,
            totalItems: filteredCards.length,
            page: page,
            itemsPerPage: itemsPerPage
        }));
    },

    getCard: (req, res, next) => {
        console.log(req.params);
        const card = cards.find(card => card.id == req.params.id);

        if (card) {
            res.send(JSON.stringify(card));
        } else {
            res.status(404).send({ status: 404, message: "Card not found" });
        }
    },

    getCardImage: (req, res, next) => {
        const filename = req.params.filename;

        const image = path.join(__dirname, '..', 'images', 'cards', filename);
        console.log(image);

        res.sendFile(image);
    },

    getCardImageSmall: (req, res, next) => {
        const filename = req.params.filename;

        const image = path.join(__dirname, '..', 'images', 'cards_small', filename);
        console.log(image);

        res.sendFile(image);
    }
}

module.exports = controller;