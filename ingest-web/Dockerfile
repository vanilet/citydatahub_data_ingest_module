FROM openjdk:8-alpine 
LABEL name="Yonghak.An"
LABEL email="neoshaby@gmail.com"

COPY src/main/docker/apk/sgerrand.rsa.pub /etc/apk/keys/sgerrand.rsa.pub
COPY src/main/docker/apk/ /


ARG TZ
ARG U_ID
ARG G_ID
ARG USER
ARG JAR_FILE

RUN apk --no-cache add ca-certificates tzdata curl && \
    apk add glibc-2.35-r0.apk glibc-bin-2.35-r0.apk glibc-i18n-2.35-r0.apk  && \
    cp /usr/share/zoneinfo/${TZ} /etc/localtime && \ 
    echo ${TZ} > /etc/timezone 

RUN /usr/glibc-compat/bin/localedef -i ko_KR -f UTF-8 ko_KR.UTF-8 && \
    /usr/glibc-compat/bin/localedef -i ko_KR -f EUC-KR ko_KR.EUC-KR && \
    echo "export LC_ALL='C.UTF-8'" >> /etc/profile && \
    echo "export LANG='C.UTF-8'" >> /etc/profile && \
    echo "export LANGUAGE='C.UTF-8'" >> /etc/profile

RUN rm -rf glibc-bin-2.35-r0.apk glibc-i18n-2.35-r0.apk glibc-2.35-r0.apk

ENV LC_ALL=C.UTF-8

EXPOSE 8080

RUN mkdir -p /opt/web/config && mkdir -p /opt/logs

WORKDIR /opt

COPY src/main/docker/config/ web/config/
  
ADD target/${JAR_FILE} web/app.jar

RUN set -eux; \
	addgroup -g ${G_ID} -S ${USER}; \
	adduser -u ${U_ID} -S -D -G ${USER} -s /bin/sh ${USER};

RUN chown -R ${USER}:${USER} web && chown -R ${USER}:${USER} logs

WORKDIR /opt/web
USER ${USER}
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","app.jar"]

