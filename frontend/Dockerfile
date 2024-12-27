FROM node:21-alpine3.19

WORKDIR .

COPY yarn.lock package.json ./

RUN yarn install

COPY . .

EXPOSE 3000

CMD ["yarn", "run", "start", "--host", "0.0.0.0"]