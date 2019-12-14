# orderingsystem：点餐系统
系统组成：

configserver（配置中心）、registrycenter（服务注册中心）、clientFeign（与前端进行交互）、account（用户、管理员的登入验证服务）、menu（菜品模块）、order（订单模块）、user（用户模块，管理员对用户的管理服务）、zuul（网关路由）

技术组成：

  前端：
	
			thymeleaf、layui
  
后端：
	
	
      配置中心：spring cloud config
      服务注册中心：eureka
      业务层：feign、ribbon、hystrix、hystrix dashboard、springboot
      dao层：mybatis（配置和注解版）、druid
 
 实现功能：
 
      1.登入验证
      2.用户点餐和查看订单的状态
      3.管理员对菜品的编辑、增加、删除，对用户的展示、删除、添加、对订单的处理
      4.feign（ribbon）做负载均衡（自定义Irule）、hystrix做异常处理、hystrix dashboard做站点监控、zuul做网关路由、eureka做服务注册、config做云配置中心
      
  结合本项目谈微服务：
	
      微服务与单个项目的区别在于：微服务的每一个服务都可以单独部署到任意一台服务器（一台服务器部署相同服务多次、注意设置端口不一样）或多台服务器
      本系统有8个模块（服务），模块之间通过http（rest风格）交互。如：前端页面发送请求到clientFeign，clientFeign针对不同的业务与不同的模块进行通讯（通过feign接口实现远程调用）
      
  注意事项：
			
	  zuul模块对clientFeign服务进行了路由，当使用zuul自定义的路径访问clientFeign下的静态资源时，html页面可以获取到，但html中的css、js等资源获取不到，未解决问题之前，建议先不要使用zuul模块
		
 项目解读：
 
 
		1.configserver服务：为了方便对配置文件的修改，我将配置中心定义为本地文件夹，要想与git进行关联只需将配置文件修改成
			
          server:
            port: 3344			#configserver服务器的端口（自定义）
          spring:
            application:
              name: configserver
            cloud:
              config:
                server:				#config的服务器配置
                  git:				#连接到git
                    uri: https://github.com/yournameincode/configserver.git		#git仓库的地址
    
  
    2.clientFeign服务下myrule文件夹（存放MyRule类用于自定义负载均衡的方法）：如果放在springboot加载配置文件的所扫描的包下（application主启动方法的所在包及其子包），那么所有的负载均衡器所共享，如果要定制的话，需要放在所扫描包外
	  
    
    3.feignfallback用于给clientFeign服务下的feign接口中的所有方法，添加fallback方法，用于当clientFeign调用其他服务出现异常（网络异常、程序异常、网络超时等），会调用的fallback方法，防止引起服务雪崩
    
    4.filter文件夹结合传递给前端的session，做路径请求过滤器，防止未登入的用户，访问需要登入才能访问的页面
    
    5.ReflectUtils类，因为用户和管理员登入页面相同（通过传入的type值来区分用户和管理员），使用account来接收来此用户和管理员的信息，通过使用ReflectUtils（类反射工具类）将account中的数据付给user或者admin。
	
	  6.使用hystrix dashboard：localhost:port/hystrix.stream（一定要在配置类中开放hystrix.stream站点）,然后在输入地址栏内输入要监控的服务（服务需要支持hystrix即有异常回调方法，hystrix dashboard只能监控含有fallback的方法）
	
	  7.前端到后端请求路径中的参数问题：
    
    
              1.请求路径带参样式：
                get方式：
                  http://localhost:8030/account/login？username=admin&password=123123
                post方式（参数保存在请求体中）：
                  请求体：username=admin&password=123123
                  只要参数的参数名与java方法中的参数名相同就能自动赋值，且可以自动封装到类对象中（不管是get还是post）
                  如果名字不相同：使用@RequestParam("username") string name
              2.路径中使用了占位符的
                @RequestMapping("/redirect/{target}") ,如：localhost：8888/ redirect/login
                @PathVariable("target") String target（则将login赋值给target）
      
     8.方法相互调用的时候，方法对应的返回值和接收的返回值类型要一一对应，为了满足前端页面的数据需求和满足框架的数据格式需求，一般都需要后台去组合类对象（即定义一个新的对象中包含数据格式中所需得对象）

	   
数据库表的关联：


	管理员单表，不与任何表关联
	用户单表，不与任何表关联
	菜品类别单表，不与任何表关联
	菜单通过tid的值（对应菜品类别表的id值），关联查询
	订单表通过（uid关联用户表、mid关联菜单表、aid关联管理员表、state表明订单状态（派送或未派送））


登录页面：（用type+switch语句区分用户和管理员，查询不同的表）将账户和密码传递到
		
		
		dao层接口：
		public interface UserRepository {
		    public User login(String username, String password);
		}
		在数据库查询是否存在与之相同的字段值：
		    <select id="login" resultType="Admin">
		select * from t_admin where username = #{param1} and password = #{param2}
		</select>
		若存在则返回账户的所有数据（Admin）


菜单： 


	1.对菜单的增删改查只需针对菜单表即可（因为菜单表中的tid关联了菜品表，在mybatis中只需做连表查询就能查询出菜单和菜品的数据）
	2.菜品中的查询所有是为了在修改、添加菜时，将菜品所有数据全部查询出（用作一个下拉列表，供选择）
	3.菜单中dao层做了关于菜单表的增删改查（查询所有、按id查询）和count（用于给前端做分页展示数据）

订单：


	1.保存订单
	2.通过uid查询所有订单（用于用户登入时，查看自己的订单）
	3.通过uid统计订单的数量（用于用户界面展示所有的订单是否分页）
	4.通过mid删除订单（用于管理员删除订单）
	5. 通过uid删除订单（用于用户删除订单）
	6.通过订单的状态查询所有订单（用于管理员派送未派送的订单）
	7.通过订单状态统计订单数量（用于展示未派送（派送）的订单是否分页）
	8.通过订单的id和aid和state实现派送订单
用户（管理员页面对用户的管理操作）：


	1.查询所有用户
	2.统计所有用户的数量
	3.保存用户
	4.删除用户
	
			
