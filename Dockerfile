FROM williamyeh/java8

ENV LEIN_ROOT true

COPY database.json /etc/tiling/database.json

RUN apt-get install -y openssl

RUN openssl genrsa -out /etc/tiling/jwt-private.pem 2048
RUN openssl rsa -in /etc/tiling/jwt-private.pem -outform PEM -pubout -out /etc/tiling/jwt-public.pem

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
EXPOSE 7001

CMD ["/usr/bin/lein", "run"]
