FROM --platform=$BUILDPLATFORM koulutus:dev AS deps

# Download deps:
COPY ./deps.edn             \
     ./package.json         \
     ./package-lock.json    \
     /app/

RUN \
  npm install               \
  clojure -A:web -P

FROM --platform=$BUILDPLATFORM koulutus:dev AS build

# Add deps:
COPY --from=deps  /root/.clojure  /root/.clojure
COPY --from=deps  /root/.m2       /root/.m2
COPY --from=deps  /app/           /app/

# Add rources:
COPY ./src /app/src

# Build:
RUN npm run release

FROM nginx:1-alpine

COPY ./docker/web/nginx.conf  /etc/nginx/nginx.conf
COPY --from=build  /app/public/  /usr/share/nginx/html/
