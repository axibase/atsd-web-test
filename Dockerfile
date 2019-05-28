FROM ubuntu:16.04

ENV DEPLOYMENT_TYPE web-test
LABEL com.axibase.maintainer="ATSD Developers <dev-atsd@axibase.com>"

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com --recv-keys 26AEE425A57967CFB323846008796A6514F3CB79 \
  && echo "deb [arch=amd64] http://axibase.com/public/repository/deb/ ./" >> /etc/apt/sources.list \
  && apt-get update \
  && apt-get install --no-install-recommends -y locales maven openjdk-8-jdk curl hostname iproute2 procps git chromium-browser=73.0.3683.86-0ubuntu0.16.04.1 \
  && locale-gen en_US.UTF-8 \
  && adduser --disabled-password --quiet --gecos "" axibase;

RUN git clone https://github.com/axibase/atsd-web-test /root/atsd-web-test
RUN mkdir -p /opt/bin && ln -sf /root/atsd-web-test/chromedriver /opt/bin/chromedriver
RUN mkdir -p /usr/bin && ln -sf `which chromium-browser` /usr/bin/google-chrome

ENTRYPOINT ["/bin/bash","/root/atsd-web-test/check_atsd.sh"]

