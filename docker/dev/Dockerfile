FROM debian:bullseye-slim

ARG CLOJURE_VERSION=1.11.1.1189
ARG JAVA_VERSION=19
ARG NODE_VERSION=16

WORKDIR /app
ENV LANG C.UTF-8

# Base layer:

RUN \
  apt update -q                                                                    && \
  rm -f /etc/ssl/certs/ca-bundle.crt                                               && \
  apt reinstall -qy                                                                \
    ca-certificates                                                                && \
  update-ca-certificates                                                           && \
  apt update -q                                                                    && \
  apt install -qy --no-install-recommends                                          \
    curl                                                                           \
    gcc                                                                            \
    g++                                                                            \
    make                                                                           \
    gnupg                                                                          \
    apt-transport-https

# Install NodeJS:

RUN \
  curl -L https://deb.nodesource.com/setup_${NODE_VERSION}.x                       \
    | bash -                                                                       && \
  apt update -q                                                                    && \
  apt install -qy --no-install-recommends                                          \
    nodejs

# Install Java:

RUN \
  curl -sSL https://packages.adoptium.net/artifactory/api/gpg/key/public           \
    | gpg --dearmor                                                                \
    > /etc/apt/trusted.gpg.d/temurin.gpg                                           && \
  echo "deb https://packages.adoptium.net/artifactory/deb bullseye main"           \
    > /etc/apt/sources.list.d/adoptium.list                                        && \
  apt update -q                                                                    && \
  apt install -qy --no-install-recommends                                          \
    temurin-${JAVA_VERSION}-jdk

# Install Clojure:

RUN \
  curl -sSL https://download.clojure.org/install/linux-install-$CLOJURE_VERSION.sh \
    | bash -                                                                       && \
  clojure -P

# Ready:

CMD ["clojure"]
