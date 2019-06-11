FROM ubuntu:16.04

ENV DEPLOYMENT_TYPE web-test
LABEL com.axibase.maintainer="ATSD Developers <dev-atsd@axibase.com>"

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com --recv-keys 26AEE425A57967CFB323846008796A6514F3CB79 \
  && echo "deb [arch=amd64] http://axibase.com/public/repository/deb/ ./" >> /etc/apt/sources.list \
  && apt-get update \
  && apt-get install --no-install-recommends -y locales maven openjdk-8-jdk curl hostname iproute2 procps git chromium-browser \
  && locale-gen en_US.UTF-8 \
  && adduser --disabled-password --quiet --gecos "" axibase \
  && ln -s /usr/bin/chromium-browser /usr/bin/google-chrome;

RUN git clone https://github.com/axibase/atsd-web-test /root/atsd-web-test

ENTRYPOINT ["/bin/bash","/root/atsd-web-test/check_atsd.sh"]

