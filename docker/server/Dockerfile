FROM --platform=$BUILDPLATFORM clojure:temurin-19-tools-deps-bullseye-slim AS deps

# Download deps:
COPY ./deps.edn /app
RUN clojure -A:server -P

FROM clojure:temurin-19-tools-deps-bullseye-slim

WORKDIR /app

# Add deps:
COPY --from=deps  /root/.clojure  /root/.clojure
COPY --from=deps  /root/.m2       /root/.m2
COPY --from=deps  /app/deps.edn   /app/deps.edn

# Add rources:
COPY ./src /app/src

# Run command:
ENV DB_HOST=db
CMD ["clojure", "-M:server:server-main"]
