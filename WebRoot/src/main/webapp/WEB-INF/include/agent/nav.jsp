<%@ page contentType="text/html;charset=UTF-8" %>
<nav class="navbar-default navbar-static-side" role="navigation">
  <div class="sidebar-collapse">
    <ul class="nav metismenu" id="side-menu">
      <li class="nav-header">
        <div class="dropdown profile-element">
          <span><img alt="image" class="img-circle" src="${ctx}/static/img/user-default.jpg"/></span>
          <a data-toggle="dropdown" class="dropdown-toggle" href="#">
              <span class="clear">
                <span class="block m-t-xs"> <strong><shiro:principal property="displayName"/></strong><b class="caret"></b>  活动商户</span>
              </span>
          </a>
          <ul class="dropdown-menu animated fadeInRight m-t-xs">
            <li><a href="${ctx}/agent/profile">账户信息</a></li>
            <li class="divider"></li>
            <li><a href="${ctx}/logout">退出</a></li>
          </ul>
        </div>
        <div class="logo-element">
          <i class="fa fa-laptop"></i>
        </div>
      </li>
      <li id="m-dashboard"><a href="${ctx}/agent"><i class="fa fa-diamond"></i> <span class="nav-label">当日消费</span></a></li>
      <li id="m-list"><a href="${ctx}/agent/consumption-records"><i class="fa fa-table"></i> <span class="nav-label">消费记录</span></a></li>
      <li id="m-utransfer"><a href="${ctx}/agent/usertransfer"><i class="fa fa-rmb"></i> <span class="nav-label">加扣款记录</span></a></li>
      <li id="m-flowpacks">
        <a href="${ctx}/agent/userflow"><i class="fa fa-laptop"></i> <span class="nav-label">产品与折扣</span> </a>
      </li>
      <li id="m-recharge"><a href="${ctx}/agent/recharge"><i class="fa fa-shopping-cart"></i> <span class="nav-label">流量充值</span></a></li>
      <li id="m-apis"><a href="${ctx}/agent/apis"><i class="fa fa-cubes"></i> <span class="nav-label">应用接入</span></a></li>
      <li id="m-profile"><a href="${ctx}/agent/profile"><i class="fa fa-edit"></i> <span class="nav-label">注册信息更新</span></a></li>
      <%--
      <li id="m-sm"><a href="${ctx}/agent/sm-template"><i class="fa fa-commenting-o"></i> <span class="nav-label">短信设置</span></a></li>
      --%>
      <li id="m-passwd"><a href="${ctx}/agent/passwd"><i class="fa fa-user"></i> <span class="nav-label">密码修改</span></a></li>
    </ul>
  </div>
</nav>

