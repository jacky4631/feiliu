<%@ page contentType="text/html;charset=UTF-8" %>
<div class="row border-bottom top-navigation">
  <nav id="main-nav" class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
      <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" href="#"><i class="fa fa-bars"></i> </a>
    </div>

    <div class="navbar-collapse collapse" id="navbar">
      <ul class="nav navbar-nav" style="margin-left: 16px;">
        <li id="ma">
          <a aria-expanded="false" role="button" href="${ctx}/oc">运营监管</a>
        </li>

        <li id="mb">
          <a aria-expanded="false" role="button" href="${ctx}/oc/statis/timeconsuming-state">经营分析</a>
        </li>

        <shiro:hasRole name="admin">
          <li id="mc">
            <a aria-expanded="false" role="button" href="${ctx}/oc/users/list">系统管理</a>
          </li>
        </shiro:hasRole>
        <li id="md" class="dropdown">
          <a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"> 能力导航<span class="caret"></span></a>
          <ul role="menu" class="dropdown-menu">
            <li><a href="${ctx}/">飞流</a></li>
          </ul>
        </li>
        <li id="me" class="dropdown">
          <a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"> 帮助 <span class="caret"></span></a>
          <ul role="menu" class="dropdown-menu">
            <li><a target="_blank" href="${cdn}/doc/introduce/">系统介绍</a></li>
            <li><a href="${cdn}/doc/商户充值协议.pdf">API手册下载</a></li>
          </ul>
        </li>

      </ul>
      <ul class="nav navbar-top-links navbar-right">
        <li>
          <a target="_blank" href="${ctx}/agent">
            <i class="fa fa-television"></i> 打开代理客户端
          </a>
        </li>
        <li>
          <a href="${ctx}/logout">
            <i class="fa fa-sign-out"></i> 退出
          </a>
        </li>
      </ul>
    </div>

  </nav>
</div>
