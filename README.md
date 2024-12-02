## 微头条（纯后端）项目
### 技术栈：  
|技术|作用|
|---------------|------|
|Spring framework| 基础框架|
|Spring MVC|       前端参数接收与发送|
|MyBatis plus|     简化持久层开发|
|Springboot|       快速启动框架|
|Lombok|           代码优化框架|
|Spring Security|  安全认证框架（整合中）|
### 接口展示
*/user  
--/login  
--/getUserInfo  
--/checkUsername  
--/register  
--/checkLogin  
/portal  
--/findAllTypes  
--/findNewsPage  
--/showHeadlineDetails  
/headline  
--/publish  
--/findHeadlineByHId  
--/update  
--/removebyHId*   
### 接口说明
|接口|参数|说明|
|---|---|---|
|/user/login|{"username":"xxx","password":"xxx","nickname":"xxx"}|用户登录，输入用户名、密码、昵称|
|/user/getUserInfo|@RequestHeader String token|获取token中的用户信息|
|/user/checkUsername|@RequestParam String username|检查用户名是否被占用|
|/user/register|{"username":"xxx","password":"xxx","nickname":"xxx"}|注册新用户|
|/user/checkLogin|@RequestHeader String token|检查token是否可用|
|/portal/findAllTypes|null|获取类型信息|
|/portal/findNewsPage|{"keyWords":"xxx","type":"xxx","pageNum":"xxx","pageSize":"xxx"}|根据keyWords关键字，type类型，pageNum和pageSize分页参数进行分页查询新闻信息|
|/portal/showHeadlineDetails|@RequestParam int hId|根据HId查询新闻信息|
|/headline/publish|{"title":"xxx","article":"xxx","type":"xxx"}|插入一条新闻信息|
|/headline/findHeadlineByHId|@RequestParam int hId|根据HId查询新闻信息|
|/headline/update|{"hId":"xxx","title":"xxx","article":"xxx","type":"xxx"}|更新新闻信息|
|/headline/update|@RequestParam int hId|根据HId删除一条记录|
## 2.0更新说明
1、重构Result结果类，以生成器模式实现  
2、强化日志输出类LogOut实现方式，改为实现自接口，并增加Controller层日志输出
### 2.1更新说明
1、实现了部分Spring Security 的authentication  
2、重构User entity类，使其能够整合进Spring Security
### 2.3更新说明
将用户管理与spring security进行进一步整合。由于项目体量小，很多功能没有实现的必要，因此这个项目在更新完OAuth2后会停止更新并完结
### 2024.12.02补充
该项目为练手项目，且考察后OAuth2的使用场景并非项目所需，所以项目不再更新了，作为我其他项目的实现参考。
