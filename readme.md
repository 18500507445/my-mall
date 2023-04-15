## 秒杀、支付优化
### 简介:
* (1)主导重构了支付项目，但是没有自己的代码，所以我想把这块继续优化一下，细细打磨，从表的设计开始到代码的落地
* (2)同时呢也负责了一个藏品项目，有些问题没有解决，把遇到的问题都会记录下来
* (2)已接入api：微信开发者联盟sdk(支付,小程序,公众号)、阿里、易宝、谷歌支付sdk、快手支付
* [梳理的下单、支付流程图](https://www.processon.com/preview/642e300f769dd24760953fd7)

### 组织结构
```
my-pay
├── annotation --注解
├── aspectj -- aop
├── common -- 常量，拦截器，结果包装类
├── config -- 配置类
├── job -- xxljob 执行器
├── payment -- 支付工具类
├── service -- 支付接口，业务接口
└── utils -- 工具类，货币转换
```

### 项目启动参数
>nohup java -Xms1g -Xmx1g -Xmn256m -Xss512k -XX:PermSize=512m -XX:MaxPermSize=512m -jar /app/my_pay.jar >/dev/null 2>&1 &

### 排查配置微信支付问题，可以看到详细的返回错误信息（其它支付方式很少出问题）
~~~linux
cat sys-info.log |grep -A 16 'orderId'
~~~


### 收集的知识
* [《淘宝面试：服务端防止重复支付》](https://mp.weixin.qq.com/s/Xlo8yCPtjjG1SdF6DS8zpg)
* [《30分钟未支付,自动取消,该怎么实现》](https://mp.weixin.qq.com/s/fQ94NgKeR6qQAcIe0CrusA)