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
      <li id="mc-user"><a href="${ctx}/oc/users/list"><i class="fa fa-user-md"></i><span class="nav-label"> 运营用户</span></a></li>
      <li id="mc-role"><a href="${ctx}/oc/roles"><i class="fa fa-tags"></i><span class="nav-label"> 角色管理</span></a></li>
      <li id="mc-plugin"><a href="${ctx}/oc/plugins"><i class="fa fa-cubes"></i><span class="nav-label"> 插件管理</span></a></li>
      <li id="mc-dic">
        <a href="${ctx}/oc/state"><i class="fa fa-database"></i><span class="nav-label"> 数据字典</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level collapse">
          <li id="mc-dic-state"><a href="${ctx}/oc/state">分省编码</a></li>
          <li id="mc-dic-location"><a href="${ctx}/oc/location">手机号段</a></li>
        </ul>
      </li>
      <li id="mc-oplog"><a href="${ctx}/oc/oplog"><i class="fa fa-file-o"></i><span class="nav-label"> 操作日志</span></a></li>
    </ul>
  </div>
</nav>