<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en" style="height: 100%">
<head>
  <title>数据模拟</title>
  <script src="resources/js/base/jquery.min.js"></script>
</head>
<style type="text/css">
  span{
    display:-moz-inline-box;
    display:inline-block;
    width: 100px;
    margin-left: 20px;
  }
  .marginLeft{
    margin-left: 20px;
  }

</style>

<body style="width: 100%;height: 100%">

  <!-- 信息及配置-->
  <div style="width:100%;height:40%">
    <div style="float:left;width:33%;height:100%">
      
      <div style="display: inline-block;width:100%;height:50%;">
        <span style="margin-top: 10px">中心地址:</span><input id="centerUrl" value="http://59.56.104.41:9886/center-web3.0/"></input><br/><br/>
        <span >账号:</span><input id="userName" value="ct_yjlo1"></input><br/><br/>
        <span >密码:</span><input id="password" value="88888888"></input><br/><br/>
        <button id="loginBtn" style="margin-left: 50px" onclick="login()">登录</button><button id="agimisReportBtn" style="margin-left: 50px" onclick="agimisReport()">位置上报</button>
      </div>

      <div style="width:100%;height:50%">
        <br/>
        <span>时间范围:</span><input id="beginTime" placeholder="例:2019-01-01 00:00:00" value="2019-11-01 00:00:00"></input>&nbsp;&nbsp;~&nbsp;&nbsp;<input id="endTime" placeholder="例:2019-12-31 23:59:59" value="2019-11-01 23:59:59"></input>
        <br/><br/>
        <span>时间间隔:</span><input id="reportInterval" width="50px" value="1"></input>&nbsp;<text>分钟</text>
        <br/><br/>
        <label style="margin-left: 60px"><input name="reportMethod" type="radio" value="0" checked  onclick="changeReportMethod(0)" />循环上报 </label> 
        <label style="margin-left: 60px"><input name="reportMethod" type="radio" value="1"  onclick="changeReportMethod(1)"/>往返上报 </label> 
      </div>
    </div>
    <div style="float:left;width:33%;height:100%">
      <div style="width:85%;height:50%;background:gray;">
        <br/>
          <span>账号:</span><b id="showUserName" ></b><br/>
          <span>集团id:</span><b id="showOrgId"></b><br/>
          <span>集团名称:</span><b id="showOrgName"></b><br/>
          <span>上报地址:</span><b id="showAgimisReportUrl"></b><br/>
      </div>
      <div style="width:100%;height:50%">
        <text class="marginLeft">终端号码:</text><br/>
        <textarea id="terminals" style="width:85%;height:85%" class="marginLeft">1410342810214;1410342845934;1410373231265;1410373231285</textarea><br/>
      </div>
    </div>
    <div style="float:left;width:33%;height:100%">
      <div style="width:85%;height:50%;background:gray;">
        <br/>
        <span>位置上报:</span><b id="agimisReportStatus" style="color:black">未开启</b><br/><br/>
        <span>事件上报:</span><b id="agimisUploadStatus" style="color:black">未开启</b><br/><br/>
        <span>巡检上报:</span><b id="patrolStatus" style="color:black">未开启</b><br/><br/>
        <span>任务上报:</span><b id="insertTeamworkStatus" style="color:black">未开启</b><br/><br/>
      </div>
      <div style="width:100%;height:50%">
        <text class="marginLeft">位置:</text><br/>
        <textarea id="pois" style="width:85%;height:85%" class="marginLeft">119.309704,26.055702;119.309558,26.056411;119.309411,26.057120;119.309265,26.057829;119.309174,26.058544</textarea><br/>
      </div>
    </div>
  </div>
  
  <!--上报 -->
  <div style=" width:100%;height:30%">
    <div style="float:left;width:35%;height:100%">
      <b style="font-size: 18px" class="marginLeft">事件、巡检上报模拟</b><br/>
      <text class="marginLeft">备注:</text><br/>
      <textarea id="remark" style="width:85%;height:50%" class="marginLeft">事件、巡检上报备注内容</textarea><br/>
      <label style="margin-left: 60px"><input name="resource" type="radio" value="" checked="checked" />视频 </label> 
      <label style="margin-left: 60px"><input name="resource" type="radio" value="" />图片 </label> 
      <br/>
      <button id="agimisUploadBtn" onclick="agimisUpload()" style="margin-left: 50px">事件上报</button><button id="patrolBtn" onclick="patrol()" style="margin-left: 50px">巡检反馈</button>
    </div>

    <div style="float:left;width:64%;height:100%">
      <b style="font-size: 18px" class="marginLeft">协同任务模拟</b>
      <br/><br/>
      <span>协同名称:</span><input id ="taskTitle" value="协同测试名称"></input><span>开始时间:</span><input id="jobStartTime" placeholder="例:2019-01-01 00:00:00" value="2019-11-01 00:00:00"></input><span>结束时间:</span><input id="jobEndTime" placeholder="例:2019-12-31 23:59:59" value="2019-11-01 23:59:59"></input>
      <br/><br/>
      <text class="marginLeft">参与人:</text>  <text style="margin-left: 400px">任务内容:</text><br/>
      <textarea id="players" style="width:35%;height:35%" class="marginLeft">1410373248218;1410373248638</textarea> <textarea id="context" style="width:35%;height:35%" class="marginLeft">协同任务测试内容</textarea>
      <button style="margin-left: 400px" id="insertTeamworkBtn" onclick="insertTeamwork()">任务上报</button>
    </div>
  </div>
  
  <!-- 日志-->
  <div style=" width:100%;height:30%">
    <span>控制台:</span><br/>&nbsp;&nbsp;&nbsp;<button onclick="clearControlContent()">清空控制台</button>
    <textarea id="controlContent" style="width:100%;height:90%" readonly="readonly"></textarea>

  </div>
  <script type="text/javascript">
  
  //保活接口调用频率 30min
  var keepActiveTimes = 30*60*1000;
  var normal_code = 1;
  var isLogin = false;
  
  //登录参数
  var centerUrl ;
  var userName ;
  var password ;
  
  //上报参数
  var terminals ;

  var pois ;
  var beginTime ;
  var endTime ;
  var reportTime ;
  var reportInterval ;
  var reportMethod ;

  var agimisReportTime = "";

  //事件和巡检参数
  var remark ;
  var resource ;

  var agimisUploadTime = "";
  var patrolTime = "";

  //协同参数
  var players ;
  var jobStartTime ;
  var jobEndTime ;
  var jobStartDate ;
  var jobEndDate ;
  var taskTitle ;
  var context ;

  var insertTeamworkTime = "";

  //定时器id
  var keepActiveTimerId = 0;
  var agimisReportTimerId = 0;
  var agimisUploadTimerId = 0;
  var patrolTimerId = 0;
  var insertTeamworkTimerId = 0;

  // 正序倒序索引值
  var agimisReportSortIndex = 0;
  var agimisUploadSortIndex = 0;
  var patrolSortIndex = 0;

  //取消
  function cancel(){
    //清空所有内容
    $("#centerUrl").val("");
    $("#userName").val("");
    $("#password").val("");
    isLogin = false;
    $("#showAgimisReportUrl").text("");
    $("#showUserName").text("");
    $("#showOrgId").text("");
    $("#showOrgName").text("");

    $("#loginBtn").text("确定");
    //关闭所有定时器
    closeTimer();

    $("#agimisReportBtn").text("位置上报");
    $("#agimisReportStatus").css("color","black");
    $("#agimisReportStatus").html("未开启");

    $("#agimisUploadBtn").text("事件上报");
    $("#agimisUploadStatus").css("color","black");
    $("#agimisUploadStatus").html("未开启");

    $("#patrolBtn").text("巡检反馈");
    $("#patrolStatus").css("color","black");
    $("#patrolStatus").html("未开启");

    $("#insertTeamworkBtn").text("任务上报");
    $("#insertTeamworkStatus").css("color","black");
    $("#insertTeamworkStatus").html("未开启");

    clearControlContent();
  }
  //关闭所有定时器
  function closeTimer(){
    clearInterval(keepActiveTimerId);
    clearInterval(agimisReportTimerId);
    clearInterval(agimisUploadTimerId);
    clearInterval(patrolTimerId);
    clearInterval(insertTeamworkTimerId);
  }

  //登录或取消登录
  function login() {
    initParams();
    //取消操作
    if ($("#loginBtn").text() == "取消") {
      cancel();
      return;
    }
    if (!checkParams(centerUrl)) {
      alert("中心地址不能为空");
      return;
    }
    if (!checkParams(userName)) {
      alert("账号不能为空");
      return;
    }
    if (!checkParams(password)) {
      alert("密码不能为空");
      return;
    }

    $("#loginBtn").text("登录中...");

		var josnObject ={
      centerUrl:centerUrl,
      userName:userName,
      password:password
    };
		$.ajax({
			url : "dispatcher/login",
			type : "get",
			dataType : 'json',
			data: josnObject,
			success : function(data) {
        //登录成功
        if (data.code == normal_code) {
          $("#loginBtn").text("取消");
          $("#showAgimisReportUrl").text(data.data.agimisReportUrl);
          $("#showUserName").text(userName);
          $("#showOrgId").text(data.data.orgIds);
          $("#showOrgName").text("未知");
          isLogin = true;
          keepActiveTimerId = setInterval(keepActive(), keepActiveTimes);
          alert("登录成功");
        }else{
          isLogin = false;
          alert("登录失败");
        }
			}
		});
	}

  //保活(必须是当前调度员已经登录,才会调用该方法)
  function keepActive(){
    initParams();
    if (isLogin == false) {
      alert("请先成功登录");
      return;
    }

    if (!checkParams(centerUrl)) {
      alert("中心地址不能为空");
      return;
    }
    if (!checkParams(userName)) {
      alert("账号不能为空");
      return;
    }

    var josnObject ={
      centerUrl:centerUrl,
      userName:userName
    };

    $.ajax({
      url : "dispatcher/keepActive",
      type : "get",
      dataType : 'json',
      data: josnObject,
      success : function(data) {
        if (data.code != normal_code) {
          alert("账号已失活,请重新登录");
        }
      }
    });
  }

  //位置上报
  function agimisReport(){
    initParams();
    //取消操作
    if ($("#agimisReportBtn").text() == "位置上报中...") {
      clearInterval(agimisReportTimerId);

      $("#agimisReportBtn").text("位置上报");
      $("#agimisReportStatus").css("color","black");
      $("#agimisReportStatus").html("未开启");
      agimisReportTime = "";
      return;
    }
    if (isLogin == false) {
      alert("请先成功登录");
      return;
    }
    if (!checkParams(terminals)) {
      alert("终端号码不能为空");
      return;
    }
    if (!checkParams(pois)) {
      alert("位置不能为空");
      return;
    }
    if (!checkParams(beginTime)) {
      alert("开始时间不能为空");
      return;
    }
    if (!checkParams(endTime)) {
      alert("结束时间不能为空");
      return;
    }
    if (!checkParams(reportInterval)) {
      alert("时间间隔不能为空");
      return;
    }
    agimisReportInterface();
    agimisReportTimerId = setInterval(agimisReportInterface, reportInterval);
  }

  //位置上报接口
  function agimisReportInterface(){
    if (agimisReportTime == "") {
      agimisReportTime = new Date(beginTime).getTime();
    }else{
      agimisReportTime = agimisReportTime + reportInterval;
    }
    var p = pois.split(";");
    var poi = "";

    if (reportMethod == "0") {
      poi = p[agimisReportSortIndex];
      if (agimisReportSortIndex < p.length-1) {
        agimisReportSortIndex ++;
      }else{
        agimisReportSortIndex = 0;
      }
    }else if(reportMethod == "1"){
      if (agimisReportSortIndex == 0) {
        agimisReportSortIndex = p.length-1;
      }else{
        agimisReportSortIndex --;
      }
      poi = p[agimisReportSortIndex];
    }
    var josnObject ={
      centerUrl:centerUrl,
      userName:userName,
      terminals:terminals,
      poi:poi,
      reportTime:agimisReportTime,
      reportMethod:reportMethod
    };
    $.ajax({
      url : "agimis/report",
      type : "get",
      dataType : 'json',
      data: josnObject,
      success : function(data) {
        if (data.code == normal_code) {
          $("#agimisReportBtn").text("位置上报中...");
          $("#agimisReportStatus").css("color","green");
          $("#agimisReportStatus").html("开启");
          for (var i = 0; i < data.data.length; i++) {
            var controlContent = $("#controlContent").val();
            $("#controlContent").val(controlContent + data.data[i]+"\n");
          }
        }else{
          alert("位置上报失败");
        }
      }
    });
  }

  //事件上报
  function agimisUpload(){
    initParams();
    //取消操作
    if ($("#agimisUploadBtn").text() == "事件上报中...") {
      clearInterval(agimisUploadTimerId);

      $("#agimisUploadBtn").text("事件上报");
      $("#agimisUploadStatus").css("color","black");
      $("#agimisUploadStatus").html("未开启");

      agimisUploadTime = "";
      return;
    }

    if (isLogin == false) {
      alert("请先成功登录");
      return;
    }
    if (!checkParams(terminals)) {
      alert("终端号码不能为空");
      return;
    }
    
    agimisUploadInterface();
    agimisUploadTimerId = setInterval(agimisUploadInterface, reportInterval);
  }

  //事件上报接口
  function agimisUploadInterface(){
    if (agimisUploadTime == "") {
      agimisUploadTime = new Date(beginTime).getTime();
    }else{
      agimisUploadTime = agimisUploadTime + reportInterval;
    }

    var p = pois.split(";");
    var poi = "";

    if (reportMethod == "0") {
      poi = p[agimisUploadSortIndex];
      if (agimisUploadSortIndex < p.length-1) {
        agimisUploadSortIndex ++;
      }else{
        agimisUploadSortIndex = 0;
      }
    }else if(reportMethod == "1"){
      if (agimisUploadSortIndex == 0) {
        agimisUploadSortIndex = p.length-1;
      }else{
        agimisUploadSortIndex = agimisUploadSortIndex --;
      }
      poi = p[agimisUploadSortIndex];
    }

    var josnObject ={
      centerUrl:centerUrl,
      userName:userName,
      terminals:terminals,
      poi:poi,
      reportTime:agimisUploadTime,
      remark:remark
    };

    $.ajax({
      url : "agimis/upload",
      type : "get",
      dataType : 'json',
      data: josnObject,
      success : function(data) {
        if (data.code == normal_code) {
          $("#agimisUploadBtn").text("事件上报中...");
          $("#agimisUploadStatus").css("color","green");
          $("#agimisUploadStatus").html("开启");
          for (var i = 0; i < data.data.length; i++) {
            var controlContent = $("#controlContent").val();
            $("#controlContent").val(controlContent + data.data[i]+"\n");
          }
        }else{
          alert("事件上报失败");
        }
      }
    });
  }

  //巡检上报
  function patrol(){
    initParams();
    //取消操作
    if ($("#patrolBtn").text() == "巡检反馈中...") {
      clearInterval(patrolTimerId);

      $("#patrolBtn").text("巡检反馈");
      $("#patrolStatus").css("color","black");
      $("#patrolStatus").html("未开启");
      
      patrolTime = "";
      return;
    }
    if (isLogin == false) {
      alert("请先成功登录");
      return;
    }
    if (!checkParams(terminals)) {
      alert("终端号码不能为空");
      return;
    }
    
    patrolInterface();
    patrolTimerId = setInterval(patrolInterface, reportInterval);
  }

  //巡检上报
  function patrolInterface(){
    if (patrolTime == "") {
      patrolTime = new Date(beginTime).getTime();
    }else{
      patrolTime = patrolTime + reportInterval;
    }

    var p = pois.split(";");
    var poi = "";

    if (reportMethod == "0") {
      poi = p[patrolSortIndex];
      if (patrolSortIndex < p.length-1) {
        patrolSortIndex ++;
      }else{
        patrolSortIndex = 0;
      }
    }else if(reportMethod == "1"){
      if (patrolSortIndex == 0) {
        patrolSortIndex = p.length-1;
      }else{
        patrolSortIndex = patrolSortIndex --;
      }
      poi = p[patrolSortIndex];
    }

    var josnObject ={
      centerUrl:centerUrl,
      userName:userName,
      terminals:terminals,
      poi:poi,
      reportTime:patrolTime,
      remark:remark
    };

    $.ajax({
      url : "agimis/jobresource/patrolup",
      type : "get",
      dataType : 'json',
      data: josnObject,
      success : function(data) {
        if (data.code == normal_code) {
          $("#patrolBtn").text("巡检反馈中...");
          $("#patrolStatus").css("color","green");
          $("#patrolStatus").html("开启");
          for (var i = 0; i < data.data.length; i++) {
            var controlContent = $("#controlContent").val();
            $("#controlContent").val(controlContent + data.data[i]+"\n");
          }
        }else{
          alert("巡检上报失败");
        }
      }
    });
  }

  //协同任务上报
  function insertTeamwork(){
    initParams();
    //取消操作
    if ($("#insertTeamworkBtn").text() == "任务上报中...") {
      clearInterval(insertTeamworkTimerId);

      $("#insertTeamworkBtn").text("任务上报");
      $("#insertTeamworkStatus").css("color","black");
      $("#insertTeamworkStatus").html("未开启");
      return;
    }

    if (isLogin == false) {
      alert("请先成功登录");
      return;
    }
    if (!checkParams(terminals)) {
      alert("终端号码不能为空");
      return;
    }
    if (!checkParams(players)) {
      alert("参与人不能为空");
      return;
    }
    if (!checkParams(jobStartTime)) {
      alert("开始时间不能为空");
      return;
    }
    if (!checkParams(jobEndTime)) {
      alert("结束时间不能为空");
      return;
    }
    if (!checkParams(taskTitle)) {
      alert("协同名称不能为空");
      return;
    }
    if (!checkParams(context)) {
      alert("任务内容不能为空");
      return;
    }

    insertTeamworkInterface();
    insertTeamworkTimerId = setInterval(insertTeamworkInterface, reportInterval);
  }

  //协同任务上报接口
  function insertTeamworkInterface(){
    var josnObject ={
      centerUrl:centerUrl,
      userName:userName,
      terminals:terminals,
      players:players,
      jobStartTime:jobStartDate,
      jobEndTime:jobEndDate,
      taskTitle:taskTitle,
      context:context
    };

    $.ajax({
      url : "jobRes/insertTeamWork",
      type : "get",
      dataType : 'json',
      data: josnObject,
      success : function(data) {
        if (data.code == normal_code) {
          $("#insertTeamworkBtn").text("任务上报中...");
          $("#insertTeamworkStatus").css("color","green");
          $("#insertTeamworkStatus").html("开启");
          for (var i = 0; i < data.data.length; i++) {
            var controlContent = $("#controlContent").val();
            $("#controlContent").val(controlContent + data.data[i]+"\n");
          }
        }else{
          alert("任务上报失败");
        }
      }
    });
  }
  
  //参数校验
  function checkParams(param){
	  if (param == null || param == undefined || param =='') {
      return false;
    }else{
      return true;
    }
  }

  function clearControlContent(){
    $("#controlContent").val("");
  }

  function changeReportMethod(reportMethod){
    initParams();
    if (reportMethod == 0) {
      agimisReportSortIndex = 0;
      agimisUploadSortIndex = 0;
      insertTeamworktSortIndex = 0;
    }else{
      var length = pois.split(";").length;
      agimisReportSortIndex = length;
      agimisUploadSortIndex = length;
      insertTeamworktSortIndex = length;
    }
  }

  function initParams(){
    //登录参数
    centerUrl = $("#centerUrl").val();
    userName = $("#userName").val();
    password = $("#password").val();
    
    //上报参数
    terminals = $("#terminals").val();

    pois = $("#pois").val();
    beginTime = $("#beginTime").val();
    endTime = $("#endTime").val();
    reportTime = new Date(beginTime).getTime();
    reportInterval = parseInt($("#reportInterval").val())*60*1000 ;
    reportMethod = $('input[name="reportMethod"]:checked').val();

    //事件和巡检参数
    remark = $("#remark").val();
    resource = $('input[name="resource"]:checked').val();

    //协同参数
    players = $("#players").val();
    jobStartTime = $("#jobStartTime").val();
    jobEndTime = $("#jobEndTime").val();
    jobStartDate = new Date(jobStartTime).getTime();
    jobEndDate = new Date(jobEndTime).getTime();
    taskTitle = $("#taskTitle").val();
    context = $("#context").val();
  }
  
  </script>
</body>
</html>