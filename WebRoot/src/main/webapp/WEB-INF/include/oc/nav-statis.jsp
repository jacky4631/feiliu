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
      <shiro:hasAnyRoles name="admin,financial">
        <li id="mb-fundflow"><a href="${ctx}/oc/statis/fundflow"><i class="fa fa-file-text-o"></i><span class="nav-label"> 资金流量表</span></a></li>
        <li id="mb-profitchannel"><a href="${ctx}/oc/statis/profit-channel"><i class="fa fa-file-text-o"></i><span class="nav-label"> 通道利润表</span></a></li>
        <li id="mb-profituser"><a href="${ctx}/oc/statis/profit-user"><i class="fa fa-file-text-o"></i><span class="nav-label"> 代理商利润表</span></a></li>
        <li id="mb-profitstate"><a href="${ctx}/oc/statis/profit-state"><i class="fa fa-file-text-o"></i><span class="nav-label"> 分省利润表</span></a></li>
        <li id="mb-profittelco"><a href="${ctx}/oc/statis/profit-telco"><i class="fa fa-file-text-o"></i><span class="nav-label"> 运营商利润表</span></a></li>
      </shiro:hasAnyRoles>
      <li id="mb-balanceusers"><a href="${ctx}/oc/statis/balance-users"><i class="fa fa-file-text-o"></i><span class="nav-label"> 用户余额一览表</span></a></li>
      <li id="mb-balancechannels"><a href="${ctx}/oc/statis/balance-channels"><i class="fa fa-file-text-o"></i><span class="nav-label"> 通道余额一览表</span></a></li>
      <li id="mb-timeconsuming"><a href="${ctx}/oc/statis/timeconsuming-state"><i class="fa fa-clock-o"></i><span class="nav-label"> 回调时长分析</span></a></li>
    </ul>
  </div>
</nav>