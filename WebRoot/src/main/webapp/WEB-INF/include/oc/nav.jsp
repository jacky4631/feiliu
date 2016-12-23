<%@ page import="com.jiam365.flow.server.params.SystemProperties" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<nav class="navbar-default navbar-static-side" role="navigation">
  <div class="sidebar-collapse">
    <ul class="nav metismenu" id="side-menu">
      <li class="nav-header">
        <div class="dropdown profile-element">
          <span><img alt="image" class="img-circle" src="${ctx}/static/img/user-default.jpg"/></span>
          <a data-toggle="dropdown" class="dropdown-toggle" href="#">
              <span class="clear">
                <span class="block m-t-xs"> <strong><shiro:principal property="displayName"/></strong><b class="caret"></b> 运营中心</span>
              </span>
          </a>
          <ul class="dropdown-menu animated fadeInRight m-t-xs">
            <li><a href="${ctx}/oc/passwd">修改密码</a></li>
            <li><a href="${ctx}/logout">退出</a></li>
          </ul>
        </div>
        <div class="logo-element">
          <i class="fa fa-laptop"></i>
        </div>
      </li>
      <%
        if (SystemProperties.isDebug()) {
      %>
      <li class="special_link">
        <a href="#"><i class="fa fa-bug"></i> <span class="nav-label">DEBUG环境</span></a>
      </li>
      <%
        }
      %>
      <shiro:hasAnyRoles name="admin,financial">
        <li id="ma-dashboard"><a href="${ctx}/oc"><i class="fa fa-th-large"></i><span class="nav-label">总控台</span><span class="label label-info pull-right">New</span></a></li>
      </shiro:hasAnyRoles>

      <li id="ma-tradelog"><a href="${ctx}/oc/tradelog"><i class="fa fa-search"></i><span class="nav-label"> 流量充值记录</span></a></li>
      <shiro:hasAnyRoles name="admin,operator">
        <li id="ma-tradelog0"><a href="${ctx}/oc/tradelog/0"><i class="fa fa-pause"></i><span class="nav-label"> 订单黑洞</span><span id="l_pending" class="label label-warning pull-right">0</span></a>
        </li>
        <li id="ma-tradelog1"><a href="${ctx}/oc/tradelog/1"><i class="fa fa-bitbucket"></i><span class="nav-label"> 待处理失败</span><span id="l_callback_pending"
                                                                                                                                       class="label label-warning pull-right">0</span></a></li>
        <li id="ma-tradelog2"><a href="${ctx}/oc/tradelog/2"><i class="fa fa-adjust"></i><span class="nav-label"> 隔夜卡单</span></a>
        <li id="ma-userreport"><a href="${ctx}/oc/userreport"><i class="fa fa-exchange"></i><span class="nav-label"> 用户回调</span></a></li>

        <li id="ma-trade">
          <a href="${ctx}/oc/product"><i class="fa fa-user-secret"></i><span class="nav-label">订单拦截</span><span class="fa arrow"></span></a>
          <ul class="nav nav-second-level collapse">
            <li id="ma-trade-interceptor"><a href="${ctx}/oc/interceptors/edit">充值拦截</a></li>
            <li id="ma-trade-failinterceptor"><a href="${ctx}/oc/interceptors/callback">失败回调拦截</a></li>
            <li id="ma-trade-blacklist"><a href="${ctx}/oc/blacklist/">手机黑名单</a></li>
          </ul>
        </li>
        <li id="ma-tools">
          <a href="${ctx}/oc/product"><i class="fa fa-wrench"></i><span class="nav-label">工具集</span><span class="fa arrow"></span></a>
          <ul class="nav nav-second-level collapse">
            <li id="ma-tools-bulletin"><a href="${ctx}/oc/bulletin"><i class="fa fa-bullhorn"></i> 通知广播</a></li>
            <li id="ma-tools-cgroup"><a href="${ctx}/oc/channelgroup"><i class="fa fa-sliders"></i> 预设通道组</a></li>
            <li id="ma-tools-smlog"><a href="${ctx}/oc/smlog"><i class="fa fa-at"></i> 充值提醒短信</a></li>
            <li id="ma-tools-smmemo"><a href="${ctx}/oc/smmemo"><i class="fa fa-files-o"></i> 短信备案</a></li>
            <li id="ma-tools-notify"><a href="${ctx}/oc/notify"><i class="fa fa-cc-visa"></i> 通道余额提醒</a></li>
          </ul>
        </li>
      </shiro:hasAnyRoles>
      <li id="ma-agent"><a href="${ctx}/oc/users/agent"><i class="fa fa-user"></i><span class="nav-label"> 代理商账户</span></a></li>
      <li id="ma-channel"><a href="${ctx}/oc/channel"><i class="fa fa-plug"></i><span class="nav-label"> 供应商及产品</span></a></li>

      <shiro:hasAnyRoles name="admin,operator">
        <li id="ma-config">
          <a href="${ctx}/oc/product"><i class="fa fa-gears"></i><span class="nav-label">系统配置</span><span class="fa arrow"></span></a>
          <ul class="nav nav-second-level collapse">
            <li id="ma-config-product"><a href="${ctx}/oc/product">基础产品库</a></li>
            <li id="ma-config-params"><a href="${ctx}/oc/params">运行参数</a></li>
            <li id="ma-config-sms"><a href="${ctx}/oc/sm-config">短信全局设置</a></li>
          </ul>
        </li>
      </shiro:hasAnyRoles>

      <shiro:hasAnyRoles name="admin,financial">
        <li id="ma-translog"><a href="${ctx}/oc/transferlog"><i class="fa fa-list-alt"></i><span class="nav-label"> 加/付款日志</span></a></li>
      </shiro:hasAnyRoles>
    </ul>
  </div>
</nav>