<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <link href="${ctx}/static/css/channel-product.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <style>
    .chosen-container {
      width: 600px !important;
    }

    #tab-basic .chosen-container {
      width: 320px !important;
    }
  </style>
</head>
<body>
<c:if test="${user.userType eq 0}">
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
</c:if>
<c:if test="${user.userType eq 1}">
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
</c:if>

<div id="page-wrapper" class="gray-bg">
  <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
  <tags:content_header icon="fa-user" sysname="${sysname}" title="用户信息"/>

  <div class="wrapper wrapper-content animated fadeInRight">
    <c:if test="${not empty message}">
      <div id="message" class="alert alert-success">
        <button data-dismiss="alert" class="close">×</button>
          ${message}
      </div>
    </c:if>
    <div class="tabs-container">
      <ul class="nav nav-tabs">
        <li class="active"><a href="#tab-basic" data-toggle="tab"> <i class="fa fa-user-secret"></i> 基本信息</a></li>
        <li class=""><a href="#tab-products" data-toggle="tab"><i class="fa fa-cart-arrow-down"></i> 产品投放</a></li>
        <li class=""><a href="#tab-channels" data-toggle="tab"><i class="fa fa-exchange"></i> 通道限定</a></li>
        <li class=""><a href="#tab-bills" data-toggle="tab"><i class="fa fa-file-text-o"></i> 对账单</a></li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="tab-basic">
          <div class="panel-body">
            <form id="frm_user" method="post" action="${ctx}/oc/users/${action}" class="form-horizontal">
              <input name="id" id="id" type="hidden" value="${user.id}"/>
              <input name="userType" type="hidden" value="${user.userType}"/>

              <div class="row">
                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="username">用户名</label>

                  <div class="col-sm-8">
                    <input type="text" id="username" name="username"
                           <c:if test="${not empty user.id}">readonly</c:if> data-validation="alphanumeric" value="${user.username}" class="form-control">
                  </div>
                </div>
                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="displayName">显示名</label>

                  <div class="col-sm-8">
                    <input type="text" id="displayName" name="displayName" data-validation="required" value="${user.displayName}" class="form-control">
                  </div>
                </div>
                <c:if test="${user.id eq null}">
                  <div class="form-group col-sm-6">
                    <label class="col-sm-4 control-label" for="plainPassword">密码</label>

                    <div class="col-sm-8">
                      <input type="text" id="plainPassword" data-validation="required" name="plainPassword" value="" class="form-control">
                    </div>
                  </div>
                </c:if>
                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="company">公司名</label>

                  <div class="col-sm-8">
                    <input type="text" id="company" name="company" value="${user.company}" class="form-control">
                  </div>
                </div>
                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="company">机构代码</label>

                  <div class="col-sm-8">
                    <input type="text" id="orgcode" name="orgcode" value="${user.orgcode}" class="form-control">
                    <small>有相同代码的用户和供货商被认为是同一组织</small>
                  </div>
                </div>
                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="linkman">联系人</label>

                  <div class="col-sm-8">
                    <input type="text" id="linkman" name="linkman" value="${user.linkman}" class="form-control">
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="mobile">手机号</label>

                  <div class="col-sm-8">
                    <input type="text" id="mobile" name="mobile" value="${user.mobile}" class="form-control">
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="email">电子邮件</label>

                  <div class="col-sm-8">
                    <input type="text" id="email" name="email" value="${user.email}" class="form-control">
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label">注册时间</label>

                  <div class="col-sm-8">
                    <input type="text" readonly value="<fmt:formatDate value="${user.registerDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="callbackUrl">状态回调地址</label>

                  <div class="col-sm-8">
                    <input type="text" id="callbackUrl" name="callbackUrl" value="${user.callbackUrl}" class="form-control">
                    <small>保留为空, 不推送充值报告, 用户需要使用查询API</small>
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label" for="allowIps">授权IP</label>

                  <div class="col-sm-8">
                    <input type="text" id="allowIps" name="allowIps" value="${user.allowIps}" class="form-control">
                    <small>不输入内容表示不进行IP鉴权, 多个IP用半角逗号分隔</small>
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label">短信提醒</label>

                  <div class="checkbox col-sm-8">
                    <label>
                      <form:checkbox path="user.sendSm" value="true"/>启用 (成功充值后,向被充值号码发送提醒)
                    </label>
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label">短信价(分)</label>

                  <div class="col-sm-8">
                    <input type="text" name="smPrice" value="${user.smPrice}" class="form-control">
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label">跳过价格保护</label>

                  <div class="checkbox col-sm-8">
                    <label>
                      <form:checkbox path="user.foreSkipPriceProtected" value="true"/>跳过 (跳过后该用户不遵守价格保护)
                    </label>
                  </div>
                </div>

                <div class="form-group col-sm-6">
                  <label class="col-sm-4 control-label">授权码</label>

                  <div class="col-sm-8">
                    <input type="text" readonly value="${user.authToken}" class="form-control">
                  </div>
                </div>
                <shiro:hasRole name="admin">
                  <c:if test="${user.userType eq 1}">
                    <div class="form-group col-sm-6">
                      <label class="col-sm-4 control-label">用户角色</label>

                      <div class="col-sm-8">
                        <form:select data-placeholder="请选择角色" id="role-select" path="user.roles" multiple="true" cssClass="form-control chosen role-select">
                          <form:options items="${roles}" itemLabel="name" itemValue="code"/>
                        </form:select>
                      </div>
                    </div>
                  </c:if>
                </shiro:hasRole>
              </div>

              <div class="hr-line-dashed"></div>
              <div class="form-group">
                <div class="col-sm-4 col-sm-offset-2">
                  <c:if test="${user.userType eq 0}">
                    <a type="button" class="btn btn-primary" href="${ctx}/oc/users/agent"><i class="fa fa-angle-double-left"></i>返回</a>
                  </c:if>
                  <c:if test="${user.userType eq 1}">
                    <a type="button" class="btn btn-primary" href="${ctx}/oc/users/list"><i class="fa fa-angle-double-left"></i>返回</a>
                  </c:if>
                  <button type="submit" class="btn btn-primary">保存</button>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div class="tab-pane" id="tab-products">
          <div class="panel-body">
            <div class="col-sm-12">
              <div class="form-inline">
                <c:if test="${user.userType eq 0}">
                  <a type="button" class="btn btn-primary btn-sm" href="${ctx}/oc/users/agent"><i class="fa fa-angle-double-left"></i>返回</a>
                </c:if>
                <c:if test="${user.userType eq 1}">
                  <a type="button" class="btn btn-primary btn-sm" href="${ctx}/oc/users/list"><i class="fa fa-angle-double-left"></i>返回</a>
                </c:if>

                <div class="form-group">
                  <div class="input-group">
                    <select name="filter_EQ_provider" id="filter_EQ_provider" class="form-control input-sm">
                      <option value="">全部</option>
                      <option value="CMCC">中国移动</option>
                      <option value="TELECOM">中国电信</option>
                      <option value="UNICOM">中国联通</option>
                    </select>
                  </div>
                  <div class="input-group">
                    <select name="filter_EQ_state" id="filter_EQ_state" class="form-control input-sm">
                      <option value="">全部</option>
                      <c:forEach var="state" items="${states}">
                        <option value="${state.code}">${state.name}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
                <div class="btn-group" role="group">
                  <button type="button" id="search" class="btn btn-sm btn-primary">查询</button>
                  <button type="button" id="reset" class="btn btn-sm btn-primary">全部</button>
                </div>
                <div class="btn-group" role="group">
                  <button id="btn-add-product" type="button" class="btn btn-sm btn-info"><i class="fa fa-unlock-alt"></i> 产品授权...</button>
                </div>
              </div>
              <table id="dt1" class="table table-stripped table-bordered">
                <thead>
                <tr>
                  <th>产品ID</th>
                  <th>产品名称</th>
                  <th>包大小</th>
                  <th>价格</th>
                  <th style="width:40px;">折扣</th>
                  <th style="width:146px;">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div class="tab-pane" id="tab-channels">
          <div class="panel-body">
            <form id="cform" class="form-horizontal" method="post" action="${ctx}/oc/users/${action}">
              <input name="id" type="hidden" value="${user.id}"/>

              <div class="form-group">
                <label class="col-sm-2 control-label">预订义产品组</label>

                <div class="col-sm-8">
                  <form:select required="false" data-placeholder="选择预定义通道组" path="user.allowChannelGroup" multiple="false" cssClass="form-control chosen">
                    <form:option label="--不限制--" value=""/>
                    <form:options items="${channelGroups}" itemLabel="title" itemValue="id"/>
                  </form:select>
                  <div style="margin:15px 0;">选择通道组后, 用户只能在选中的预订通道组及用户通道组下购买授权产品, 都不指定则可自由订购</div>
                </div>
              </div>
              <div class="form-group">
                <div class="col-sm-2 control-label">
                  <label class="control-label">用户产品组</label>

                  <div style="margin-top: 10px;"><a id="btn-modi-channels" href="#">修改...</a></div>
                </div>
                <div class="col-sm-8">
                  <div class="channel" id="disChannel">
                  </div>
                  <input type="hidden" id="channelprods" name="allowChannelProducts" value="${fn:join(user.allowChannelProducts, ",")}"/>
                  <div style="margin:15px 0;">限定通道后, 用户只能在该通道组及选中的预订通道组下购买授权产品, 都不指定则可自由订购</div>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">保护产品授权</label>

                <div class="col-sm-8">
                  <form:select data-placeholder="受保护通道只有单独授权后方可使用" path="user.grantedSpecialChannelProducts" multiple="true" cssClass="form-control chosen">
                    <form:options items="${protectedProfile}" itemLabel="name" itemValue="id"/>
                  </form:select>
                </div>
              </div>
              <div class="hr-line-dashed"></div>
              <div class="form-group">
                <div class="col-sm-4 col-sm-offset-2">
                  <c:if test="${user.userType eq 0}">
                    <a type="button" class="btn btn-primary" href="${ctx}/oc/users/agent"><i class="fa fa-angle-double-left"></i>返回</a>
                  </c:if>
                  <c:if test="${user.userType eq 1}">
                    <a type="button" class="btn btn-primary" href="${ctx}/oc/users/list"><i class="fa fa-angle-double-left"></i>返回</a>
                  </c:if>
                  <button type="button" id="channels-save" class="btn btn-primary">保存</button>
                </div>
              </div>
            </form>
          </div>
        </div>

        <div class="tab-pane" id="tab-bills">
          <div class="panel-body">
            <form id="bform" class="form-horizontal" method="post" action="${ctx}/oc/users/${action}">
              <input name="id" type="hidden" value="${user.id}"/>

              <div class="form-group">
                <label class="col-sm-2 control-label">传送对账单</label>

                <div class="checkbox col-sm-6">
                  <label>
                    <form:checkbox path="user.sendBillFile" value="true"/>启用(每天凌晨将自动传递2天前的日对账单到用户指定位置)
                  </label>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">传输协议</label>

                <div class="col-sm-6">
                  <select id="protocol" name="billFileAccount.protocol" class="form-control input-sm">
                    <option value="FTP">FTP</option>
                    <option value="SFTP">SFTP</option>
                    <option value="HTTP">HTTP</option>
                  </select>
                  <small>当前仅支持FTP协议</small>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">主机名</label>

                <div class="col-sm-6">
                  <input type="text" id="hostname" name="billFileAccount.hostname" value="${user.billFileAccount.hostname}" class="form-control">
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">端口</label>

                <div class="col-sm-6">
                  <input type="text" id="port" name="billFileAccount.port" data-validation="number" value="${user.billFileAccount.port}" class="form-control">
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">用户名</label>

                <div class="col-sm-6">
                  <input type="text" id="billUsername" name="billFileAccount.username" value="${user.billFileAccount.username}" class="form-control">
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">密码</label>

                <div class="col-sm-6">
                  <input type="password" id="billPassword" name="billFileAccount.password" value="${user.billFileAccount.password}" class="form-control">
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">传送路径</label>

                <div class="col-sm-6">
                  <input type="text" id="rootPath" name="billFileAccount.rootPath" value="${user.billFileAccount.rootPath}" class="form-control">
                  <small>保留为空表示传递到用户的Home目录</small>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">FTP服务器类型</label>

                <div class="col-sm-6">
                  <select id="style" name="billFileAccount.style" class="form-control input-sm">
                    <option value="Unix" <c:if test="${user.billFileAccount.style eq 'Unix'}">selected="selected"</c:if>>Unix/Linux/BSD</option>
                    <option value="Windows" <c:if test="${user.billFileAccount.style eq 'Windows'}">selected="selected"</c:if>>Windows</option>
                  </select>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">被动模式</label>

                <div class="col-sm-6 checkbox">
                  <label>
                    <form:checkbox id="passiveMode" path="user.billFileAccount.passiveMode" value="true"/>启用
                  </label>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">字符集</label>

                <div class="col-sm-6">
                  <select id="remoteEncoding" name="billFileAccount.remoteEncoding" class="form-control input-sm">
                    <option value="UTF-8" <c:if test="${user.billFileAccount.remoteEncoding eq 'UTF-8'}">selected="selected"</c:if>>UTF-8</option>
                    <option value="GBK" <c:if test="${user.billFileAccount.remoteEncoding eq 'GBK'}">selected="selected"</c:if>>GBK</option>
                  </select>
                </div>
              </div>
              <div class="hr-line-dashed"></div>
              <div class="form-group">
                <div class="col-sm-4 col-sm-offset-2">
                  <c:if test="${user.userType eq 0}">
                    <a type="button" class="btn btn-primary" href="${ctx}/oc/users/agent"><i class="fa fa-angle-double-left"></i>返回</a>
                  </c:if>
                  <c:if test="${user.userType eq 1}">
                    <a type="button" class="btn btn-primary" href="${ctx}/oc/users/list"><i class="fa fa-angle-double-left"></i>返回</a>
                  </c:if>
                  <button type="submit" class="btn btn-primary">保存</button>
                  <button type="button" id="btn-verify" class="btn btn-info">账号校验</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
</div>
<div id="dlgForm" style="height:450px;width:620px;display:none;"></div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${ctx}/static/js/oc/channel-product.js"></script>
<script type="text/javascript">
  var dlg, dt;
  $(function () {
    $.validate({
      lang: "zh"
    });
    $(".chosen").chosen({});

    toastr.options = {
      closeButton: true,
      progressBar: false,
      positionClass: "toast-top-center",
      timeOut: 550
    };

    <c:if test="${user.userType eq 0}">
    activeMenu("ma-agent");
    </c:if>
    <c:if test="${user.userType eq 1}">
    activeMenu("mc-user");
    </c:if>

    $("#message").delay(2000).hide(300);

    $.fn.dataTable.ext.legacy.ajax = true;
    dt = $('#dt1').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "dom": "<f<t>p>",
      "ajax": {
        "url": "${ctx}/oc/users/product/data",
        "method": "post",

        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_username": $("#username").val(),
            "filter_EQ_state": $("#filter_EQ_state").val(),
            "filter_EQ_provider": $("#filter_EQ_provider").val()
          });
        }
      },

      "columns": [
        {"data": "productId"},
        {"data": "name"},
        {"data": "size"},
        {"data": "price"},
        {"data": "discount"},
        {"data": "id"}
      ],
      "columnDefs": [
        {
          "render": function (data, type, row) {
            return "<input name='discount' type='text' value='" + data + "'>";
          },
          className: 'dt-for-input',
          "targets": -2
        },
        {
          "render": function (data, type, row) {
            return '<div class="btn-group">' +
              '<button class="btn-white btn btn-xs savedis" data-id="' + data + '">保存</button>' +
              '<button class="btn-white btn btn-xs applydis" data-id="' + data + '">全设</button>' +
              '<button class="btn-white btn btn-xs unauth" data-id="' + data + '">删除</button>' +
              '<button class="btn-white btn btn-xs unauthgrp" data-id="' + data + '">全删</button>' +
              '</div>'
          },
          "targets": -1
        },
        {
          "render": function (data, type, row) {
            return data + 'MB';
          },
          "targets": 2
        },
        {
          orderable: false,
          targets: [0, 1, 2, 3, 4, 5]
        }
      ],
      "filter": false,
      "info": false,
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("#filter_EQ_provider option:first").prop("selected", 'selected');
      $("#filter_EQ_state option:first").prop("selected", 'selected');
      dt.draw();
    });

    var addProduct = function () {
      var uid = $("#id").val();
      if (uid == '') {
        return;
      }
      var elem = document.getElementById('dlgForm');
      dlg = dialog({
        "content": elem,
        "title": "产品授权",
        "zIndex": 2000,
        "onshow": function () {
          if ($(elem).html() == "")
            $(elem).load($ctx + '/oc/product/forchoose');
        },
        statusbar: '<label id="msg"></label>'
      });
      dlg.showModal();
    };

    $("#btn-add-product").on('click', function () {
      addProduct();
    });

    $("#channels-save").on('click', function () {
      if ($('#id').val() == '') {
        toastr.error("请先填写和保存基本信息, 之后再设置通道组", '保存失败');
        return;
      }
      $("#cform").submit();
    });

    $(document).on('click', '.savedis', function (e) {
      var discount = $(e.target).parents('tr').find("input[name='discount']").val();
      if (isNaN(discount)) {
        alert("未输入正确的折扣, 折扣应该是一个小数");
        return;
      }
      var id = $(e.target).data("id");
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/users/product/discount",
        data: {
          "id": id, "discount": discount
        },
        success: function (data) {
          if (data.status == 'success') {
            toastr.success(data.message, '保存成功');
          } else {
            toastr.error(data.message, '保存失败');
          }
        }
      });
    });

    $(document).on('click', '.applydis', function (e) {
      var discount = $(e.target).parents('tr').find("input[name='discount']").val();
      if (isNaN(discount)) {
        alert("未输入正确的折扣, 折扣应该是一个小数");
        return;
      }
      var id = $(e.target).data("id");
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/users/product/apply-dis",
        data: {
          "id": id, "discount": discount
        },
        success: function (data) {
          if (data.status == 'success') {
            toastr.success('已成功将当前折扣应用到本组产品', '保存成功');
            dt.draw(false);
          } else {
            toastr.error(data.message, '保存失败');
          }
        }
      });
    });

    $(document).on('click', '.unauthgrp', function (e) {
      if (!confirm("这将删除同组所有产品的授权, 是否继续?")) {
        return;
      }
      var id = $(e.target).data("id");
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/users/product/remove-grp",
        data: {
          "id": id
        },
        success: function (data) {
          if (data.status == 'success') {
            toastr.success('已成功将本组产品全部解除授权', '解除成功');
            dt.draw(false);
          } else {
            toastr.error(data.message, '解除失败');
          }
        }
      });
    });

    $(document).on('click', '.unauth', function (e) {
      var id = $(e.target).data("id");
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/users/product/remove",
        data: {
          "id": id
        },
        success: function (data) {
          if (data.status == 'success') {
            toastr.success('已成功解除授权', '解除成功');
            dt.draw(false);
          } else {
            toastr.error('解除授权失败', '解除失败');
          }
        }
      });
    });

    $("#btn-verify").on('click', function () {
      var protocol = $("#protocol").val();
      var hostname = $("#hostname").val();
      var port = $("#port").val();
      var billUsername = $("#billUsername").val();
      var billPassword = $("#billPassword").val();
      var style = $("#style").val();
      var remoteEncoding = $("#remoteEncoding").val();
      var passiveMode = $('#passiveMode').is(':checked');
      if (hostname == '' || isNaN(port) || billUsername == '' || billPassword == '') {
        toastr.error('请输入服务器信息和账号信息', '提示');
        return;
      }
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/ftp/verify",
        data: {
          "protocol": protocol,
          "hostname": hostname,
          "port": port,
          "username": billUsername,
          "password": billPassword,
          "passiveMode": passiveMode,
          "remoteEncoding": remoteEncoding,
          "style": style
        },
        success: function (data) {
          if (data.status == 'success') {
            toastr.success(data.message, '提示');
          } else {
            toastr.error(data.message, '提示');
          }
        }
      });
    });

  });
</script>
</body>
</html>
