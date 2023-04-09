FROM node:lst-alpine as builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:stable-alpine
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY --from=buildder /app/nginx/default.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /user/share/nginx/html/*
COPY --from=builder /app/dist /user/share/nginx/html

EXPOSE 80
ENTRYPOINT ["nginx", "-g","daemon off;"]

