FROM node:20-alpine3.17 AS ui-build
WORKDIR ./app
COPY public/ /app/public
COPY src/ /app/src
COPY .env.development /app
COPY .env.production /app
COPY ./package.json /app
RUN npm install
CMD ["npm", "start"]
