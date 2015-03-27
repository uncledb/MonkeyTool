# MonkeyTool

Monkey可视化执行工具   

版本：1.0

开发环境：  
OS:windows 64bit   
IDE:Eclipse  
JDK:1.8  

运行环境：  
OS Windows     
1.jdk  
2.android sdk adb   
  
功能：   
1.支持自定义操作频率，seed，次数三个属性，指定测试package  
2.一键运行monkey  
3.在手机端生成日志文件  
4.导出报告到pc  
5.随时停止monkey的运行  

亮点：  
1.可以实现“无连接”运行monkey,即使ADB在运行过程中断开，也会生成报告在手机中,最后通过导出操作，进行报告的pc导出    
2.运行过程中，ADB连接正常时，可以进行一键停止monkey操作      

如何开始使用monkeyTool?   
1.完成运行环境搭建  
2.手机电脑连接，运行monkeyTool程序，填写monkey命令参数 ，包名为必填项，其他项不填写会有默认值，但是默认值生成的monkey指令测试意义不大，所以大家尽量自己填写      
3.运行结束，导出报告并查看   
提示：monkey日志主要关注三个点：ANR,Crash,Exception  





 


