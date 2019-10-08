删除一个已经爬取的任务
srem set_finance.sina.com.cn http://finance.sina.com.cn/realstock/company/sz002432/nc.shtml
查看所有任务
smembers set_finance.sina.com.cn

java -jar .\finance-service-0.1.1.jar --spring.config.location=E:\finance-service\releases\application.properties