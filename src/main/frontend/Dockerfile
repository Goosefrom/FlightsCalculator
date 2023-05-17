FROM node:20.1.0-alpine
WORKDIR /flightsFrontend
ENV PATH /flightsFrontend/node_modules/.bin:$PATH
COPY package.json .
COPY package-lock.json .
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]