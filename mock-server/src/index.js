const express = require("express");
const heros = require("./heros.json");

const bodyParser = require('body-parser')//add this

const app = express();
const PORT = 3086;
const TIME_OUT = 5000;

app.use(bodyParser())//add this before any route or before using req.body

app.get("/health", (req, res) => res.send("Ok"))

app.get("/context/v2/heros/:heroid", (req,res) => {
    logRequest('GET', req.url, req.body, req.params);
    return setTimeout(() => res.json(heros), TIME_OUT);
})

app.post("/context/v2/Heros", (req, res) => {
    logRequest('POST', req.url, req.body, req.params);
    return setTimeout(() => {
        res.status(200).send()
    }, TIME_OUT)
})

app.post("/context/v2/SaveHero", (req, res) => {
    logRequest('POST', req.url, req.body, req.params);
    return setTimeout(() => {
        res.status(200).send()
    }, TIME_OUT)
})

app.post("/context/v2/SaveHeros", (req, res) => {
    logRequest('POST', req.url, req.body, req.params);
    return setTimeout(() => {
        res.status(200).send()
    }, TIME_OUT)
})

app.listen(PORT, () => console.log(`Mock now running on ${PORT}`));

const reset = '\x1b[0m';
const green = '\x1b[32m';
const yellow = '\x1b[33m';
const cyan = '\x1b[36m'

const logRequest = (method, url, body, params) => {

    const resource = `${green}${method} ${url}${reset}`;
    const requestBody = body
        ? `${yellow}\n${JSON.stringify(body, null, 4)}${reset}`
        : '';
    const requestParams = params
        ? `${cyan}\n${JSON.stringify(params, null, 4)}${reset}`
        : '';
    console.log(`${resource}${requestBody}${requestParams}`); // eslint-disable-line
};
