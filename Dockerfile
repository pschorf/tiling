FROM williamyeh/java8

ENV LEIN_ROOT true

RUN wget -q -O /usr/bin/lein \
    https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein \
    && chmod +x /usr/bin/lein

RUN lein upgrade

ADD . /tiling-build

WORKDIR /tiling-build

RUN ["/usr/bin/lein", "deps"]

VOLUME /tiling

WORKDIR /tiling

EXPOSE 3000 

CMD ["/usr/bin/lein", "run"]
