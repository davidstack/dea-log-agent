dea-log-agent
=============

cloudfoundry dea agent
基于CloudFoundry CFv145

主要解决的问题：
            CloudFoundry 应用的实例无状态性，无法获取并记录应用的每个实例的详细信息，历史信息，可以作为监控instance的客户端程序。

1、用于采集部署在DEA上的app instance 信息，可以获取instance的version,appName,startTime,app log 地址
2、提供rest接口 ，可以获取应用的日志信息，状态信息




