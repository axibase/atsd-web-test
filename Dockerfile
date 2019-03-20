FROM ubuntu:16.04

LABEL com.axibase.maintainer="ATSD Developers <dev-atsd@axibase.com>"

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com --recv-keys 26AEE425A57967CFB323846008796A6514F3CB79 \
  && echo "deb [arch=amd64] http://axibase.com/public/repository/deb/ ./" >> /etc/apt/sources.list \
  && apt-get update \
  && apt-get install --no-install-recommends -y locales maven openjdk-8-jdk curl hostname iproute2 procps git \
  && locale-gen en_US.UTF-8 \
  && adduser --disabled-password --quiet --gecos "" axibase;

RUN git clone https://github.com/axibase/atsd-web-test /root/atsd-web-test
RUN mkdir -p /opt/bin && ln -sf /root/atsd-web-test/phantomjs /opt/bin/phantomjs

ENTRYPOINT ["/bin/bash","/root/atsd-web-test/check_atsd.sh"]

