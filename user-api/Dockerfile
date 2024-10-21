FROM openjdk:17-jdk-slim-buster

ARG PROFILES

RUN apt-get -y update
RUN apt -y install wget
RUN apt -y install unzip
RUN apt -y install curl
RUN apt-get -y install xvfb

# 영어로 언어 설정 - 셀레니움을 위해서
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8


# google chrome 설치
RUN wget https://chrome-versions.com/google-chrome-stable-114.0.5735.106-1.deb
#RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get -y update
RUN apt -y install ./google-chrome-stable-114.0.5735.106-1.deb
#RUN apt -y install ./google-chrome-stable_current_amd64.deb

# chromedriver 설치
RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/` curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE_114`/chromedriver_linux64.zip
RUN unzip /tmp/chromedriver.zip chromedriver -d .

# Xvfb 설정
RUN apt-get install -y xvfb
RUN Xvfb :99 -ac &
ENV DISPLAY=:99

COPY ./build/libs/user-api-0.0.1-SNAPSHOT.jar biengual/user-app.jar
COPY ./src/main/resources/.env biengual/.env

EXPOSE 8080

WORKDIR /biengual
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=${PROFILES}", "-jar", "user-app.jar"]